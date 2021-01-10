package com.febs.web.utils;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

public class UploadUtil {

	public static String uploadImg(MultipartFile[] file) {
		// 接收处理后的文件名
		String fileName = "";

		// 判断file数组不能为空并且长度大于0
		if (file != null && file.length > 0) {
			// 循环获取file数组中得文件
			for (int i = 0; i < file.length; i++) {
				MultipartFile multipartFile = file[i];

				// 判断文件是否为空
				if (!multipartFile.isEmpty()) {
					try {
						// 上传文件的源文件名
						String oldFileName = multipartFile.getOriginalFilename();

						// 处理后的文件名
						fileName = StringUtils.substringBeforeLast(oldFileName, ".") + "-" + System.currentTimeMillis()
								+ "." + StringUtils.substringAfterLast(oldFileName, ".");

						// 存储在当前web服务路径下的upload路径
						String path = ResourceUtils.getURL("classpath:").getPath();
						String destPath = path + "static" + File.separator + "upload" + File.separator;
						// 判断该路径是否存在,如果不存在则创建该路径
						File destFile = new File(destPath);
						if (!destFile.exists()) {
							destFile.mkdirs();
						}
						// 文件最终保存的路径
						String filePath = destPath + fileName;
						// 文件保存路径
						// String filePath =
						// govProperties.getFileUploadPath()+file.getOriginalFilename();
						// 转存文件
						multipartFile.transferTo(new File(filePath));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return fileName;
	}
}
