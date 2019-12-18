package com.w2a.suite.bankmanager.testcases;

import java.net.MalformedURLException;
import java.util.Hashtable;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import com.graphbuilder.math.OpNode;
import com.relevantcodes.extentreports.LogStatus;
import com.w2a.datadriven.base.TestBase;
import com.w2a.utilities.Constants;
import com.w2a.utilities.DataProviders;
import com.w2a.utilities.DataUtil;
import com.w2a.utilities.ExcelReader;

public class AddCustomerTest extends TestBase {
	
	@Test (dataProviderClass = DataProviders.class,dataProvider = "bankManagerDP", groups = "customerCreation" )
	public void addCustomerTest(Hashtable<String, String> data) throws MalformedURLException {
		
		super.setUp();
		test=rep.startTest("Add Customer Test" + "  "+ data.get("browser"));
		setExtentTest(test);
		ExcelReader excel=new ExcelReader(Constants.SUITE1_XL_PATH);
		DataUtil.checkExecution("BankManagerSuite", "AddCustomerTest", data.get("Runmode"), excel);
		openBrowser(data.get("browser"));
		navigate("testsiteurl");
		click("bmlBtn_CSS");
		click("addCustBtn_CSS");
		type("firstName_CSS", data.get("firstname"));
		type("lastName_XPATH", data.get("lastname"));
		type("postCode_CSS", data.get("postcode"));
		click("addBtn_CSS");
		
		reportPass("Add Customer Test Pass");
		
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
