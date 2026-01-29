package com.selenium.poc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SeleniumPocUtil {
	
	private static final Properties properties = new Properties();
	
	static {
		try (InputStream input = SeleniumPocUtil.class.getClassLoader().getResourceAsStream("selenium.properties")) {
			if (input == null) {
				throw new RuntimeException("Unable to find selenium.properties");
			}
			properties.load(input);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load selenium.properties", e);
		}
	}

    public static boolean openPlaygroundAndGetTitle() {
    	WebDriver driver = null;
    	
    	try {
    		// Open browser
    		driver = new ChromeDriver();
    		
    		// Get URL from properties
    		String url = properties.getProperty("selenium.url");
    		String expectedText = properties.getProperty("selenium.expected.text");
    		
    		// Navigate to the URL
    		driver.get(url);
    		
    		// Get page source
    		String pageSource = driver.getPageSource();
    		
    		// Check if the page contains the expected text
    		boolean textFound = pageSource.contains(expectedText);
    		
    		if (textFound) {
    			System.out.println("SUCCESS - Text found: '" + expectedText + "'");
    		} else {
    			System.out.println("FAILURE - Text not found: '" + expectedText + "'");
    		}
    		
    		// Return true if text found, false otherwise
    		return textFound;
    		
    	} finally {
    		// Close the browser cleanly
            // commented for testing purposes
    		// if (driver != null) {
    		// 	driver.quit();
    		// }
    	}
    }
}