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

import com.gatech.saad.form.StudentCourseForm;
import com.gatech.saad.service.StudentService;
import com.gatech.saad.validator.StudentFormValidator;

@Controller
public class StudentController {

	Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private StudentService studentService;

	@Autowired
	private StudentFormValidator studentFormValidator;

	@RequestMapping(value = "/student_courses", method = RequestMethod.POST)
	public String submitStudentPreferences(StudentCourseForm studentCourseForm,
			BindingResult bindingResult, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		
		getStudentFormValidator().validate(studentCourseForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("courseList1", populateCourseList());
			model.addAttribute("courseList2", populateCourseList());
			model.addAttribute("courseList3", populateCourseList());

			model.addAttribute("studentCourseForm", studentCourseForm);
			return "studentInput";
		}
		logger.info(" Got the form values " + studentCourseForm.getStudentId());

		List<String> courseList  = getStudentService().processStudentData(studentCourseForm);
		
		model.addAttribute("courseList", courseList);
		return "studentOutput";
	}

	@RequestMapping(value = "/student**", method = RequestMethod.GET)
	public String studentPage(Model model, HttpServletRequest request,
			HttpServletResponse response) {

		model.addAttribute("name", "tmpStudent");
		model.addAttribute("courseList1", populateCourseList());
		model.addAttribute("courseList2", populateCourseList());
		model.addAttribute("courseList3", populateCourseList());

		model.addAttribute("studentCourseForm", new StudentCourseForm());
		return "studentInput";

	}

	private Map<String, String> populateCourseList() {
		Map<String, String> courseMap = new HashMap<String, String>();
		courseMap.put("CS 6210", "CS6210 Advanced Operating Systems");
		courseMap.put("CS 8803", "CS8803 Special Topics");
		courseMap.put("CS 6300", "CS6300 Software Development Process");
		courseMap.put("CS 7637", "CS7637 Knowledge-Based AI");
		courseMap.put("CS 6290",
				"CS6290 High Performance Computer Architecture");
		courseMap.put("CS 6440", "CS6440 Introduction to Health Informatics");
		courseMap.put("CS 6475", "CS6475 Computer Photography");
		courseMap.put("CS 6250", "CS6250 Computer Networks");
		courseMap.put("CS 6505", "CS6505 Computability and Algorithms");
		courseMap.put("CS 7641", "CS7641 Machine Learning");
		courseMap.put("CS 6310", "CS6310 Software Architecture  and  Design");
		courseMap.put("CS 4495", "CS4495 Computer Vision");
		return courseMap;
	}

	public StudentService getStudentService() {
		return studentService;
	}

	public void setStudentService(StudentService studentService) {
		this.studentService = studentService;
	}

	public StudentFormValidator getStudentFormValidator() {
		return studentFormValidator;
	}

	public void setStudentFormValidator(
			StudentFormValidator studentFormValidator) {
		this.studentFormValidator = studentFormValidator;
	}

}
