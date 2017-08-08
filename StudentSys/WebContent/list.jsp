<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

//当前页
Integer curPage = (Integer)request.getAttribute("curPage");
System.out.println("jsp:" + curPage);
//总页数
String sumPage = request.getParameter("sumPage");
//设置默认值
if(curPage == null || "".equals(curPage)) {
	curPage = 1;
}
request.setAttribute("curPage", curPage);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">    
    <title>员工列表页面</title>
    
  </head>
 	<body>
	<h1 align="center">===学生列表===</h1>
		<form action="emps?type=list&curPage=${curPage }" method="post">
			<table align="center" border="1" width="50%">
				<tr align="center">
					<td align="right" width="10%">姓名：</td>
					<td><input id="name" name="name" type="text" style="width: 70px" value="${name }"></td>

					<td align="right">年龄：</td>
					<td><input name="agePre" type="text" style="width: 70px" value="${agePre }">-<input name="ageLast" type="text" style="width: 70px" value="${ageLast }"></td>

					<td align="right">成绩：</td>
					<td><input name="salaryPre" type="text" style="width: 70px" value="${salaryPre }">--<input name="salaryLast" type="text" style="width: 70px" value="${salaryLast }"></td>

					<td><input type="submit" value="查询"></td>
				</tr>
			</table>
		</form>
			<table border="1px" width="50%" align="center">
				<tr>
		    		<th>编号</th>
		    		<th>姓名</th>
		    		<th>年龄</th>
		    		<th>薪水</th>
		    		<th>操作</th>
    			</tr>
			<!-- 显示员工列表数据  -->
			<c:forEach items="${empList}" var="emp">
		  		<tr>
		  			<td>${emp.id }</td>
		  			<td>${emp.name }</td>
		  			<td>${emp.age }</td>
		  			<td>${emp.salary }</td>
		  			<td>
		  				<a href="modEmp.jsp?id=${emp.id}&name=${emp.name}&age=${emp.age}&salary=${emp.salary}">更新</a>&nbsp;
		  				<a href="emps?type=del&id= ${emp.id }">删除</a>
		  				${emp.id }
		  			</td>
		  		</tr>
			</c:forEach>
		    	<tr align="right">
		    		<td colspan="5"><a href="addEmp.jsp">添加</a></td>
		    	</tr>
			</table>
		<table align="center" width="50%">
			<tr>
				<td><a href="emps?type=upPageQuery&curPage=${curPage }" onclick="dealDate(this)">上一页</a></td>
				<td><a href="emps?type=curPage&curPageNo=">第<input type="text" id="curPageId" value="${curPage }">页</a></td>
				<td><input type="button" id="goPageQuery" onclick="" value="GO"></td>			
				<td><a href="emps?type=downPageQuery&curPage=${curPage }">下一页</a></td>
				<td>总页数：${sumPage }</td>
			</tr>
		</table>
		
		
		<%-- <table width="50%" border="0" align="center" cellpadding="0" cellspacing="0">  
	        <tr align="center">  
	            <td>  
	                <a href="${pageList.currentUrl}&page=0">第一页</a>&nbsp;&nbsp;&nbsp;&nbsp;  
	                <a href="${pageList.currentUrl}&page=${pageList.previousIndex}">上一页</a>&nbsp;&nbsp;&nbsp;&nbsp;  
	                <c:forEach items="${pageList.indexes}" var="itempage"  
	                    varStatus="stuts">  
	                    <c:choose>  
	                        <c:when test="${pageList.currentPage ==stuts.index+1}">  
	                            <a style="color: red"> ${stuts.index+1}</a>  
	                        </c:when>  
	                        <c:otherwise>  
	                        </c:otherwise>  
	                    </c:choose>  
	                </c:forEach>  
	                &nbsp;&nbsp;  
	                <a href="${pageList.currentUrl}&page=${pageList.nextIndex}">  
	                    	下一页</a> &nbsp;&nbsp;
	                <a href="${pageList.currentUrl}&page=${pageList.lastIndex}">最后页</a>  
	                &nbsp;&nbsp;总数： ${ pageList.totalCount}  
	                &nbsp;&nbsp;总页数： ${ pageList.pageCount}  
	            </td>  
	        </tr>  
	    </table> --%>  
				
		
		<script type="text/javascript">
			//查询列表
			function dealDate(obj) {
				
			}
		</script>
	</body>
</html>
