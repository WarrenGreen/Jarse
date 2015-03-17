package jarse;

public class Jarse {
	
	private static String url;
	private static String database;
	private static String username;
	private static String password;
	
	public static void init(String url, String database, String username, String password) {
		Jarse.url = url;
		Jarse.database = database;
		Jarse.username = username;
		Jarse.password = password;
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		Jarse.url = url;
	}

	public static String getDatabase() {
		return database;
	}

	public static void setDatabase(String database) {
		Jarse.database = database;
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		Jarse.username = username;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		Jarse.password = password;
	}
	
}
