package testSuit.utils;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.datafaker.Faker;
import org.aeonbits.owner.ConfigFactory;
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

    /**
     * wiremock server
     */
    public static WireMockServer wireMockServer;

    /**
     * Faker instance
     */
    public static Faker faker = new Faker();

    //    code related to config reader
    public static ConfigUtil configUtil = ConfigFactory.create(ConfigUtil.class);
}
