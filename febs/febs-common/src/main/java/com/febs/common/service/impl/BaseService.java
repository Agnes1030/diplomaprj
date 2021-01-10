package com.febs.common.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.IService;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public abstract class BaseService<T> implements IService<T> {

	@Autowired
	protected Mapper<T> mapper;

	public Mapper<T> getMapper() {
		return mapper;
	}


	@Override
	public List<T> selectAll() {
		return getMapper().selectAll();
	}

	@Override
	public T selectByKey(Object key) {
		return getMapper().selectByPrimaryKey(key);
	}

	@Override
	@Transactional
	public int save(T entity) {
		return getMapper().insert(entity);
	}

	@Override
	@Transactional
	public int delete(Object key) {
		return getMapper().deleteByPrimaryKey(key);
	}

	@Override
	@Transactional
	public int batchDelete(List<String> list, String property, Class<T> clazz) {
		Example example = new Example(clazz);
		example.createCriteria().andIn(property, list);
		return this.getMapper().deleteByExample(example);
	}

	@Override
	@Transactional
	public int updateAll(T entity) {
		return getMapper().updateByPrimaryKey(entity);
	}

	@Override
	@Transactional
	public int updateNotNull(T entity) {
		return getMapper().updateByPrimaryKeySelective(entity);
	}

	@Override
	@Transactional
	public int deleteByExample(Object example) {
		return getMapper().deleteByExample(example);
	}

	@Override
	public List<T> selectByExample(Object example) {
		return getMapper().selectByExample(example);
	}

	@Override
	public T selectOneByExample(Object example) {
		return getMapper().selectOneByExample(example);
	}

	@Override
	public List<T> selectByExampleAndRowBounds(Object example, RowBounds rowBounds) {
		return getMapper().selectByExampleAndRowBounds(example, rowBounds);
	}
}
