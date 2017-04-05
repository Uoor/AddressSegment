package com.AddressSegment.tool.dao.impl;

import com.AddressSegment.tool.dao.declare.NominalClassificationDao;
import com.AddressSegment.tool.dao.service.NominalClassificationInterface;

public class NominalClassificationImpl extends NominalClassificationDao implements NominalClassificationInterface {

	@Override
	public String matchNominalClassification(String original) {
		// TODO Auto-generated method stub
		if (d.matcher(original).matches()) {
			return "d";
		}else if(b.matcher(original).matches()){
			return "b";
		}else if(r.matcher(original).matches()){
			return "r";
		}
		return null;
	}

}
