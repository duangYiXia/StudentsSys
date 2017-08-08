<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


	String id = request.getParameter("id");
	String name = request.getParameter("name");
	String age = request.getParameter("age");
	String salary = request.getParameter("salary");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>员工添加页面</title>
	
  </head>
  
	<body>
		<form action="emps?type=mod" method="post">
			<input type="hidden" name="id" value="<%=id %>">
	   		<p>员工姓名：<input type="text" name="name" value="<%=name %>"/> </p>
	   		<p>员工年龄：<input type="text" name="age" value="<%=age %>"/> </p>
	   		<p>员工薪水：<input type="text" name="salary" value="<%=salary %>"/> </p>
	   		<p><input type="submit" value="mod员工"/> </p>
	   		<p>${msg }</p>
		</form>
	</body>
</html>
