package config;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;

public class Config {

    private final static Logger LOG = Logger.getLogger(Config.class);
    private Properties properties = new Properties();
    private static final String propertiePath = "src\\main\\resources\\config.properties";

    private static final String PREFERRED_BROWSER = "preferred_browser";
    private static final String WAIT_TIME = "wait";

    private static final String GMAIL_EMAIL = "gmail_email";
    private static final String GMAIL_PASSWORD = "gmail_password";

    public Config() {

        try (FileInputStream input = new FileInputStream(new File(propertiePath));
             InputStreamReader reader = new InputStreamReader(input, Charset.forName("UTF-8"));) {
            // load a properties file
            properties.load(reader);

        } catch (IOException ex) {
            LOG.error(ex.getMessage());
        }

    }


    public String getGmailEmail() {
        return properties.getProperty(GMAIL_EMAIL);
    }

    public String getGmailPassword() {
        return properties.getProperty(GMAIL_PASSWORD);
    }

    public String getPreferredBrowser() {
        return properties.getProperty(PREFERRED_BROWSER);
    }

    public long getWaitTime() {
        return Long.valueOf(properties.getProperty(WAIT_TIME));
    }


}
