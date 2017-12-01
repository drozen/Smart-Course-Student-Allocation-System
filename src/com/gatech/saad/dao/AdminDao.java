package com.gatech.saad.dao;

import java.util.List;
import java.util.Map;

import com.gatech.saad.form.AdminCourseForm;

public interface AdminDao {
	
	public Map<String, String> getProfessorsForTerm(String termCode, String profCode);
	
	public Map<String, String> getTAsForTerm(String termCode, String profCode);
	
	public void insertProfessorCourseAssociation(AdminCourseForm adminCourseForm);
	
	public void insertTACourseAssociation(AdminCourseForm adminCourseForm);
	
	public List<String> getCourseForProfessorTA(String profCode);
	
}
