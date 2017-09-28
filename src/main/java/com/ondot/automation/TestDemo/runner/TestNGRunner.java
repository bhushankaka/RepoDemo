package com.ondot.automation.TestDemo.runner;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.testng.TestNG;
import org.testng.xml.XmlSuite;

import com.atmecs.falcon.automation.dataprovider.TestDataProvider;
import com.atmecs.falcon.automation.util.main.AbstractTestNGEngine;
import com.atmecs.falcon.automation.util.main.TestNGEngineFactory;
import com.atmecs.falcon.automation.util.main.TestNGEngineTemplateType;
import com.atmecs.falcon.automation.util.reporter.ReportLogService;
import com.atmecs.falcon.automation.util.reporter.ReportLogServiceImpl;
import com.atmecs.falcon.automation.util.reporter.TestReportUploadClient;
/**
 * TestNGRunner is the Main Class of MOJO type generates new Xml Suites on runtime for each client
 * for each child suite in the existing parent Xml Suite and executes
 * @author nv092106
 */
public class TestNGRunner {
    private static AbstractTestNGEngine testNGEngine = new TestNGEngineFactory()
            .getTestNGEngine(TestNGEngineTemplateType.DESIRED_SUITE_FOR_GIVEN_CLIENT_BASED_MODULES);
    private static String filename = System.getProperty("user.dir") + File.separator + "src"
            + File.separator + "main" + File.separator + "resources" + File.separator
            + "config.properties";
    private static ReportLogService report = new ReportLogServiceImpl(TestNGRunner.class);
    private static TestNG testng = new TestNG();
    private static List<XmlSuite> suitesToRun = null;
    private static Properties props = null;
    private static TestDataProvider dataProvider = TestDataProvider.getInstance();

    private static void initialize() throws Exception {
        // Loading properties file
        props = dataProvider.loadProperties(filename);
    }

    /**
     * Purpose: The main method invoked by the Maven plugin that uses the services of TestNGEngine
     * to create new Xml Suites on runtime and executes them
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        initialize();
        suitesToRun =
                testNGEngine.getSuitesToRunForWebServices(readEnvOrConfigProperty("SuiteFileName"),
                        readEnvOrConfigProperty("ClientName"),
                        readEnvOrConfigProperty("ModuleName"));
        testng.setXmlSuites(suitesToRun);
        testng.run();
        uploadTestNGResultsXml(); // upload the results to the reporting server
    }
    
    public static String readEnvOrConfigProperty(String key) {
        // first pref for env, next for config file
        String value = System.getProperty(key);
        if (value == null || value.trim().length() == 0) {
            value = props.getProperty(key);
        }
        return value;

    }
    
    public static void uploadTestNGResultsXml() {
        try {
            String uploadUrl = props.getProperty("testreport.uploadurl");
            String testNGResultsXmlFilePath =
                    System.getProperty("user.dir") + File.separator + "test-output"
                            + File.separator + "testng-results.xml";
            TestReportUploadClient testReportUploadClient = new TestReportUploadClient(uploadUrl);
            report.info("Started Uploading Results to Report Server...");
            
            String response =
                    testReportUploadClient.upload(dataProvider.getSessionId(), "FALCON", "Web Services", "QA", "Regression",
                            "", "", "", "", testNGResultsXmlFilePath);
            report.info("Response : " + response);

        } catch (Exception e) {
            report.error("Unknown error : Cannot Upload the testng-results.xml " + e.getMessage());
        }
    }
}
