package testSuit.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Abhishek Kadavil
 */
public class RestAssuredLoggingFilter {

    public static List<Filter> getLoggingFilters() {
        List<Filter> restAssuredFilter = new ArrayList<>();
        restAssuredFilter.add(new RequestLoggingFilter());
        restAssuredFilter.add(new ResponseLoggingFilter());
        return restAssuredFilter;
    }
}
