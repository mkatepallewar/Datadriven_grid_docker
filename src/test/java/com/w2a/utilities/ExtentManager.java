package com.w2a.utilities;

import java.io.File;
import java.util.Date;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {
	
	private static ExtentReports extent;
	
	public static ExtentReports getInstance() {
		
		if (extent==null) {
			
			Date d=new Date();
			String fileName=d.toString().replace(":", "_").replace(" ","_")+".html";
			extent=new ExtentReports(System.getProperty("user.dir")+"\\report\\"+fileName,true,DisplayOrder.OLDEST_FIRST);
			extent.loadConfig(new File (System.getProperty("user.dir") + "\\src\\test\\resources\\extentConfig\\ReportConfig.xml"));
		}
		return extent;
	}
	

}
