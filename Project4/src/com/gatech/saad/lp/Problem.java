package com.gatech.saad.lp;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.gatech.saad.beans.Course;
import com.gatech.saad.beans.Professor;
import com.gatech.saad.beans.Student;
import com.gatech.saad.beans.TA;

/**
 * Problem Class
 * Blaine Spear, Alan Hyde, Daniel Rozen
 * bspear8
 * 
 * 
 * Purpose: create the constraints for running the Gurobi model 
 *  * Intended to be used as a black box. 
 */
public class Problem {
	private GRBEnv env;

	private GRBModel model;

	private GRBVar x; //What we want to minimize - maximum course capacity

	private ArrayList<Student> students;

	private ArrayList<Course> courses;

	private Semester semester;

	private Integer numSemesters;

	private GRBVar studentVars[][][];
	
	private ArrayList<TA> TAs;
	private GRBVar taVars[][];
	
	private ArrayList<Professor> professors;
	private GRBVar professorVars[][];
	
	GRBLinExpr expr;

	/*
	 * Constructor
	 * Takes students, course, semester, creates an uninitialized 3D array for Gurobi variables.
	 */
	public Problem(ArrayList<Student> students, ArrayList<Course> courses, Semester semester, Integer numSemesters, ArrayList<TA> TAs, ArrayList<Professor> professors){
		this.students = students;
		this.courses = courses;
		this.semester = semester;
		this.numSemesters = numSemesters;
		studentVars = new GRBVar[students.size()][courses.size()][numSemesters]; //Yijk
		
		this.TAs = TAs;
		taVars = new GRBVar[TAs.size()][courses.size()];
		this.professors = professors;
		professorVars = new GRBVar[professors.size()][courses.size()];
	}


	public void solve(Connection dbConn) throws SQLException {
		try {
			env   = new GRBEnv("Solve.log");
			model = new GRBModel(env);


			// we don't have an objective function to minimize/maximize for now
			//    	      //Minimze X
			setupObjective();
			//    	      
			//Subject To
			setupConstraints();


			//Run Engine
			System.out.println("solver starting");
			model.optimize();
			System.out.println("solver ended");
			
			model.write("test.lp");

			//Print X to console
			//    	      System.out.println("Obj: " + model.get(GRB.DoubleAttr.ObjVal));      
			//    	      System.out.println(x.get(GRB.StringAttr.VarName)
			//                      + " " +x.get(GRB.DoubleAttr.X)); 

			//Print the student assignment solution to a file
			model.write("Project1Assignments.mst");

			// delete old results before storing new ones
			Statement delete = dbConn.createStatement();
			delete.execute("delete from course_assignment");
			delete.execute("delete from course_resrc");
			delete.close();
			
			int assignments = 0;
			// store results into db
			for (int stu = 0; stu < studentVars.length; stu++)
			{
				for (int course = 0; course < studentVars[stu].length; course++) 
				{
					for (int sem = 0; sem < studentVars[stu][course].length; sem++)
					{
						GRBVar yy = studentVars[stu][course][sem];
						Double varValue = yy.get(GRB.DoubleAttr.X);
						
						if (varValue != 0.0)
						{
							// need to add this row to the db
							PreparedStatement stmt = null;
							try
							{
								stmt = dbConn.prepareStatement("insert into course_assignment (course_ref_nbr, gt_id) values (?,?)");
								stmt.setString(1, Integer.toString(courses.get(course).getID()));
								stmt.setString(2, students.get(stu).getID());
								//stmt.setString(3, semester.getSemesterTime());
								//stmt.setNull(3, java.sql.Types.VARCHAR);
								stmt.executeUpdate();
								assignments++;
							}
							catch (SQLException e)
							{
								e.printStackTrace();
								System.out.println(e.getMessage());
							}
							finally
							{
								try
								{
									if (stmt != null) { stmt.close(); }
								}
								catch (Exception e)
								{
									// nobody cares for this exception
								}
							}
						}
					}
				}
			}
			System.out.println("Inserted into course_assignment: " + assignments);
			for (int prof = 0; prof < professorVars.length; prof++)
			{
				for (int course = 0; course < professorVars[prof].length; course++) 
				{
					GRBVar yy = professorVars[prof][course];
					Double varValue = yy.get(GRB.DoubleAttr.X);
					
					if (varValue != 0.0)
					{
						// need to add this row to the db
						PreparedStatement stmt = null;
						try
						{
							stmt = dbConn.prepareStatement("insert into course_resrc (course_ref_nbr, gt_id, resource_type_cd) values (?,?,?)");
							stmt.setString(1, Integer.toString(courses.get(course).getID()));
							stmt.setString(2, professors.get(prof).getId());
							stmt.setString(3, "PROF");
							stmt.executeUpdate();
						}
						catch (SQLException e)
						{
							e.printStackTrace();
							System.out.println(e.getMessage());
						}
						finally
						{
							try
							{
								if (stmt != null) { stmt.close(); }
							}
							catch (Exception e)
							{
								// nobody cares for this exception
							}
						}
					}
				}
			}
			for (int ta = 0; ta < taVars.length; ta++)
			{
				for (int course = 0; course < taVars[ta].length; course++) 
				{
					GRBVar yy = taVars[ta][course];
					Double varValue = yy.get(GRB.DoubleAttr.X);
					
					if (varValue != 0.0)
					{
						// need to add this row to the db
						PreparedStatement stmt = null;
						try
						{
							stmt = dbConn.prepareStatement("insert into course_resrc (course_ref_nbr, gt_id, resource_type_cd) values (?,?,?)");
							stmt.setString(1, Integer.toString(courses.get(course).getID()));
							stmt.setString(2, TAs.get(ta).getId());
							stmt.setString(3, "TA");
							stmt.executeUpdate();
						}
						catch (SQLException e)
						{
							e.printStackTrace();
							System.out.println(e.getMessage());
						}
						finally
						{
							try
							{
								if (stmt != null) { stmt.close(); }
							}
							catch (Exception e)
							{
								// nobody cares for this exception
							}
						}
					}
				}
			}

			
			
			//Dispose of model and environment
			model.dispose();
			env.dispose();

		} catch (GRBException e) {
			System.out.println("Error code: " + e.getErrorCode() + ". " +
					e.getMessage());
		}
	}

