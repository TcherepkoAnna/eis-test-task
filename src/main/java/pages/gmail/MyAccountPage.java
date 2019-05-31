package pages.gmail;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MyAccountPage {
    private WebDriver driver;
    public static final String URL_MYACCOUNT = "https://myaccount.google.com";
    private static final Logger LOG = Logger.getLogger(MyAccountPage.class);

    @FindBy(xpath = "//a[contains(@href, 'products') and contains(@class, 'gb_x')]")
    private WebElement googleAppsButton;
    @FindBy(xpath = "//span[contains(text(), 'Gmail')]")
    private WebElement gmailButton;

    public MyAccountPage(WebDriver driver) {
        LOG.debug("creating MyAccountPage obj");
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean checkUrl() {
        LOG.debug("verifying url. current url: " + driver.getCurrentUrl() + " should contain: " + URL_MYACCOUNT);
        return driver.getCurrentUrl().contains(URL_MYACCOUNT.substring(URL_MYACCOUNT.indexOf('/')));
    }

    public void clickGoogleAppsTab() {
        LOG.debug("clicking Google Apps tab");
        googleAppsButton.click();
    }

    public InboxPage clickGmailTab() {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.elementToBeClickable(gmailButton));
        LOG.debug("clicking Gmail tab");
        gmailButton.click();
        return new InboxPage(driver);
    }
}
