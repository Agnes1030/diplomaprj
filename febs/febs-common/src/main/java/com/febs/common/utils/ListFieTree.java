package com.febs.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.febs.common.domain.Tree;

public class ListFieTree {
	private static Integer id = 0;// 因为测试使用，当初主键id来用

	public static void main(String[] args) {
		// 默认路径，扫描此文件夹下面的所有文件
		String filepath = "/Users/wangtao/workspace/febs/febs-web/src/main/resources/templates/themes";
		File file = new File(filepath);
		int parentid = 0; // 初始化父节点id
		List<Tree> list = getFileList(filepath);
		for (int i = 0; i < list.size(); i++) {
			Tree tree = list.get(i);
			System.out.println("id:" + tree.getId() + "  parentId:" + tree.getParentId() + "==" + tree.getPath());
		}

	}
	public static List<Tree> getFileList(String filePath){
		return getFileList(filePath,"0");
	}
	public static List<Tree> getFileList(String filepath, String parentId) {
		List<Tree> fileList = new ArrayList<Tree>();
		File file = new File(filepath);
		if(!file.exists()) {
			return fileList;
		}
		File[] childs = file.listFiles();
		for (int i = 0; i < childs.length; i++) {
			File child = childs[i];
			String fileName = child.getName();
			String path = child.getAbsolutePath();
			Tree tree = new Tree();
			String childId = (++id).toString();
			tree.setId(childId);
			tree.setParentId(parentId);
			tree.setText(fileName);
			tree.setPath(path);
			if (child.isDirectory()) {
				List<Tree> subChilds = getFileList(path, childId);
				tree.setChildren(subChilds);
			}
           fileList.add(tree);
		}
		return fileList;

	}

}
