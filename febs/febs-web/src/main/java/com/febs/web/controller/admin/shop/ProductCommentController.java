package com.febs.web.controller.admin.shop;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.febs.common.annotation.Log;
import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.ResponseBo;
import com.febs.common.utils.FileUtils;
import com.febs.shop.domain.Product;
import com.febs.shop.domain.ProductComment;
import com.febs.shop.service.ProductCommentService;
import com.febs.system.domain.MyUser;
import com.febs.web.controller.base.BaseController;

import tk.mybatis.mapper.entity.Example;

@Controller
@RequestMapping("/admin")
public class ProductCommentController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ProductCommentService productCommentService;

	@Log("评论列表")
	@RequestMapping("/productComment")
	@PreAuthorize("hasAuthority('productComment:list')")
	public String index() {
		return "admin/shop/productComment/productComment";
	}

	@Log("获取商品评论列表信息")
	@RequestMapping("/productComment/list")
	@PreAuthorize("hasAuthority('productComment:list')")
	@ResponseBody
	public Map<String, Object> productCommentList(QueryRequest request, ProductComment productComment) {
		log.debug("查询评论列表");
		Example example = new Example(ProductComment.class);
		if (null != productComment && StringUtils.isNotBlank(productComment.getContent())) {
			example.createCriteria().andLike("content", "%" + productComment.getContent() + "%");
		}
		return super.selectByPageNumSize(request, () -> productCommentService.selectByExample(example));
	}

	@RequestMapping("/productComment/getProductComment")
	@ResponseBody
	public ResponseBo getProductComment(Long productCommentId) {
		try {
			ProductComment productComment = this.productCommentService.selectByKey(productCommentId);
			return ResponseBo.ok(productComment);
		} catch (Exception e) {
			log.error("获取商品评论失败", e);
			return ResponseBo.error("获取商品评论失败，请联系网站管理员！");
		}
	}

	@Log("新增商品评论 ")
	@PreAuthorize("hasAuthority('productComment:add')")
	@RequestMapping("/productComment/add")
	@ResponseBody
	public ResponseBo addProductComment(ProductComment productComment, HttpServletRequest request) {
		MyUser currentUser = this.getCurrentUser();
		productComment.setUserId(currentUser.getUserId());
		productComment.setAuthor(currentUser.getUsername());
		productComment.setStatus(ProductComment.STATUS_VALID);
		productCommentService.save(productComment);
		return ResponseBo.ok("新增商品评论成功！");
	}

	@Log("更新商品评论 ")
	@PreAuthorize("hasAuthority('productComment:update')")
	@RequestMapping("/productComment/update")
	@ResponseBody
	public ResponseBo updateProductComment(ProductComment productComment, HttpServletRequest request) {
		MyUser currentUser = this.getCurrentUser();
		productComment.setModifyUid(currentUser.getUserId());
		productComment.setModifyUserName(currentUser.getUsername());
		productComment.setModifyTime(new Date());
		productCommentService.updateNotNull(productComment);
		return ResponseBo.ok("更新商品评论成功！");
	}

	@Log("删除商品评论")
	@PreAuthorize("hasAuthority('productComment:delete')")
	@RequestMapping("/productComment/delete")
	@ResponseBody
	public ResponseBo deleteProductComments(String ids) {
		try {
			this.productCommentService.deleteProductComments(ids);
			return ResponseBo.ok("删除商品评论成功！");
		} catch (Exception e) {
			log.error("删除商品评论失败", e);
			return ResponseBo.error("删除商品评论失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/productComment/excel")
	@ResponseBody
	public ResponseBo productCommentExcel(ProductComment productComment) {
		try {
			Example example = new Example(ProductComment.class);
			example.createCriteria().andLike("content", "%" + productComment.getContent() + "%");
			List<ProductComment> list = productCommentService.selectByExample(example);
			return FileUtils.createExcelByPOIKit("商品评论表", list, Product.class);
		} catch (Exception e) {
			log.error("导出商品信息Excel失败", e);
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/productComment/csv")
	@ResponseBody
	public ResponseBo productCommentCsv(ProductComment productComment) {
		try {
			Example example = new Example(ProductComment.class);
			example.createCriteria().andLike("content", "%" + productComment.getContent() + "%");
			List<ProductComment> list = productCommentService.selectByExample(example);
			return FileUtils.createCsv("商品评论表", list, Product.class);
		} catch (Exception e) {
			log.error("获取商品信息Csv失败", e);
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}
}
