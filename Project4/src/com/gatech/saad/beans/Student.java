package com.gatech.saad.beans;

import java.util.ArrayList;

/*
 * Student Class
 * Blaine Spear
 * bspear8
 * CS6310 Project 1
 * 
 * Tracks student ID, desired courses.
 * Some attributes and methods were left in for future expansion.
 */
public class Student {

  private String studentID; //Unique to each student

  private ArrayList<String> desiredCourses; //An array of course IDs that the student wishes to take

  private ArrayList<String>  takenCourses; //Array containing all course IDs of taken courses

  private ArrayList<String>  registeredCourses; //An array that contains all the course IDs the student is currently registered for
  
  private Integer maxCourses;
  
  private Integer creditHours;

  public Student(String theStudentID) {
	  studentID = theStudentID;
	  desiredCourses = new ArrayList<String>();
	  maxCourses = 2;
	  creditHours = 0;
  }
  
  /*
   * Creates a Student with an ID number and an array of course IDs that the student desires to take
   */  
  public Student(String theStudentID, Integer maxCourseLoad, Integer hours){
	  studentID = theStudentID;
	  desiredCourses = new ArrayList<String>();
	  maxCourses = maxCourseLoad;
	  creditHours = hours;
  }
  
  public Student(Student newStudent){
	  studentID = newStudent.getID();
	  desiredCourses = newStudent.getDesiredCourses();
  }

  /*
   * Returns a student's ID number (for example 0-600)
   */
  public String getID() {
	  return studentID;
  }
  
  public ArrayList<String>  getDesiredCourses(){
	  return desiredCourses;
  }

  public void addDesiredCourse(String course) {
	  desiredCourses.add(course);
  }
  
  /*
   * Returns true if the student has already taken the course passed
   */
  public Boolean hasTakenCourse(Integer courseID) {
	  if(takenCourses.contains(courseID)){
		  return true;
	  }
	  else{
		  return false;
	  }
  }

  /*
   * Assumed to be called after a semester is completed (end turn)
   * Move all courseIDs from registered to taken... empty registered array.
   */
  public void passClasses(){
	  for(int i = 0; !registeredCourses.isEmpty(); i++){
		  takenCourses.add(registeredCourses.remove(i));
	  }
  }
  
  /*
   * Adds class to registered courses list
   */
  public void enroll(String courseID){
	  registeredCourses.add(courseID);
  }
  
  /*
   * Returns number of courses currently registered in
   */
  public ArrayList<String> getCoursesReg() {
	  return registeredCourses;
  }

  /*
   * Returns any array filled with the course IDs of every course this student has taken
   */
  public ArrayList<String> getCoursesTaken() {
	  return takenCourses;
  }

	/**
	 * @return the maxCourses
	 */
	public Integer getMaxCourses() {
		return maxCourses;
	}
	
	/**
	 * @param maxCourses the maxCourses to set
	 */
	public void setMaxCourses(Integer maxCourses) {
		this.maxCourses = maxCourses;
	}
	
	/**
	 * 
	 * @return the number of credit hours taken by this student
	 */
	public Integer getCreditHours()
	{
		return creditHours;
	}
	
	public void setCreditHours(Integer hours) {
		creditHours = hours;
	}
}
