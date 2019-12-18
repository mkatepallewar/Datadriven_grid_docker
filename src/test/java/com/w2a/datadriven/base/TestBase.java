package com.w2a.datadriven.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.w2a.utilities.ExtentManager;


public class TestBase {

	/*
	 * WebDriver Logs properties excel db mail extent reports
	 * 
	 * 
	 */

	public static ThreadLocal<RemoteWebDriver> dr = new ThreadLocal<RemoteWebDriver>();
	public RemoteWebDriver driver = null;

	public Properties OR = new Properties();
	public Properties Config = new Properties();
	public FileInputStream fis;
	public Logger log=Logger.getLogger("devpinoyLogger");
	public WebDriverWait wait;
	public ExtentReports rep = ExtentManager.getInstance();
	public ExtentTest test;

	public ThreadLocal<ExtentTest> exTest = new ThreadLocal<ExtentTest>();

	public static String screenShotFile;
	public static String screenShotName;
	public String browser;
	
	public void addLog(String message) {
		log.debug("Thread : "+getThreadValue(dr.get())+" Browser : " + browser + " " + message);
	}
	
	public String getThreadValue(Object value) {
		
		String text=value.toString();
		String[] new_text=text.split(" ");
		String text2=new_text[new_text.length-1].replace("(", "").replace(")", "");
		
		String[] newText2=text2.split("-");
		String requiredText=newText2[newText2.length-1];
		
		return requiredText;
		
	}
	
	public void captureScreeShot() {

		File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		Date d = new Date();
		screenShotName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";

		try {
			FileUtils.copyFile(srcFile,
					new File(System.getProperty("user.dir") + "\\target\\surefire-reports\\html\\" + screenShotName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		getExtentTest().log(LogStatus.INFO, " Screen Shot -->" + test.addScreenCapture(
				System.getProperty("user.dir") + "\\target\\surefire-reports\\html\\" + screenShotName));
	}

	public void setUp() {

		if (driver == null) {
			try {
				fis = new FileInputStream(
						System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\config.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Config.load(fis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				fis = new FileInputStream(
						System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\OR.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				OR.load(fis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public WebDriver getDriver() {
		return dr.get();
	}

	public void setDriver(RemoteWebDriver driver) {
		dr.set(driver);
	}

	public ExtentTest getExtentTest() {
		return exTest.get();
	}

	public void setExtentTest(ExtentTest et) {
		exTest.set(et);
	}

	public void reportPass(String msg) {
		getExtentTest().log(LogStatus.PASS, msg);
	}

	public void reportFailure(String msg) {
		getExtentTest().log(LogStatus.FAIL, msg);
		captureScreeShot();

	}

	public void openBrowser(String browser) throws MalformedURLException {
		this.browser=browser;
		DesiredCapabilities cap = null;
			
		if (browser.equals("firefox")) {
			cap = DesiredCapabilities.firefox();
			cap.setBrowserName("firefox");
			cap.setPlatform(Platform.ANY);
		} else if (browser.equals("chrome")) {
			cap = DesiredCapabilities.chrome();
			cap.setBrowserName("chrome");
			cap.setPlatform(Platform.ANY);
		}
		if (browser.equals("iexplore")) {
			cap = DesiredCapabilities.internetExplorer();
			cap.setBrowserName("iexplore");
			cap.setPlatform(Platform.WINDOWS);
		}
		driver = new RemoteWebDriver(new URL("http://192.168.99.100:4444/wd/hub"), cap);
		setDriver(driver);
		getDriver().manage().timeouts().implicitlyWait(Integer.parseInt(Config.getProperty("implicit.wait")),
				TimeUnit.SECONDS);
		driver.manage().window().maximize();
		getExtentTest().log(LogStatus.INFO, "Browser Opened Successfully " + browser);
		
		System.out.println("Thread value is : " + getThreadValue(dr.get()));
		

	}

	public void navigate(String url) {
		getDriver().get(Config.getProperty(url));
		getExtentTest().log(LogStatus.INFO, "Nevigating to " + Config.getProperty(url));

	}

	static WebElement dropdown;

	public void select(String locator, String value) {
		try {
		if (locator.endsWith("_CSS")) {
			dropdown = getDriver().findElement(By.cssSelector(OR.getProperty(locator)));
		} else if (locator.endsWith("_XPATH")) {
			dropdown = getDriver().findElement(By.xpath(OR.getProperty(locator)));
		} else if (locator.endsWith("_ID")) {
			dropdown = getDriver().findElement(By.id(OR.getProperty(locator)));
		}
		Select select = new Select(dropdown);
		select.selectByVisibleText(value);
		getExtentTest().log(LogStatus.INFO, "Selecting drop down value  " + value);
		}catch(Throwable t) {
			reportFailure("Failing while Selecting an element " + locator);
		}

	}

	public void click(String locator) {
		try {
		if (locator.endsWith("_CSS")) {
			getDriver().findElement(By.cssSelector(OR.getProperty(locator))).click();
		}else if(locator.endsWith("_XPATH")) {
			getDriver().findElement(By.xpath(OR.getProperty(locator))).click();
		}else if(locator.endsWith("_ID")) {
			getDriver().findElement(By.id(OR.getProperty(locator))).click();
		}
		getExtentTest().log(LogStatus.INFO, "Clicking " + OR.getProperty(locator));
		
		addLog("Clicking on an element : " +locator);
		
		}catch (Throwable t){
			reportFailure("Failing while clicking on element " + locator);
		}
	}

	public void type(String locator, String value) {
		try {
		if (locator.endsWith("_CSS")) {
			getDriver().findElement(By.cssSelector(OR.getProperty(locator))).sendKeys(value);
		} else if (locator.endsWith("_XPATH")) {
			getDriver().findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);
		} else if (locator.endsWith("_ID")) {
			getDriver().findElement(By.id(OR.getProperty(locator))).sendKeys(value);
		}

		getExtentTest().log(LogStatus.INFO, "Entering value  " + value);
		addLog("Typing in an element : " +locator);
	}catch(Throwable t){
		reportFailure("Failing while typing on element " + locator);
	}
	}
		

	public boolean isElementPresent(By by) {
		try {
			getDriver().findElement(by);
			return true;

		} catch (NoSuchElementException e) {
			return false;
		}
	}

}
