package com.AddressSegment.logic;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.hadoop.fs.FileSystem;

import com.AddressSegment.logic.service.AddressSplit;
import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;

public class AddressSplitImpl implements AddressSplit {

	@Override
	public ArrayList<String> Split(String strAddress, WordDictionary inWordDict, CharDictionary<String> inCharDict, FileSystem inFs) throws URISyntaxException, IOException {
		// TODO Auto-generated method stub
		AlgorithmDaoImpl algImpl = new AlgorithmDaoImpl(inWordDict, inCharDict, inFs);
		return algImpl.runRMM(strAddress);
	}

	@Override
	public ArrayList<String> Split(String strAddress, FileSystem inFs) throws URISyntaxException, IOException {
		// TODO Auto-generated method stub
		AlgorithmDaoImpl algImpl = new AlgorithmDaoImpl(inFs);
		return algImpl.runRMM(strAddress);
	}
}
