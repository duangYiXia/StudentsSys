package com.itheima.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;

import com.itheima.model.Emp;
import com.itheima.utils.DBUtilsTemplate;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 操作数据库
 * @author zjx
 *
 */
public class EmpDao {
	
	private QueryRunner runner = new QueryRunner(new ComboPooledDataSource());

	/**
	 * 添加员工
	 * @param emp
	 * @return boolean
	 */
	public boolean insertEmp(Emp emp) {
		String sql="INSERT INTO emps(id, NAME,age,salary) VALUES(?,?,?,?)";
		try {
			return runner.update(sql,emp.getId(), emp.getName(),emp.getAge(),emp.getSalary())>0;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public boolean mod(Emp emp) {
		String sql = "update emps set name=?,age=?,salary=? where id=?";
		try {
			return runner.update(sql,emp.getName(),emp.getAge(),emp.getSalary(),emp.getId())>0;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public boolean delEmp(int id) {
		String sql = "delete from emps where emps.id=?";
		try {
			return runner.update(sql,id)>0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 员工列表
	 * @return
	 */
	public Map getEmpList(int curPage, int dispalyNum, Map paramMap) {
		//获取查询条件
		String name = (String) paramMap.get("name");
		String agePre = (String) paramMap.get("agePre");
		String salaryPre = (String) paramMap.get("salaryPre");
		String ageLast = (String) paramMap.get("ageLast");
		String salaryLast = (String) paramMap.get("salaryLast");
		List<Emp> list = new ArrayList<Emp>();
		Map ret = new HashMap();
		//方案一：自动添加一行rowNum作为行号。String sql = "select * from (SELECT *,(@rowNum:=@rowNum+1) as rowNo FROM emps,(Select (@rowNum :=0) ) b) res where rowNo >" + curPage*(dispalyNum-1) +" and rowNo <" + dispalyNum;
		//方案二：使用工具类
		List tempParamList = new ArrayList();
		Map tempMap = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select * from emps t");
		if(paramMap.size() != 0) {
			sql.append(" where 1=1");
		}
		if(name != null && !"".equals(name)) {
			sql.append(" and t.name=?");
			tempParamList.add(name);
			tempMap.put("name", name);
		}
		if(agePre != null && !"".equals(agePre)) {
			sql.append(" and t.age > ?");
			tempParamList.add(StringTransToInt(agePre));
			tempMap.put("agePre", agePre);
		}
			
		if(ageLast != null && !"".equals(ageLast)) {
			sql.append(" and t.age < ?");
			tempParamList.add(StringTransToInt(ageLast));
			tempMap.put("ageLast", ageLast);
		}
		
		if(salaryPre != null && !"".equals(salaryPre)) {
			sql.append(" and t.salary > ?");
			tempParamList.add(StringTransToInt(salaryPre));
			tempMap.put("salaryPre", salaryPre);
		}
				
		if(salaryLast != null && !"".equals(salaryLast)) {
			sql.append(" and t.salary < ?");
			tempParamList.add(StringTransToInt(salaryLast));
			tempMap.put("salaryLast", salaryLast);
		}
		System.out.println("sql="+sql);
		//组装查询入参
		Object[] param = new Object[tempParamList.size()];
		int i = 0;
		for(Object o :  tempParamList) {
			param[i] = o;
			i++;
		}
		DBUtilsTemplate db = new DBUtilsTemplate(new ComboPooledDataSource());
		int page = (curPage - 1)*dispalyNum;
		list= db.findPage(Emp.class, sql.toString(), page, dispalyNum, param);
		System.out.println("list="+list);
		ret.put("list", list);
		//总页数
		int totalCount = db.find(sql.toString()).size();
		int realCount = (totalCount / dispalyNum);
		if (totalCount % dispalyNum > 0) {
			realCount++;
		}
		ret.put("sumPage", realCount);//返回总数
		
		return ret;
	}
	
	private int StringTransToInt(String str) {
		return Integer.parseInt(str.trim());	
	}
}
