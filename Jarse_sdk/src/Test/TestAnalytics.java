package Test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import jarse.Jarse;
import jarse.JarseAnalytics;

import org.junit.Test;

public class TestAnalytics {
	
	public TestAnalytics() {
		Jarse.init("127.0.0.1", "test", "jarse", "poop");
	}

	@Test
	public void testAnalytics() {
		Map<String, String> dimensions = new HashMap<String, String>();
		dimensions.put("price", "1000-1500");
		dimensions.put("source", "craigslist");
		dimensions.put("dayType", "weekday");
		JarseAnalytics.trackEvent("search", dimensions);
		
	}

}
