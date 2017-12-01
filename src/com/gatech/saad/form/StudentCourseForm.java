package com.gatech.saad.form;

import java.util.Map;

import javax.validation.constraints.NotNull;


public class StudentCourseForm {
	
	@NotNull
	private String coursePriority1;
	
	@NotNull
	private String coursePriority2;
	
	@NotNull
	private String coursePriority3;
	
	private String coursePriority4;
	
	private String coursePriority5;
	
	private Map<Integer, String> coursePriorityMap;
	
	private String termCode = "201502";
	
	@NotNull
	private String studentId;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getCoursePriority1() {
		return coursePriority1;
	}

	public void setCoursePriority1(String coursePriority1) {
		this.coursePriority1 = coursePriority1;
	}

	public String getCoursePriority2() {
		return coursePriority2;
	}

	public void setCoursePriority2(String coursePriority2) {
		this.coursePriority2 = coursePriority2;
	}

	public String getCoursePriority3() {
		return coursePriority3;
	}

	public void setCoursePriority3(String coursePriority3) {
		this.coursePriority3 = coursePriority3;
	}

	public String getCoursePriority4() {
		return coursePriority4;
	}

	public void setCoursePriority4(String coursePriority4) {
		this.coursePriority4 = coursePriority4;
	}

	public String getCoursePriority5() {
		return coursePriority5;
	}

	public void setCoursePriority5(String coursePriority5) {
		this.coursePriority5 = coursePriority5;
	}

	public String getTermCode() {
		return termCode;
	}

	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}

	public Map<Integer, String> getCoursePriorityMap() {
		return coursePriorityMap;
	}

	public void setCoursePriorityMap(Map<Integer, String> coursePriorityMap) {
		this.coursePriorityMap = coursePriorityMap;
	}
}
