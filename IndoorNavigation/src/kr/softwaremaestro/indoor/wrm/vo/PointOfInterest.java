package kr.softwaremaestro.indoor.wrm.vo;

import java.io.Serializable;
//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Untitled
//  @ File Name : PointOfInterest.java
//  @ Date : 2012-10-25
//  @ Author : 
//
//




public class PointOfInterest extends Point{
	private String name;
	
	public PointOfInterest() {
		super();
	}

	public PointOfInterest(Integer id, Double x, Double y, Double z, String name) {
		super(id, x, y, z);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
