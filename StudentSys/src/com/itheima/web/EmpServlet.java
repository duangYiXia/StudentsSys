package com.itheima.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.model.Emp;
import com.itheima.service.EmpService;

public class EmpServlet extends HttpServlet {
	private static final long serialVersionUID = -4387123565705204638L;
	private static int countId=10;
	private int dispalyNum = 3;//每页设置展示3条数据
	Map paramMap = new HashMap();
	/**
	 * Constructor of the object.
	 */
	public EmpServlet() {
		super();
	}

	/**
	 * doget2
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	private EmpService empService = new EmpService();

	/**
	 * dopost2
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			request.setCharacterEncoding("utf-8");

			String type = request.getParameter("type");

			// 添加员工请求
			if ("add".equals(type)) {

				// 获取数据
				String name = request.getParameter("name").trim();
				String age = request.getParameter("age").trim();
				String salary = request.getParameter("salary").trim();

				// 封装数据
				Emp emp = new Emp(name, Integer.parseInt(age), Double.parseDouble(salary));
				
				emp.setId(countId++);// 这里的ID是否应该是字符串才对,如果是ID作为主键的字符串可用这个UUID.randomUUID();
				// 处理添加员工页面
				if (empService.addEmp(emp)) {					
					//添加成功 request.setAttribute("msg","添加成功");				
					// 跳转到员工列表页面，跳转不需要传递数据
					response.sendRedirect("emps?type=list");

				} else {
					// 添加失败
					request.setAttribute("msg", "添加失败");
					// 页面跳转到addEmp.jsp
					request.getRequestDispatcher("addEmp.jsp").forward(request, response);
				}

				// 员工列表查询请求
			} else if ("mod".equals(type)) {
				// 获取数据
				int id = Integer.valueOf(request.getParameter("id").trim());
				String name = request.getParameter("name").trim();
				String age = request.getParameter("age").trim();
				String salary = request.getParameter("salary").trim();

				// 封装数据
				Emp emp = new Emp(name, Integer.parseInt(age), Double.parseDouble(salary));
				emp.setId(id);
				if (empService.modEmp(emp)) {
					response.sendRedirect("emps?type=list");
				} else {
					request.setAttribute("msg", "mod失败");
					// 页面跳转到modEmp.jsp
					request.getRequestDispatcher("modEmp.jsp").forward(request, response);
				}
			} else if ("del".equals(type)) {
				// 获取数据
				int id = Integer.valueOf(request.getParameter("id").trim());
				if (empService.delEmp(id)) {
					response.sendRedirect("emps?type=list");
				} else {
					request.setAttribute("msg", "del失败");
				}
			} else if ("list".equals(type)) {
				String curPage = request.getParameter("curPage");
				String name = request.getParameter("name"); 
				String agePre = request.getParameter("agePre");
				String salaryPre = request.getParameter("salaryPre");
				String ageLast = request.getParameter("ageLast");
				String salaryLast = request.getParameter("salaryLast");
							
				// 调用业务逻辑查询员工列表
				Map map = null;
				if(curPage==null || name==null || agePre == null || ageLast == null || salaryPre == null || salaryLast == null){
					map = empService.getEmpList(Integer.parseInt(curPage), dispalyNum, paramMap);
				} else {
					//组装查询条件
					paramMap.put("name", name);
					paramMap.put("agePre", agePre);
					paramMap.put("salaryPre", salaryPre);
					paramMap.put("ageLast", ageLast);
					paramMap.put("salaryLast", salaryLast);
					map = empService.getEmpList(Integer.parseInt(curPage.trim()), dispalyNum, paramMap);
				}
				List<Emp> list = (List<Emp>) map.get("list");
				int sumPage = (int) map.get("sumPage");
				// 存储到域对象里面
				request.setAttribute("empList", list);
				request.setAttribute("sumPage", sumPage);
				//送回页面参数
				request.setAttribute("name", name);
				request.setAttribute("agePre", agePre);
				request.setAttribute("ageLast", ageLast);
				request.setAttribute("salaryPre", salaryPre);
				request.setAttribute("salaryLast", salaryLast);
				
				// 跳转页面去显示，list.jsp
				request.getRequestDispatcher("list.jsp").forward(request, response);

			} else if("upPageQuery".equals(type)) {
				int curPage = Integer.parseInt(request.getParameter("curPage").trim());
				System.out.println("curPage="+curPage);
				if(curPage > 1) {
					curPage -= 1;
				} else {
					request.setAttribute("curPage", curPage);//用于前端弹出提示框，已经到第一页
					return;
				}
				// 调用业务逻辑查询员工列表
				Map map = empService.getEmpList(curPage, dispalyNum, paramMap);
				List<Emp> list = (List<Emp>) map.get("list");
				int sumPage = (int) map.get("sumPage");
				// 存储到域对象里面
				request.setAttribute("empList", list);
				request.setAttribute("sumPage", sumPage);
				request.setAttribute("curPage", curPage);
				// 跳转页面去显示，list.jsp
				request.getRequestDispatcher("list.jsp").forward(request, response);
			} else if("downPageQuery".equals(type)) {
				int curPage = Integer.parseInt(request.getParameter("curPage").trim());
				// 调用业务逻辑查询员工列表
				Map map = empService.getEmpList(curPage+1, dispalyNum, paramMap);
				List<Emp> list = (List<Emp>) map.get("list");
				int sumPage = (int) map.get("sumPage");
				request.removeAttribute("curPage");
				request.setAttribute("curPage", (curPage+1));
				// 存储到域对象里面
				request.setAttribute("empList", list);
				request.setAttribute("sumPage", sumPage);
				System.out.println("curPage=" + (curPage+1));
				// 跳转页面去显示，list.jsp
				request.getRequestDispatcher("list.jsp").forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
