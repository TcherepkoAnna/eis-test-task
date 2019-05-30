package drivers;

import config.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class FirefoxCreator implements DriverCreator{
    private Config config = new Config();

    @Override
    public WebDriver createDriver() {
        FirefoxOptions options = new FirefoxOptions();
        FirefoxProfile profile = new FirefoxProfile();
        options.setProfile(profile);
        WebDriver driver = new FirefoxDriver(options);
        return driver;
    }
}
