package com.febs.web.controller.cms;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.febs.web.controller.base.WebBaseController;
import com.febs.web.controller.cms.vo.ImageUploadVo;

/**
 * Ckeditor 图片上传上相关
 */

@Controller
@RequestMapping(value = "/cms")
public class CmsCkeditorController extends WebBaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private Boolean haveCreatePath = false;

	/**
	 * 上传图片
	 *
	 * @param file
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ckeditorUpload")
	public ImageUploadVo ckeditorUpload(@RequestParam("upload") MultipartFile file, HttpServletRequest request)
			throws Exception {
		log.info("开始上传图片");
		// 获取文件名
		String fileName = file.getOriginalFilename();
		// 获取文件的后缀名
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		// 上传文件的源文件名
		String oldFileName = file.getOriginalFilename();

		// 处理后的文件名
		fileName = StringUtils.substringBeforeLast(oldFileName, ".") + "-" + System.currentTimeMillis() + suffixName;
		log.info("上传文件文件名称：{}", fileName);
		log.info("上传文件大小 ：{}" + file.getSize());
		// 存储在当前web服务路径下的upload路径
		String path = ResourceUtils.getURL("classpath:").getPath();
		String destPath = path + "static" + File.separator + "upload" + File.separator;
		// 判断该路径是否存在,如果不存在则创建该路径
		if (!haveCreatePath) {
			File destFile = new File(destPath);
			destFile.mkdirs();
			haveCreatePath = true;
		}
		// 文件最终保存的路径
		String filePath = destPath + fileName;
		// 转存文件
		file.transferTo(new File(filePath));
		ImageUploadVo imageUploadVo = new ImageUploadVo();
		imageUploadVo.setUploaded(1);
		imageUploadVo.setFileName(fileName);
		// 获取域名
		// String domainName=request.getServerName();
		// 获取端口号
		// int serverPort = request.getServerPort();
		// String imgUrl = "http://"+domainName+":"+serverPort+"/"+"upload/"+fileName;
		String imgUrl = "/" + "upload/" + fileName;
		imageUploadVo.setUrl(imgUrl);
		return imageUploadVo;
	}
}
