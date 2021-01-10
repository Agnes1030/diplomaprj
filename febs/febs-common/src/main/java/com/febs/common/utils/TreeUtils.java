package com.febs.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.febs.common.domain.Tree;

public class TreeUtils {

	protected TreeUtils() {

	}

	public static <T> void setActiveNode(List<Tree<T>> nodes, String nodeId) {
		for (Tree<T> node : nodes) {
			if (node.getId().equals(nodeId)) {
				node.setChecked(true);
				setActiveNode(node.getChildren(), node.getParentId());
			} else {
				node.setChecked(false);
				setActiveNode(node.getChildren(), node.getId());
			}
		}
	}

	public static <T> List<Tree<T>> recursionTree(List<Tree<T>> nodes, String parentId) {
		List<Tree<T>> childNodes = new ArrayList<>();
		for (Tree<T> node : nodes) {
			String id = node.getId();
			String pid = node.getParentId();
			if (pid.equals(parentId)) {
				List<Tree<T>> subNodes = recursionTree(nodes, id);
				node.setChildren(subNodes);
				if (subNodes.size() > 0) {
					node.setChildren(true);
				}
				if (!parentId.equals("0")) {
					node.setHasParent(true);
				}
				childNodes.add(node);
			}
		}
		return childNodes;
	}

	public static <T> Tree<T> build(List<Tree<T>> nodes) {
		if (nodes == null) {
			return null;
		}
		List<Tree<T>> topNodes = recursionTree(nodes, "0");
		Tree<T> root = new Tree<>();
		root.setId("0");
		root.setParentId("");
		root.setHasParent(false);
		root.setChildren(true);
		root.setChecked(true);
		root.setChildren(topNodes);
		root.setText("全部");
		Map<String, Object> state = new HashMap<>(16);
		state.put("opened", true);
		root.setState(state);
		return root;
	}

	public static <T> Tree<T> buildByParent(List<Tree<T>> nodes, Tree<T> parentNode) {
		if (nodes == null) {
			return null;
		}
		List<Tree<T>> topNodes = recursionTree(nodes, parentNode.getId().toString());
		parentNode.setHasParent(false);
		parentNode.setChildren(true);
		parentNode.setChecked(true);
		parentNode.setChildren(topNodes);
		Map<String, Object> state = new HashMap<>(16);
		state.put("opened", true);
		parentNode.setState(state);
		return parentNode;
	}

	public static <T> Tree<T> buildTree(List<Tree<T>> nodes) {
		if (nodes == null) {
			return null;
		}
		List<Tree<T>> topNodes = new ArrayList<>();
		nodes.forEach(children -> {
			String pid = children.getParentId();
			if (pid == null || "0".equals(pid)) {
				topNodes.add(children);
				return;
			}
			for (Tree<T> parent : nodes) {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);
					children.setHasParent(true);
					parent.setChildren(true);
					return;
				}
			}
		});

		Tree<T> root = new Tree<>();
		root.setId("0");
		root.setParentId("");
		root.setHasParent(false);
		root.setChildren(true);
		root.setChecked(true);
		root.setChildren(topNodes);
		root.setText("根节点");
		Map<String, Object> state = new HashMap<>(16);
		state.put("opened", true);
		root.setState(state);
		return root;
	}

	public static <T> List<Tree<T>> buildList(List<Tree<T>> nodes, String idParam) {
		if (nodes == null) {
			return new ArrayList<>();
		}
		List<Tree<T>> topNodes = new ArrayList<>();
		nodes.forEach(children -> {
			String pid = children.getParentId();
			if (pid == null || idParam.equals(pid)) {
				topNodes.add(children);
				return;
			}
			nodes.forEach(parent -> {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);
					children.setHasParent(true);
					parent.setChildren(true);
				}
			});
		});
		return topNodes;
	}
}