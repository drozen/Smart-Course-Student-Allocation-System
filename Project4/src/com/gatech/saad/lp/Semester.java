package com.gatech.saad.lp;

import java.util.ArrayList;

/*
 * Semester Class
 * Blaine Spear
 * bspear8
 * CS6310 Project 1
 * 
 * Tracks time & courses available in fall and spring as well as courses available always.
 */
public class Semester {

  private ArrayList<Integer> fallCourses;
  
  private ArrayList<Integer> springCourses;
  
  private ArrayList<Integer> universalCourses;
  
  private int semesterValue;

  private String semesterTime;

  public Semester(){
	  semesterTime = "Fall2015";
	  semesterValue = 1;
  }
  
  public Semester(String time, ArrayList<Integer> fall, ArrayList<Integer> spring, ArrayList<Integer> all){
	  semesterTime = time;
	  setFallCourses(fall);
	  setSpringCourses(spring);
	  universalCourses = all;
	  semesterValue = 1;
  }

  public String getSemesterTime() {
	  return semesterTime;
  }
  
  /*
   * Civilization anyone?
   * This method/function/operation determines whether it's fall, spring, or summer, then determines the year, then increments
   * time appropriately. For example, a Fall 2014 will become a Spring 2015, and so on.
   */
  public int endTurn(){
	  String[] splinter = semesterTime.split(" ");
	  if(splinter[0].equals("Fall")){
		  splinter[1] = String.valueOf(Integer.parseInt(splinter[1])+1);
		  splinter[0] = "Spring";
	  }else if(splinter[0].equals("Spring")){
		  splinter[0] = "Summer";
	  }else{
		  splinter[0] = "Fall";
	  }
	  semesterTime = splinter[0]+" "+splinter[1];
	  semesterValue++;
	  return semesterValue;
  }

  
  public boolean isSpring(){
	  String[] splinter = semesterTime.split(" ");
	  if(splinter[0].equals("Spring")){
		  return true;
	  }
	  return false;
  }
  
  public boolean isFall(){
	  String[] splinter = semesterTime.split(" ");
	  if(splinter[0].equals("Fall")){
		  return true;
	  }
	  return false;
  }
  
  public boolean isOffered(Integer courseID){
	  if(universalCourses.contains(courseID))
		  return true;
	  if(!isFall() & fallCourses.contains(courseID))
		  return false;
	  if(!isSpring() & springCourses.contains(courseID))
		  return false;
	  return true;
  }
/**
 * @return the fallCourses
 */
public ArrayList<Integer> getFallCourses() {
	return fallCourses;
}

/**
 * @param fallCourses the fallCourses to set
 */
public void setFallCourses(ArrayList<Integer> fallCourses) {
	this.fallCourses = fallCourses;
}

/**
 * @return the springCourses
 */
public ArrayList<Integer> getSpringCourses() {
	return springCourses;
}

/**
 * @param springCourses the springCourses to set
 */
public void setSpringCourses(ArrayList<Integer> springCourses) {
	this.springCourses = springCourses;
}


}
