package com.AddressSegment.logic.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IHttpResponseHandler 
{
	String handleHttpResponse(Map<String, List<String>> responseHeader, InputStream responseStream);
}
