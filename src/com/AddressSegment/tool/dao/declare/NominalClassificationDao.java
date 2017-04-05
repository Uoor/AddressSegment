package com.AddressSegment.tool.dao.declare;

//import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NominalClassificationDao {
	protected Pattern r = Pattern.compile("?路");
	protected Pattern b = Pattern.compile("?弄|?支弄|?临号|?号|?号楼|?单元|?座|?馆|?片|?块|?栋|?幢|?层|?层半|?室|?区");
	protected Pattern d = Pattern.compile("黄浦区|卢湾区|徐汇区|长宁区|静安区|普陀区|闸北区|虹口区|杨浦区|宝山区|闵行区|嘉定区|浦东新区|松江区|金山区|青浦区|南汇区|奉贤区|崇明县|?镇");
	//protected Matcher m = null;
}
