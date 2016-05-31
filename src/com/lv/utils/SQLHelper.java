package com.lv.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lv.entity.OrderOthers;
import com.lv.entity.RepairOthers;

public class SQLHelper {
	/**
	 * 驱动
	 */
	// public static String driver =
	// "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	public static String driver = "com.mysql.jdbc.Driver";
	/**
	 * 连接字符串
	 */
	// public static String url =
	// "jdbc:microsoft:sqlserver://192.168.24.246:1433;DatabaseName=LiNing";
	public static String url = "jdbc:mysql://127.0.0.1:3306/test";
	/**
	 * 用户名
	 */
	public static String user = "root";
	/**
	 * 密码
	 */
	public static String password = "pass";

	/**
	 * 不允许实例化该类
	 */
	private SQLHelper() {
	}

	/**
	 * 获取一个数据库连接 通过设置类的 driver / url / user / password 这四个静态变量来 设置数据库连接属性
	 * 
	 * @return 数据库连接
	 */
	public static Connection getConnection() {
		try {
			// 获取驱动,这里使用的是 sqljdbc_1.2.2828.100_chs.exe,不同版本的驱动,语句有所不同
			Class.forName(driver);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException ex) {
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
					ex);
			return null;
		}
	}

	/**
	 * 获取一个 Statement 该 Statement 已经设置数据集 可以滚动,可以更新
	 * 
	 * @return 如果获取失败将返回 null,调用时记得检查返回值
	 */
	public static Statement getStatement() {
		Connection conn = getConnection();
		if (conn == null) {
			return null;
		}
		try {
			return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			// 设置数据集可以滚动,可以更新
		} catch (SQLException ex) {
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
					ex);
			close(conn);
		}
		return null;
	}

	/**
	 * 获取一个 Statement 该 Statement 已经设置数据集 可以滚动,可以更新
	 * 
	 * @param conn
	 *            数据库连接
	 * @return 如果获取失败将返回 null,调用时记得检查返回值
	 */
	public static Statement getStatement(Connection conn) {
		if (conn == null) {
			return null;
		}
		try {
			return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			// 设置数据集可以滚动,可以更新
		} catch (SQLException ex) {
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
					ex);
			return null;
		}
	}

	/**
	 * 获取一个带参数的 PreparedStatement 该 PreparedStatement 已经设置数据集 可以滚动,可以更新
	 * 
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return 如果获取失败将返回 null,调用时记得检查返回值
	 */
	public static PreparedStatement getPreparedStatement(String cmdText,
			Object... cmdParams) {
		Connection conn = getConnection();
		if (conn == null) {
			return null;
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = conn
					.prepareStatement(cmdText, ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			int i = 1;
			for (Object item : cmdParams) {
				pstmt.setObject(i, item);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			close(conn);
		}
		return pstmt;
	}

	/**
	 * 获取一个带参数的 PreparedStatement 该 PreparedStatement 已经设置数据集 可以滚动,可以更新
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return 如果获取失败将返回 null,调用时记得检查返回值
	 */
	public static PreparedStatement getPreparedStatement(Connection conn,
			String cmdText, List<Object> cmdParams) {
		if (conn == null) {
			return null;
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = conn
					.prepareStatement(cmdText, ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			if (cmdParams != null && cmdParams.size() > 0) {
				int j = 1;
				for (int i = 0; i < cmdParams.size(); i++) {
					pstmt.setObject(j, "%" + cmdParams.get(i) + "%");
					j++;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			close(pstmt);
		}
		return pstmt;
	}

	/**
	 * 执行 SQL 语句,返回结果为整型 主要用于执行非查询语句
	 * 
	 * @param cmdText
	 *            SQL 语句
	 * @return 非负数:正常执行; -1:执行错误; -2:连接错误
	 */
	public static int ExecSql(String cmdText) {
		Statement stmt = getStatement();
		if (stmt == null) {
			return -2;
		}
		int i;
		try {
			i = stmt.executeUpdate(cmdText);
		} catch (SQLException ex) {
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
					ex);
			i = -1;
		}
		closeConnection(stmt);
		return i;
	}

	/**
	 * 执行 SQL 语句,返回结果为整型 主要用于执行非查询语句
	 * 
	 * @param cmdText
	 *            SQL 语句
	 * @return 非负数:正常执行; -1:执行错误; -2:连接错误
	 */
	public static int ExecSql(Connection conn, String cmdText) {
		Statement stmt = getStatement(conn);
		if (stmt == null) {
			return -2;
		}
		int i;
		try {
			i = stmt.executeUpdate(cmdText);
		} catch (SQLException ex) {
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
					ex);
			i = -1;
		}
		close(stmt);
		return i;
	}

	/**
	 * 执行 SQL 语句,返回结果为整型 主要用于执行非查询语句
	 * 
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return 非负数:正常执行; -1:执行错误; -2:连接错误
	 */
	public static int ExecSql(String cmdText, Object... cmdParams) {
		PreparedStatement pstmt = getPreparedStatement(cmdText, cmdParams);
		if (pstmt == null) {
			return -2;
		}
		int i;
		try {
			i = pstmt.executeUpdate();
		} catch (SQLException ex) {
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
					ex);
			i = -1;
		}
		closeConnection(pstmt);
		return i;
	}

	/**
	 * 执行 SQL 语句,返回结果为整型 主要用于执行非查询语句
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return 非负数:正常执行; -1:执行错误; -2:连接错误
	 */
	public static int ExecSql(Connection conn, String cmdText,
			List<Object> cmdParams) {
		PreparedStatement pstmt = getPreparedStatement(conn, cmdText, cmdParams);
		if (pstmt == null) {
			return -2;
		}
		int i;
		try {
			i = pstmt.executeUpdate();
		} catch (SQLException ex) {
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
					ex);
			i = -1;
		}
		close(pstmt);
		return i;
	}

	/**
	 * 返回结果集的第一行的一列的值,其他忽略
	 * 
	 * @param cmdText
	 *            SQL 语句
	 * @return
	 */
	public static Object ExecScalar(String cmdText) {
		ResultSet rs = getResultSet(cmdText);
		Object obj = buildScalar(rs);
		closeConnection(rs);
		return obj;
	}

	/**
	 * 返回结果集的第一行的一列的值,其他忽略
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            SQL 语句
	 * @return
	 */
	public static Object ExecScalar(Connection conn, String cmdText) {
		ResultSet rs = getResultSet(conn, cmdText);
		Object obj = buildScalar(rs);
		closeEx(rs);
		return obj;
	}

	/**
	 * 返回结果集的第一行的一列的值,其他忽略
	 * 
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return
	 */
	public static Object ExecScalar(String cmdText, Object... cmdParams) {
		ResultSet rs = getResultSet(cmdText, cmdParams);
		Object obj = buildScalar(rs);
		closeConnection(rs);
		return obj;
	}

	/**
	 * 返回结果集的第一行的一列的值,其他忽略
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return
	 */
	public static Object ExecScalar(Connection conn, String cmdText,
			List<Object> cmdParams) {
		ResultSet rs = getResultSet(conn, cmdText, cmdParams);
		Object obj = buildScalar(rs);
		closeEx(rs);
		return obj;
	}

	/**
	 * 返回一个 ResultSet
	 * 
	 * @param cmdText
	 *            SQL 语句
	 * @return
	 */
	public static ResultSet getResultSet(String cmdText) {
		Statement stmt = getStatement();
		if (stmt == null) {
			return null;
		}
		try {
			return stmt.executeQuery(cmdText);
		} catch (SQLException ex) {
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
					ex);
			closeConnection(stmt);
		}
		return null;
	}

	/**
	 * 返回一个 ResultSet
	 * 
	 * @param conn
	 * @param cmdText
	 *            SQL 语句
	 * @return
	 */
	public static ResultSet getResultSet(Connection conn, String cmdText) {
		Statement stmt = getStatement(conn);
		if (stmt == null) {
			return null;
		}
		try {
			return stmt.executeQuery(cmdText);
		} catch (SQLException ex) {
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
					ex);
			close(stmt);
		}
		return null;
	}

	/**
	 * 返回一个 ResultSet
	 * 
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return
	 */
	public static ResultSet getResultSet(String cmdText, Object... cmdParams) {
		PreparedStatement pstmt = getPreparedStatement(cmdText, cmdParams);
		if (pstmt == null) {
			return null;
		}
		try {
			return pstmt.executeQuery();
		} catch (SQLException ex) {
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
					ex);
			closeConnection(pstmt);
		}
		return null;
	}

	/**
	 * 返回一个 ResultSet
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return
	 */
	public static ResultSet getResultSet(Connection conn, String cmdText,
			List<Object> cmdParams) {
		PreparedStatement pstmt = getPreparedStatement(conn, cmdText, cmdParams);
		if (pstmt == null) {
			return null;
		}
		try {
			return pstmt.executeQuery();
		} catch (SQLException ex) {
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
					ex);
			close(pstmt);
		}
		return null;
	}

	public static List<OrderOthers> getOrderList(ResultSet r) {
		List<OrderOthers> list = new ArrayList<OrderOthers>();
		try {
			while (r.next()) {
				OrderOthers other = new OrderOthers();
				other.setId(r.getInt("id"));
				other.setOrderbh(r.getString("orderbh"));
				other.setUsername(r.getString("username"));
				other.setOwner(r.getString("owner"));
				other.setType(r.getString("type"));
				other.setColor(r.getString("color"));
				other.setImage(r.getString("image"));
				other.setOwnerphone(r.getString("ownerphone"));
				other.setIsback(r.getInt("isback"));
				other.setStartTime(r.getDate("startTime"));
				other.setEndTime(r.getDate("endTime"));
				other.setCarno(r.getString("carno"));
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				try {
					other.setCzsj(sdf.parse(r.getString("czsj")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				list.add(other);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static OrderOthers getOrderObj(ResultSet r) {
		OrderOthers other = new OrderOthers();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			while (r.next()) {
				other.setId(r.getInt("id"));
				other.setOrderbh(r.getString("orderbh"));
				//用户信息
				other.setUserbh(r.getString("userbh"));
				other.setUsername(r.getString("username"));
				other.setAccountnum(r.getString("accountnum"));
				other.setSex(r.getInt("sex"));
				other.setAge(r.getInt("age"));
				other.setPhone(r.getString("phone"));
				other.setAddress(r.getString("address"));
				other.setUsersfz(r.getString("usersfz"));
				try {
					other.setApplyDate(sdf.parse(r.getString("applyDate")));
				} catch (ParseException e) {
					e.printStackTrace();
				}

				//车辆信息
				other.setCarbh(r.getString("carbh"));
				other.setOwner(r.getString("owner"));
				other.setCarno(r.getString("carno"));
				other.setType(r.getString("type"));
				other.setColor(r.getString("color"));
				other.setImage(r.getString("image"));
				other.setOwnerphone(r.getString("ownerphone"));
				other.setOwnersfz(r.getString("ownersfz"));
				other.setStartTime(r.getDate("startTime"));
				other.setEndTime(r.getDate("endTime"));
				try {
					other.setCzsj(sdf.parse(r.getString("czsj")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				other.setIsback(r.getInt("isback"));
				other.setPrice(r.getDouble("price"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return other;
	}
		

	public static List<RepairOthers> getRepairList(ResultSet r) {
		List<RepairOthers> list = new ArrayList<RepairOthers>();
		try {
			while (r.next()) {
				RepairOthers other = new RepairOthers();
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				other.setId(r.getInt("id"));
				other.setRepairbh(r.getString("repairbh"));
				other.setCarbh(r.getString("carbh"));
				other.setState(r.getInt("state"));
				try {
					other.setStartTime(sdf.parse(r.getString("startTime")));
					other.setEndTime(sdf.parse(r.getString("endTime")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				other.setDescribes(r.getString("describes"));
				other.setCarno(r.getString("carno"));
				other.setColor(r.getString("color"));
				other.setType(r.getString("type"));
				other.setOwner(r.getString("owner"));
				other.setImage(r.getString("image"));
				try {
					other.setCzsj(sdf.parse(r.getString("czsj")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				list.add(other);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static RepairOthers getRepairObj(ResultSet r) {
		RepairOthers other = new RepairOthers();
		try {
			while (r.next()) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				other.setId(r.getInt("id"));
				other.setRepairbh(r.getString("repairbh"));
				other.setCarbh(r.getString("carbh"));
				other.setState(r.getInt("state"));
				try {
					other.setStartTime(sdf.parse(r.getString("startTime")));
					other.setEndTime(sdf.parse(r.getString("endTime")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				other.setDescribes(r.getString("describes"));
				other.setCarno(r.getString("carno"));
				other.setColor(r.getString("color"));
				other.setType(r.getString("type"));
				other.setOwner(r.getString("owner"));
				other.setImage(r.getString("image"));
				try {
					other.setCzsj(sdf.parse(r.getString("czsj")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return other;
	}

	public static Object buildScalar(ResultSet rs) {
		if (rs == null) {
			return null;
		}
		Object obj = null;
		try {
			if (rs.next()) {
				obj = rs.getObject(1);
			}
		} catch (SQLException ex) {
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return obj;
	}

	/**
	 * 获取一个具有更新功能的数据模型 如果只要读取数据，就不要用它了
	 * 
	 * @param cmdText
	 *            能返回一个数据集的查询SQL 语句
	 * @return 表格数据模型
	 * 
	 * 
	 *         DataSet 没有找到在哪个包中,因为暂时用不到所以省略此方法
	 * 
	 *         public static DataSet getDataSet(String cmdText) { Statement stmt
	 *         = getStatement(); DataSet dbc = new DataSet(); if (stmt == null)
	 *         { dbc.code = -2; return dbc; } try { // 查询语句 dbc.rs =
	 *         stmt.executeQuery(cmdText); dbc.model = buildTableModel(dbc.rs);
	 *         dbc.code = dbc.model.getRowCount(); } catch (SQLException ex) {
	 *         Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE,
	 *         null, ex); dbc.code = -1; } return dbc; }
	 */

	/**
	 * 获取一个具有更新功能的数据模型 如果只要读取数据，就不要用它了
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            能返回一个数据集的查询SQL 语句
	 * @return 表格数据模型
	 * 
	 *         同上一个方法
	 * 
	 *         public static DataSet getDataSet(Connection conn, String cmdText)
	 *         { Statement stmt = getStatement(conn); DataSet dbc = new
	 *         DataSet(); if (stmt == null) { dbc.code = -2; return dbc; } try {
	 *         // 查询语句 dbc.rs = stmt.executeQuery(cmdText); dbc.model =
	 *         buildTableModel(dbc.rs); dbc.code = dbc.model.getRowCount(); }
	 *         catch (SQLException ex) {
	 *         Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE,
	 *         null, ex); dbc.code = -1; } return dbc; }
	 */
	/**
	 * 获取一个具有更新功能的数据模型 如果只要读取数据，就不要用它了
	 * 
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return 表格数据模型
	 * 
	 * 
	 *         同上一个方法 *
	 * 
	 * 
	 *         public static DataSet getDataSet(String cmdText, Object...
	 *         cmdParams) { PreparedStatement pstmt =
	 *         getPreparedStatement(cmdText, cmdParams); DataSet dbc = new
	 *         DataSet(); if (pstmt == null) { dbc.code = -2; return dbc; } try
	 *         { // 查询语句 dbc.rs = pstmt.executeQuery(); dbc.model =
	 *         buildTableModel(dbc.rs); dbc.code = dbc.model.getRowCount(); }
	 *         catch (SQLException ex) {
	 *         Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE,
	 *         null, ex); dbc.code = -1; } return dbc; }
	 */
	/**
	 * 获取一个具有更新功能的数据模型 如果只要读取数据，就不要用它了
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return 表格数据模型
	 * 
	 * 
	 *         同上
	 * 
	 * 
	 *         public static DataSet getDataSet(Connection conn, String cmdText,
	 *         Object... cmdParams) { PreparedStatement pstmt =
	 *         getPreparedStatement(conn, cmdText, cmdParams); DataSet dbc = new
	 *         DataSet(); if (pstmt == null) { dbc.code = -2; return dbc; } try
	 *         { // 查询语句 dbc.rs = pstmt.executeQuery(); dbc.model =
	 *         buildTableModel(dbc.rs); dbc.code = dbc.model.getRowCount(); }
	 *         catch (SQLException ex) {
	 *         Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE,
	 *         null, ex); dbc.code = -1; } return dbc; }
	 */
	private static void close(Object obj) {
		if (obj == null) {
			return;
		}
		try {
			if (obj instanceof Statement) {
				((Statement) obj).close();
			} else if (obj instanceof PreparedStatement) {
				((PreparedStatement) obj).close();
			} else if (obj instanceof ResultSet) {
				((ResultSet) obj).close();
			} else if (obj instanceof Connection) {
				((Connection) obj).close();
			}
		} catch (SQLException ex) {
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	private static void closeEx(Object obj) {
		if (obj == null) {
			return;
		}
		try {
			if (obj instanceof Statement) {
				((Statement) obj).close();
			} else if (obj instanceof PreparedStatement) {
				((PreparedStatement) obj).close();
			} else if (obj instanceof ResultSet) {
				((ResultSet) obj).getStatement().close();
			} else if (obj instanceof Connection) {
				((Connection) obj).close();
			}
		} catch (SQLException ex) {
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	public static void closeConnection(Object obj) {
		if (obj == null) {
			return;
		}
		try {
			if (obj instanceof Statement) {
				((Statement) obj).getConnection().close();
			} else if (obj instanceof PreparedStatement) {
				((PreparedStatement) obj).getConnection().close();
			} else if (obj instanceof ResultSet) {
				((ResultSet) obj).getStatement().getConnection().close();
			} else if (obj instanceof Connection) {
				((Connection) obj).close();
			}
		} catch (SQLException ex) {
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}
}
