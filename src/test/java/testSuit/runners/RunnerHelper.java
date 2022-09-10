package testSuit.runners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.aeonbits.owner.ConfigFactory;
import testSuit.utils.ConfigUtil;
import testSuit.utils.TestContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RunnerHelper {

    public static ExtentSparkReporter spark;
    public static ExtentReports extent;

    public static void beforeTestSuit()
    {
        //code related to report
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String reportFileName="Test-Report-"+timeStamp+".html";

        RunnerHelper.spark = new ExtentSparkReporter(System.getProperty("user.dir")+"/Report/"+reportFileName);
        RunnerHelper.extent = new ExtentReports();
        RunnerHelper.extent.attachReporter(RunnerHelper.spark);
        RunnerHelper.extent.setSystemInfo("os", "Ubuntu");

        /**
         * Wiremock server
         */
        TestContext.wireMockServer = new WireMockServer(Integer.parseInt(TestContext.configUtil.getWiremockPort()));
        TestContext.wireMockServer.start();
    }

    public static void afterTestSuit()
    {

        TestContext.wireMockServer.stop();
        RunnerHelper.extent.flush();
    }

}
