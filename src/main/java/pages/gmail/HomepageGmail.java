package pages.gmail;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomepageGmail {

    private static final Logger LOG = Logger.getLogger(HomepageGmail.class);
    private WebDriver driver;
    public static final String URL_GMAIL_HOMEPAGE = "https://www.google.com/gmail/";

    @FindBy(xpath = "//a[contains(text(), 'Войти')]")
    private WebElement signInButton;

    public HomepageGmail(WebDriver driver) {
        LOG.debug("creating homepage");
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public SignInPage chooseSigningIn(){
        LOG.debug("going to accounts signin page at: " + signInButton.getAttribute("href"));
        signInButton.click();
        return new SignInPage(driver);
    }

    public boolean checkUrl() {
        LOG.debug("verifying url. current url: " + driver.getCurrentUrl()+" should contain: " + URL_GMAIL_HOMEPAGE);
        return driver.getCurrentUrl().contains(URL_GMAIL_HOMEPAGE.substring('/'));
    }
}
