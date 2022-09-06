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

    @Key("POSTGRES_DB_DRIVER_CLASS")
    String getPostgresDriver();

    @Key("POSTGRES_DB_URL")
    String getPostgresDbURL();

    @Key("POSTGRES_DB_USERNAME")
    String getPostgresUsername();

    @Key("POSTGRES_DB_PASSWORD")
    String getPostgresPassword();

}
