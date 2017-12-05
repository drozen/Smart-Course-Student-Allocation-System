package com.gatech.saad.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.gatech.saad.form.StudentCourseForm;

@Component("studentFormValidator")
public class StudentFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> classParam) {
		// TODO Auto-generated method stub
		return StudentCourseForm.class.equals(classParam);
	}

	@Override
	public void validate(Object obj, Errors errors) {

		StudentCourseForm studentCourseForm = (StudentCourseForm) obj;

		if (studentCourseForm.getCoursePriority1().equalsIgnoreCase("NONE")
				|| studentCourseForm.getCoursePriority2().equalsIgnoreCase(
						"NONE")
				|| studentCourseForm.getCoursePriority3().equalsIgnoreCase(
						"NONE")) {
			errors.rejectValue("coursePriority1", "student.course.required",
					"Please select all course priorities before submitting ");
		}
	}

}
