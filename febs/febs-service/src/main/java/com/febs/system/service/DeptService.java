package com.febs.system.service;

import java.util.List;

import com.febs.common.domain.Tree;
import com.febs.common.service.IService;
import com.febs.system.domain.Dept;

public interface DeptService extends IService<Dept> {

	Tree<Dept> getDeptTree();

	List<Dept> findAllDepts(Dept dept);

	Dept findByName(String deptName);

	Dept findById(Long deptId);
	
	void addDept(Dept dept);
	
	void updateDept(Dept dept);

	void deleteDepts(String deptIds);
}
