package com.febs.security.xss;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

/**
 * Xss过滤工具
 *
 */
public class JsoupUtil {

	protected JsoupUtil() {

	}

	/**
	 * 使用自带的basicWithImages 白名单
	 * 允许的便签有a,b,blockquote,br,cite,code,dd,dl,dt,em,i,li,ol,p,pre,q,small,span,
	 * strike,strong,sub,sup,u,ul,img
	 * 以及a标签的href,img标签的src,align,alt,height,width,title属性
	 */
	private static final Whitelist whitelist = new Whitelist()
			.addTags("a", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup", "dd", "div", "dl",
					"dt", "em", "h1", "h2", "h3", "h4", "h5", "h6", "i", "img", "li", "ol", "p", "pre", "q", "small",
					"span", "strike", "strong", "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u",
					"ul")

			.addAttributes("a", "href", "title").addAttributes("blockquote", "cite")
			.addAttributes("col", "span", "width").addAttributes("colgroup", "span", "width")
			.addAttributes("img", "align", "alt", "height", "src", "title", "width")
			.addAttributes("ol", "start", "type").addAttributes("q", "cite").addAttributes("table", "summary", "width")
			.addAttributes("td", "abbr", "axis", "colspan", "rowspan", "width")
			.addAttributes("th", "abbr", "axis", "colspan", "rowspan", "scope", "width").addAttributes("ul", "type")

			.addProtocols("a", "href", "ftp", "http", "https", "mailto")
			.addProtocols("blockquote", "cite", "http", "https").addProtocols("cite", "cite", "http", "https")
			// .addProtocols("img", "src", "http", "https") 暂时关闭，线上可能要开启。
			.addProtocols("q", "cite", "http", "https");
	/*
	 * 配置过滤化参数,不对代码进行格式化
	 */
	private static final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
	static {
		/*
		 * 富文本编辑时一些样式是使用style来进行实现的 比如红色字体 style="color:red;" 所以需要给所有标签添加style属性
		 */
		// addAttributes(":all", "style","class") 所有的元素都支持style和class属性
		whitelist.addTags("section", "script").addAttributes(":all", "style", "class");
	}

	public static String clean(String content) {
		return Jsoup.clean(content, "", whitelist, outputSettings);
	}

}
