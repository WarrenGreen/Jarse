package Test;

import static org.junit.Assert.*;
import jarse.Jarse;
import jarse.JarseException;
import jarse.JarseObject;
import jarse.JarseQuery;

import org.junit.Test;

public class TestObject {

	public TestObject() {
		Jarse.init("127.0.0.1", "test", "jarse", "poop");
	}
	
	@Test
	public void testInsert() {
		JarseObject jo = new JarseObject("test");
		jo.setObjectId(8989+"");
		jo.put("K", "demo");

		jo.save();
		
		JarseQuery jq = new JarseQuery("test");
		try {
			JarseObject j1 = jq.get(8989+"");
			assertEquals(j1.get("K").toString(), "demo");
		} catch (JarseException e) {
			System.err.println(e.getMessage());
		}
		
		jo.delete();
	}
	
	@Test
	public void testDelete() {
		JarseQuery jq = new JarseQuery("test");
		JarseObject jo = new JarseObject("test");
		jo.setObjectId("8989");
		jo.put("K", "demo");
		jo.save();
		
		try {
			assertEquals(jq.get("8989").get("K").toString(), "demo"); 
			
			jo.delete();
			jq.get("8989");
			
		} catch (JarseException e) {
			assertEquals(e.getMessage(), "No object with that id."); 
		}
	}

}
