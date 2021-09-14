package testcases;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.impl.Log4JLogger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import utilities.ReadConfig;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;

public class BaseTestClass {

	static WebDriver driver;
	String browser;
	String appUrl;
	String name;
	String userId;
	String password;
	String wrongUserId;
	String wrongPassword;
	ReadConfig readConfig;
	Logger log4jlogger;

	@Parameters("browser")
	@BeforeClass
	public void setup(String browser) {
		this.browser = browser;
		readConfig = new ReadConfig();
		appUrl = readConfig.getAppUrl();
		name = readConfig.getName();
		userId = readConfig.getUserId();
		password = readConfig.getPassword();
		wrongUserId = readConfig.getWrongUserId();
		wrongPassword = readConfig.getWrongPassword();

		log4jlogger = Logger.getLogger("Amazon.in - AUT");
		PropertyConfigurator.configure("log4j.properties");
	}

	@BeforeMethod
	public void beforeEachTest() {
		if (browser.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", readConfig.getChromeDriverPath());
			driver = new ChromeDriver();
			log4jlogger.info("Browser : Google Chrome");
		} else if (browser.equals("firefox")) {
			System.setProperty("webdriver.gecko.driver", readConfig.getFireFoxDriverPath());
			driver = new FirefoxDriver();
			log4jlogger.info("Browser : Mozilla Firefox");
		} else if (browser.equals("ie")) {
			System.setProperty("webdriver.ie.driver", readConfig.getIEDriverPath());
			driver = new InternetExplorerDriver();
			log4jlogger.info("Browser : Internet Explorer");
		} else if (browser.equals("edge")) {
			System.setProperty("webdriver.edge.driver", readConfig.getEdgeDriverPath());
			driver = new EdgeDriver();
			log4jlogger.info("Browser : Microsoft Edge");
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
		driver.get(appUrl);
	}

	@AfterMethod
	public void afterEachTest(ITestResult tr) {
		driver.quit();
	}

	@AfterClass
	public void tearDown() {

	}

	boolean areMostOfOutputsMatchingInput(String input, List<String> outputs) {
		int matchedCount = 0, unmatchedCount = 0;
		for (String item : outputs) {
			if (StringUtils.containsIgnoreCase(item, input)) {
				matchedCount++;
			} else {
				unmatchedCount++;
			}
		}
		log4jlogger.info("No. of matched items : " + matchedCount);
		log4jlogger.info("No. of unmatched items : " + unmatchedCount);
		return matchedCount > unmatchedCount;
	}

	public static void captureScreenshot(String testName) {

		File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File target = new File("./screenshots/" + testName + ".png");
		try {
			FileUtils.copyFile(source, target);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
