package com.gatech.saad.beans;

public class Professor {
	
	private String id;
	private String course;
	
	public Professor(String id) 
	{
		this.id = id;
		this.course = null;
	}
	
	public Professor(String id, String course) {
		this.id = id;
		this.course = course;
	}
	
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	
	public String getCourse() { return course; }
	public void setCourse(String course) { this.course = course; }
}
