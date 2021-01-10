package com.febs.web.controller.cms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.febs.common.domain.ResponseBo;
import com.febs.web.controller.base.WebBaseController;
import com.febs.web.utils.UploadUtil;

@Controller
public class CmsIndexController extends WebBaseController {
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@GetMapping("/")
	public void success(HttpServletRequest request, HttpServletResponse response) throws IOException {
		redirectStrategy.sendRedirect(request, response, "/index");
	}

	@RequestMapping(value = "/index")
	public String index(HttpServletResponse response) {
		return "forward:/page/index";
	}

	@PostMapping("/cms/filesUpload/thumbnail")
	@ResponseBody
	public ResponseBo thumbnailUpload(@RequestParam("file") MultipartFile[] file) {
		String fileName = UploadUtil.uploadImg(file);
		// 返回前端data数据中存储修改后的文件名
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("src", "/upload/" + fileName);
		return ResponseBo.ok(msg);
	}

	@PostMapping("/cms/filesUpload/logo")
	@ResponseBody
	public ResponseBo logoUpload(@RequestParam("site_logo_file") MultipartFile[] file) {
		String fileName = UploadUtil.uploadImg(file);
		// 返回前端data数据中存储修改后的文件名
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("src", "/upload/" + fileName);
		return ResponseBo.ok(msg);
	}

	@PostMapping("/cms/filesUpload/banner")
	@ResponseBody
	public ResponseBo bannerUpload(@RequestParam("bannerFile") MultipartFile[] file) {
		String fileName = UploadUtil.uploadImg(file);
		// 返回前端data数据中存储修改后的文件名
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("src", "/upload/" + fileName);
		return ResponseBo.ok(msg);
	}
}
