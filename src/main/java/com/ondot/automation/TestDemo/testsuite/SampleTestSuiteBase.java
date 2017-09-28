/****
 * This is the TestSuite Base for Company Setup configuration.
 * This Class extends the Test Base Class
 * Class has Before & After Suite method to connect/Disconnect Database
 * Class has Before Suite method to get Company setup jersey Client.
 * This is a must file for Company setup testNg script to execute & should not be deleted.
 */
package com.ondot.automation.TestDemo.testsuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.config.SSLConfig;

/**
 * 
 *         SampletestSuiteBase class holds the services common for all the
 *         scripts in the suite
 */
public class SampleTestSuiteBase {

	/**
	 * Properties File holds common values which are used across all the scripts
	 */
	public static Properties CONFIG = null;


	public static boolean isInitialized = false;
	
	@BeforeSuite
	public void configure(){
		RestAssured.config = RestAssuredConfig.config().sslConfig(SSLConfig.sslConfig().allowAllHostnames());
		intialize();	
	}
	private void intialize() {
		
		if(!isInitialized){
			CONFIG = new Properties();
			try {
				FileInputStream inputStream = new FileInputStream(System.getProperty("user.dir") + File.separator + "src"
			            + File.separator + "main" + File.separator + "resources" + File.separator
			            + "config.properties");
				CONFIG.load(inputStream);
				isInitialized = true;
			} catch (FileNotFoundException e) {
		
				e.printStackTrace();
			} catch (IOException e) {
		
				e.printStackTrace();
			}
			
		}
	}


}