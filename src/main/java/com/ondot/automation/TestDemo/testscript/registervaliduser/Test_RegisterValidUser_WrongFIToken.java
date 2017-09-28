package com.ondot.automation.TestDemo.testscript.registervaliduser;

import java.io.File;

import org.codehaus.jettison.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.atmecs.falcon.automation.dataprovider.TestDataProvider;
import com.atmecs.falcon.automation.rest.endpoint.RequestBuilder;
import com.atmecs.falcon.automation.rest.endpoint.ResponseService;
import com.atmecs.falcon.automation.util.reporter.ReportLogService;
import com.atmecs.falcon.automation.util.reporter.ReportLogServiceImpl;
import com.atmecs.falcon.automation.verifyresult.VerificationManager;
import com.ondot.automation.TestDemo.testfunction.OnDotConstants;
import com.ondot.automation.TestDemo.testfunction.OnDotUtilities;
import com.ondot.automation.TestDemo.testsuite.SampleTestSuiteBase;

/*
 * This class verifies the acual and expected data after changing FIToken and AppToken values for Register_Valid_User operation.
 */

public class Test_RegisterValidUser_WrongFIToken extends SampleTestSuiteBase{
	
	JSONObject registerTestData;
	JSONObject requestBody;
	JSONObject expectedResponseBody;
	
	TestDataProvider dataProvider = TestDataProvider.getInstance();
	
	RequestBuilder requestBuilder = new RequestBuilder();
	ReportLogService report = new ReportLogServiceImpl(Test_RegisterValidUser.class);
	
	OnDotUtilities utils= new OnDotUtilities();
	
	@BeforeTest
	public void dataSetup() throws Exception {
		TestDataProvider.folderPath =OnDotConstants.TESTDATA_FOLDER_REGISTER_VALID_USER;
	}
	
	@Test(dataProvider = "DataProvider", dataProviderClass = TestDataProvider.class)
	public void testfitoken(File testDataFile) throws Exception {
		
		registerTestData = dataProvider.getJSONObject(testDataFile);
		requestBody = registerTestData.getJSONObject("requestBody");
		expectedResponseBody = registerTestData.getJSONObject("responseBody");
		
		
		report.info("Step #1: Do Post Call for Register_Valid_User and Store Response");
		ResponseService responseService = requestBuilder.contentType("application/json").queryParam("apiVersion", "v2.0")
				.queryParam("opId", "REGISTER_VALID_USER").body(requestBody.toString()).build()
				.post(CONFIG.getProperty("baseURI")+CONFIG.getProperty("registerEndPoint"));
		
		
		JSONObject actualResponseJSONObject = new JSONObject(responseService.getResponseBody());
		
		report.info("Step #2: Get Actual data  => " + testDataFile.getName());
		String actualmessageCode=utils.getValueFromJsonData(actualResponseJSONObject, "responseStatus", "messageCode");
		String actualoperationId=utils.getValueFromJsonData(actualResponseJSONObject, "opId");
		String actualresponseCode=utils.getValueFromJsonData(actualResponseJSONObject, "responseStatus", "responseCode");
		
		report.info("Step #2: Get expected message code  => " + testDataFile.getName());
		String expectedmessagecode=utils.getValueFromJsonData(expectedResponseBody, "responseStatus", "messageCode");
		String expectedoperationId=utils.getValueFromJsonData(actualResponseJSONObject, "opId");
		String expectedresponseCode=utils.getValueFromJsonData(expectedResponseBody, "responseStatus", "responseCode");
		
		report.info("Step #3: compare actual vs expected  => " + testDataFile.getName());
		VerificationManager.verifyString(actualmessageCode, expectedmessagecode, "verification of message code");
		VerificationManager.verifyString(actualoperationId, expectedoperationId, "verification of operation code");
		VerificationManager.verifyString(actualresponseCode, expectedresponseCode, "verification of responsecode");
	}
}

