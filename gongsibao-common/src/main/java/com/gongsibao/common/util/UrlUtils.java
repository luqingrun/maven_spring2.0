package com.gongsibao.common.util;

import org.apache.commons.collections.CollectionUtils;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author YanBing
 * 
 */
public class UrlUtils {
	
	/**
	 * 匹配路径是否在控制域的范围内
	 * 
	 * @param urls
	 * @param path
	 * @return
	 */
	public static boolean urlMatch(SortedSet<String> urls, String path) {

		if (CollectionUtils.isEmpty(urls)) {
			return false;
		}

		SortedSet<String> hurl = urls.headSet(path + "\0");
		SortedSet<String> turl = urls.tailSet(path + "\0");
		
		if(hurl.size() > 0) {
			String before = hurl.last();
			if(pathMatch(path, before))
				return true;
		}
		
		if(turl.size() > 0) {
			String after = turl.first();
			if(pathMatch(path, after))
				return true;
		}
		
		return false;
	}
	
	/**
	 * 匹配路径是否在控制域的范围内
	 * 
	 * @param path
	 * @param domain
	 * @return
	 */
	private static boolean pathMatch(String path, String domain) {
		if(PathPatternMatcher.isPattern(domain)) {
			return PathPatternMatcher.match(domain, path);
		} else {
			return domain.equals(path);
		}
	}
	
	public static void main(String[] args) {
		String path = "/gongsibao_uc/ucuser/user.do?a=inituser";
		
		SortedSet<String> urls = new TreeSet<String>();
		urls.add("/gongsibao_uc/ucuser/**");
		
		boolean b = urlMatch(urls, path);
		
		System.out.println(b);
	}
	
}
