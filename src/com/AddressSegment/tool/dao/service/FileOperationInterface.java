package com.AddressSegment.tool.dao.service;


import org.apache.hadoop.util.LineReader;

import com.AddressSegment.metadata.model.WordDictionary;

public abstract interface FileOperationInterface {
	String readFileByLines(LineReader reader);
	void writeFileByLines(WordDictionary WordDict);
}
