package com.gatech.saad.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.gatech.saad.form.AdminCourseForm;

@Component("adminCourseFormValidator")
public class AdminCourseFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> classParam) {
		// TODO Auto-generated method stub
		return AdminCourseForm.class.equals(classParam);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		AdminCourseForm adminCourseForm = (AdminCourseForm)obj;
		
		if(adminCourseForm.getCourseSelected().equalsIgnoreCase("NONE")){
			errors.rejectValue("courseSelected", "student.course.required", "Please select a course ");
		}
		if(adminCourseForm.getProfessorAssigned().equalsIgnoreCase("NONE")){
			errors.rejectValue("professorAssigned", "student.course.required", "Please select a Professor ");
		}
		if(adminCourseForm.getTaAssigned().equalsIgnoreCase("NONE")){
			errors.rejectValue("taAssigned", "student.course.required", "Please select a Teaching Assistant ");
		}
		if(adminCourseForm.getCourseCapacity() <= 0 || adminCourseForm.getCourseCapacity() > 200){
			errors.rejectValue("courseCapacity", "student.course.required", "Please enter a value between 0 and 200 for course capacity");
		}
		if(adminCourseForm.getTaCapacity() <= 0 ){
			errors.rejectValue("taCapacity", "student.course.required", "Please enter a positive value for teaching assistant capacity");
		}		
	}

}
