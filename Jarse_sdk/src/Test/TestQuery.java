package Test;

import static org.junit.Assert.*;

import java.util.List;

import jarse.Jarse;
import jarse.JarseException;
import jarse.JarseQuery;
import jarse.JarseObject;

import org.junit.Test;

public class TestQuery {
	JarseObject jo;

	public TestQuery() {
		Jarse.init("127.0.0.1", "test", "jarse", "poop");
		jo = new JarseObject("test");
		jo.setObjectId(6968+"");
		jo.put("K", "testtest");
		jo.save();
	}
	
	public void tearDown() {
		jo.delete();
	}
	
	@Test
	public void testGet() {
		JarseQuery query = new JarseQuery("test");
		JarseObject j1;
		try {
			j1 = query.get(jo.getObjectId());
			assertEquals(j1.get("K").toString(), "testtest");
		} catch (JarseException e) {
			System.out.println(e.getMessage());
		}
		
		tearDown();
	}
	
	@Test
	public void testFind() {
		JarseQuery query = new JarseQuery("test");
		query.whereEqualTo("K", "testtest");
		query.orderBy("objectId");
		query.orderDescending();
		List<JarseObject> result = query.find();
		assertEquals(result.get(0).get("K").toString(), "testtest");
		tearDown();
	}

}
