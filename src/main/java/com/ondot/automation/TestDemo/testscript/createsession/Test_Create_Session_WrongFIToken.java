package com.ondot.automation.TestDemo.testscript.createsession;

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
import com.ondot.automation.TestDemo.testsuite.SampleTestSuiteBase;

/*
 * This class verifies that after putting wrong FIToken, the results are getting correct or not
 */

public class Test_Create_Session_WrongFIToken extends SampleTestSuiteBase{
	
	JSONObject createSessionTestData;
	JSONObject requestBody;
	JSONObject expectedresponseBody;
	
	TestDataProvider dataprovider=TestDataProvider.getInstance();
	RequestBuilder requestBuilder = new RequestBuilder();
	ReportLogService report = new ReportLogServiceImpl(Test_Create_Session.class);
	
	Map<String, String> requestHeaders = new HashMap<String, String>();
	OnDotUtilities utils=new OnDotUtilities();
	
	@BeforeTest
	public void dataSetup() throws Exception {
		TestDataProvider.folderPath =OnDotConstants.TESTDATA_FOLDER_CRAEATE_SESSION;
	}
	
	@Test(dataProvider = "DataProvider", dataProviderClass = TestDataProvider.class)
	public void loginTestCall(File testDataFile) throws Exception {
		createSessionTestData=dataprovider.getJSONObject(testDataFile);
		requestBody=createSessionTestData.getJSONObject("requestBody");
		expectedresponseBody=createSessionTestData.getJSONObject("responseBody");
		
		report.info("Step #1: Do Post Call for Create_Session and Store Response");
		ResponseService responseService = requestBuilder.contentType("application/json").queryParam("apiVersion", "v2.0")
				.queryParam("opId", "CREATE_SESSION").body(requestBody.toString()).build()
				.post(CONFIG.getProperty("baseURI")+CONFIG.getProperty("registerEndPoint"));
		
		//report.info("Step #2: Prepare Request Body for create_session API");
		/*requestBuilder.headers(requestHeaders).queryParam("apiVersion", "v2.0").queryParam("opId", "CREATE_SESSION")
				.body(expectedresponseBody.toString()).build();*/
		
		
		
		JSONObject actualResponseJSONObject = new JSONObject(responseService.getResponseBody());
		
		report.info("Step #2: Get Actual Data  " );
		String actualresponseCode=utils.getValueFromJsonData(actualResponseJSONObject, "responseStatus", "responseCode");
		String actualmessageCode=utils.getValueFromJsonData(actualResponseJSONObject, "responseStatus", "messageCode");
		
		report.info("Step #3:Get expected data");
		String expectedResposeCode=utils.getValueFromJsonData(expectedresponseBody, "responseStatus", "responseCode");
		String expectedmessageCode=utils.getValueFromJsonData(expectedresponseBody, "responseStatus", "messageCode");
		
		report.info("Step #4: Verify Actual vs Expected");
		VerificationManager.verifyString(actualresponseCode, expectedResposeCode, "Verifing the response code");
		VerificationManager.verifyString(actualmessageCode, expectedmessageCode, "verifying messagecode");
}
}