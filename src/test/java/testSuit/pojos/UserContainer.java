package testSuit.pojos;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author Abhishek Kadavil
 */

@Setter
@Getter
public class UserContainer {
    // Getter and Setter
    private Map<String, User> users;

}
