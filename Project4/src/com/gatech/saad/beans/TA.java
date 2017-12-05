package com.gatech.saad.beans;

public class TA {

	private String id;
	private String course;
	
	public TA(String id) 
	{
		this.id = id;
	}
	
	public TA(String id, String course) {
		this.id = id;
		this.course = course;
	}
	
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	
	public String getCourse() { return course; }
	public void setCourse(String course) { this.course = course; }
}
