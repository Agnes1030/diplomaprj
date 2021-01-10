package com.febs.system.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.config.MyMapper;
import com.febs.common.domain.QueryRequest;
import com.febs.common.service.impl.BaseService;
import com.febs.system.dao.DictMapper;
import com.febs.system.domain.Dict;
import com.febs.system.service.DictService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("dictService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl extends BaseService<Dict> implements DictService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DictMapper dictMapper;

	@Override
	public List<Dict> findAllDicts(Dict dict, QueryRequest request) {
		try {
			Example example = new Example(Dict.class);
			Criteria criteria = example.createCriteria();
			if (StringUtils.isNotBlank(dict.getKeyy())) {
				criteria.andCondition("keyy=", Long.valueOf(dict.getKeyy()));
			}
			if (StringUtils.isNotBlank(dict.getValuee())) {
				criteria.andCondition("valuee=", dict.getValuee());
			}
			if (StringUtils.isNotBlank(dict.getTableName())) {
				criteria.andCondition("table_name=", dict.getTableName());
			}
			if (StringUtils.isNotBlank(dict.getFieldName())) {
				criteria.andCondition("field_name=", dict.getFieldName());
			}
			example.setOrderByClause("dict_id");
			return this.selectByExample(example);
		} catch (Exception e) {
			log.error("获取字典信息失败", e);
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional
	public void addDict(Dict dict) {
		this.save(dict);
	}

	@Override
	@Transactional
	public void deleteDicts(String dictIds) {
		List<String> list = Arrays.asList(dictIds.split(","));
		this.batchDelete(list, "dictId", Dict.class);
	}

	@Override
	@Transactional
	public void updateDict(Dict dict) {
		this.updateNotNull(dict);
	}

	@Override
	public List<Dict> findDictByFieldName(String fieldName) {
		return this.dictMapper.findDictByFieldName(fieldName);
	}

	@Override
	public Dict findDictByFieldNameAndKeyy(String fieldName, String keyy) {
		return this.dictMapper.findDictByFieldNameAndKeyy(fieldName, keyy);
	}

	@Override
	public Dict findById(Long dictId) {
		return this.selectByKey(dictId);
	}

}
