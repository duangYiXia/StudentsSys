package com.itheima.utils;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DBUtilsTemplate {

	private DataSource dataSource;
	private QueryRunner queryRunner;
	private static final Log LOG = LogFactory.getLog(DBUtilsTemplate.class);

	public DBUtilsTemplate(DataSource dataSources) {
		//this();
		this.dataSource = dataSources;
	}

	public DBUtilsTemplate() {
		dataSource = MyDataSource.getdataSource();
	}

	/**
	 * 
	 * @param sql
	 *            ����sql���
	 * @param params
	 *            �������
	 * @return ����Ӱ������
	 */
	public int insert(String sql, Object[] params) {
		queryRunner = new QueryRunner(dataSource);
		int affectedRows = 0;
		try {
			if (params == null) {
				affectedRows = queryRunner.update(sql);
			} else {
				affectedRows = queryRunner.update(sql, params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("insert.�����¼����" + sql, e);
		}
		return affectedRows;
	}

	/**
	 * �������ݿ⣬�����Զ�����������
	 * 
	 * @param sql -
	 *            ִ�е�sql���
	 * @return ���� ע�⣻�˷���û�ر���Դ
	 */
	public int insertForKeys(String sql, Object[] params) {
		int key = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ParameterMetaData pmd = stmt.getParameterMetaData();
			if (params.length < pmd.getParameterCount()) {
				throw new SQLException("��������:" + pmd.getParameterCount());
			}
			for (int i = 0; i < params.length; i++) {
				stmt.setObject(i + 1, params[i]);
			}
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				key = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("insertForKey.���뷵����������" + sql, e);
		} finally {
			if (rs != null) { // �رռ�¼��
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) { // �ر�����
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) { // �ر����Ӷ���
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return key;
	}

	private ScalarHandler scalarHandler = new ScalarHandler() {
		@Override
		public Object handle(ResultSet rs) throws SQLException {
			Object obj = super.handle(rs);
			if (obj instanceof BigInteger)
				return ((BigInteger) obj).longValue();
			return obj;
		}
	};

	public long count(String sql, Object... params) {
		Number num = 0;
		try {
			queryRunner = new QueryRunner(dataSource);
			if (params == null) {
				num = (Number) queryRunner.query(sql, scalarHandler);
			} else {
				num = (Number) queryRunner.query(sql, scalarHandler, params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("count.ͳ����������" + sql, e);
		}
		return (num != null) ? num.longValue() : -1;
	}

	/**
	 * ִ��sql���
	 * 
	 * @param sql
	 *            sql���
	 * @return ��Ӱ�������
	 */
	public int update(String sql) {
		return update(sql, null);
	}

	/**
	 * �����޸ļ�¼
	 * 
	 * @param sql
	 *            sql���
	 * @param param
	 *            ����
	 * @return ��Ӱ�������
	 */
	public int update(String sql, Object param) {
		return update(sql, new Object[] { param });
	}

	/**
	 * �����޸ļ�¼
	 * 
	 * @param sql
	 *            sql���
	 * @param params
	 *            ��������
	 * @return ��Ӱ�������
	 */
	public int update(String sql, Object[] params) {
		queryRunner = new QueryRunner(dataSource);
		int affectedRows = 0;
		try {
			if (params == null) {
				affectedRows = queryRunner.update(sql);
			} else {
				affectedRows = queryRunner.update(sql, params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("update.�����޸ļ�¼����" + sql, e);
		}
		return affectedRows;
	}

	/**
	 * �����޸ļ�¼
	 * 
	 * @param sql
	 *            sql���
	 * @param params
	 *            ��ά��������
	 * @return ��Ӱ�������������
	 */
	public int[] batchUpdate(String sql, Object[][] params) {
		queryRunner = new QueryRunner(dataSource);
		int[] affectedRows = new int[0];
		try {
			affectedRows = queryRunner.batch(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("update.�����޸ļ�¼����" + sql, e);
		}
		return affectedRows;
	}

	/**
	 * ִ�в�ѯ����ÿ�еĽ�����浽һ��Map�����У�Ȼ������Map���󱣴浽List��
	 * 
	 * @param sql
	 *            sql���
	 * @return ��ѯ���
	 */
	public List<Map<String, Object>> find(String sql) {
		return find(sql, null);
	}

	/**
	 * ִ�в�ѯ����ÿ�еĽ�����浽һ��Map�����У�Ȼ������Map���󱣴浽List��
	 * 
	 * @param sql
	 *            sql���
	 * @param param
	 *            ����
	 * @return ��ѯ���
	 */
	public List<Map<String, Object>> find(String sql, Object param) {
		return find(sql, new Object[] { param });
	}

	/**
	 * ִ�в�ѯ����ÿ�еĽ�����浽һ��Map�����У�Ȼ������Map���󱣴浽List��
	 * 
	 * @param sql
	 *            sql���
	 * @param params
	 *            ��������
	 * @return ��ѯ���
	 */
	public List<Map<String, Object>> findPage(String sql, int page, int count, Object... params) {
		sql = sql + " LIMIT ?,?";
		queryRunner = new QueryRunner(dataSource);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			if (params == null) {
				list = (List<Map<String, Object>>) queryRunner.query(sql, new MapListHandler(), new Integer[] { page,
						count });
			} else {
				list = (List<Map<String, Object>>) queryRunner.query(sql, new MapListHandler(), ArrayUtils.addAll(
						params, new Integer[] { page, count }));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("map ���ݷ�ҳ��ѯ����", e);
		}
		return list;
	}

	/**
	 * ִ�в�ѯ����ÿ�еĽ�����浽һ��Map�����У�Ȼ������Map���󱣴浽List��
	 * 
	 * @param sql
	 *            sql���
	 * @param params
	 *            ��������
	 * @return ��ѯ���
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> find(String sql, Object[] params) {
		queryRunner = new QueryRunner(dataSource);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			if (params == null) {
				list = (List<Map<String, Object>>) queryRunner.query(sql, new MapListHandler());
			} else {
				list = (List<Map<String, Object>>) queryRunner.query(sql, new MapListHandler(), params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("map ���ݲ�ѯ����", e);
		}
		return list;
	}

	/**
	 * ִ�в�ѯ����ÿ�еĽ�����浽Bean�У�Ȼ������Bean���浽List��
	 * 
	 * @param entityClass
	 *            ����
	 * @param sql
	 *            sql���
	 * @return ��ѯ���
	 */
	public <T> List<T> find(Class<T> entityClass, String sql) {
		return find(entityClass, sql, null);
	}

	/**
	 * ִ�в�ѯ����ÿ�еĽ�����浽Bean�У�Ȼ������Bean���浽List��
	 * 
	 * @param entityClass
	 *            ����
	 * @param sql
	 *            sql���
	 * @param param
	 *            ����
	 * @return ��ѯ���
	 */
	public <T> List<T> find(Class<T> entityClass, String sql, Object param) {
		return find(entityClass, sql, new Object[] { param });
	}

	/**
	 * ִ�в�ѯ����ÿ�еĽ�����浽Bean�У�Ȼ������Bean���浽List��
	 * 
	 * @param entityClass
	 *            ����
	 * @param sql
	 *            sql���
	 * @param params
	 *            ��������
	 * @return ��ѯ���
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> find(Class<T> entityClass, String sql, Object[] params) {
		queryRunner = new QueryRunner(dataSource);
		List<T> list = new ArrayList<T>();
		try {
			if (params == null) {
				list = (List<T>) queryRunner.query(sql, new BeanListHandler(entityClass));
			} else {
				list = (List<T>) queryRunner.query(sql, new BeanListHandler(entityClass), params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("Error occured while attempting to query data", e);
		}
		return list;
	}

	/**
	 * ��ѯ��������еĵ�һ����¼������װ�ɶ���
	 * 
	 * @param entityClass
	 *            ����
	 * @param sql
	 *            sql���
	 * @return ����
	 */
	public <T> T findFirst(Class<T> entityClass, String sql) {
		return findFirst(entityClass, sql, null);
	}

	/**
	 * ��ѯ��������еĵ�һ����¼������װ�ɶ���
	 * 
	 * @param entityClass
	 *            ����
	 * @param sql
	 *            sql���
	 * @param param
	 *            ����
	 * @return ����
	 */
	public <T> T findFirst(Class<T> entityClass, String sql, Object param) {
		return findFirst(entityClass, sql, new Object[] { param });
	}

	/**
	 * ��ѯ��������еĵ�һ����¼������װ�ɶ���
	 * 
	 * @param entityClass
	 *            ����
	 * @param sql
	 *            sql���
	 * @param params
	 *            ��������
	 * @return ����
	 */
	@SuppressWarnings("unchecked")
	public <T> T findFirst(Class<T> entityClass, String sql, Object[] params) {
		queryRunner = new QueryRunner(dataSource);
		Object object = null;
		try {
			if (params == null) {
				object = queryRunner.query(sql, new BeanHandler(entityClass));
			} else {
				object = queryRunner.query(sql, new BeanHandler(entityClass), params);
			}
		} catch (SQLException e) {
			LOG.error("����һ����¼����findFirst" + e.getMessage());
			e.printStackTrace();
		}
		return (T) object;
	}

	/**
	 * ��ѯ��������еĵ�һ����¼������װ��Map����
	 * 
	 * @param sql
	 *            sql���
	 * @return ��װΪMap�Ķ���
	 */
	public Map<String, Object> findFirst(String sql) {
		return findFirst(sql, null);
	}

	/**
	 * ��ѯ��������еĵ�һ����¼������װ��Map����
	 * 
	 * @param sql
	 *            sql���
	 * @param param
	 *            ����
	 * @return ��װΪMap�Ķ���
	 */
	public Map<String, Object> findFirst(String sql, Object param) {
		return findFirst(sql, new Object[] { param });
	}

	/**
	 * ��ѯ��������еĵ�һ����¼������װ��Map����
	 * 
	 * @param sql
	 *            sql���
	 * @param params
	 *            ��������
	 * @return ��װΪMap�Ķ���
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> findFirst(String sql, Object[] params) {
		queryRunner = new QueryRunner(dataSource);
		Map<String, Object> map = null;
		try {
			if (params == null) {
				map = (Map<String, Object>) queryRunner.query(sql, new MapHandler());
			} else {
				map = (Map<String, Object>) queryRunner.query(sql, new MapHandler(), params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("findFirst.��ѯһ����¼����" + sql, e);
		}
		return map;
	}

	/**
	 * ��ѯĳһ����¼������ָ���е�����ת��ΪObject
	 * 
	 * @param sql
	 *            sql���
	 * @param columnName
	 *            ����
	 * @return �������
	 */
	public Object findBy(String sql, String params) {
		return findBy(sql, params, null);
	}

	/**
	 * ��ѯĳһ����¼������ָ���е�����ת��ΪObject
	 * 
	 * @param sql
	 *            sql���
	 * @param columnName
	 *            ����
	 * @param param
	 *            ����
	 * @return �������
	 */
	public Object findBy(String sql, String columnName, Object param) {
		return findBy(sql, columnName, new Object[] { param });
	}

	/**
	 * ��ѯĳһ����¼������ָ���е�����ת��ΪObject
	 * 
	 * @param sql
	 *            sql���
	 * @param columnName
	 *            ����
	 * @param params
	 *            ��������
	 * @return �������
	 */
	public Object findBy(String sql, String columnName, Object[] params) {
		queryRunner = new QueryRunner(dataSource);
		Object object = null;
		try {
			if (params == null) {
				object = queryRunner.query(sql, new ScalarHandler(columnName));
			} else {
				object = queryRunner.query(sql, new ScalarHandler(columnName), params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("findBy������" + sql, e);
		}
		return object;
	}

	/**
	 * ��ѯĳһ����¼������ָ���е�����ת��ΪObject
	 * 
	 * @param sql
	 *            sql���
	 * @param columnIndex
	 *            ������
	 * @return �������
	 */
	public Object findBy(String sql, int columnIndex) {
		return findBy(sql, columnIndex, null);
	}

	/**
	 * ��ѯĳһ����¼������ָ���е�����ת��ΪObject
	 * 
	 * @param sql
	 *            sql���
	 * @param columnIndex
	 *            ������
	 * @param param
	 *            ����
	 * @return �������
	 */
	public Object findBy(String sql, int columnIndex, Object param) {
		return findBy(sql, columnIndex, new Object[] { param });
	}

	/**
	 * ��ѯĳһ����¼������ָ���е�����ת��ΪObject
	 * 
	 * @param sql
	 *            sql���
	 * @param columnIndex
	 *            ������
	 * @param params
	 *            ��������
	 * @return �������
	 */
	public Object findBy(String sql, int columnIndex, Object[] params) {
		queryRunner = new QueryRunner(dataSource);
		Object object = null;
		try {
			if (params == null) {
				object = queryRunner.query(sql, new ScalarHandler(columnIndex));
			} else {
				object = queryRunner.query(sql, new ScalarHandler(columnIndex), params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("findBy.����" + sql, e);
		}
		return object;
	}

	/**
	 * 
	 * @param <T>��ҳ��ѯ
	 * @param beanClass
	 * @param sql
	 * @param page
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public <T> List<T> findPage(Class<T> beanClass, String sql, int page, int pageSize, Object... params) {
		if (page <= 1) {
			page = 0;
		}
		return query(beanClass, sql + " LIMIT ?,?", ArrayUtils.addAll(params, new Integer[] { page, pageSize }));
	}

	public <T> List<T> query(Class<T> beanClass, String sql, Object... params) {
		try {
			queryRunner = new QueryRunner(dataSource);
			return (List<T>) queryRunner.query(sql, isPrimitive(beanClass) ? columnListHandler : new BeanListHandler(
					beanClass), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<Class<?>> PrimitiveClasses = new ArrayList<Class<?>>() {
		{
			add(Long.class);
			add(Integer.class);
			add(String.class);
			add(java.util.Date.class);
			add(java.sql.Date.class);
			add(java.sql.Timestamp.class);
		}
	};
	// ���ص�һ��ʱ�õ���handler
	private final static ColumnListHandler columnListHandler = new ColumnListHandler() {
		@Override
		protected Object handleRow(ResultSet rs) throws SQLException {
			Object obj = super.handleRow(rs);
			if (obj instanceof BigInteger)
				return ((BigInteger) obj).longValue();
			return obj;
		}

	};

	// �ж��Ƿ�Ϊԭʼ����
	private boolean isPrimitive(Class<?> cls) {
		return cls.isPrimitive() || PrimitiveClasses.contains(cls);
	}
	// map

}
