package net.sf.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.category.FastTests;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author Qt
 * @since Oct 12, 2019
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JdbcTest {
	static Connection conn;

	@AfterClass
	public static void destroy() throws SQLException {
		conn.close();
	}
	@BeforeClass
	public static void init() throws SQLException, IOException, ClassNotFoundException {
		conn = getConnection();
	}

	public static Connection getConnection() throws IOException, SQLException, ClassNotFoundException {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("dbconfig.properties");
		Properties info = new Properties();
		info.load(in);
		Class.forName(info.getProperty("driverClass"));
		return DriverManager.getConnection(info.getProperty("jdbcUrl"), info);
	}

	@Test
	@Category(FastTests.class)
	public void test01() {
		String sql = "create table sys_msg ( " +
			"id bigint unsigned not null auto_increment comment '消息ID', " +
			"code varchar(128) not null comment '消息键', " +
			"value varchar(255) not null comment '消息值', " +
			"deleted tinyint(1) not null default 0 comment '是否删除', " +
			"create_time timestamp not null default current_timestamp," +
			"update_time timestamp not null default current_timestamp," +
			"primary key (id), " +
			"unique key i_sys_msg_code (code) " +
			") comment='消息'";

		Statement st = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
			}
		}
	}

	@Test
	@Category(FastTests.class)
	public void test02() {
		String sql = "insert into sys_msg (code,value) values(?,?)";
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(sql);
			for (int i = 'a'; i < 'g'; i++) {
				st.setString(1, Character.toString((char) i));
				st.setString(2, Integer.toString(i));
				st.addBatch();
			}
			st.executeBatch();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
			}
		}
	}

	@Test
	@Category(FastTests.class)
	public void test03() {
		String sql = "select * from sys_msg";
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			int c = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				System.out.printf("%s", rs.getString(1));
				for (int i = 2; i <= c; i++) {
					System.out.printf("\t|\t%s", rs.getString(i));
				}
				System.out.println();
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
			}
		}
	}
}
