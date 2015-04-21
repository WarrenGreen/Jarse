package jarse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JarseQuery {
	
	private String classname;
	private Map<String, String> qWhereEqualTo;
	private Map<String, String[]> qWhere;
	private ArrayList<String> qOrderBy;
	private boolean qOrderDesc;
	private int qLimit;

	private Connection connect = null;
	private Statement statement = null;
	private ResultSet result = null;
	
	public JarseQuery(String classname) {
		this.classname = classname;
		qWhereEqualTo = new HashMap<String, String>();
		qWhere = new HashMap<String, String[]>();
		qOrderBy = new ArrayList<String>();
		qLimit = -1;
		qOrderDesc = false;
	}
	
	/**
	 * Get object by the objectId.
	 * @param objectId
	 * @return
	 * @throws JarseException 
	 */
	public JarseObject get(String objectId) throws JarseException {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://"
					+ Jarse.getUrl() + "/" + Jarse.getDatabase() + "?"
					+ "user=" + Jarse.getUsername() + "&password="
					+ Jarse.getPassword());

			statement = connect.createStatement();

			String query = String.format("SELECT * FROM %s WHERE objectId = %s;", classname, objectId);
			result = statement.executeQuery(query);
			
			if(!result.next()) 
				throw new JarseException("No object with that id.");
			
			ResultSetMetaData rsmd = result.getMetaData();
			JarseObject ret = new JarseObject(classname);
			ret.setObjectId(objectId);
			
			//result.next();
			for(int i=1;i<=rsmd.getColumnCount();i++){
				ret.put(rsmd.getColumnName(i), result.getString(i));
			}
			
			return ret;
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		
		return null;
	}
	
	/**
	 * Find object based on supplied parameters.
	 * @return
	 */
	public List<JarseObject> find() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://"
					+ Jarse.getUrl() + "/" + Jarse.getDatabase() + "?"
					+ "user=" + Jarse.getUsername() + "&password="
					+ Jarse.getPassword());

			statement = connect.createStatement();

			String query = String.format("SELECT * FROM %s", classname);
			
			if(qWhere.size() > 0)
				query += " WHERE ";
			Iterator<Entry<String, String[]>> dIt = qWhere.entrySet().iterator();
			Entry<String, String[]> d;
			while (dIt.hasNext()) {
				d = dIt.next();
				query += d.getKey() +d.getValue()[0]+ "'"+d.getValue()[1]+"'";
				if(dIt.hasNext())
					query += " AND ";
			}
			
			if(qOrderBy.size() > 0)
				query += " ORDER BY ";
			for(int i=0;i<qOrderBy.size();i++) {
				query += qOrderBy.get(i);
				if(i+1 != qOrderBy.size())
					query += ", ";
			}
			
			if(qOrderDesc && qOrderBy.size() > 0)
				query += " DESC";
			
			query += ";";
			
			result = statement.executeQuery(query);
			ResultSetMetaData rsmd = result.getMetaData();
			
			List<JarseObject> ret = new ArrayList<JarseObject>();
			while(result.next()) {
				JarseObject jo = new JarseObject(classname);
				for(int i=1;i<=rsmd.getColumnCount();i++){
					jo.put(rsmd.getColumnName(i), result.getObject(i));
				}
				ret.add(jo);
			}
			
			return ret;
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		
		return null;
	}
	
	/**
	 * Add where = to query
	 * @param key
	 * @param value
	 */
	public void whereEqualTo(String key, String value) {
		qWhere.put(key, new String[]{"=", value});
	}
	
	/**
	 * Add where <> to query
	 * @param key
	 * @param value
	 */
	public void whereNotEqualTo(String key, String value) {
		qWhere.put(key, new String[]{"<>", value});
	}
	
	/**
	 * Add where > to query
	 * @param key
	 * @param value
	 */
	public void whereGreaterThan(String key, String value) {
		qWhere.put(key, new String[]{">", value});
	}
	
	/**
	 * Add where >= to query
	 * @param key
	 * @param value
	 */
	public void whereGreaterThanOrEqualTo(String key, String value) {
		qWhere.put(key, new String[]{">=", value});
	}
	
	/**
	 * Add where < to query
	 * @param key
	 * @param value
	 */
	public void whereLessThan(String key, String value) {
		qWhere.put(key, new String[]{"<", value});
	}
	
	/**
	 * Add where <= to query
	 * @param key
	 * @param value
	 */
	public void whereLessThanOrEqualTo(String key, String value) {
		qWhere.put(key, new String[]{"<=", value});
	}
	
	/**
	 * Add where key in (values) to query
	 * @param key
	 * @param values
	 */
	public void whereContainedIn(String key, List<String> values) {
		String value = "(";
		for(int i=0;i < values.size(); i++) {
			value += String.format("'%s'", values.get(i));
			if(i+1 != values.size())
				value += ", ";
		}
		value += ")";
		qWhere.put(key, new String[]{"IN", value});
	}
	
	public void whereContainedIn(String key, String...values) {
		whereContainedIn(key, Arrays.asList(values));
	}
	
	/**
	 * Add columns to order by
	 * @param columns
	 */
	public void orderBy(String... columns) {
		for(String col: columns) {
			qOrderBy.add(col);
		}
	}
	
	/**
	 * Order results ascending
	 */
	public void orderAscending() {
		qOrderDesc = false;
	}
	
	/**
	 * Order results descending
	 */
	public void orderDescending() {
		qOrderDesc = true;
	}
	
	public int getLimit() {
		return qLimit;
	}

	/**
	 * Limit the number of results.
	 * Limit less than or equal to zero means no limit.
	 * @param limit
	 */
	public void setLimit(int limit) {
		this.qLimit = limit;
	}

	private void close() {
		try {
			if (connect != null)
				connect.close();
			if (statement != null)
				statement.close();
			if (result != null)
				result.close();
		} catch (SQLException e) {

		}
	}
}
