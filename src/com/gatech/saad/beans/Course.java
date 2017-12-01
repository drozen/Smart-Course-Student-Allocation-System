package com.gatech.saad.beans;

import java.util.ArrayList;

/*
Course Class
Blaine Spear
bspear8
CS6310 Project 1

Course Class tracks, name, preReq required (a limitation is that there can only be one preReq).
Some attributes and methods were left in for future expansion.
 */
public class Course {

  private Integer courseID; //Unique course ID numeric code
  
  private String courseName; //Actual name of course

  private Integer maxStudents; //Objective function goal -- we're striving to find the optimal number for this!

  private Integer preReq = 0; //Course ID for a pre-req course -- FUTURE CHANGE: turn into an array for limitless pre-reqs

  public ArrayList<Student> studentsReg; //An array of student objects - these are all the Students the course has


  public Course(Integer theCourseID, String name, Integer preReqCourseID){
	  courseID = theCourseID; //1-18
	  setCourseName(name);
	  if(preReqCourseID > 0){
		  preReq = preReqCourseID; //If 0, there is no preReq
	  }else{
		  preReq = 0;
	  }
  }
  
  public Integer getID() {
	  return courseID;
  }

  public Integer getMaxStudents() {
	  return maxStudents;
  }

  public Integer getPreReq() {
	  return preReq;
  }
  
  public void dropStudent(Student dropper){
	  studentsReg.remove(dropper);
  }

  public Integer getCurrentNumStudents() {
	  return studentsReg.size();
  }

  public ArrayList<Student> getStudents() {
	  return studentsReg;
  }

	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}
	
	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

}
