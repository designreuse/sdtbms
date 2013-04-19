package com.bus.util;

public class SelectBoxOption {
	private String label;
	private String value;
	
	public SelectBoxOption(String label, String value){
		this.label = label;
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	
}
