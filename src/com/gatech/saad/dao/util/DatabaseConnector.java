package com.gatech.saad.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("databaseConnector")
public class DatabaseConnector {
	
	@Autowired
	private DataSource dataSource;
	
	public Connection getConnection(){
		Connection conn = null;
		try {
			 return getDataSource().getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return null;
	}
	
	public static void testDB(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection dbConn = DriverManager.getConnection("jdbc:mysql://localhost/scheduling?user=root&password=test");
			System.out.println(" YES ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	
	public static void main(String[] args){
		testDB();
	}



	public DataSource getDataSource() {
		return dataSource;
	}



	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
