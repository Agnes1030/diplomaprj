package com.febs.search.lucene.service;/**
 * Created by Administrator on 2019/11/12/012.
 */


import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.febs.common.domain.QueryRequest;
import com.febs.search.lucene.model.PageQuery;
import com.febs.shop.domain.Product;
import com.github.pagehelper.PageInfo;

/**
 * @ClassName ILuceneService
 **/
public interface LuceneService {
	public void createProductIndex(Product product) throws IOException ;
	/**
	 * 
	 */
	public void deleteAllIndex();
    /**
     * 增加索引
     * @param list
     * @throws IOException
     */
    public void createProductIndex(List<Product> list) throws IOException;

    /**
     * 查询
     * @param pageQuery
     * @return
     * @throws Exception
     * @throws ParseException
     */
    public PageInfo searchProduct(QueryRequest queryRequest,Product product) throws Exception, ParseException;

    /**
     *删除
     * @param id
     * @throws IOException
     */
    public void deleteProductIndexById(String id) throws IOException;
}
