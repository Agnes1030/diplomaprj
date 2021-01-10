package com.febs.search.lucene.config;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.ControlledRealTimeReopenThread;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix = "febs.lucence")
public class LuceneConfig {
	/**
	 * lucene索引,存放位置
	 */
	public String indexdir;

	public String getIndexdir() {
		return indexdir;
	}

	public void setIndexdir(String indexdir) {
		this.indexdir = indexdir;
	}

	/**
	 * 创建一个 Analyzer 实例
	 *
	 * @return
	 */
	@Bean
	public Analyzer analyzer() {
		return new SmartChineseAnalyzer();
	}

	/**
	 * 索引位置
	 *
	 * @return
	 * @throws IOException
	 */
	@Bean
	public Directory directory() throws IOException {

		Path path = Paths.get(indexdir);
		File file = path.toFile();
		if (!file.exists()) {
			// 如果文件夹不存在,则创建
			file.mkdirs();
		}
		return FSDirectory.open(path);
	}

	/**
	 * 创建indexWriter
	 *
	 * @param directory
	 * @param analyzer
	 * @return
	 * @throws IOException
	 */
	@Bean
	public IndexWriter indexWriter(Directory directory, Analyzer analyzer) throws IOException {
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
		/**
		 * 清空索引,下面两个方法，打开时，启动应用时，会自动清空所有索引， 如果项目的数据比较多，建索引时间长或索引不能清空，这个一定要装着
		 * 所以大家要根据实际情况慎用此开关
		 */
		indexWriter.deleteAll();
		indexWriter.commit();
		return indexWriter;
	}

	/**
	 * SearcherManager管理
	 *
	 * @param directory
	 * @return
	 * @throws IOException
	 */
	@Bean
	public SearcherManager searcherManager(Directory directory, IndexWriter indexWriter) throws IOException {
		SearcherManager searcherManager = new SearcherManager(indexWriter, false, false, new SearcherFactory());
		ControlledRealTimeReopenThread cRTReopenThead = new ControlledRealTimeReopenThread(indexWriter, searcherManager,
				5.0, 0.025);
		cRTReopenThead.setDaemon(true);
		// 线程名称
		cRTReopenThead.setName("更新IndexReader线程");
		// 开启线程
		cRTReopenThead.start();
		return searcherManager;
	}

}
