<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page session="true"%>
<html>
<head>
<meta charset="utf-8"> 
  
<!-- CSS stylesheets -->
<link href="css/jquery-ui.css" rel="stylesheet" /> 
<link href="css/style.css" rel="stylesheet" />
  
<!-- jQuery / JavaScript -->
<script src="//code.jquery.com/jquery-1.11.2.min.js"></script>

  
<meta name="description" content="Administrator Page" /> 
<meta name="keywords" content="Administrator Page" /> 
<title>Administrator Page</title> 
<style>
.error{
	display: block;
	color: #ff0000;
    font-style: italic;
    font-weight: bold;
}
</style>
</head>
<body> 
  	
	<c:url value="/j_spring_security_logout" var="logoutUrl" />
 
	<!-- csrt for log out-->
	<form action="${logoutUrl}" method="post" id="logoutForm">
	  <input type="hidden" 
		name="${_csrf.parameterName}"
		value="${_csrf.token}" />
	</form>
 
	<script>
		function formSubmit() {
			document.getElementById("logoutForm").submit();
		}
	</script>
 
	<c:if test="${pageContext.request.userPrincipal.name != null}">
		<h2>
			Welcome : ${pageContext.request.userPrincipal.name} | <a
				href="javascript:formSubmit()"> Logout</a>
		</h2>
	</c:if>



    <!-- Begin Wrapper -->
    <div id="wrapper"> 
  	<h2>Instructions:-</h2>
    <p style="font-size: 14px;"> 
		Click add course and select a course from the drop down menu.You may delete an added course by clicking the delete button next to that course.The first course selected will be your highest priority course.
    </p> 
  	

    </div> 

    <div id="course_selection" style="width: 100%">


	    <form:form style="margin-top: 1em;" action="/Project4/admin_courses" method="POST" commandName="adminCourseForm">
	    	<table id="course" style="margin: 0px auto;">
	    		<tbody>
	    			<tr>
	    				<td>
	    					<h2>Course Select:- &nbsp;&nbsp;</h2>
	    				</td>
	    				<td>
	    					<form:select id="selectmenud" path="courseSelected">
	    						<form:option value="NONE" label="--- Select ---"/>
							    <form:options items="${courseList}" />
							</form:select>
							<form:errors path="courseSelected" class="error"/>									
	    				</td>
	    				<td>
	    					<h2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Course Capacity:- &nbsp;&nbsp;</h2>
	    				</td>
	    				<td>
	    					<form:input id="course_spinner" path="courseCapacity"/>
	    					<form:errors path="courseCapacity" class="error"/>		
	    				</td>
	    			</tr>
	    			<tr>
	    				<td>
	    					<h2>Semesters Available:- &nbsp;&nbsp;</h2>
	    				</td>
	    				<td>
	    					<div id="radioset">
								<input type="checkbox" id="semester_0" name="radio" disabled readonly><label for="semester_0">Spring</label>
								<input type="checkbox" id="semester_1" name="radio" checked="checked" disabled readonly><label for="semester_1">Summer</label>
								<input type="checkbox" id="semester_2" name="radio" disabled readonly><label for="semester_2">Fall</label>
							</div>
	    				</td>
	    				<td>
	    					<h2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Professor Assigned:- &nbsp;&nbsp;</h2>
	    				</td>
	    				<td>
	    					<form:select id="professormenu" path="professorAssigned">
	    						<form:option value="NONE" label="--- Select ---"/>
							    <form:options items="${professorList}" />
							</form:select>
							<form:errors path="professorAssigned" class="error"/>		
	    				</td>
	    			</tr>
					<tr>
	    				<td>
	    					<h2>TA Capacity:- &nbsp;&nbsp;</h2>
	    				</td>
	    				<td>
	    					<form:input id="ta_spinner" path="taCapacity"/>
							<form:errors path="taCapacity" class="error"/>			    					
	    				</td>
	    				<td>
	    					<h2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TA Assigned:- &nbsp;&nbsp;</h2>
	    				</td>
	    				<td>
	    					<form:select id="tamenu" path="taAssigned">
	    						<form:option value="NONE" label="--- Select ---"/>
							    <form:options items="${taList}" />
							</form:select>
							<form:errors path="taAssigned" class="error"/>			    												
	    				</td>
	    			</tr>
	    		</tbody>


	    	</table>

	    	<div id="submit_form">
	    		<button id="submit">SUBMIT</button>
	    	</div>
	   	</form:form>
	   </div>
   	</div>
<script src="js/jquery.js"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/admin.js"></script>

</body>
</html>