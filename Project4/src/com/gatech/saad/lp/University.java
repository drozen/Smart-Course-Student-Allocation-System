package com.gatech.saad.lp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gatech.saad.beans.Course;
import com.gatech.saad.beans.Professor;
import com.gatech.saad.beans.Student;
import com.gatech.saad.beans.TA;
import com.gatech.saad.dao.util.DatabaseConnector;


/*
University Class
Blaine Spear
bspear8
CS6310 Project 1

Desc:
The University Class
 */
@Component("university")
public class University {
	
	Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private DatabaseConnector databaseConnector;
	
	private ArrayList<Student> students; //An array of student objects; contains every student in the university
	
	private ArrayList<Course> courses; //An array of course objects; contains every course in the university
	
	private ArrayList<TA> tas;
	
	private ArrayList<Professor> professors;
	
	private Semester semester;
	
	private Integer maxCourseLoad; //Maximum amount of courses a student can take at one time
	
	private Connection dbConn;
	
	public University(){
		
	}
	
	public University(int maxLoad, Connection conn)
		throws SQLException
	{
		maxCourseLoad = maxLoad; // maximum a student can take per semester  = 2 for fall/spring, 1 for summer
		dbConn = conn;
		ArrayList<Integer> springCourses = new ArrayList<Integer>();
		ArrayList<Integer> fallCourses = new ArrayList<Integer>();
		ArrayList<Integer> universalCourses = new ArrayList<Integer>();
		students = new ArrayList<Student>(); 
		courses = new ArrayList<Course>(); 
		tas = new ArrayList<TA>();
		professors = new ArrayList<Professor>();
		
		/*
		 * These are getting hard coded for now because I've had to spend every ounce of free-time learning how to work with an LP solver and
		 * build ridiculous algorithms that construct its' constraints, and I didn't have time to practice software engineering and build a nice robust
		 * system of easily adding and removing semester requirements.
		 */
		
		// TODO:
		// HARDCODE ALL THE CLASSES IN FOR NOW
		
		fallCourses.add(1);fallCourses.add(2);fallCourses.add(3);fallCourses.add(4);

		setSemester(new Semester("Fall 2015",springCourses, fallCourses, universalCourses));
		
	}
	
	public Boolean startSolver(){
		
		maxCourseLoad = 2; // maximum a student can take per semester  = 2 for fall/spring, 1 for summer
		dbConn = getDatabaseConnector().getConnection();
		
		ArrayList<Integer> springCourses = new ArrayList<Integer>();
		ArrayList<Integer> fallCourses = new ArrayList<Integer>();
		ArrayList<Integer> universalCourses = new ArrayList<Integer>();
		students = new ArrayList<Student>(); 
		courses = new ArrayList<Course>(); 
		tas = new ArrayList<TA>();
		professors = new ArrayList<Professor>();
		
		fallCourses.add(1);fallCourses.add(2);fallCourses.add(3);fallCourses.add(4);
		setSemester(new Semester("Fall 2015",springCourses, fallCourses, universalCourses));
		
		try {
			populateStudents();
			populateCourses();
			populateTAs();
			populateProfessors();
			System.out.println("num students: " + getStudents().size());
			System.out.println("num courses: " + getCourses().size());
			System.out.println("num TAs: " + getTAs().size());
			System.out.println("num professors: " +getProfessors().size());
			Problem p = new Problem(getStudents(), getCourses(), getSemester(), 1, getTAs(), getProfessors()); 
			p.solve(dbConn);
			return Boolean.TRUE;
		} catch (SQLException e) {
			logger.error(" Could not connect to the database.. Problem solver exiting...");
			e.printStackTrace();
		} 

		return Boolean.FALSE;
	}
	
