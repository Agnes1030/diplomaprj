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

import com.febs.cms.domain.NavMenu;
import com.febs.cms.service.NavMenuService;
import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.ResponseBo;
import com.febs.common.utils.PinyinUtil;
import com.febs.shop.domain.Product;
import com.febs.shop.domain.ProductTopic;
import com.febs.shop.domain.ProductTopicMapping;
import com.febs.shop.service.ProductTopicMappingService;
import com.febs.shop.service.ProductTopicService;
import com.febs.web.controller.base.BaseController;

import tk.mybatis.mapper.entity.Example;
/**
 * 商品专题管理controller
 * @author wtsoftware 
 * @date 2020-06-14
 */
@Controller
@RequestMapping("/admin")
public class TopicController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ProductTopicService productTopicService;
	@Autowired
	private ProductTopicMappingService productTopicMappingService;
	@Autowired
	private NavMenuService navMenuService;

	@RequestMapping("/topic")
	@PreAuthorize("hasAuthority('topic:list')")
	public String index() {
		return "admin/shop/topic/topic";
	}

	@RequestMapping("/topic/list")
	@PreAuthorize("hasAuthority('topic:list')")
	@ResponseBody
	public Map<String, Object> topicList(QueryRequest request) {
		return this.selectByPageNumSize(request, () -> productTopicService.selectAll());
	}

	@RequestMapping("/topic/viewTopic")
	@PreAuthorize("hasAuthority('topic:list')")
	public String viewTopic(HttpServletRequest request, Long topicId) {
		ProductTopic topic = productTopicService.selectByKey(topicId);
		request.setAttribute("topic", topic);
		return "admin/shop/topic/product";
	}

	@RequestMapping("/topic/productList")
	@PreAuthorize("hasAuthority('topic:list')")
	@ResponseBody
	public Map<String, Object> productList(HttpServletRequest request, QueryRequest queryRequest, Product product) {
		Long topicId = Long.parseLong(request.getParameter("topicId"));
		return super.selectByPageNumSize(queryRequest, () -> this.productTopicService.getTopicProducts(topicId));
	}

	@RequestMapping("/topic/getProductTopic")
	@ResponseBody
	public ResponseBo getProductTopic(Long topicId) {
		try {
			ProductTopic topic = this.productTopicService.selectByKey(topicId);
			return ResponseBo.ok(topic);
		} catch (Exception e) {
			log.error("获取专题信息失败", e);
			return ResponseBo.error("获取专题信息失败，请联系网站管理员！");
		}
	}

	@PreAuthorize("hasAuthority('topic:add')")
	@RequestMapping("/topic/add")
	@ResponseBody
	public ResponseBo addProductTopic(ProductTopic topic) {
		try {
			topic.setCreateTime(new Date());
			if (null == topic.getSlug()) {
				String slug = PinyinUtil.getFullPinyin(topic.getName());
				topic.setSlug(slug);
			}
			this.productTopicService.save(topic);
			if (topic.getNavShow().equals(ProductTopic.STATUS_VALID)) {
				NavMenu navMenu = new NavMenu();
				navMenu.setName(topic.getName());
				navMenu.setSeqNum(1);
				navMenu.setNavType("productTopic");
				navMenu.setRelationId(topic.getId());
				navMenu.setNavUrl("/productTopic/" + topic.getSlug());
				navMenu.setTarget("_self");
				navMenu.setParentId(0L);
				navMenuService.addNavMenu(navMenu);
			}
			return ResponseBo.ok("新增专题成功！");
		} catch (Exception e) {
			log.error("新增专题失败", e);
			return ResponseBo.error("新增专题失败，请联系网站管理员！");
		}
	}

	@PreAuthorize("hasAuthority('topic:update')")
	@RequestMapping("/topic/update")
	@ResponseBody
	public ResponseBo updateRole(ProductTopic topic) {
		try {
			if (null == topic.getSlug()) {
				String slug = PinyinUtil.getFullPinyin(topic.getName());
				topic.setSlug(slug);
			}
			this.productTopicService.updateNotNull(topic);
			if (topic.getNavShow().equals(ProductTopic.STATUS_VALID)) {
				NavMenu navMenu = new NavMenu();
				navMenu.setName(topic.getName());
				navMenu.setSeqNum(1);
				navMenu.setNavType("productTopic");
				navMenu.setRelationId(topic.getId());
				navMenu.setNavUrl("/productTopic/" + topic.getSlug());
				navMenu.setTarget("_self");
				navMenu.setParentId(0L);
				NavMenu params = new NavMenu();
				params.setNavType("productTopic");
				params.setRelationId(topic.getId());
				List<NavMenu> _menus = navMenuService.findAllNavMenus(params);
				if (null != _menus && _menus.size() > 0) {
					navMenu.setId(_menus.get(0).getId());
					// 先删除原来的菜单再新添加
					navMenuService.updateNavMenu(navMenu);
				} else {
					navMenuService.addNavMenu(navMenu);
				}
			}

			return ResponseBo.ok("修改专题成功！");
		} catch (Exception e) {
			log.error("修改专题失败", e);
			return ResponseBo.error("修改专题失败，请联系网站管理员！");
		}
	}

	@PreAuthorize("hasAuthority('topic:delete')")
	@RequestMapping("/topic/delete")
	@ResponseBody
	public ResponseBo deleteProductTopics(String ids) {
		try {
			List<String> list = Arrays.asList(ids.split(","));
			this.productTopicService.batchDelete(list, "id", ProductTopic.class);
			// 删除专题和商品的关联关系
			this.productTopicMappingService.batchDelete(list, "topicId", ProductTopicMapping.class);
			// 删除导航关联
			this.navMenuService.deleteNavByTopicIds(ids);
			return ResponseBo.ok("删除专题成功！");
		} catch (Exception e) {
			log.error("删除专题失败", e);
			return ResponseBo.error("删除专题失败，请联系网站管理员！");
		}
	}

	@PreAuthorize("hasAuthority('topic:add')")
	@RequestMapping("/topic/addToTopic")
	@ResponseBody
	public ResponseBo addToTopic(Long topicId, String ids) {
		List<String> list = Arrays.asList(ids.split(","));
		for (String id : list) {
			Long productId = Long.parseLong(id);
			Example topicMappingExample = new Example(ProductTopicMapping.class);
			topicMappingExample.createCriteria().andEqualTo("topicId", topicId).andEqualTo("productId", productId);
			List<ProductTopicMapping> lists = this.productTopicMappingService.selectByExample(topicMappingExample);
			if (null == lists || lists.size() == 0) {
				ProductTopicMapping topicProductMapping = new ProductTopicMapping();
				topicProductMapping.setTopicId(topicId);
				topicProductMapping.setProductId(productId);
				this.productTopicMappingService.save(topicProductMapping);
			}
		}
		return ResponseBo.ok("加入专题成功！");
	}

	/**
	 * 在专题中移除商品关联
	 */
	@PreAuthorize("hasAuthority('topic:delete')")
	@RequestMapping("/topic/deleteTopicMapping")
	@ResponseBody
	public ResponseBo deleteTopicMapping(Long topicId, String ids) {
		List<String> list = Arrays.asList(ids.split(","));
		for (String id : list) {
			Long productId = Long.parseLong(id);
			Example topicMappingExample = new Example(ProductTopicMapping.class);
			topicMappingExample.createCriteria().andEqualTo("topicId", topicId).andEqualTo("productId", productId);
			this.productTopicMappingService.deleteTopicMapping(topicMappingExample);
		}
		return ResponseBo.ok("取消专题成功！");
	}
}
