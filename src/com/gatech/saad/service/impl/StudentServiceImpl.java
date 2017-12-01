package com.gatech.saad.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gatech.saad.dao.StudentDao;
import com.gatech.saad.form.StudentCourseForm;
import com.gatech.saad.lp.University;
import com.gatech.saad.service.StudentService;

@Service("studentService")
public class StudentServiceImpl implements StudentService{
	
	Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private University unversity;

	@Override
	public List<String> processStudentData(StudentCourseForm studentCourseForm) {
		logger.info(" Processing the student data entered...");
		List<String> courseNameList = new ArrayList<String>();
		Map<Integer, String> courseMap = constructCourseMap(studentCourseForm);
		
		studentCourseForm.setCoursePriorityMap(courseMap);
		
		getStudentDao().insertStudentRequest(studentCourseForm);
		
		if(getUnversity().startSolver()){
			logger.info("SOLVED...");
			List<String> courseRefNbrList = getStudentDao().getCourseAssignment(studentCourseForm.getStudentId());
			
			courseNameList = constructCourseNameList(courseRefNbrList);
			
	
		}else{
			logger.error(" Could not solve the problem using solver.. exiting now.. ");
		}
			
		return courseNameList;

	}
	

	@Override
	public String getCourseRefNbr(String courseDescription) {
				
		return getStudentDao().getCourseRefNbr(courseDescription);
	}
	
	
	private List<String> constructCourseNameList(List<String> courseRefNbrList){
		List<String> courseNameList= new ArrayList<String>();
		int i=0;
		for(String courseRefNbr : courseRefNbrList){
			i++;
			courseNameList.add(getStudentDao().getCourseNameForRefNbr(courseRefNbr));
		}
		return courseNameList;
	}
	
	/**
	 * 
	 * @param studentCourseForm
	 * @return
	 */
	private Map<Integer, String> constructCourseMap(StudentCourseForm studentCourseForm){		
		Map<Integer,String> courseMap = new HashMap<Integer, String>();
		courseMap.put(1, getCourseRefNbr(studentCourseForm.getCoursePriority1()));
		courseMap.put(2, getCourseRefNbr(studentCourseForm.getCoursePriority2()));
		courseMap.put(3, getCourseRefNbr(studentCourseForm.getCoursePriority3()));
		return courseMap;
	}

	public StudentDao getStudentDao() {
		return studentDao;
	}

	public void setStudentDao(StudentDao studentDao) {
		this.studentDao = studentDao;
	}


	public University getUnversity() {
		return unversity;
	}


	public void setUnversity(University unversity) {
		this.unversity = unversity;
	}
}
