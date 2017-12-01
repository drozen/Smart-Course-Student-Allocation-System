package com.gatech.saad.service;

import java.util.List;

import com.gatech.saad.form.StudentCourseForm;

public interface StudentService {
	
	public List<String> processStudentData(StudentCourseForm studentCourseForm);
	
	public String getCourseRefNbr(String courseDescription);

}
