package resources;
/* by: Daniel Alon Rozen
This was adapted from Mip1.java from the Gurobi Quickstart guide
*/

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ClassCapacityOptimizer {

	public static void main(String[] args) {
	    try {
	      // Obtain a Gurobi environment
	      GRBEnv    env   = new GRBEnv("ClassCapacityOptimizer.log");
	      // create empty model object
	      GRBModel  model = new GRBModel(env);
	      
	      // Read in student_schedule.txt file (adapted from John Hameier 
	      // https://piazza.com/class/i4cwsoaak7t4y9?cid=190)
	      
	      List<List<Integer>> studentSchedule = new ArrayList<List<Integer>>();
	      try {
	      	BufferedReader reader = new BufferedReader(
	      		new FileReader("resources/student_schedule.txt"));
	      	String line = null;
	      	while ((line = reader.readLine()) != null) {
	      		if (line.startsWith("%")) {
	      			continue;
	      		}

	      		line = line.replace(".", "");
	      		line = line.trim();

	      		String[] tokens = line.split(" ");

	      		List<Integer> list = new ArrayList<Integer>();
	      		for (String token : tokens) {
	      			if (token.equals(""))
	      				continue;
	      			list.add(Integer.parseInt(token));
	      		}
	      		if (!list.isEmpty()) {
	      			studentSchedule.add(list);
	      		}
	      			
	      	}
	      	reader.close();
	      } catch (Exception e) {
	      	e.printStackTrace();
	      }
	        
	      // Create variables     
	      
	      int numStudents = studentSchedule.size();
//	      int numStudents = 6;
	      int numCourses = 18;
	      int numSemesters = studentSchedule.get(0).size(); // total number of semesters available 
	     	      
//	      System.out.println("studentSchedule.get(0).get(0)" + studentSchedule.get(0).get(0));
//	      System.out.println("studentSchedule" + studentSchedule);

	      
	      // initialize variable array
	      GRBVar[][][] y = new GRBVar[numStudents][numCourses][numSemesters];
	      
	      //Populate variables (The following code inspired by Blain Spear)
	      // taking into account for course availability during semesters
	      
	      int varCount = 0;
	        for(int i = 1; i <= numStudents; i++){
	              for(int j = 1; j <= numCourses; j++){
	                  for(int k = 1; k <= numSemesters; k++){
	            	      // addVar(lower bound, upper bound, linear objective coefficient,
	            	      // variable type, var name)
//	            	      The objective coefficient is just the coefficient that the variable has in your expressions. 
	            	      // For example for the variable x, in 6x=12, the coefficient is 6.
//	                      y[i][j][k] = model.addVar(0.0,1.0,0.0,GRB.BINARY, "y"+i+"_"+j+"_"+k);
                	                	  
	                	  // only add course if available:
	                	  
	                	  // Courses 2, 3, 4, 6, 8, 9, 12, 13 are offered every semester
	                	  if (j == 2 || j == 3 || j == 4 || j == 6 || j == 8 || j == 9 || j == 12 || j == 13 )
		                      y[i-1][j-1][k-1] = model.addVar(0.0,1.0,0.0,GRB.BINARY, "y"+i+"_"+j+"_"+k);

//	                	  Courses 1,7,11,15,17 are offered during the fall terms only, with a ca-
//	                	  pacity of X students per class.
	                	  
	                	  else if (j == 1 || j == 7 || j == 11 || j == 15 || j == 17 ) {
	                		  if (k == 1 || k == 4 || k == 7 || k == 10 )
	                			  y[i-1][j-1][k-1] = model.addVar(0.0,1.0,0.0,GRB.BINARY, "y"+i+"_"+j+"_"+k);
	                	  }
	                	 
//	                	  _ Courses 5,10,14,16,18 are offered during the spring term only, with a
//	                	  capacity of X students per class.
	                	  else {
	                		  if (k == 2 || k == 5 || k == 8 || k == 11 )
	                			  y[i-1][j-1][k-1] = model.addVar(0.0,1.0,0.0,GRB.BINARY, "y"+i+"_"+j+"_"+k);
	                	  }
				  varCount++;
	                  }
	              }
	          }
	        System.out.println("num vars: " + varCount);
		
	        // Add X variable = the numbers of courses offered, for optimization
		GRBVar X = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.INTEGER, "X");
	        
		// Integrate new variables

		model.update(); //Add variables to model
	     
	      	      
	      // Set optimization objective: minimize X, the numbers of courses offered
	      // in the matrix As:

	       GRBLinExpr expr = new GRBLinExpr();
	       expr.addTerm(1.0, X);
	       model.setObjective(expr, GRB.MINIMIZE);
	      


	      

////	       
//              //1st constraint: All students can only take a course once
	      //(adapted from Blain Spear)
//    
	       for(int i = 1; i <= numStudents; i++){
		   for(int j = 1; j <= numCourses; j++){
		       expr = new GRBLinExpr();
		       for(int k = 1; k <= numSemesters; k++){
			   if (y[i-1][j-1][k-1] != null)
			       expr.addTerm(1.0, y[i-1][j-1][k-1]);
		       }
		       model.addConstr(expr, GRB.LESS_EQUAL, 1.0, "c0"+"_"+i+"_"+j+"k");
		   }
	       }
	            
		  // 2nd constraint: No student can take more than two courses per semester:
		      
	       for(int i = 1; i <= numStudents; i++){
		   
		   for(int k = 1; k <= numSemesters; k++){
		       expr = new GRBLinExpr();
		       
		       for(int j = 1; j <= numCourses; j++){
			   if (y[i-1][j-1][k-1] != null)
			       expr.addTerm(1.0, y[i-1][j-1][k-1]);
		       }
		       model.addConstr(expr, GRB.LESS_EQUAL, 2.0, "c1"+"_"+i+"j"+k);
		   }
	       }
	      
	       //3rd constraint: The total number of courses a student can take during 
	       // all 12 semesters is 18
	       for(int i = 1; i <= numStudents; i++){
		   for(int j = 1; j <= numCourses; j++){
		       expr = new GRBLinExpr();
		       for(int k = 1; k <= numSemesters; k++){
			   if (y[i-1][j-1][k-1] != null)
			       expr.addTerm(1.0, y[i-1][j-1][k-1]);
		       }
		       model.addConstr(expr, GRB.LESS_EQUAL, 12, "c2"+"_"+i+"_"+j+"k");
		   }
	       }
        	  
	      // 4th constraint: The number of students in any course at any time <= X
	       for(int j = 1; j <= numCourses; j++){
		   for(int k = 1; k <= numSemesters; k++){
		       expr = new GRBLinExpr();
		       for(int i = 1; i <= numStudents; i++){
			   if (y[i-1][j-1][k-1] != null)
			       expr.addTerm(1.0, y[i-1][j-1][k-1]);
		       }
		       model.addConstr(expr, GRB.LESS_EQUAL, X, "c3"+"_"+"i"+j+"_"+k);
		   }
	       }
        	  

	      // 5th constraint: course order must fulfill pre-requisites 

	       // idea based on Randy Keeling and code aided by Blaine Speare
	       // https://piazza.com/class/i4cwsoaak7t4y9?cid=411
	       
	       //4 is a prerequisite for 16;

	       for(int i = 1; i <= numStudents; i++){

		   // non-pre req course
		   int course = 16;
		   int preReq = 4; // pre-req course
		   
		   GRBLinExpr preReqExpr = new GRBLinExpr();
		   if (y[i-1][course-1][0] != null)
		       model.addConstr(y[i-1][course-1][0], GRB.EQUAL, 0.0, "c4_"+i+"_"+course+"_"+1);
		   if (y[i-1][course-1][1] != null)
		       preReqExpr.addTerm(1.0, y[i-1][preReq-1][1]);
		   
		   for(int k1 = 2; k1 < numSemesters; k1++){ // non-pre req course loop
		       GRBLinExpr expr2 = new GRBLinExpr();
		       if (y[i-1][course-1][k1-1] != null)
			   expr2.addTerm(1.0, y[i-1][course-1][k1-1]); // add obpreReqective course
		       
		       for(int k = 1; k < k1; k++){ // pre-req course loop
			   if (y[i-1][preReq-1][k-1] != null)
			       preReqExpr.addTerm(1.0, y[i-1][preReq-1][k]);
			   // add pre-req terms	
		       } // end inner pre-req for
		       if (y[i-1][course-1][k1-1] != null) 
			   model.addConstr(y[i-1][course-1][k1-1], GRB.LESS_EQUAL, preReqExpr, "c4_"+i+"_"+course+"_"+k1);	                  
		       preReqExpr = new GRBLinExpr();
		   } // end outer  for
	       } // end outer students for
	       

			  
//////			  
////////			 12 is a prerequisite for 1; 
        	  
	       for(int i = 1; i <= numStudents; i++){
		   
		   // non-pre req course
		   int course = 1;
		   int preReq = 12; // pre-req course
    			  
		   GRBLinExpr preReqExpr = new GRBLinExpr();
		   if (y[i-1][course-1][0] != null)
		       model.addConstr(y[i-1][course-1][0], GRB.EQUAL, 0.0, "c4_"+i+"_"+course+"_"+1);
		   if (y[i-1][course-1][1] != null)
		       preReqExpr.addTerm(1.0, y[i-1][preReq-1][1]);
		   
		   for(int k1 = 2; k1 < numSemesters; k1++){ // non-pre req course loop
		       GRBLinExpr expr2 = new GRBLinExpr();
		       if (y[i-1][course-1][k1-1] != null)
			   expr2.addTerm(1.0, y[i-1][course-1][k1-1]); // add obpreReqective course
		       
		       for(int k = 1; k < k1; k++){ // pre-req course loop
			   if (y[i-1][preReq-1][k-1] != null)
			       preReqExpr.addTerm(1.0, y[i-1][preReq-1][k]);
			   // add pre-req terms	
		       } // end inner pre-req for
		       if (y[i-1][course-1][k1-1] != null) 
			   model.addConstr(y[i-1][course-1][k1-1], GRB.LESS_EQUAL, preReqExpr, "c4_"+i+"_"+course+"_"+k1);	                  
		       preReqExpr = new GRBLinExpr();
		   } // end outer  for
	       } // end outer students for
	       //
////			  
////			  //9 is a prerequisite for 13; 
////        	  
        	  for(int i = 1; i <= numStudents; i++){

        		  // non-pre req course
        		  int course = 13;
    			  int preReq = 9; // pre-req course
    			  
    		      GRBLinExpr preReqExpr = new GRBLinExpr();
				  if (y[i-1][course-1][0] != null)
					  model.addConstr(y[i-1][course-1][0], GRB.EQUAL, 0.0, "c4_"+i+"_"+course+"_"+1);
				  if (y[i-1][course-1][1] != null)
				        preReqExpr.addTerm(1.0, y[i-1][preReq-1][1]);

				  for(int k1 = 2; k1 < numSemesters; k1++){ // non-pre req course loop
	    			  GRBLinExpr expr2 = new GRBLinExpr();
					  if (y[i-1][course-1][k1-1] != null)
						  expr2.addTerm(1.0, y[i-1][course-1][k1-1]); // add obpreReqective course
	    			  
					  for(int k = 1; k < k1; k++){ // pre-req course loop
						  if (y[i-1][preReq-1][k-1] != null)
							  preReqExpr.addTerm(1.0, y[i-1][preReq-1][k]);
						  // add pre-req terms	
					  } // end inner pre-req for
					  if (y[i-1][course-1][k1-1] != null) 
						  	model.addConstr(y[i-1][course-1][k1-1], GRB.LESS_EQUAL, preReqExpr, "c4_"+i+"_"+course+"_"+k1);	                  
					  preReqExpr = new GRBLinExpr();
				  } // end outer  for
        	  } // end outer students for
//    	  
////			  
////			  //3 is a prerequisite 7.
////        	  
        	  for(int i = 1; i <= numStudents; i++){

        		  // non-pre req course
        		  int course = 7;
    			  int preReq = 3; // pre-req course
    			  
    		      GRBLinExpr preReqExpr = new GRBLinExpr();
				  if (y[i-1][course-1][0] != null)
					  model.addConstr(y[i-1][course-1][0], GRB.EQUAL, 0.0, "c4_"+i+"_"+course+"_"+1);
				  if (y[i-1][course-1][1] != null)
				        preReqExpr.addTerm(1.0, y[i-1][preReq-1][1]);

				  for(int k1 = 2; k1 < numSemesters; k1++){ // non-pre req course loop
	    			  GRBLinExpr expr2 = new GRBLinExpr();
					  if (y[i-1][course-1][k1-1] != null)
						  expr2.addTerm(1.0, y[i-1][course-1][k1-1]); // add obpreReqective course
	    			  
					  for(int k = 1; k < k1; k++){ // pre-req course loop
						  if (y[i-1][preReq-1][k-1] != null)
							  preReqExpr.addTerm(1.0, y[i-1][preReq-1][k]);
						  // add pre-req terms	
					  } // end inner pre-req for
					  if (y[i-1][course-1][k1-1] != null) 
						  	model.addConstr(y[i-1][course-1][k1-1], GRB.LESS_EQUAL, preReqExpr, "c4_"+i+"_"+course+"_"+k1);	                  
					  preReqExpr = new GRBLinExpr();
				  } // end outer  for
        	  } // end outer students for
        

	      
			  
	      // 6th constraint: : The list of courses that a student can take must be within student_schedule.txt
		      
        	  for(int i = 1; i <= numStudents; i++){
		      for (int n = 0; n < 12; n++) { // select courses
			  //array studentSchedule.get(student = i).get(course position = n)
			  int j = studentSchedule.get(i-1).get(n);
			  expr = new GRBLinExpr();
			  for(int k = 1; k <= numSemesters; k++){
			      if (y[i-1][j-1][k-1] != null) {// check if course offered for that semester.  
				  expr.addTerm(1.0, y[i-1][j-1][k-1]);
			      } // end if
			  } // end 
	                  model.addConstr(expr, GRB.EQUAL, 1.0, "c5_"+i+"_"+j+"_"+"k");
			  
		      }  
		  }
        	  
	      

	      // once the model has been built the next step is to:
	      // Optimize model

	      model.optimize();
	      
		//   give more detail around inconsistencies in the model
//    	  model.computeIIS();
	      
//	      write the entire model into a file 
	      model.write("modelOutput.lp");
    	  
	      model.write("variableOutput.mst");

	      
	      
	      // obtain the objective value for the current solution:

	      System.out.println("Obj: " + model.get(GRB.DoubleAttr.ObjVal));
     
	      // Cleaning up:
	      // Dispose of model and environment

	      model.dispose();
	      env.dispose();

	    } catch (GRBException e) {
	      System.out.println("Error code: " + e.getErrorCode() + ". " +
	                         e.getMessage());
	    }
	    
	    
	    
	    
	  } // end main
	
}
