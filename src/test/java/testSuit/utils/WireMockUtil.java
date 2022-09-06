package testSuit.utils;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@ScenarioScoped
public class WireMockUtil {

    @Inject
    TestContext testContext;

    /**
     * Wiremock server
     */
    public WireMockServer wireMockServer;

    /**
     * Map of wiremock wireMockBuilderMap.
     */
    public Map<String, MappingBuilder> wireMockBuilderMap = new HashMap<>();

    /**
     * Wiremock port
     */
    public String wiremockPort = testContext.getConfigUtil().getWiremockPort();

    @Before
    public void startWireMock() {
        wireMockServer = new WireMockServer(options().port(Integer.valueOf(wiremockPort)));
        wireMockServer.start();
    }

    @After
    public void stopWireMock() {
        wireMockServer.stop();
    }

}
