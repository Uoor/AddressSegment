package com.AddressSegment.tool.dao.service;

public interface RegexMatchInterface {
	public boolean matchAddressRegex(String original);
	public void matchUnidentifiedWord(String sequence);
}
