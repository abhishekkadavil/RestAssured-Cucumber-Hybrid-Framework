package testSuit.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import testSuit.pojos.User;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Abhishek Kadavil
 */
public class TestContext {

    private static Map<String, User> users;

    public static void readTestContextJSON(String filePath) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(filePath);
        // Deserialize JSON from file into Map<String, User>
        Type userType = new TypeToken<Map<String, User>>() {
        }.getType();
        users = gson.fromJson(reader, userType);
    }

    public static User getTestContext(String userIdToFind)
    {
        return users.get(userIdToFind);
    }
}
