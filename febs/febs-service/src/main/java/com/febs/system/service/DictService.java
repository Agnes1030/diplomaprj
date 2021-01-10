package com.febs.system.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.febs.common.domain.QueryRequest;
import com.febs.common.service.IService;
import com.febs.system.domain.Dict;

import java.util.List;

@CacheConfig(cacheNames = "DictService")
public interface DictService extends IService<Dict> {

    @Cacheable(key = "#p0.toString() + (#p1 != null ? #p1.toString() : '')")
    List<Dict> findAllDicts(Dict dict, QueryRequest request);

    @Cacheable(key = "#p0")
    Dict findById(Long dictId);

    @CacheEvict(allEntries = true)
    void addDict(Dict dict);

    @CacheEvict(key = "#p0", allEntries = true)
    void deleteDicts(String dictIds);

    @CacheEvict(key = "#p0", allEntries = true)
    void updateDict(Dict dicdt);

    /**根据fieldName（字典编码）查询字典表集合
     *
     * @param fieldName
     * @return
     */
    @Cacheable(key = "#p0.toString()")
    List<Dict> findDictByFieldName(String fieldName);

    /**根据 fieldName（字典编码）, keyy（字典项值）查询字典表实体对象
     *
     * @param fieldName
     * @param keyy
     * @return
     */
    @Cacheable(key = "#p0.toString() + (#p1 != null ? #p1.toString() : '')")
    Dict findDictByFieldNameAndKeyy(String fieldName, String keyy);

}
