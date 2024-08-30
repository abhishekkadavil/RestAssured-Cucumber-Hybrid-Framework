package testSuit.runners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.github.tomakehurst.wiremock.WireMockServer;
import testSuit.utils.ScenarioContext;
import testSuit.utils.TestContext;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Abhishek Kadavil
 */
public class RunnerHelper {

    public static ExtentSparkReporter spark;
    public static ExtentReports extent;

    public static void beforeTestSuit() throws IOException {
        //code related to report
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String reportFileName = "Test-Report-" + timeStamp + ".html";

        RunnerHelper.spark = new ExtentSparkReporter(System.getProperty("user.dir") + "/Report/" + reportFileName);
        RunnerHelper.extent = new ExtentReports();
        RunnerHelper.extent.attachReporter(RunnerHelper.spark);
        RunnerHelper.extent.setSystemInfo("OS", "Ubuntu");
        RunnerHelper.extent.setSystemInfo("Tester", "Abhishek Kadavil");

        /*
         * Wiremock server
         */
        ScenarioContext.wireMockServer = new WireMockServer(Integer.parseInt(ScenarioContext.configUtil.getWiremockPort()));
        ScenarioContext.wireMockServer.start();

        /*
         * Test context
         */
        TestContext.readTestContextJSON(System.getProperty("user.dir") + ScenarioContext.configUtil.getTestContextEnvPath());
    }

    public static void afterTestSuit() {

        ScenarioContext.wireMockServer.stop();
        RunnerHelper.extent.flush();
    }

}
