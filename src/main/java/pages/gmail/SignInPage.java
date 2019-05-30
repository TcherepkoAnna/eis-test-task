package pages.gmail;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.Util;

public class SignInPage {

    private static final Logger LOG = Logger.getLogger(SignInPage.class);
    private WebDriver driver;
    public static final String URL_SIGNIN = "https://accounts.google.com";

    @FindBy(id = "identifierId")
    private WebElement emailInput;

    @FindBy(id = "identifierNext")
    private WebElement emailNextButton;

    @FindBy(xpath = "//input[@name='password']")
    private WebElement passwordInput;

    @FindBy(id = "passwordNext")
    private WebElement passwordNextButton;

    public SignInPage(WebDriver driver) {
        LOG.debug("creating signin page");
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void setEmail(String email) {
        LOG.debug("setting email to: " + email);
        emailInput.sendKeys(email);
    }
    public void setPassword(String password) {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.elementToBeClickable(passwordInput));
        LOG.debug("setting password to: " + password);
        passwordInput.sendKeys(password);
    }

    public boolean checkUrl() {
        LOG.debug("verifying url. current url: " + driver.getCurrentUrl()+" should contain: " + URL_SIGNIN);
        return driver.getCurrentUrl().contains(URL_SIGNIN.substring(URL_SIGNIN.indexOf('/')));
    }


    public void clickEmailNext() {
        LOG.debug("clicking identifier next button ");
        emailNextButton.click();
    }

    public MyAccountPage clickPasswordNext() {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.elementToBeClickable(passwordNextButton));
        Util.scrollIntoView((JavascriptExecutor)driver, passwordNextButton);
        LOG.debug("clicking password next button");
        passwordNextButton.click();
        LOG.debug("returning MyAccountPage obj");
        return new MyAccountPage(driver);
    }
}
