package com.w2a.utilities;

import org.testng.annotations.Test;

import com.w2a.datadriven.base.TestBase;

public class TestLog4j extends TestBase{
	
	@Test
	public void testLog4j() {
		log.debug("Hello Debugger");
	}

}
