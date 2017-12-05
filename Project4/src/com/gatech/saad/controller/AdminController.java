package com.gatech.saad.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gatech.saad.form.AdminCourseForm;
import com.gatech.saad.service.AdminService;
import com.gatech.saad.validator.AdminCourseFormValidator;

@Controller
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private AdminCourseFormValidator adminCourseFormValidator;

	Log logger = LogFactory.getLog(this.getClass());
	
	@RequestMapping(value = "/admin_courses" , method=RequestMethod.POST)
	public String submitAdminPreferences(
			AdminCourseForm adminCourseForm, BindingResult bindingResult,
			Model model, HttpServletRequest request,
			HttpServletResponse response) {
		getAdminCourseFormValidator().validate(adminCourseForm, bindingResult);
		if(bindingResult.hasErrors()){
			model.addAttribute("adminCourseForm", adminCourseForm);
			model.addAttribute("professorList", getAdminService().getProfessorData("201502", "PROF"));
			model.addAttribute("taList", getAdminService().getTAData("201502", "TA"));
			model.addAttribute("courseList",populateCourseList());
			return "adminInput";
		}
		
		logger.info(" Got the form values "+adminCourseForm.getCourseSelected());
		
		Map<String, List<String>> courseListMap = getAdminService().insertAdminData(adminCourseForm);
		model.addAttribute("profCourseList", courseListMap.get("PROF"));
		model.addAttribute("taCourseList", courseListMap.get("TA"));
		return "adminOutput";
	}
	

	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
	public String adminPage(Model model, HttpServletRequest request,
			HttpServletResponse response) {
 
		model.addAttribute("adminCourseForm", new AdminCourseForm());
		model.addAttribute("professorList", getAdminService().getProfessorData("201502", "PROF"));
		model.addAttribute("taList", getAdminService().getTAData("201502", "TA"));
		model.addAttribute("courseList",populateCourseList());
		
		return "adminInput";
 
	}
	
	private Map<String, String> populateCourseList(){
		Map<String, String> courseMap = new HashMap<String, String>();
		courseMap.put("CS 6210", "CS6210 Advanced Operating Systems");
		courseMap.put("CS 8803", "CS8803 Special Topics");
		courseMap.put("CS 6300", "CS6300 Software Development Process");
		courseMap.put("CS 7637", "CS7637 Knowledge-Based AI");
		courseMap.put("CS 6290", "CS6290 High Performance Computer Architecture");
		courseMap.put("CS 6440", "CS6440 Introduction to Health Informatics");
		courseMap.put("CS 6475", "CS6475 Computer Photography");
		courseMap.put("CS 6250", "CS6250 Computer Networks");
		courseMap.put("CS 6505", "CS6505 Computability and Algorithms");
		courseMap.put("CS 7641", "CS7641 Machine Learning");
		courseMap.put("CS 6310", "CS6310 Software Architecture  and  Design");		
		courseMap.put("CS 4495", "CS4495 Computer Vision");
		return courseMap;
	}
		

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}


	public AdminCourseFormValidator getAdminCourseFormValidator() {
		return adminCourseFormValidator;
	}


	public void setAdminCourseFormValidator(
			AdminCourseFormValidator adminCourseFormValidator) {
		this.adminCourseFormValidator = adminCourseFormValidator;
	}
}
