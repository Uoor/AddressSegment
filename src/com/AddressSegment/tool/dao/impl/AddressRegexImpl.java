package com.AddressSegment.tool.dao.impl;

import java.util.regex.Matcher;

import com.AddressSegment.tool.dao.declare.AddressRegexDao;
import com.AddressSegment.tool.dao.service.RegexMatchInterface;

public class AddressRegexImpl extends AddressRegexDao implements RegexMatchInterface {
	
	protected Matcher m = null;

	public boolean matchAddressRegex(String original) {
		// TODO Auto-generated method stub
		m = p.matcher(original);
		return m.matches();
	}

	@Override
	public void matchUnidentifiedWord(String sequence) {
		// TODO Auto-generated method stub
		m = q.matcher(sequence);
	}

	public boolean matchCustomizePattern(String sequence, String strPattern) {
		// TODO Auto-generated method stub
		this.setInputPattern(strPattern);
		m = inputPattern.matcher(sequence);
		return m.matches();
	}
	
	public Matcher getM() {
		return m;
	}
	
	public boolean findMatchWord() {
		return m.find();
	}
	
	public boolean hitEnd(){
		return m.hitEnd();
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
