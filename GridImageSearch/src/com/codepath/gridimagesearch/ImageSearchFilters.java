package com.codepath.gridimagesearch;

import java.io.Serializable;

public class ImageSearchFilters implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String curSize;
	private String curColor;
	private String curType;
	private String site;
	
	public ImageSearchFilters() {
		curSize = null;
		curColor = null;
		curType = null;
		site = null;
	}
	
	public String getSize() {
		return curSize;
	}
	
	public String getColor() {
		return curColor;
	}
	
	public String getType() {
		return curType;
	}
	
	public String getSite() {
		return site;
	}
	
	public void setSize(String size) {
		curSize = size;
	}
	
	public void setColor(String color) {
		curColor = color;
	}
	
	public void setType(String type) {
		curType=type;
	}
	
	public void setSite(String site) {
		this.site = site;
	}
}
