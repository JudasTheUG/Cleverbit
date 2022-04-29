package com.saf.framework;


import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import org.openqa.selenium.chrome.ChromeDriver;



public class DriverFactory {

	private static DriverFactory instance = null;
	ThreadLocal<WebDriver> oDriver = new ThreadLocal<WebDriver>();

	private DriverFactory() {

	}

	public static DriverFactory getInstance() {
		if (instance == null) {
			instance = new DriverFactory();
		}
		return instance;
	}


	public final void setWebDriver(String sDriverName) throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();


		if (sDriverName.equalsIgnoreCase("chrome")) {
			CommonLib.getDriver(sDriverName);
			//Use a higher value if your mobile elements take time to show up
			oDriver.get().manage().timeouts().implicitlyWait(35, TimeUnit.SECONDS);

		}

	}

	public WebDriver getWebDriver() {
		return oDriver.get();
	}


}
