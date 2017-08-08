package com.itheima.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.itheima.dao.EmpDao;
import com.itheima.model.Emp;

/**
 * 业务逻辑类
 * @author songyu
 *
 */
public class EmpService {
	
	private EmpDao empDao = new EmpDao();

	/**
	 * 添加员工
	 * @param emp
	 * @return boolean
	 */
	public boolean addEmp(Emp emp) {
		//调用数据访问层添加员工
		//emp.setId();
		return empDao.insertEmp(emp);
	}
	
	/**
	 * 修改
	 * @param emp
	 * @return boolean
	 */
	public boolean modEmp(Emp emp) {
		//调用数据访问层添加员工
		
		return empDao.mod(emp);
	}
	
	/**
	 * 删除
	 * @param emp
	 * @return boolean
	 */
	public boolean delEmp(int id) {
		//调用数据访问层添加员工	
		return empDao.delEmp(id);
	}

	/**
	 * 查询员工列表
	 * @return  
	 */
	public Map getEmpList(int curPage, int dispalyNum, Map paramMap) {
		//调用数据访问层查询员工列表
		return empDao.getEmpList(curPage, dispalyNum, paramMap);
	}
}
