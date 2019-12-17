package com.w2a.datadriven.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.LogStatus;

public class TestBase {
	
	/*
	 * WebDriver
	 * Logs
	 * properties
	 * excel
	 * db
	 * mail
	 * extent reports
	 * 
	 * 
	 */
	
	public static ThreadLocal<RemoteWebDriver> dr=new ThreadLocal<RemoteWebDriver>();
	public RemoteWebDriver driver=null;
	
	public Properties OR=new Properties();
	public Properties Config=new Properties();
	public FileInputStream fis;
	public Logger log=Logger.getLogger("devpinoyLogger");
	public WebDriverWait wait;
	
	@BeforeSuite
	public void setUp() {
		
		if (driver==null) {
			try {
				fis=new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\config.properties");
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
				fis=new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\OR.properties");
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
	
	public void openBrowser(String browser) throws MalformedURLException {
		
		DesiredCapabilities cap=null;
		if (browser.equals("firefox")) {
			cap=DesiredCapabilities.firefox();
			cap.setBrowserName("firefox");
			cap.setPlatform(Platform.ANY);
		}else if (browser.equals("chrome")) {
			cap=DesiredCapabilities.chrome();
			cap.setBrowserName("chrome");
			cap.setPlatform(Platform.ANY);
		} if (browser.equals("iexplore")) {
			cap=DesiredCapabilities.internetExplorer();
			cap.setBrowserName("iexplore");
			cap.setPlatform(Platform.WINDOWS);
		}
		driver=new RemoteWebDriver(new URL("http://192.168.99.100:4444/wd/hub"), cap);
		setDriver(driver);
		getDriver().manage().timeouts().implicitlyWait(Integer.parseInt(Config.getProperty("implicit.wait")), TimeUnit.SECONDS);
		driver.manage().window().maximize();
		
		
	}
	
	public void navigate (String url) {
		getDriver().get(Config.getProperty(url));
		
	}
	
static WebElement dropdown;
	
	public void select(String locator, String value) {
		if (locator.endsWith("_CSS")) {
			dropdown= getDriver().findElement(By.cssSelector(OR.getProperty(locator)));
		}else if(locator.endsWith("_XPATH")) {
			dropdown= getDriver().findElement(By.xpath(OR.getProperty(locator)));
		}else if(locator.endsWith("_ID")) {
			dropdown= getDriver().findElement(By.id(OR.getProperty(locator)));
		}
		Select select=new Select(dropdown);
		select.selectByVisibleText(value);
		
	}
	public void click(String locator) {
		
		if (locator.endsWith("_CSS")) {
			getDriver().findElement(By.cssSelector(OR.getProperty(locator))).click();
		}else if(locator.endsWith("_XPATH")) {
			getDriver().findElement(By.xpath(OR.getProperty(locator))).click();
		}else if(locator.endsWith("_ID")) {
			getDriver().findElement(By.id(OR.getProperty(locator))).click();
		}
	}
	
	public void type(String locator, String value) {
		
		if (locator.endsWith("_CSS")) {
			getDriver().findElement(By.cssSelector(OR.getProperty(locator))).sendKeys(value);
		}else if(locator.endsWith("_XPATH")) {
			getDriver().findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);
		}else if(locator.endsWith("_ID")) {
			getDriver().findElement(By.id(OR.getProperty(locator))).sendKeys(value);
		}		
	}
	
	
	
	
	public boolean isElementPresent(By by) {
		try {
			getDriver().findElement(by);
			return true;
			
		}catch(NoSuchElementException e) {
			return false;
		}
	}
	


}
