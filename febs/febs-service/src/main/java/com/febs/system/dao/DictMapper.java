package com.febs.system.dao;

import java.util.List;

import com.febs.common.config.MyMapper;
import com.febs.system.domain.Dict;
public interface DictMapper extends MyMapper<Dict> {

    List<Dict> findDictByFieldName(String fieldName);

    Dict findDictByFieldNameAndKeyy(String fieldName, String keyy);
}