package com.AddressSegment.metadata.model;

public class Word extends BaseModel {
    private String Name;
    private String Nominal;
    
    public Word(String wordName){
    	Name = wordName;
    }
    
    public Word(String wordName, String wordNominal){
    	Name = wordName;
    	Nominal = wordNominal;
    }
    
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getNominal() {
		return Nominal;
	}
	public void setNominal(String nominal) {
		Nominal = nominal;
	}
	
}
