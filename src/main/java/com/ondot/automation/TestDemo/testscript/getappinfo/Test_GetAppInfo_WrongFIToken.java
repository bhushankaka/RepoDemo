package com.ondot.automation.TestDemo.testscript.getappinfo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
import com.ondot.automation.TestDemo.testscript.createsession.Test_Create_Session;
import com.ondot.automation.TestDemo.testsuite.SampleTestSuiteBase;

public class Test_GetAppInfo_WrongFIToken extends SampleTestSuiteBase{
	JSONObject getappindodata;
	JSONObject requestBody;
	JSONObject expectedresponseBody;
	
	TestDataProvider dataprovider=TestDataProvider.getInstance();
	RequestBuilder requestBuilder = new RequestBuilder();
	ReportLogService report = new ReportLogServiceImpl(Test_Create_Session.class);
	
	Map<String, String> requestHeaders = new HashMap<String, String>();
	OnDotUtilities utils=new OnDotUtilities();
	
	@BeforeTest
	public void dataSetup() throws Exception {
		TestDataProvider.folderPath =OnDotConstants.TESTDATA_FOLDER_GET_APP_INFO;
	}
	
	@Test(dataProvider = "DataProvider", dataProviderClass = TestDataProvider.class)
	public void test_get_app_info_wrongfitoken(File testDataFile) throws Exception {
		
		getappindodata = dataprovider.getJSONObject(testDataFile);
		requestBody = getappindodata.getJSONObject("requestBody");
		expectedresponseBody = getappindodata.getJSONObject("responseBody");
		
		
		report.info("Step #1: Do Post Call for GET_APP_INFO and Store Response");
		ResponseService responseService = requestBuilder.contentType("application/json").queryParam("apiVersion", "v2.0")
				.queryParam("opId", "GET_APP_INFO").body(requestBody.toString()).build()
				.post(CONFIG.getProperty("baseURI")+CONFIG.getProperty("registerEndPoint"));
		
		JSONObject actualResponseJSONObject = new JSONObject(responseService.getResponseBody());
		
		report.info("Step #2: Get Actual data => " + testDataFile.getName());
		
		String actualmessageCode=utils.getValueFromJsonData(actualResponseJSONObject, "responseStatus", "messageCode");
		String actualresponseCode=utils.getValueFromJsonData(actualResponseJSONObject, "responseStatus","responseCode"); 
		String actualOperationId=utils.getValueFromJsonData(actualResponseJSONObject, "opId");
		
		report.info("Step #3: Get Expected data => " + testDataFile.getName());
		String expectedmessageCode=utils.getValueFromJsonData(expectedresponseBody, "responseStatus", "messageCode");
		String expectedresponseCode=utils.getValueFromJsonData(expectedresponseBody, "responseStatus","responseCode"); 
		String expectedOperationId=utils.getValueFromJsonData(expectedresponseBody, "opId");
		
		report.info("Step #4: compare Actual vs Expected => " + testDataFile.getName());
		VerificationManager.verifyString(actualOperationId, expectedOperationId, "verification of operationId");
		VerificationManager.verifyString(actualresponseCode, expectedresponseCode, "verification of responseCode");
		VerificationManager.verifyString(actualmessageCode, expectedmessageCode, "verification of message code");
	}
	

}
