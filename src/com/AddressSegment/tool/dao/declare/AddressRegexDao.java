package com.AddressSegment.tool.dao.declare;

import java.util.regex.Pattern;

public class AddressRegexDao {
	protected Pattern p = Pattern.compile(
			"[甲乙丙丁上下东西南北一二三四五六七八九十百千万ａ-ｚＡ-Ｚa-zA-Z0-9０-９~`\\-_=+;:,.?/!@#$%^&*～·－——＋＝：；“‘”’《，》。？、！＠＃￥％……＆×|(\\d*)|<\\d*>|（\\d*）]*[弄|支|临|号|座|楼|号|单|馆|片|块|栋|幢|层|室][弄|号|楼|库|体|元|半|门]*");
	//protected Pattern q = Pattern.compile("(0-9)*(11+)(0-9)*");
	protected Pattern q = Pattern.compile("11+");
	protected Pattern inputPattern = null;
	
	public AddressRegexDao() {
	}
	
	public Pattern getInputPattern() {
		return inputPattern;
	}

	public void setInputPattern(Pattern inputPattern) {
		this.inputPattern = inputPattern;
	}
	
	public void setInputPattern(String inputPattern) {
		this.inputPattern = Pattern.compile(inputPattern);
	}

	public AddressRegexDao(String strPattern) {
		inputPattern = Pattern.compile(strPattern);
	}

	
}
