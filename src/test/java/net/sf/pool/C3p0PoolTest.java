package net.sf.pool;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.category.FastTests;
import org.junit.experimental.categories.Category;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Qt
 * @since Oct 12, 2019
 */
@Slf4j
public class C3p0PoolTest {
	static ComboPooledDataSource dataSource;

	@AfterClass
	public static void destroy() throws SQLException {
		dataSource.close();
	}

	@BeforeClass
	public static void init() throws SQLException, IOException, ClassNotFoundException, PropertyVetoException {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("dbconfig.properties");
		Properties info = new Properties();
		info.load(in);
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setProperties(info);
		dataSource.setJdbcUrl(info.getProperty("jdbcUrl"));
		dataSource.setDriverClass(info.getProperty("driverClass"));
		C3p0PoolTest.dataSource = dataSource;
	}

	@Test
	@Category(FastTests.class)
	public void test01() {
		String sql = "select now()";
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
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
				conn.close();
			} catch (SQLException e) {
			}
		}
	}
}
