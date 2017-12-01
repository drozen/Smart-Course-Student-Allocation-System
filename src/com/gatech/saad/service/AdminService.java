package com.gatech.saad.service;

import java.util.List;
import java.util.Map;

import com.gatech.saad.form.AdminCourseForm;

public interface AdminService {
	
	public Map<String,String> getProfessorData(String termCode, String profCode);

	public Map<String, String> getTAData(String termCode, String taCode);
	
	public Map<String, List<String>> insertAdminData(AdminCourseForm adminCourseForm);
}