	private void setupObjective()  throws GRBException {
		GRBLinExpr expr = new GRBLinExpr();
		x = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.INTEGER, "x");
		expr.addTerm(1.0, x);

		model.update();
		
		try {
			model.setObjective(expr, GRB.MAXIMIZE);
		} catch (GRBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/*
	 * Subject To:
	 * Constraint 1: Students can take a course once
	 * Constraint 2: Students can take only their max course load
	 * Constraint 3: Each course can only seat X many students
	 * Constraint 4: Semesters only offer certain classes
	 * Constraint 5: pre-reqs (Not Applicable)
	 * Constraint 6: pre-reqs (Not Applicable)
	 * Constraint 7: 1 class per Professor
	 * Constraint 8: 1 class per TA
	 * Constraint 9: Each class must have at least 1 professor
	 * Constraint 10: Each class must have at least 1 TA per 50 students
	 * 	              / Ensure that the total students in a class is <= 50 * numTA
	 * Constraint 11: Ensure that students are not taking a previously passed class
	 *  			 	- 
	 * Constraint 12: Professors that must be assigned to a specific course, must be set to true.  
	 * Constraint 13: TAs that must be assigned to a specific course, must be set to true.
	 * Constraint 14??: Students must be assigned to at least 1 course  
	 */
	private void setupConstraints() throws GRBException{

		//Populate variables as binaries - doing this first reduces model.update() calls, reducing runtime
		int varCount = 0;
		for(int i = 0; i < students.size(); i++){
			for(int j = 0; j < courses.size(); j++){
				for(int k = 0; k < numSemesters; k++){
					studentVars[i][j][k] = model.addVar(0.0,1.0,0.0,GRB.BINARY, "y"+(i+1)+"_"+(j+1)+"_"+(k+1));
					varCount++;
				}
			}
		}
		
		for (int i=0; i < TAs.size(); i++) {
			for (int j = 0; j < courses.size(); j++) {
				taVars[i][j] = model.addVar(0.0, 1.0,  0.0, GRB.BINARY,  "ta" + i + "_course" + j);
			}
		}
		
		for (int i=0; i < professors.size(); i++) {
			for (int j = 0; j < courses.size(); j++) {
				professorVars[i][j] = model.addVar(0.0, 1.0,  0.0, GRB.BINARY,  "professor" + i + "_course" + j);
			}
		}
		
		model.update(); //Add variables to model
		System.out.println("Added vars: " + varCount);

		//CONSTRAINT #1: Students can take a course only once
		for(int i = 0; i < students.size(); i++){
			for(int j = 0; j < courses.size(); j++){
				expr = new GRBLinExpr();
				if(students.get(i).getDesiredCourses().contains(j+1)){
					for(int k = 0; k < numSemesters; k++){
						expr.addTerm(1.0, studentVars[i][j][k]);
					}
					model.addConstr(expr, GRB.LESS_EQUAL, 1.0, "c1_"+i+"_"+j);
				}
			}
		}

		// maximum courses student can take is equal to their set preference
		for (int s = 0; s < students.size(); s++) {
			for (int sem = 0; sem < numSemesters; sem++) {
				expr = new GRBLinExpr();
				for (int c = 0; c < courses.size(); c++) {
					expr.addTerm(1.0, studentVars[s][c][sem]);
				}
				model.addConstr(expr, GRB.LESS_EQUAL, students.get(s).getMaxCourses(), "StudentPrefLoad_stu" + s + "_sem"+sem);
			}
		}
		// minimum courses student can take is 1, each student should always get some course
		for (int s = 0; s < students.size(); s++) {
			for (int sem = 0; sem < numSemesters; sem++) {
				expr = new GRBLinExpr();
				for (int c = 0; c < courses.size(); c++) {
					expr.addTerm(1.0, studentVars[s][c][sem]);
				}
				model.addConstr(expr, GRB.GREATER_EQUAL, 1.0, "mustTake1Course_stu" + s + "_sem"+sem);
			}
		}
		
		// Constraint 8: 1 class per TA
		// ensure each TA has at most 1 course each semester
		for (int s = 0; s < TAs.size(); s++) {
			expr = new GRBLinExpr();
			for (int c = 0; c < courses.size(); c++) {
				expr.addTerm(1.0, taVars[s][c]);
			}
			model.addConstr(expr, GRB.LESS_EQUAL, 1.0, "1perSem_ta" + s);
		}
		
		// ensure each professor has = 1 course each semester
		for (int s = 0; s < professors.size(); s++) {
			expr = new GRBLinExpr();
			for (int c = 0; c < courses.size(); c++) {
				expr.addTerm(1.0, professorVars[s][c]);
			}
			model.addConstr(expr, GRB.EQUAL, 1.0, "1perSem_prof" + s);
		}
		
		// Constraint 9: Each class must have at least 1 professor		
		for (int c = 0; c < courses.size(); c++) {
			expr = new GRBLinExpr();
			for (int s = 0; s < professors.size(); s++) {
				expr.addTerm(1.0, professorVars[s][c]);
			}
			model.addConstr(expr, GRB.GREATER_EQUAL, 1.0, "max1profPerCourse_course" + c);
		}

		
		// Constraint 10: Each class must have at least 1 TA per 50 students
		// restrict course size to 50 students per TA
		for (int c = 0; c < courses.size(); c++) {
			expr = new GRBLinExpr();
			for (int t = 0; t < TAs.size(); t++) {								
				expr.addTerm(50.0, taVars[t][c]);
			}
			for (int s = 0; s < students.size(); s++) {
				expr.addTerm(-1.0, studentVars[s][c][0]);
			}
			model.addConstr(expr, GRB.GREATER_EQUAL, 0.0, "taStudentLimit_course" + c);
		}
		
		// Constraint 11: Account for student seniority and priority of courses
		// X = Sum (SC(s,c) * Seniority(s)^2 * 10-Priority(s,c))
		// X is the objective to be maximized
		// Squaring Seniority was found through testing, the best method for achieving the desired result: 
		//    that students with higher seniority get their course preferences first
		expr = new GRBLinExpr();
		expr.addTerm(-1.0, x);
		for (int s=0; s < students.size(); s++) {
			
			Student student = students.get(s);
			
			int seniority = student.getCreditHours();
			seniority = seniority * seniority;

			ArrayList<String> priority = students.get(s).getDesiredCourses();
			
			for (int c=0; c < courses.size(); c++) {
				
				Course currentCourse = courses.get(c);
				String courseId = Integer.toString(currentCourse.getID());
				
				if (priority.contains(courseId)) {
					
					int p = 10 - priority.indexOf(courseId);
										
					expr.addTerm(
							seniority * p, 
							studentVars[s][c][0]);
				}
			}
		}
		model.addConstr(expr, GRB.GREATER_EQUAL, 0.0, "seniority");

		// if there are any professors assigned to specific courses add a constraint for that
		for (int p=0; p < professors.size(); p++) {
			Professor professor = professors.get(p);
			String course = professor.getCourse();
			if (course != null) {
				for (int c=0; c < courses.size(); c++) {
					Course courseIter = courses.get(c);
					if (Integer.toString(courseIter.getID()).equals(course)) {
						// it's a match, so set the var
						String constName = "ProfessorCourse_" + professor.getId() + "_" + courseIter.getID();
						expr = new GRBLinExpr();
						expr.addTerm(1.0, professorVars[p][c]);;
						model.addConstr(expr, GRB.EQUAL, 1.0, constName);
						System.out.println(constName);
					}
				}
			}
		}
		
		// same as above, except for TAs instead of professors
		for (int t=0; t < TAs.size(); t++) {
			TA ta = TAs.get(t);
			String course = ta.getCourse();
			if (course != null) {
				for (int c=0; c < courses.size(); c++) {
					Course courseIter = courses.get(c);
					if (Integer.toString(courseIter.getID()).equals(course)) {
						// it's a match, so set the var
						String constName = "TACourse_" + ta.getId() + "_" + courseIter.getID();
						expr = new GRBLinExpr();
						expr.addTerm(1.0, taVars[t][c]);;
						model.addConstr(expr, GRB.EQUAL, 1.0, constName);
						System.out.println(constName);
					}
				}
			}
		}
		
		model.update();
	}


	private void preReqConstraint(int j, Integer preReq, int i) {
		// TODO Auto-generated method stub

	}

} // end Problem.java
