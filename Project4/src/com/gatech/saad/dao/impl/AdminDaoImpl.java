package com.gatech.saad.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gatech.saad.dao.AdminDao;
import com.gatech.saad.dao.util.DatabaseConnector;
import com.gatech.saad.form.AdminCourseForm;

@Component("adminDao")
public class AdminDaoImpl implements AdminDao {

	Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private DatabaseConnector databaseConnector;
	
	private String GET_PROFESSOR_TA_DETAILS = "SELECT GT_ID FROM COURSE_RESRC_AVAIL WHERE RESOURCE_TYPE_CD= ?";
	
	private String UPDATE_PROFESSOR_TA_COURSE_ASSOCIATION = "UPDATE COURSE_RESRC_AVAIL SET COURSE_REF_NBR = ? WHERE GT_ID = ?";
	
	private String GET_COURSE_NBR_REF_FOR_PROF = "select course_id, course_title_desc from course_catalog where course_ref_nbr in (select course_ref_nbr from course_resrc_avail where gt_id = ?)";
	
	
	@Override
	public Map<String, String> getProfessorsForTerm(String termCode, String profCode) {
		Connection conn = getDatabaseConnector().getConnection();
		Map<String, String> professorMap = new HashMap<String, String>();
		String professor = "";
		if(null != conn){
			try {
				PreparedStatement prepare = conn.prepareStatement(GET_PROFESSOR_TA_DETAILS);
				prepare.setString(1, profCode);
				ResultSet result = prepare.executeQuery();
				if(null != result){
					while(result.next()){
						professor = result.getString("GT_ID");
						if(!professorMap.containsKey(professor)){
							professorMap.put(professor, professor);
						}						
					}
					prepare.close();
					conn.close();
					return professorMap;
				}
			} catch (SQLException e) {
				logger.error(" Could not fetch the data for admins");
				e.printStackTrace();
			} 
		}
		return null;
	}
	


	@Override
	public Map<String, String> getTAsForTerm(String termCode, String profCode) {
		Connection conn = getDatabaseConnector().getConnection();
		Map<String, String> taMap = new HashMap<String, String>();
		String professor = "";
		if(null != conn){
			try {
				PreparedStatement prepare = conn.prepareStatement(GET_PROFESSOR_TA_DETAILS);
				prepare.setString(1, profCode);
				ResultSet result = prepare.executeQuery();
				if(null != result){
					while(result.next()){
						professor = result.getString("GT_ID");
						if(!taMap.containsKey(professor)){
							taMap.put(professor, professor);
						}						
					}
					prepare.close();
					conn.close();
					return taMap;
				}
			} catch (SQLException e) {
				logger.error(" Could not fetch the data for admins");
				e.printStackTrace();
			}		
		}
		return null;
	}

	@Override
	public void insertProfessorCourseAssociation(AdminCourseForm adminCourseForm) {
		Connection conn = getDatabaseConnector().getConnection();
		if(null != conn){
			try {
				PreparedStatement prepare = conn.prepareStatement(UPDATE_PROFESSOR_TA_COURSE_ASSOCIATION);
				
				prepare.setString(1, adminCourseForm.getCourseRefNbr());
				prepare.setString(2, adminCourseForm.getProfessorAssigned());
			
				prepare.executeUpdate();

				prepare.close();
				conn.close();
			} catch (SQLException e) {
				logger.error(" Could not update data for the professors. ");
				e.printStackTrace();
			} 
		}		
	}
	
	
	@Override
	public void insertTACourseAssociation(AdminCourseForm adminCourseForm) {
		Connection conn = getDatabaseConnector().getConnection();
		if(null != conn){
			try {
				PreparedStatement prepare = conn.prepareStatement(UPDATE_PROFESSOR_TA_COURSE_ASSOCIATION);
				
				prepare.setString(1, adminCourseForm.getCourseRefNbr());
				prepare.setString(2, adminCourseForm.getTaAssigned());

				prepare.executeUpdate();

				prepare.close();
				conn.close();

			} catch (SQLException e) {
				logger.error(" Could not update data for the tas.");
				e.printStackTrace();
			} 
		}		
	}
	


	@Override
	public List<String> getCourseForProfessorTA(String profCode) {
		List<String> courseList = new ArrayList<String>();
 		Connection conn = getDatabaseConnector().getConnection();
		if(null != conn){
			try {
				PreparedStatement prepare = conn.prepareStatement(GET_COURSE_NBR_REF_FOR_PROF);
				
				prepare.setString(1, profCode);
			
				ResultSet result = prepare.executeQuery();
				
				while(result.next()){
					courseList.add(result.getString("course_id")+ " "+result.getString("course_title_desc"));
				}
				
				
				prepare.close();
				conn.close();
				
				return courseList;
			} catch (SQLException e) {
				logger.error(" Could not update data for the professors. ");
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
