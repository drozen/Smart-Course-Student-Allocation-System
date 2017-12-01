package com.gatech.saad.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gatech.saad.beans.Student;
import com.gatech.saad.dao.StudentDao;
import com.gatech.saad.dao.util.DatabaseConnector;
import com.gatech.saad.form.StudentCourseForm;

@Component("studentDao")
public class StudentDaoImpl implements StudentDao {
	
	Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private DatabaseConnector databaseConnector;

	private String INSERT_STUDENT_REQUEST = "INSERT INTO STUDENT_COURSE_REQUEST VALUES (?, ? ,?, ?)";
	
	private String GET_STUDENT_REQUEST = "SELECT * FROM STUDENT_COURSE_REQUEST WHERE GT_ID = ?";
	
	private String GET_COURSE_REF_NBR = "select ca.course_ref_nbr from courses_offered co, "
			+ "course_catalog ca where ca.course_id = ? "
			+" and co.term_cd = '201502' and ca.course_ref_nbr = co.course_ref_nbr";
	
	private String GET_COURSE_ASSIGNMENT = "SELECT COURSE_REF_NBR FROM COURSE_ASSIGNMENT WHERE GT_ID = ?";
	
	private String GET_COURSE_NAME = "SELECT COURSE_ID, COURSE_TITLE_DESC FROM COURSE_CATALOG WHERE COURSE_REF_NBR = ?";
	
	@Override
	public void insertStudentRequest(StudentCourseForm studentCourseForm) {
		Connection conn = getDatabaseConnector().getConnection();	
		if(null != conn){
			try {
				PreparedStatement prepare = conn.prepareStatement(INSERT_STUDENT_REQUEST);
				
				for(Map.Entry<Integer, String> entry : studentCourseForm.getCoursePriorityMap().entrySet()){
					prepare.setString(1, studentCourseForm.getStudentId());
					prepare.setString(2, (String)entry.getValue());
					prepare.setString(3, studentCourseForm.getTermCode());
					prepare.setString(4, String.valueOf((Integer)entry.getKey()));
					prepare.executeUpdate();
				}
				prepare.close();
				conn.close();
			} catch (SQLException e) {
				logger.error("Could not insert data into the table STUDENT_COURSE_REQUEST");
				e.printStackTrace();
			}			
		}else{
			logger.error("Connection is null. Check Connection Setting");			
		}
	}

	@Override
	public Student getStudentRequest() {
		Connection conn = getDatabaseConnector().getConnection();
		if(null != conn){
			try {
				PreparedStatement prepare = conn.prepareStatement(GET_STUDENT_REQUEST);
				prepare.setString(1, "");
				ResultSet result = prepare.executeQuery();
				logger.info("The number of records fetched are as follows "+result.getFetchSize());
				prepare.close();
				conn.close();
			} catch (SQLException e) {
				logger.error("Could not insert data into the table STUDENT_COURSE_REQUEST");
				e.printStackTrace();
			}			
		}
		return null;
	}
	

	@Override
	public String getCourseRefNbr(String courseDescription) {
		Connection conn = getDatabaseConnector().getConnection();
		String courseRefNbr = "";
		if(null != conn){
			try{
				PreparedStatement prepare = conn.prepareStatement(GET_COURSE_REF_NBR);
				prepare.setString(1, courseDescription);
				ResultSet result = prepare.executeQuery();
				if(null != result && result.next()){
					courseRefNbr = result.getString("course_ref_nbr");
					return courseRefNbr;
				}
				prepare.close();
				conn.close();
			}catch(SQLException e){
				logger.error("Could not insert data into the table GET_COURSE_REF_NBR");
				e.printStackTrace();				
			}
		}
		return null;
	}
	

	@Override
	public List<String> getCourseAssignment(String studentId) {
		Connection conn = getDatabaseConnector().getConnection();
		List<String> courseRefNbrList = new ArrayList<String>();
		if(null != conn){
			try{
				PreparedStatement prepare = conn.prepareStatement(GET_COURSE_ASSIGNMENT);
				prepare.setString(1, studentId);
				ResultSet result = prepare.executeQuery();
				if(null != result){
					while(result.next()){
						courseRefNbrList.add(result.getString("course_ref_nbr"));
					}				
					return courseRefNbrList;
				}
				prepare.close();
				conn.close();
			}catch(SQLException e){
				logger.error("Could not insert data into the table GET_COURSE_REF_NBR");
				e.printStackTrace();				
			}
		}
		return null;
	}
	
	
	public String getCourseNameForRefNbr(String courseRefNbr){
		Connection conn = getDatabaseConnector().getConnection();
		if(null != conn){
			try{
				PreparedStatement prepare = conn.prepareStatement(GET_COURSE_NAME);
				prepare.setString(1, courseRefNbr);
				ResultSet result = prepare.executeQuery();
				if(null != result && result.next()){
					
					return result.getString("COURSE_ID") + " "+ result.getString("COURSE_TITLE_DESC");
												
				}
				prepare.close();
				conn.close();
			}catch(SQLException e){
				logger.error("Could not insert data into the table GET_COURSE_REF_NBR");
				e.printStackTrace();				
			}
		}
		return null;
	}
	

	public DatabaseConnector getDatabaseConnector() {
		return databaseConnector;
	}

	public void setDatabaseConnector(DatabaseConnector databaseConnector) {
		this.databaseConnector = databaseConnector;
	}
}
