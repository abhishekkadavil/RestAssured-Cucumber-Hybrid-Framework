package testSuit.utils;

import org.aeonbits.owner.Config;

@Config.Sources("file:./src/test/resources/Config.properties")
public interface ConfigUtil extends Config {

    @Key("protocol")
    String getProtocol();

    @Key("host")
    String getHost();

    @Key("token")
    String getToken();

    @Key("wiremock.port")
    String getWiremockPort();

}
