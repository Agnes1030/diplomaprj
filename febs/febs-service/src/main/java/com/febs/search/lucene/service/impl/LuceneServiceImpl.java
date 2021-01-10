package com.febs.search.lucene.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleDocValuesField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.febs.common.domain.QueryRequest;
import com.febs.search.lucene.service.LuceneService;
import com.febs.shop.domain.Product;
import com.github.pagehelper.PageInfo;

/**
 * @ClassName LuceneServiceImpl
 **/
@Service
public class LuceneServiceImpl implements LuceneService {
	@Autowired
	private IndexWriter indexWriter;

	@Autowired
	private Analyzer analyzer;

	@Autowired
	private SearcherManager searcherManager;

	@Override
	public void createProductIndex(List<Product> productList) throws IOException {
		for (Product p : productList) {
			this.createProductIndex(p);
		}
	}

	@Override
	public PageInfo searchProduct(QueryRequest queryRequest, Product product) throws Exception, ParseException {
		searcherManager.maybeRefresh();
		IndexSearcher indexSearcher = searcherManager.acquire();
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		Sort sort = new Sort();
		// 排序规则
		sort.setSort(new SortField("id", SortField.Type.LONG, true));

		// 模糊匹配,匹配词
		String keyStr = product.getTitle();
		if (product != null && product.getTitle() != null) {
			// 输入空格,不进行模糊查询
			if (!"".equals(keyStr.replaceAll(" ", ""))) {
				builder.add(new QueryParser("title", analyzer).parse(keyStr), BooleanClause.Occur.MUST);
			}
		}
		// 按keywords检索分类
		String keywrods = product.getKeywords();
		if (product != null && product.getKeywords() != null) {
			// 输入空格,不进行模糊查询
			if (!"".equals(keywrods.replaceAll(" ", ""))) {
				builder.add(new QueryParser("keywords", analyzer).parse(keywrods), BooleanClause.Occur.MUST);
			}
		}
		// 精确查询
		if (product != null && product.getId() != null) {
			builder.add(new TermQuery(new Term("id", String.valueOf(product.getId()))), BooleanClause.Occur.MUST);
		}
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageNum(queryRequest.getPageNum() == 0 ? 1 : queryRequest.getPageNum());
		pageInfo.setPageSize(queryRequest.getPageSize() == 0 ? 10 : queryRequest.getPageSize());

		Query query = builder.build();
		ScoreDoc sd;
		int currentPage = pageInfo.getPageNum();
		int pageSize = pageInfo.getPageSize();
		if (pageInfo.getPageNum() == 1) {
			sd = null;
		} else {
			int num = pageSize * (currentPage - 1);// 获取上一页的最后是多少
			TopDocs td = indexSearcher.search(query, num, sort);
			sd = td.scoreDocs[num - 1];
		}
		TopDocs topDocs = indexSearcher.searchAfter(sd, query, currentPage * pageSize, sort);
		pageInfo.setTotal(topDocs.totalHits.value);
		ScoreDoc[] hits = topDocs.scoreDocs;
		List<Product> pList = new ArrayList<Product>();
		for (int i = 0; i < hits.length; i++) {
			Document doc = indexSearcher.doc(hits[i].doc);
			Product item = new Product();
			item.setId(Long.parseLong(doc.get("id")));
			item.setTitle(doc.get("title"));
			item.setThumbnail(doc.get("thumbnail"));
			item.setKeywords(doc.get("keywords"));
			item.setSummary(doc.get("summary"));
			item.setPrice(new BigDecimal(doc.get("price")));
			if (null != doc.get("originPrice")) {
				item.setOriginPrice(new BigDecimal(doc.get("originPrice")));
			}
			pList.add(item);
		}
		pageInfo.setList(pList);
		return pageInfo;
	}

	public void deleteProductIndexById(String id) throws IOException {
		indexWriter.deleteDocuments(new Term("id", id));
		indexWriter.commit();
	}

	@Override
	public void createProductIndex(Product product) throws IOException {
		Document doc = new Document();
		doc.add(new StringField("id", product.getId().toString(), Field.Store.YES));
		doc.add(new TextField("title", product.getTitle(), Field.Store.YES));
		doc.add(new TextField("keywords", product.getKeywords(), Field.Store.YES));
		doc.add(new TextField("thumbnail", product.getThumbnail(), Field.Store.YES));
		doc.add(new TextField("summary", product.getSummary(), Field.Store.YES));
		doc.add(new TextField("linkUrl", product.getLinkUrl(), Field.Store.YES));

		doc.add(new DoubleDocValuesField("id", product.getId()));
		// 存储到索引库
		doc.add(new StoredField("id", product.getId()));
		BigDecimal price = product.getPrice();
		doc.add(new StoredField("price", product.getPrice().toPlainString()));
		if (null != product.getOriginPrice() && product.getOriginPrice().longValue() > 0) {
			doc.add(new StoredField("originPrice", product.getOriginPrice().toPlainString()));
		}
		// 正排索引用于排序、聚合
		doc.add(new DoubleDocValuesField("price", price.doubleValue()));
		indexWriter.addDocument(doc);
		indexWriter.commit();

	}

	@Override
	public void deleteAllIndex() {
		try {
			indexWriter.deleteAll();
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
