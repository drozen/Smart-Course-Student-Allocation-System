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

<meta name="description" content="Professor/TA Course Allocation" /> 
<meta name="keywords" content="Professor/TA Course Allocation" /> 
<title>Professor/TA Course Allocation</title> 
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
    	Following are the courses that have been assigned to you: -
    </p> 
  	

    </div> 


    <div id="course_selection" style="width: 100%">
    	<div id="inner">
	    <form:form style="margin-top: 1em;">
	    	<table id="course">
	    		<tbody>
	    			  <c:if test="${not empty profCourseList}">
	    			  	<tr>
	    			  		<td><h2>Course Assigned to Professor are :-</h2></td>
	    			  	</tr>
	    			  </c:if>	
	    			  <c:forEach var="profCourse" items="${profCourseList}">
	    			  	<tr>
	    			  		<td>
					     		<h1><c:out value="${profCourse}" /></h1>
					     	</td>
					    </tr>
					  </c:forEach>	
					  <c:if test="${not empty taCourseList}">
	    			  	<tr>
	    			  		<td><h2>Course Assigned to TA are :-</h2></td>
	    			  	</tr>
	    			  </c:if>
					  <c:forEach var="taCourse" items="${taCourseList}">
	    			  	<tr>
	    			  		<td>
					     		<h1><c:out value="${taCourse}" /></h1>
					     	</td>
					    </tr>
					  </c:forEach>	    				    			
	    		</tbody>
				

	    	</table>
			
			
	    </form:form>
	   </div>
   	</div>
<script src="${url}/js/jquery.js"></script>
<script src="${url}/js/jquery-ui.js"></script>
<script src="${url}/js/student.js"></script>
</body>

</html>