package com.ondot.automation.TestDemo.testscript.getappinfo;

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
import com.ondot.automation.TestDemo.testscript.registervaliduser.Test_RegisterValidUser;
import com.ondot.automation.TestDemo.testsuite.SampleTestSuiteBase;

public class Test_GetAppInfo extends SampleTestSuiteBase{
	JSONObject getappinfodata;
	JSONObject requestBody;
	JSONObject expectedResponseBody;
	
TestDataProvider dataProvider = TestDataProvider.getInstance();
	
	RequestBuilder requestBuilder = new RequestBuilder();
	ReportLogService report = new ReportLogServiceImpl(Test_RegisterValidUser.class);
	
	OnDotUtilities utils= new OnDotUtilities();
	
	@BeforeTest
	public void dataSetup() throws Exception {
		TestDataProvider.folderPath =OnDotConstants.TESTDATA_FOLDER_GET_APP_INFO;
	}
	
	@Test(dataProvider = "DataProvider", dataProviderClass = TestDataProvider.class)
	public void test_get_app_info(File testDataFile) throws Exception {
		
		 getappinfodata= dataProvider.getJSONObject(testDataFile);
		requestBody = getappinfodata.getJSONObject("requestBody");
		expectedResponseBody = getappinfodata.getJSONObject("responseBody");
		
		
		report.info("Step #1: Do Post Call for GET_APP_INFO and Store Response");
		ResponseService responseService = requestBuilder.contentType("application/json").queryParam("apiVersion", "v2.0")
				.queryParam("opId", "GET_APP_INFO").body(requestBody.toString()).build()
				.post(CONFIG.getProperty("baseURI")+CONFIG.getProperty("registerEndPoint"));
		
		JSONObject actualResponseJSONObject = new JSONObject(responseService.getResponseBody());
		
		report.info("Step #2: Get Actual data => " + testDataFile.getName());
		
		String actualmessageCode=utils.getValueFromJsonData(actualResponseJSONObject, "responseStatus", "messageCode");
		String actualresponseCode=utils.getValueFromJsonData(actualResponseJSONObject, "responseStatus","responseCode"); 
		String actualOperationId=utils.getValueFromJsonData(actualResponseJSONObject, "opId");
		
		
		report.info("Step #3: Get expected data  => " + testDataFile.getName());
		String expectedmessageCode=utils.getValueFromJsonData(expectedResponseBody, "responseStatus", "messageCode");
		String expectedresponseCode=utils.getValueFromJsonData(expectedResponseBody, "responseStatus", "responseCode");
		String expectedoperationId=utils.getValueFromJsonData(expectedResponseBody, "opId");
		
		report.info("Step #4: compare the actual vs expected data");
		VerificationManager.verifyString(actualmessageCode, expectedmessageCode, "Verification of message code");
		VerificationManager.verifyString(actualOperationId, expectedoperationId, "verification of Operation Id");
		VerificationManager.verifyString(actualresponseCode, expectedresponseCode, "Verification of response code");
		
		
		
		
		
	}

}
