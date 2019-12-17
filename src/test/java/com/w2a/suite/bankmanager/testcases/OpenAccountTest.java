package com.w2a.suite.bankmanager.testcases;

import java.net.MalformedURLException;
import java.util.Hashtable;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.w2a.datadriven.base.TestBase;
import com.w2a.utilities.Constants;
import com.w2a.utilities.DataProviders;
import com.w2a.utilities.DataUtil;
import com.w2a.utilities.ExcelReader;

public class OpenAccountTest extends TestBase {
	
	@Test (dataProviderClass = DataProviders.class,dataProvider = "bankManagerDP" )
	public void openAccountTest(Hashtable<String, String> data) throws MalformedURLException {
		
		super.setUp();
		test=rep.startTest("Open Account Test " + "  "+ data.get("browser"));
		setExtentTest(test);
		ExcelReader excel=new ExcelReader(Constants.SUITE1_XL_PATH);
		DataUtil.checkExecution("BankManagerSuite", "OpenAccountTest", data.get("Runmode"), excel);
		openBrowser(data.get("browser"));
		navigate("testsiteurl");
		reportPass("Open Account Test Pass");
		
	}
	
	@AfterMethod
	public void tearDown() {
		if (rep!=null) {
			rep.endTest(getExtentTest());
			rep.flush();
		}
		getDriver().quit();
		
	}
}
