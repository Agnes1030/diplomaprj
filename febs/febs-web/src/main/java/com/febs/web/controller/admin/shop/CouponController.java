package com.febs.web.controller.admin.shop;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.febs.system.service.UserService;
import com.febs.web.controller.base.BaseController;

import tk.mybatis.mapper.entity.Example;
/**
 * 优惠券controller
 * @author wtsoftware 
 * @date 2020-06-14
 */
@Controller
@RequestMapping("/admin")
public class CouponController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private CouponService couponService;
	@Autowired
	private CouponUserService couponUserService;
	@Autowired
	private UserService userService;

	@RequestMapping("/coupon")
	@PreAuthorize("hasAuthority('coupon:list')")
	public String index() {
		return "admin/shop/coupon/coupon";
	}

	@RequestMapping("/coupon/list")
	@PreAuthorize("hasAuthority('coupon:list')")
	@ResponseBody
	public Map<String, Object> couponList(QueryRequest request) {
		return this.selectByPageNumSize(request, () -> couponService.selectAll());
	}

	@RequestMapping("/coupon/getCoupon")
	@ResponseBody
	public ResponseBo getCoupon(Long couponId) {
		try {
			Coupon coupon = couponService.selectByKey(couponId);
			return ResponseBo.ok(coupon);
		} catch (Exception e) {
			log.error("获取优惠券信息失败", e);
			return ResponseBo.error("获取优惠券信息失败，请联系网站管理员！");
		}
	}

	@PreAuthorize("hasAuthority('coupon:add')")
	@RequestMapping("/coupon/add")
	@ResponseBody
	public ResponseBo addCoupon(Coupon coupon) {
		coupon.setCreateTime(new Date());
		coupon.setModifiedTime(new Date());
		coupon.setCouStatus(1);
		coupon.setUsedCount(0);
		coupon.setTakeCount(0);
		MyUser myUser = this.getCurrentUser();
		coupon.setCreateUserId(myUser.getUserId());
		couponService.save(coupon);
		return ResponseBo.ok("添加优惠券成功");
	}

	@PreAuthorize("hasAuthority('coupon:update')")
	@RequestMapping("/coupon/update")
	@ResponseBody
	public ResponseBo updateCoupon(Coupon coupon) {
		coupon.setModifiedTime(new Date());
		couponService.updateNotNull(coupon);
		return ResponseBo.ok("更新优惠券成功");
	}

	@PreAuthorize("hasAuthority('coupon:delete')")
	@RequestMapping("/coupon/delete")
	@ResponseBody
	public ResponseBo deleteCoupons(String ids) {
		try {
			List<String> list = Arrays.asList(ids.split(","));
			this.couponService.batchDelete(list, "id", Coupon.class);
			return ResponseBo.ok("删除优惠券类型成功！");
		} catch (Exception e) {
			log.error("删除优惠券类型失败", e);
			return ResponseBo.error("删除优惠券类型失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/couponUser")
	@PreAuthorize("hasAuthority('coupon:list')")
	public String couponUser(HttpServletRequest request, Long couponId) {
		Coupon coupon = couponService.selectByKey(couponId);
		request.setAttribute("coupon", coupon);
		return "admin/shop/coupon/couponUser";
	}

	@RequestMapping("/couponUser/list")
	@PreAuthorize("hasAuthority('coupon:list')")
	@ResponseBody
	public Map<String, Object> couponUserList(QueryRequest request, Long couponId) {
		Example example = new Example(CouponUser.class);
		example.createCriteria().andEqualTo("couponId", couponId);
		return this.selectByPageNumSize(request, () -> couponUserService.selectByExample(example));
	}

	/**
	 * 给指定用户发放优惠券
	 */
	@PreAuthorize("hasAuthority('coupon:add')")
	@RequestMapping("/couponUser/send")
	@ResponseBody
	public ResponseBo sendCouponUser(Long couponId, String ids) {
		Coupon coupon = couponService.selectByKey(couponId);
		if (coupon.getCouStatus() == 0) {
			log.error(couponId + "优惠券已锁定");
			return ResponseBo.error("领取失败");
		}
		if (coupon.getTakeCount() == coupon.getQuota()) {
			log.error(couponId + "优惠券已领完");
			return ResponseBo.error("领取失败");
		}
		List<String> list = Arrays.asList(ids.split(","));
		MyUser myUser = this.getCurrentUser();
		for (String idStr : list) {
			Long userId = Long.parseLong(idStr);
			MyUser reciever = userService.findById(userId);
			CouponUser couponUser = new CouponUser();
			couponUser.setTitle(coupon.getTitle());
			couponUser.setSendUid(myUser.getUserId());
			couponUser.setUserId(reciever.getUserId());
			couponUser.setUserName(reciever.getUsername());
			couponUser.setCreateTime(new Date());
			couponUser.setCouStatus(1);
			couponUser.setCouponId(couponId);
			couponUser.setGetTime(new Date());
			couponUserService.save(couponUser);
		}
		return ResponseBo.ok("发放优惠券成功");
	}

	@PreAuthorize("hasAuthority('coupon:delete')")
	@RequestMapping("/couponUser/delete")
	@ResponseBody
	public ResponseBo deleteCouponUsers(String ids) {
		try {
			List<String> list = Arrays.asList(ids.split(","));
			this.couponUserService.batchDelete(list, "id", CouponUser.class);
			return ResponseBo.ok("删除优惠券成功！");
		} catch (Exception e) {
			log.error("删除优惠券失败", e);
			return ResponseBo.error("删除优惠券失败，请联系网站管理员！");
		}
	}
}
