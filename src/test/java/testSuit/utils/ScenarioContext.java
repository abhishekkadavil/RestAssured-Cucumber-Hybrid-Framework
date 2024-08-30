package testSuit.utils;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import io.cucumber.guice.ScenarioScoped;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Abhishek Kadavil
 */
@Getter
@ScenarioScoped
public class ScenarioContext {

    HashMap<String, Response> responseContext = new HashMap<>();

    HashMap<String, String> reqBodyContext = new HashMap<>();

    HashMap<String, String> contextValues = new HashMap<>();

    HashMap<String, RequestSpecification> requestBuilder = new HashMap<>();

    /**
     * Map of wiremock wireMockBuilderMap.
     */
    public Map<String, MappingBuilder> wireMockBuilderMap = new HashMap<>();

    /**
     * auto generate id for creating request specification
     */
    @Setter
    String reqId;

    public String generateReqId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Token management
     */
    public String getCookieToken() {
        return (System.getProperty("cookieToken") == null) ? TestContext.configUtil.getCookieToken() :
                System.getProperty("cookieToken");
    }

    public String getToken() {
        return (System.getProperty("token") == null) ? TestContext.configUtil.getToken() : System.getProperty("token");
    }

}
