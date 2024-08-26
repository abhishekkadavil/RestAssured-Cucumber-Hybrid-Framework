package testSuit.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Abhishek Kadavil
 */

public class TestContext {
    private Map<String, Object> contextMap = new HashMap<>();

    public void setContext(String key, Object value) {
        contextMap.put(key, value);
    }

    public Object getContext(String key) {
        return contextMap.get(key);
    }

    public void clear() {
        contextMap.clear();
    }
}
