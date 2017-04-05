package com.AddressSegment.logic;

import com.AddressSegment.logic.service.AddressEncoding;
import com.AddressSegment.metadata.model.CoordinateCode;

public class AddressEncodingService implements AddressEncoding<String, String> {
	private GaodeEncodingServiceInvoker invoker;

	@Override
	public CoordinateCode<String, String> addressEncoding(String address) {
		// TODO Auto-generated method stub
		invoker = new GaodeEncodingServiceInvoker();
		return invoker.getGaodeResult(address);
	}

	@Override
	public CoordinateCode<String, String> addressEncoding(String[] address) {
		// TODO Auto-generated method stub
		invoker = new GaodeEncodingServiceInvoker();
		for(int i = 0; i < address.length; i++){
			invoker.getGaodeResult(address[i]);
		}
		return invoker.getCoordinate();
	}

}
