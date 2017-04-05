package com.AddressSegment.logic.service;

import com.AddressSegment.tool.dao.declare.AddressTageRegexDao;

public class AddressTageMaking extends AddressTageRegexDao {

	public AddressTageMaking() {
	}

	public AddressTageMaking(String strPattern) {
		super(strPattern);
		// TODO Auto-generated constructor stub
	}

	public String TageMaking(String word) {
		String result = null;
		if (null != word) {
			if (false == RE.matcher(word).matches()) {
				if (false == RO.matcher(word).matches()) {
					if (false == NU.matcher(word).matches()) {
						if (false == NUM.matcher(word).matches()) {
							if (false == NUB.matcher(word).matches()) {
								if (false == NUS.matcher(word).matches()) {
									if (false == POI.matcher(word).matches()) {
										result = "OT";
									} else {
										return "POI";
									}
								} else {
									result = "NUS";
								}
							} else {
								result = "NUB";
							}
						} else {
							result = "NUM";
						}
					} else {
						result = "NU";
					}
				} else {
					result = "RO";
				}
			} else {
				result = "RE";
			}
			return result;
		} else {
			return " ";
		}
	}

}
