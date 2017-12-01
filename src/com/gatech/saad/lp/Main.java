package com.gatech.saad.lp;

import java.sql.Connection;
import java.sql.DriverManager;
 

public class Main 
{
	/** 
	 * Description: has methods for setting up the data
	 * @param args command line options passed down
	 *
	 */
    public static void main(String[] args) throws Exception
    {
	    int numSemesters = 1; // number of semesters the model is going to solve for
	    int numCoursesPerSemester = 2;
	    	
		Connection dbConn = DriverManager.getConnection("jdbc:mysql://localhost/scheduling?user=root&password=test");

		University u = new University(numCoursesPerSemester, dbConn); 
		u.populateStudents(); 
		u.populateCourses();
		u.populateTAs();
		u.populateProfessors();
		System.out.println("num students: " + u.getStudents().size());
		System.out.println("num courses: " + u.getCourses().size());
		System.out.println("num TAs: " + u.getTAs().size());
		System.out.println("num professors: " + u.getProfessors().size());
		Problem p = new Problem(u.getStudents(), u.getCourses(), u.getSemester(), numSemesters, u.getTAs(), u.getProfessors()); 
		p.solve(dbConn);
		
		dbConn.close();
    }
}