	/*
	 * A very sub-par, clunky way of reading in a pre-formated file and building a new Course object, then adding it to the list of courses.
	 * Checks for a few extenuating circumstances, can be easily broken with file that is not properly formatted.
	 */
	public void populateCourses() throws SQLException{

		String courseSql = "select course_ref_nbr, concat(course_id, ' ', course_title_desc) as title from course_catalog where course_ref_nbr in (select course_ref_nbr from courses_offered where term_cd = '201502')";

		Statement stmt = dbConn.createStatement();
		ResultSet rows = stmt.executeQuery(courseSql);

		while (rows.next())
		{
			courses.add(new Course(rows.getInt("course_ref_nbr"),
								   rows.getString("title"),
								   0)); // hard-code no prereq at this point
		}
		rows.close();
		stmt.close();
	}
	
	/*
	 * A method for reading in a pre-formated list of students, parsing the lines and building a student object, then
	 * adding that student to the university's list of students.
	 */
	public void populateStudents() throws SQLException{
		
		Statement stmt = dbConn.createStatement();

		// map used locally to simplify student lookup when populating history and desired courses
		Map<String, Student> studentMap = new HashMap<String, Student>();

		// create list of students
		ResultSet studentRows = stmt.executeQuery("select gt_id, total_credit_hours from students");
		while (studentRows.next())
		{
			String student = studentRows.getString("gt_id");
			Integer numClasses = studentRows.getInt("total_credit_hours");
		
			Student newStudent = new Student(student);
			newStudent.setMaxCourses(numClasses);
			
			studentMap.put(student, newStudent);
			students.add(newStudent);

		}
		studentRows.close();
		
		// then populate course history
		ResultSet courseHistoryRows = stmt.executeQuery("select gt_id, sum(credit_hours) credits from student_course_hist group by gt_id");
		while  (courseHistoryRows.next()) {
			String student = courseHistoryRows.getString("gt_id");
			Integer credits = courseHistoryRows.getInt("credits");
			
			studentMap.get(student).setCreditHours(credits);
		}
		courseHistoryRows.close();
		
		// then populate course requests
		ResultSet courseRequestRows = stmt.executeQuery("select gt_id, course_ref_nbr from student_course_request order by gt_id, course_req_rank_cd");
		while (courseRequestRows.next()) {
			String student = courseRequestRows.getString("gt_id");
			String course = courseRequestRows.getString("course_ref_nbr");
			if(studentMap.get(student) != null){
				studentMap.get(student).addDesiredCourse(course);
			}
		}
		courseRequestRows.close();
		stmt.close();
	}
	
	public ArrayList<Course> getCourses(){
		return courses;
	}
	
	public ArrayList<Student> getStudents(){
		return students;
	}

	/**
	 * @return the semester
	 */
	public Semester getSemester() {
		return semester;
	}

	/**
	 * @param semester the semester to set
	 */
	public void setSemester(Semester semester) {
		this.semester = semester;
	}

	public void populateTAs() throws SQLException {

		Statement stmt = dbConn.createStatement();
		ResultSet rows = stmt.executeQuery("select distinct gt_id, course_ref_nbr from course_resrc_avail where resource_type_cd = 'TA'");

		while (rows.next())
		{
			tas.add(new TA(rows.getString("gt_id"), rows.getString("course_ref_nbr")));
		}
		rows.close();
		stmt.close();
	}

	public void populateProfessors() throws SQLException {

		Statement stmt = dbConn.createStatement();
		ResultSet rows = stmt.executeQuery("select distinct gt_id, course_ref_nbr from course_resrc_avail where resource_type_cd = 'PROF'");
		
		while (rows.next())
		{
			professors.add(new Professor(rows.getString("gt_id"), rows.getString("course_ref_nbr")));
		}
		rows.close();
		stmt.close();
	}

	public ArrayList<TA> getTAs() {
		return tas;
	}

	public ArrayList<Professor> getProfessors() {
		return professors;
	}

	public DatabaseConnector getDatabaseConnector() {
		return databaseConnector;
	}

	public void setDatabaseConnector(DatabaseConnector databaseConnector) {
		this.databaseConnector = databaseConnector;
	}

	
}
