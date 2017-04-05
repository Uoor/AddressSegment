package com.AddressSegment.logic.service;

import com.AddressSegment.metadata.model.CoordinateCode;

public interface AddressEncoding<E, W> {
	
	public CoordinateCode<E, W> addressEncoding(String address);
	public CoordinateCode<E, W> addressEncoding(String[] address);

}
