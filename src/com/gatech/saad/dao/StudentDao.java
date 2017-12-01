package com.gatech.saad.dao;

import java.util.List;

import com.gatech.saad.beans.Student;
import com.gatech.saad.form.StudentCourseForm;

public interface StudentDao {
	
	public void insertStudentRequest(StudentCourseForm studentCourseForm);
	
	public Student getStudentRequest();
	
	public String getCourseRefNbr(String courseDescription);
	
	public List<String> getCourseAssignment(String studentId);
	
	public String getCourseNameForRefNbr(String courseRefNbr);

}
