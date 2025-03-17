package testSuit.stepDef.frameworkUTs;

import com.aventstack.extentreports.Status;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import testSuit.factories.ReporterFactory;
import testSuit.contexts.TestContext;

/**
 * @author Abhishek Kadavil
 */
@Slf4j
public class Other {

    @Given("test sbid in the test context should be {string} for user {string}")
    public void test_userId_in_the_test_context_should_be_string_for_user_string (String sbid, String userId) {

        Assert.assertEquals(TestContext.getTestContext(userId).getSbid(),sbid);
        ReporterFactory.getInstance().getExtentTest().log(Status.PASS, "sbid: " + TestContext.getTestContext(userId).getSbid());
    }
}
