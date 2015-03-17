package jarse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
	
	public Test() {
		Jarse.init("127.0.0.1", "test", "jarse", "poop");
		
	}
	
	public void insert() {
		JarseObject jo = new JarseObject("test");
		jo.put("K", "poop");
		jo.save();
	}
	
	public void delete() {
		JarseObject jo = new JarseObject("test");
		jo.put("K", "poop");
		jo.save();
		jo.setObjectId("6");
		jo.delete();
	}
	
	public void get() {
		JarseQuery query = new JarseQuery("test");
		JarseObject jo = query.get(1+"");
		System.out.println(jo.get("K"));
	}
	
	public void find() {
		JarseQuery query = new JarseQuery("test");
		//query.whereEqualTo("K", "dog");
		query.orderBy("objectId");
		query.orderDescending();
		List<JarseObject> result = query.find();
		for(JarseObject jo: result) {
			System.out.println(jo.getObjectId()+", "+jo.getString("K")+", "+ jo.getInt("num")+", "+jo.getBoolean("bool"));
		}
	}
	
	
	public void analytics() {
		Map<String, String> dimensions = new HashMap<String, String>();
		dimensions.put("price", "1000-1500");
		dimensions.put("source", "craigslist");
		dimensions.put("dayType", "weekday");
		JarseAnalytics.trackEvent("search", dimensions);
	}
	
	public static void main(String[] args) {
		Test t = new Test();
		//t.insert();
		//t.delete();
		//t.get();
		t.find();
		//t.analytics();
	}
}
