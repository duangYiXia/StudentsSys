<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>员工添加页面</title>
	
  </head>
  
  <body>
   <form action="emps?type=add" method="post">
   		<p>员工姓名：<input type="text" name="name"/> </p>
   		<p>员工年龄：<input type="text" name="age"/> </p>
   		<p>员工薪水：<input type="text" name="salary"/> </p>
   		<p><input type="submit" value="添加员工"/> </p>
   		<p>${msg }</p>
   </form>
  </body>
</html>
