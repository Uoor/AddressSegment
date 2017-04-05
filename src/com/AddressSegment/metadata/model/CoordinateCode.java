package com.AddressSegment.metadata.model;
import java.util.LinkedHashMap;
import java.util.Map;


public class CoordinateCode<E, W> {
	private Map<E, W> coordinate = null;
	
	public CoordinateCode(){
		coordinate = new LinkedHashMap<E, W>();
	}

	public Map<E, W> getCoordinate() {
		return coordinate;
	}

	public void addCoordinate(E e, W w) {
		this.getCoordinate().put(e, w);
	}	
}
