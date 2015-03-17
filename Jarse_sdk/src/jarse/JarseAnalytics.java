package jarse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class JarseAnalytics {

	private static Connection connect = null;
	private static Statement statement = null;
	
	private static final String ANALYTICS_TABLE = "jarse_analytics";
	
	public static void trackEvent(String event, Map<String, String> dimensions) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://"
					+ Jarse.getUrl() + "/" + Jarse.getDatabase() + "?"
					+ "user=" + Jarse.getUsername() + "&password="
					+ Jarse.getPassword());

			statement = connect.createStatement();

			for(Entry<String, String> e: dimensions.entrySet()){
				String query = String.format("INSERT INTO %s.%s VALUES ('%s', '%s', '%s', null);",
						Jarse.getDatabase(), ANALYTICS_TABLE, event, e.getKey(), e.getValue());
				statement.executeUpdate(query);
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	private static void close() {
		try {
			if (connect != null)
				connect.close();
			if (statement != null)
				statement.close();
		} catch (SQLException e) {

		}
	}
}
