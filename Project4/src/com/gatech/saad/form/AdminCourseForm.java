package com.gatech.saad.form;

import javax.validation.constraints.NotNull;

public class AdminCourseForm {
	
	@NotNull
	private String courseSelected;
	
	@NotNull
	private int courseCapacity;
	
	@NotNull
	private int taCapacity;
	
	@NotNull
	private String professorAssigned;
	
	@NotNull
	private String taAssigned;
	
	private String courseRefNbr;
	
	private String termCode = "201502";

	public String getCourseSelected() {
		return courseSelected;
	}

	public void setCourseSelected(String courseSelected) {
		this.courseSelected = courseSelected;
	}

	public int getCourseCapacity() {
		return courseCapacity;
	}

	public void setCourseCapacity(int courseCapacity) {
		this.courseCapacity = courseCapacity;
	}

	public int getTaCapacity() {
		return taCapacity;
	}

	public void setTaCapacity(int taCapacity) {
		this.taCapacity = taCapacity;
	}

	public String getProfessorAssigned() {
		return professorAssigned;
	}

	public void setProfessorAssigned(String professorAssigned) {
		this.professorAssigned = professorAssigned;
	}

	public String getTaAssigned() {
		return taAssigned;
	}

	public void setTaAssigned(String taAssigned) {
		this.taAssigned = taAssigned;
	}

	public String getCourseRefNbr() {
		return courseRefNbr;
	}

	public void setCourseRefNbr(String courseRefNbr) {
		this.courseRefNbr = courseRefNbr;
	}

	public String getTermCode() {
		return termCode;
	}

	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}
	

}
