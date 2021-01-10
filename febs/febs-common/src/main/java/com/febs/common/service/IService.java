package com.febs.common.service;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IService<T> {

	List<T> selectAll();

	T selectByKey(Object key);

	int save(T entity);

	int delete(Object key);

	int batchDelete(List<String> list, String property, Class<T> clazz);

	int updateAll(T entity);

	int updateNotNull(T entity);

	T selectOneByExample(Object example);

	List<T> selectByExample(Object example);

	List<T> selectByExampleAndRowBounds(Object example, RowBounds rowBounds);

	int deleteByExample(Object example);
}