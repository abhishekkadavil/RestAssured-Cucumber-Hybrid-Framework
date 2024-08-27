package testSuit.utils;

import com.google.inject.Singleton;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Abhishek Kadavil
 */
@Getter
@Singleton
public class TestContext {

    Map<String, Object> testContextMap = new HashMap<>();

    public void clear() {
        testContextMap.clear();
    }
}
