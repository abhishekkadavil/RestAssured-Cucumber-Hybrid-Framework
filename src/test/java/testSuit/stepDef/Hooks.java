package testSuit.stepDef;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import testSuit.factories.ReporterFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Abhishek Kadavil
 */
@Slf4j
public class Hooks {

    @Before
    public void beforeScenario(Scenario scenario) {
        log.info("beforeScenario {}", scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        log.info("afterScenario {}", scenario.getName());

        /* Code to manage @Author and @Category tags */
        Collection<String> tagsCollection = scenario.getSourceTagNames();
        Set<String> tags = new HashSet<>(tagsCollection);

        tags.forEach(tag -> {
            if (tag.startsWith("@Author")) {
                ReporterFactory.getInstance().getExtentTest().assignAuthor(extractedTagValue(tag));
            } else if (tag.startsWith("@Category")) {
                ReporterFactory.getInstance().getExtentTest().assignCategory(extractedTagValue(tag));
            }
        });
    }

    private String extractedTagValue(String tag) {

        Pattern pattern;
        Matcher matcher = null;
        if (tag.startsWith("@Author")) {
            pattern = Pattern.compile("@Author\\(\"([^\"]+)\"\\)");
            matcher = pattern.matcher(tag);
        }
        if (tag.startsWith("@Category")) {
            pattern = Pattern.compile("@Category\\(\"([^\"]+)\"\\)");
            matcher = pattern.matcher(tag);
        }

        if(matcher != null)
        {
            if (matcher.find()) {
                return matcher.group(1);  // Return the author's name
            }
        }
        return "Unknown";
    }
}
