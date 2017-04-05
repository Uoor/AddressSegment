package com.AddressSegment.tool.dao.declare;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AddressTageRegexDao {
	protected Pattern RE = Pattern.compile("(黄浦|徐汇|长宁|静安|普陀|虹口|杨浦|闸北|闵行|宝山|嘉定|浦东新|金山|松江|青浦|奉贤|南汇|南市|南汇)区|.*[镇]|上海市|崇明县");
	protected Pattern RO = Pattern.compile(".*[路|街|道|街道]");
	//"[甲乙丙丁上下东西南北一二三四五六七八九十百千万ａ-ｚＡ-Ｚa-zA-Z0-9０-９~\`\"\\-_=+;:,<.>?/!@#$%^&*()～·－——＋＝：；“‘”’《，》。？、！＠＃￥％……＆×（）]*[弄|支|临|号|座|楼|号|单|馆|片|块|栋|幢|层|室][弄|号|楼|库|体|元|半|门]*");
	protected Pattern NU = Pattern.compile(".*[号|弄|支弄|临号]");
	protected Pattern NUB = Pattern.compile("[\u4e00-\u9fa5ａ-ｚＡ-Ｚa-zA-Z0-9０-９]*[座|单元|栋|幢|片|区|号楼|号库|单体|块|片|阁|库|楼]");
	protected Pattern NUM = Pattern.compile("[一二三四五六七八九十百千万ａ-ｚＡ-Ｚa-zA-Z0-9０-９~`\\-_=+;:,<.>?/!@#$%^&*()～·－——＋＝：；“‘”’《，》。？、！＠＃￥％……＆×（）]*(楼|层|楼半|层半)");
	protected Pattern NUS = Pattern.compile(".*[室]");
	protected Pattern POI = Pattern.compile(".*(一|二|三|四|五|六|七|八|九|十)*(保税区|开发区|新村|新城|城|苑|小区|公寓|解放|村|别墅|庭|学院|学校|小学|中学|大学|高中|幼儿园|托儿所|师大|职校|宅|机场|站|公园|医院|局|银行|所|巷|园区|广场|大厦|大楼|中心|商务楼|公司|工坊|店|市场|馆|市场|馆|厂|商城|餐厅|队|组|号门)");
	protected Pattern DI = Pattern.compile(".*[东|南|西|北|隔壁|旁边|前|后]。");
	//protected Pattern OT = Pattern.compile(".*");
	protected Pattern inputPattern = null;
	protected Matcher m = null;
	
	
	public Pattern getInputPattern() {
		return inputPattern;
	}
	
	public void setInputPattern(Pattern inputPattern) {
		this.inputPattern = inputPattern;
	}
	
	public void setInputPattern(String inputPattern) {
		this.inputPattern = Pattern.compile(inputPattern);
	}
	
	public AddressTageRegexDao() {
	}
	
	public AddressTageRegexDao(String strPattern) {
		inputPattern = Pattern.compile(strPattern);
	}
	
	public boolean findMatchWord() {
		return m.find();
	}
	
	public String getUnidentifiedWord() {
		return m.group(0);
	}
	
	public int getStart() {
		return m.start();
	}
	
	public int getEnd() {
		return m.end();
	}
	
	public int getGroupCount() {
		return m.groupCount();
	}
	
	public void resetMatch() {
		m.reset();
	}
}
