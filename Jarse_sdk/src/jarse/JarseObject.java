package jarse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class JarseObject {

	private String classname;
	private HashMap<String, Object> data;

	private Connection connect = null;
	private Statement statement = null;

	public static JarseObject create(String classname) {
		return new JarseObject(classname);
	}

	public JarseObject(String classname) {
		this.classname = classname;
		data = new HashMap<String, Object>();
	}

	public void save() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://"
					+ Jarse.getUrl() + "/" + Jarse.getDatabase() + "?"
					+ "user=" + Jarse.getUsername() + "&password="
					+ Jarse.getPassword());

			String id = getObjectId();
			
			if(getObjectId() == null || countRows() == 0) {
				insert();
			} else {
				update();
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
	
	/**
	 * Save object via an insert command
	 * @throws SQLException
	 */
	public void insert() throws SQLException {
		statement = connect.createStatement();

		String q1 = String.format("INSERT INTO %s.%s(",
				Jarse.getDatabase(), classname);
		String q2 = "VALUES (";

		Iterator<Entry<String, Object>> dIt = data.entrySet().iterator();
		Entry<String, Object> d;
		while (dIt.hasNext()) {
			d = dIt.next();
			q1 += d.getKey();
			q2 += "'" + d.getValue() + "'";
			if (dIt.hasNext()) {
				q1 += ", ";
				q2 += ", ";
			}
		}

		q1 += ") ";
		q2 += ");";

		statement.executeUpdate(q1 + q2);
	}
	
	/**
	 * Save object via an update command
	 * @throws SQLException
	 */
	public void update() throws SQLException {
		statement = connect.createStatement();

		String query = String.format("UPDATE %s.%s SET ",
				Jarse.getDatabase(), classname);

		Iterator<Entry<String, Object>> dIt = data.entrySet().iterator();
		Entry<String, Object> d;
		while (dIt.hasNext()) {
			d = dIt.next();
			query += String.format("%s='%s'", d.getKey(), d.getValue());
			if(dIt.hasNext()) 
				query += ", ";
		}
		
		query += String.format(" WHERE objectId=%s;", getObjectId());

		statement.executeUpdate(query);
	}

	public int countRows() throws SQLException {
		Statement statement = null;
		ResultSet result = null;
		int rowCount = -1;
		try {
			statement = connect.createStatement();
			result = statement.executeQuery("SELECT COUNT(*) FROM " + classname
					+ " WHERE objectId="+ getObjectId());
			result.next();
			rowCount = result.getInt(1);
		} finally {
			statement.close();
			result.close();
		}
		return rowCount;
	}

	/**
	 * Delete object
	 */
	public void delete() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://"
					+ Jarse.getUrl() + "/" + Jarse.getDatabase() + "?"
					+ "user=" + Jarse.getUsername() + "&password="
					+ Jarse.getPassword());

			statement = connect.createStatement();

			String query = String.format("DELETE FROM %s WHERE objectId = %s",
					classname, getObjectId());

			statement.executeUpdate(query);
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
	
	private void close() {
		try {
			if (connect != null)
				connect.close();
			if (statement != null)
				statement.close();
		} catch (SQLException e) {

		}
	}

	public void put(String key, Object value) {
		data.put(key, value);
	}

	public Object get(String key) {
		return data.get(key);
	}
	
	public String getString(String key) {
		Object value = get(key);
		
		if(value == null || value.getClass() != String.class) 
			return null;
		
		return (String) value;
	}
	
	public Integer getInt(String key) {
		Object value = get(key);

		if(value == null || value.getClass() != Integer.class) 
			return null;
		
		return (Integer) value;
	}
	
	public Double getDouble(String key) {
		Object value = get(key);
		
		if(value == null || value.getClass() != Double.class) 
			return null;
		
		return (Double) value;
	}
	
	public Boolean getBoolean(String key) {
		Object value = get(key);
		
		if(value == null || value.getClass() != Boolean.class) 
			return null;
		
		return (Boolean) value;
	}
	
	public Timestamp getTimestamp(String key) {
		Object value = get(key);

		if(value == null || value.getClass() != Timestamp.class) 
			return null;
		
		return (Timestamp) value;
	}

	public boolean contains(String key) {
		return data.containsKey(key);
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getObjectId() {
		if(data.containsKey("objectId"))
			return data.get("objectId").toString();
		else
			return null;
	}

	public void setObjectId(String id) {
		data.put("objectId", id);
	}

}
