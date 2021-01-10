package com.febs.web.controller.admin.fastdfs;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.febs.fastdfs.utils.FileDfsUtil;
import com.febs.web.controller.base.BaseController;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/admin")
public class FastFileController extends BaseController {
	@Resource
	private FileDfsUtil fileDfsUtil;

	/**
	 * http://localhost:7010/swagger-ui.html
	 * http://192.168.72.130/group1/M00/00/00/wKhIgl0n4AKABxQEABhlMYw_3Lo825.png
	 */
	@ApiOperation(value = "上传文件", notes = "测试FastDFS文件上传")
	@ResponseBody
	@RequestMapping(value = "/uploadFile", headers = "content-type=multipart/form-data", method = RequestMethod.POST)
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		String result;
		try {
			String path = fileDfsUtil.upload(file);
			if (!StringUtils.isEmpty(path)) {
				result = path;
			} else {
				result = "上传失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "服务异常";
		}
		return ResponseEntity.ok(result);
	}

	/**
	 * 文件删除
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteByPath", method = RequestMethod.GET)
	public ResponseEntity<String> deleteByPath() {
		String filePathName = "group1/M00/00/00/wKhIgl0n4AKABxQEABhlMYw_3Lo825.png";
		fileDfsUtil.deleteFile(filePathName);
		return ResponseEntity.ok("SUCCESS");
	}
}
