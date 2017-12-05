<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page session="true"%>
    <c:set var="url">${pageContext.request.contextPath}</c:set>
    
<html>
<head>
<meta charset="utf-8"> 
  
<!-- CSS stylesheets -->
<link href="${url}/css/jquery-ui.css" rel="stylesheet" /> 
<link href="${url}/css/style.css" rel="stylesheet" />
  
<!-- jQuery / JavaScript -->

<meta name="description" content="Student Course Allocation" /> 
<meta name="keywords" content="Student Course Allocation" /> 
<title>Student Course Allocation</title> 
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
		Please select courses from the drop down menu.
		The first course selected will be your highest priority course.
    </p> 
  	

    </div> 


    <div id="course_selection" style="width: 100%">
    	<div id="inner">
	    <form:form style="margin-top: 1em;" action="/Project4/student_courses" commandName="studentCourseForm">
	    	<form:errors path="coursePriority1" class="error"/>
	    	<table id="course">
	    		<tbody>
	    			<tr>
	    				<td>
	    					<h2>Priority 1:- &nbsp;&nbsp;</h2>
	    				</td>
	    				<td>
	    					<form:select id="selectmenua" path="coursePriority1">
	    						<form:option value="NONE" label="--- Select ---"/>
							    <form:options items="${courseList1}" />
							</form:select>
	    				</td>
	    			</tr>
	    			<tr>
	    				<td>
	    					<h2>Priority 2:- &nbsp;&nbsp;</h2>
	    				</td>
	    				<td>
	    					<form:select id="selectmenub" path="coursePriority2">
	    						<form:option value="NONE" label="--- Select ---"/>
							    <form:options items="${courseList2}" />
							</form:select>
	    				</td>
	    			</tr>
	    			<tr>
	    				<td>
	    					<h2>Priority 3:- &nbsp;&nbsp;</h2>
	    				</td>
	    				<td>
	    					<form:select id="selectmenuc" path="coursePriority3">
	    						<form:option value="NONE" label="--- Select ---"/>
							    <form:options items="${courseList3}" />
							</form:select>
	    				</td>
	    			</tr>	    				    				    			
	    		</tbody>
				

	    	</table>
			
			<form:input type="hidden" value="${pageContext.request.userPrincipal.name}" path="studentId"/>
			
	    	<div id="submit_form">
	    		<button id="submit">SUBMIT</button>
	    	</div>
	    </form:form>
	   </div>
   	</div>
<script src="${url}/js/jquery.js"></script>
<script src="${url}/js/jquery-ui.js"></script>
<script src="${url}/js/student.js"></script>
</body>

</html>