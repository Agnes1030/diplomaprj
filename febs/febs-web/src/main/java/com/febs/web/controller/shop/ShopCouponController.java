package com.febs.web.controller.shop;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.ResponseBo;
import com.febs.shop.domain.Coupon;
import com.febs.shop.domain.CouponUser;
import com.febs.shop.service.CouponService;
import com.febs.shop.service.CouponUserService;
import com.febs.system.domain.MyUser;
import com.febs.web.controller.base.WebBaseController;

import tk.mybatis.mapper.entity.Example;

@Controller
@RequestMapping("/member")
public class ShopCouponController extends WebBaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private CouponService couponService;
	@Autowired
	private CouponUserService couponUserService;

	/**
	 * 我的优惠券
	 */
	@RequestMapping("/couponUser")
	public String index() {
		return "member/couponUser";
	}

	/**
	 * 我的优惠券
	 */
	@RequestMapping("/couponUser/list")
	@ResponseBody
	public Map<String, Object> couponUserList(QueryRequest request) {
		MyUser myUser = this.getCurrentUser();
		Example example = new Example(CouponUser.class);
		example.createCriteria().andEqualTo("userId", myUser.getUserId());
		return this.selectByPageNumSize(request, () -> couponUserService.selectByExample(example));
	}

	/**
	 * 用户登录成功通过前台调用此接口来领取此类型的优惠券
	 */
	@RequestMapping("/getShopCoupon")
	@ResponseBody
	public ResponseBo getCoupon(Long couponId) {
		Coupon coupon = couponService.selectByKey(couponId);
		if (coupon.getCouStatus() == 0) {
			log.error(couponId + "优惠券已锁定");
			return ResponseBo.error("领取失败");
		}
		if (coupon.getTakeCount() == coupon.getQuota()) {
			log.error(couponId + "优惠券已领完");
			return ResponseBo.error("领取失败");
		}
		MyUser myUser = this.getCurrentUser();
		CouponUser couponUser = new CouponUser();
		couponUser.setTitle(coupon.getTitle());
		couponUser.setUserId(myUser.getUserId());
		couponUser.setUserName(myUser.getUsername());
		couponUser.setCouponId(couponId);
		couponUser.setCouStatus(1);
		couponUser.setCreateTime(new Date());
		couponUserService.save(couponUser);
		// 更新优惠券领取数量
		Integer count = coupon.getTakeCount() + 1;
		couponService.updateTakeCount(count, coupon.getId());
		return ResponseBo.ok("领取成功");
	}
}
