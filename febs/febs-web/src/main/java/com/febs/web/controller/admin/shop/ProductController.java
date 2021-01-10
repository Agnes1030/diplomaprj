package com.febs.web.controller.admin.shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.ResponseBo;
import com.febs.common.utils.FileUtils;
import com.febs.search.lucene.service.LuceneService;
import com.febs.shop.domain.Product;
import com.febs.shop.domain.ProductCategory;
import com.febs.shop.domain.ProductCategoryMapping;
import com.febs.shop.domain.ProductImage;
import com.febs.shop.domain.ProductSku;
import com.febs.shop.domain.ProductSpecification;
import com.febs.shop.domain.ProductSpecificationValue;
import com.febs.shop.service.ProductCategoryMappingService;
import com.febs.shop.service.ProductCategoryService;
import com.febs.shop.service.ProductImageService;
import com.febs.shop.service.ProductService;
import com.febs.shop.service.ProductSkuService;
import com.febs.shop.service.ProductSpecificationService;
import com.febs.shop.service.ProductSpecificationValueService;
import com.febs.system.domain.MyUser;
import com.febs.web.controller.base.BaseController;
import com.febs.web.utils.UploadUtil;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;

@Controller
@RequestMapping("/admin")
public class ProductController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private ProductCategoryMappingService productCategoryMappingService;
	@Autowired
	private ProductImageService productImageService;
	@Autowired
	private ProductSpecificationService productSpecificationService;
	@Autowired
	private ProductSpecificationValueService productSpecificationValueService;
	@Autowired
	private ProductSkuService productSkuService;
	@Autowired
	private LuceneService luceneService;

	@RequestMapping("/product")
	@PreAuthorize("hasAuthority('product:list')")
	public String index() {
		return "admin/shop/product/product";
	}

	@RequestMapping("/product/list")
	@PreAuthorize("hasAuthority('product:list')")
	@ResponseBody
	public Map<String, Object> productList(HttpServletRequest request, QueryRequest queryRequest, Product product) {
		if (null == request.getParameter("categoryId")) {
			Example example = new Example(Product.class);
			if (StringUtils.isNotBlank(product.getTitle())) {
				example.createCriteria().andLike("title", "%" + product.getTitle() + "%");
			}
			return super.selectByPageNumSize(queryRequest, () -> this.productService.selectByExample(example));
		}
		Long categoryId = Long.parseLong(request.getParameter("categoryId").toString());
		List<Long> ids = new ArrayList<Long>();
		ids.add(categoryId);
		return super.selectByPageNumSize(queryRequest, () -> this.productService.findAllProducts(product, ids));
	}

	@RequestMapping("/product/getProduct")
	@ResponseBody
	public ResponseBo getProduct(Long productId) {
		try {
			Product product = this.productService.findById(productId);
			if (null == product) {
				return ResponseBo.error();
			}
			List<ProductImage> images = this.productImageService.getProductImgs(productId);
			product.setProductImages(images);
			return ResponseBo.ok(product);
		} catch (Exception e) {
			log.error("获取商品信息失败", e);
			return ResponseBo.error("获取商品信息失败，请联系网站管理员！");
		}
	}

	@PostMapping("/product/filesUpload/thumbnail")
	@ResponseBody
	public ResponseBo logoUpload(@RequestParam("productFile") MultipartFile[] file) {
		String fileName = UploadUtil.uploadImg(file);
		// 返回前端data数据中存储修改后的文件名
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("src", "/upload/" + fileName);
		return ResponseBo.ok(msg);
	}

	@GetMapping("/product/add")
	public String toAdd(Model ui) {
		ui.addAttribute("action", "save");
		return "admin/shop/product/productAdd";
	}

	@PreAuthorize("hasAuthority('product:add')")
	@PostMapping("/product/add")
	@ResponseBody
	public ResponseBo addProduct(Product product, String[] productImgs, HttpServletRequest request) {
		try {
			MyUser currentUser = this.getCurrentUser();
			product.setUserId(currentUser.getUserId());
//			if (StringUtils.isBlank(product.getSlug())) {
//				String slug = PinyinUtil.getFirstSpellPinYin(product.getTitle(), false);
//				product.setSlug(slug);
//			}
			this.productService.addProduct(product);
			Long productId = product.getId();
			// 图片集处理
			if (null != productImgs && productImgs.length > 0) {
				for (int i = 0; i < productImgs.length; i++) {
					String srcUrl = productImgs[i];
					ProductImage productImg = new ProductImage();
					productImg.setSrc(srcUrl);
					productImg.setProductId(productId);
					productImg.setCreateTime(new Date());
					productImg.setSeqNum(0);
					productImageService.save(productImg);
				}
			}
			// 分类相关处理
			String categoryIdsStr = product.getCategoryIds();
			String[] categoryIds = categoryIdsStr.split(",");
			for (String catId : categoryIds) {
				Long categoryId = Long.parseLong(catId);
				ProductCategoryMapping productProductCategoryMapping = new ProductCategoryMapping();
				productProductCategoryMapping.setProductId(productId);
				productProductCategoryMapping.setCategoryId(categoryId);
				productCategoryMappingService.save(productProductCategoryMapping);
			}
			// sku相关处理
			String specStr = request.getParameter("specStr");
			String skuStr = request.getParameter("skuStr");
			this.setSku(productId, specStr, skuStr);
			// 索引
			luceneService.createProductIndex(product);
			return ResponseBo.ok("新增商品成功！");
		} catch (Exception e) {
			log.error("新增商品失败", e);
			return ResponseBo.error("新增商品失败，请联系网站管理员！");
		}
	}

	@GetMapping("/product/edit")
	public String toEdit(Model ui, HttpServletRequest request) {
		String productId = request.getParameter("productId");
		Example specExample = new Example(ProductSpecification.class);
		specExample.createCriteria().andEqualTo("productId", productId);
		List<ProductSpecification> specList = this.productSpecificationService.selectByExample(specExample);
		specExample.createCriteria().andEqualTo("productId", productId);
		Example skuExample = new Example(ProductSku.class);
		skuExample.createCriteria().andEqualTo("productId", productId);
		List<ProductSku> skuList = this.productSkuService.selectByExample(skuExample);

		ui.addAttribute("skuList", skuList);
		ui.addAttribute("specList", specList);
		ui.addAttribute("productId", productId);
		ui.addAttribute("action", "update");
		return "admin/shop/product/productAdd";
	}

	@PreAuthorize("hasAuthority('product:update')")
	@PostMapping("/product/update")
	@ResponseBody
	public ResponseBo updateProduct(Product product, String[] productImgs, HttpServletRequest request) {
		try {
//			if (StringUtils.isBlank(product.getSlug())) {
//				String slug = PinyinUtil.getFirstSpellPinYin(product.getTitle(), false);
//				product.setSlug(slug);
//			}
			this.productService.updateProduct(product);
			Long productId = product.getId();
			// 图片集处理
			// 删除原关系重新添加
			productImageService.deleteProductImgs(productId);
			if (null != productImgs && productImgs.length > 0) {
				for (int i = 0; i < productImgs.length; i++) {
					String srcUrl = productImgs[i];
					ProductImage productImg = new ProductImage();
					productImg.setSrc(srcUrl);
					productImg.setProductId(productId);
					productImg.setCreateTime(new Date());
					productImg.setSeqNum(0);
					productImageService.save(productImg);
				}
			}
			// 分类相关处理
			this.productCategoryMappingService.deleteProductCategoryMapping(productId);
			String catIds = product.getCategoryIds();
			String[] categoryIds = catIds.split(",");
			for (String catId : categoryIds) {
				Long categoryId = Long.parseLong(catId);
				ProductCategoryMapping productProductCategoryMapping = new ProductCategoryMapping();
				productProductCategoryMapping.setProductId(productId);
				productProductCategoryMapping.setCategoryId(categoryId);
				productCategoryMappingService.save(productProductCategoryMapping);
			}
			// sku相关处理
			String specStr = request.getParameter("specStr");
			String skuStr = request.getParameter("skuStr");
			this.setSku(productId, specStr, skuStr);
			luceneService.deleteProductIndexById(productId.toString());
			luceneService.createProductIndex(product);
			return ResponseBo.ok("修改商品成功！");
		} catch (Exception e) {
			log.error("修改商品失败", e);
			return ResponseBo.error("修改商品失败，请联系网站管理员！");
		}
	}

	/**
	 * 重新设置产品sku相关参数，在新增和修改商品时候都要调用
	 * 
	 * @param productId 商品ID
	 */
	public void setSku(Long productId, String specStr, String skuStr) {
		// 删除原来sku重新设置关联关系
		Example specExample = new Example(ProductSpecification.class);
		specExample.createCriteria().andEqualTo("productId", productId);
		productSpecificationService.deleteByCondition(specExample);
		Example specValueExample = new Example(ProductSpecificationValue.class);
		specValueExample.createCriteria().andEqualTo("productId", productId);
		productSpecificationValueService.deleteByCondition(specValueExample);
		Example skuExample = new Example(ProductSku.class);
		skuExample.createCriteria().andEqualTo("productId", productId);
		productSkuService.deleteByCondition(skuExample);
		List<ProductSpecification> specList = JSONArray.parseArray(specStr, ProductSpecification.class);
		for (ProductSpecification spec : specList) {
			// System.out.println("name:" + spec.getSpecification() + ",value:" +
			// spec.getValue());
			spec.setProductId(productId);
			spec.setCreateTime(new Date());
			spec.setModifiedTime(new Date());
			spec.setPicUrl("");
			productSpecificationService.save(spec);
			// 开始保存对应value
			String[] values = spec.getValue().split(",");
			for (int i = 0; i < values.length; i++) {
				String vv = values[i];
				ProductSpecificationValue specValue = new ProductSpecificationValue();
				specValue.setProductId(productId);
				specValue.setSeqNum(i + 1);
				specValue.setSpecificationId(spec.getId());
				specValue.setSpecValue(vv);
				specValue.setCreateTime(new Date());
				productSpecificationValueService.save(specValue);
			}

		}
		List<ProductSku> skuList = JSONArray.parseArray(skuStr, ProductSku.class);
		for (ProductSku sku : skuList) {
			String[] skuValueNames = sku.getSpecification().split(",");
			// 格式有可能value值会重复例如:(外观:红色,内饰:黑色,尺寸:1.8*1.5)选split(",")然后再split(:)
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < skuValueNames.length; i++) {
				String value = skuValueNames[i];
				String[] nv = value.split(":");
				// 外观
				String nv0 = nv[0];
				// 红色
				String nv1 = nv[1];
				Example nv0Example = new Example(ProductSpecification.class);
				nv0Example.createCriteria().andEqualTo("specification", nv0).andEqualTo("productId", productId);
				Long specId = productSpecificationService.selectByExample(nv0Example).get(0).getId();
				Example skuSpecExample = new Example(ProductSpecificationValue.class);
				skuSpecExample.createCriteria().andEqualTo("specificationId", specId).andEqualTo("specValue", nv1);
				String specValueId = productSpecificationValueService.selectByExample(skuSpecExample).get(0).getId()
						.toString();
				if (i == skuValueNames.length - 1) {
					sb.append(specValueId);
				} else {
					sb.append(specValueId + ",");
				}
			}
			sku.setSpecificationIds(sb.toString());
			sku.setProductId(productId);
			sku.setCreateTime(new Date());
			sku.setModifiedTime(new Date());
			productSkuService.save(sku);
		}
	}

	@PreAuthorize("hasAuthority('product:delete')")
	@RequestMapping("/product/delete")
	@ResponseBody
	public ResponseBo deleteProducts(String ids) {
		try {
			this.productService.deleteProducts(ids);
			// 删除索引
			String[] arrs = ids.split(",");
			for (int i = 0; i < arrs.length; i++) {
				this.luceneService.deleteProductIndexById(arrs[i]);
			}
			return ResponseBo.ok("删除商品成功！");
		} catch (Exception e) {
			log.error("删除商品失败", e);
			return ResponseBo.error("删除商品失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/product/excel")
	@ResponseBody
	public ResponseBo productExcel(Product product, HttpServletRequest request) {
		try {
			String categoryIdStr = request.getParameter("categoryId");
			Long categoryId = Long.parseLong(categoryIdStr);
			List<ProductCategory> categorys = productCategoryService.findProductCategoryByPid(categoryId);
			List<Long> ids = new ArrayList<Long>();
			for (ProductCategory category : categorys) {
				ids.add(category.getId());
			}
			List<Product> list = this.productService.findAllProducts(product, ids);
			return FileUtils.createExcelByPOIKit("商品表", list, Product.class);
		} catch (Exception e) {
			log.error("导出商品信息Excel失败", e);
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/product/csv")
	@ResponseBody
	public ResponseBo productCsv(Product product, HttpServletRequest request) {
		try {
			String categoryIdStr = request.getParameter("categoryId");
			Long categoryId = Long.parseLong(categoryIdStr);
			List<ProductCategory> categorys = productCategoryService.findProductCategoryByPid(categoryId);
			List<Long> ids = new ArrayList<Long>();
			for (ProductCategory category : categorys) {
				ids.add(category.getId());
			}
			List<Product> list = this.productService.findAllProducts(product, ids);
			return FileUtils.createCsv("商品表", list, Product.class);
		} catch (Exception e) {
			log.error("获取商品信息Csv失败", e);
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}

	/**
	 * 
	 * 重建商品索引，可能会比较慢
	 */
	@RequestMapping("/product/reIndexs")
	@ResponseBody
	public ResponseBo reIndexs() {
		luceneService.deleteAllIndex();

		try {
			for (int i = 0; i < 1000; i++) {
				RowBounds rowBounds = new RowBounds(i * 200, 200); // 每次查询20条
				Example example = new Example(Product.class);
				PageInfo pageInfo = new PageInfo<>(
						(List) productService.selectByExampleAndRowBounds(example, rowBounds));
				List<Product> products = pageInfo.getList();
				if (null == products || products.isEmpty()) {
					break;
				} else {
					luceneService.createProductIndex(products);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseBo.ok();
	}
}
