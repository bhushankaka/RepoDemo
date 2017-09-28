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
 * This class verifies the positive scenarios for operation register_valid_user
 */

public class Test_RegisterValidUser extends SampleTestSuiteBase{
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
	public void test_register_valid_user(File testDataFile) throws Exception {
		
		registerTestData = dataProvider.getJSONObject(testDataFile);
		requestBody = registerTestData.getJSONObject("requestBody");
		expectedResponseBody = registerTestData.getJSONObject("responseBody");
		
		
		report.info("Step #1: Do Post Call for Register_Valid_User and Store Response");
		ResponseService responseService = requestBuilder.contentType("application/json").queryParam("apiVersion", "v2.0")
				.queryParam("opId", "REGISTER_VALID_USER").body(requestBody.toString()).build()
				.post(CONFIG.getProperty("baseURI")+CONFIG.getProperty("registerEndPoint"));
		
		//VerificationManager.verifyInteger(responseService.getStatusCode(), 201, "Verifying the status code");
		
		
		JSONObject actualResponseJSONObject = new JSONObject(responseService.getResponseBody());
		
		report.info("Step #2: Get Actual data from the test data file => " + testDataFile.getName());
		String actualFiToken=utils.getValueFromJsonData(actualResponseJSONObject, "responseDataList", 0, "fiToken");
		String actualsubscriberReferenceId=utils.getValueFromJsonData(actualResponseJSONObject, "responseDataList", 0, "subscriberReferenceId"); 
		String actualmessageCode=utils.getValueFromJsonData(actualResponseJSONObject, "responseStatus", "messageCode");
		//String actualoperationTraceId=utils.getValueFromJsonData(actualResponseJSONObject, "responseStatus", "operationTraceId");
		
		report.info("Step #3: Get Expected data from the test data file => " + testDataFile.getName());
		String expectedFiToken=utils.getValueFromJsonData(expectedResponseBody,"responseDataList", 0,"fiToken");
		String expectedSubscriberReferenceId= utils.getValueFromJsonData(expectedResponseBody,"responseDataList" , 0,"subscriberReferenceId");
		String expectedmessageCode=utils.getValueFromJsonData(expectedResponseBody, "responseStatus", "messageCode");
		//String expectedoparationTraceId=utils.getValueFromJsonData(expectedResponseBody, actualoperationTraceId, "operationTraceId");
		
		report.info("Step #4: Compare Actual vs Excepted");
		VerificationManager.verifyString(actualFiToken, expectedFiToken,"verification of FI Token");
		VerificationManager.verifyString(actualsubscriberReferenceId, expectedSubscriberReferenceId, "verification of subscriberReferenceId");
		VerificationManager.verifyString(actualmessageCode, expectedmessageCode, "verification of messagecode");
		//VerificationManager.verifyInteger(responseService.getStatusCode(), 200, "Verifying the status code");
		//VerificationManager.verifyString(actualoperationTraceId, actualoperationTraceId, "verification of oparationTraceId");
	}
}
