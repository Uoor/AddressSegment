package com.AddressSegment.logic.service;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.hadoop.fs.FileSystem;

import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;


public interface AddressSplit {
	public ArrayList<String> Split(String strAddress, FileSystem fs) throws URISyntaxException, IOException;
	public ArrayList<String> Split(String strAddress, WordDictionary inWordDict, CharDictionary<String> inCharDict, FileSystem inFs) throws URISyntaxException, IOException;
}
