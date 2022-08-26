package testSuit.utils;

import io.cucumber.guice.ScenarioScoped;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import org.aeonbits.owner.ConfigFactory;

import java.util.HashMap;

@Getter
@ScenarioScoped
public class TestContext {

    HashMap<String, Response> responseContext  = new HashMap<>();

    HashMap<String, String> reqBodyContext  = new HashMap<>();

    HashMap<String, String> contextValues  = new HashMap<>();

    HashMap<String, RequestSpecification> requestBuilder = new HashMap<>();

//    code related to config reader
    ConfigUtil configUtil = ConfigFactory.create(ConfigUtil.class);

}
