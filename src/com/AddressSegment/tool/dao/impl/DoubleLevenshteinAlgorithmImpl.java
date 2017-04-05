package com.AddressSegment.tool.dao.impl;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hadoop.fs.FileSystem;

import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;
import com.AddressSegment.tool.dao.declare.DoubleLevenshteinAlgorithm;
import com.AddressSegment.tool.dao.service.DoubleLevenshteinAlgorithmInterface;

public class DoubleLevenshteinAlgorithmImpl extends DoubleLevenshteinAlgorithm implements DoubleLevenshteinAlgorithmInterface {
	
	public DoubleLevenshteinAlgorithmImpl(WordDictionary WDInput, CharDictionary<String> CDInput, FileSystem FSInput) throws URISyntaxException, IOException {
		super(WDInput, CDInput, FSInput);
		// TODO Auto-generated constructor stub
	}

	@Override
	public float DoubleLevenshtein(String S, String T) {
		// TODO Auto-generated method stub
		return this.run(S, T);
	}

}
