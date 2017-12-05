package com.gatech.saad.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gatech.saad.dao.AdminDao;
import com.gatech.saad.dao.StudentDao;
import com.gatech.saad.form.AdminCourseForm;
import com.gatech.saad.lp.University;
import com.gatech.saad.service.AdminService;

@Service("adminService")
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminDao adminDao;
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private University university;
	
	@Override
	public Map<String, String> getProfessorData(String termCode, String profCode) {
		
		return getAdminDao().getProfessorsForTerm(termCode, profCode);
	}
	
	@Override
	public Map<String, String> getTAData(String termCode, String taCode) {

		return getAdminDao().getTAsForTerm(termCode, taCode);
	}
	

	@Override
	public Map<String, List<String>> insertAdminData(AdminCourseForm adminCourseForm) {
		Map<String, List<String>> courseListMap = new HashMap<String, List<String>>();
		List<String> profCourseNbrList = new ArrayList<String>();
		List<String> taCourseNbrList = new ArrayList<String>();
		adminCourseForm.setCourseRefNbr(getStudentDao().getCourseRefNbr(adminCourseForm.getCourseSelected()));
		
		getAdminDao().insertProfessorCourseAssociation(adminCourseForm);
		
		getAdminDao().insertTACourseAssociation(adminCourseForm);
		
		if(getUniversity().startSolver()){
			profCourseNbrList = getAdminDao().getCourseForProfessorTA(adminCourseForm.getProfessorAssigned());
			courseListMap.put("PROF", profCourseNbrList);
			taCourseNbrList = getAdminDao().getCourseForProfessorTA(adminCourseForm.getTaAssigned());
			courseListMap.put("TA", taCourseNbrList);			
		}
		
		return courseListMap;
	}

	public AdminDao getAdminDao() {
		return adminDao;
	}

	public void setAdminDao(AdminDao adminDao) {
		this.adminDao = adminDao;
	}

	public StudentDao getStudentDao() {
		return studentDao;
	}

	public void setStudentDao(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public University getUniversity() {
		return university;
	}

	public void setUniversity(University university) {
		this.university = university;
	}



}
