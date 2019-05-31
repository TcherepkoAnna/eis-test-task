package pages.gmail;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.Util;

import java.util.List;

public class InboxPage {
    private WebDriver driver;
    private JavascriptExecutor jsExecutor;
    WebDriverWait wait;
    public static final String URL_INBOX = "https://mail.google.com/mail/u/0/#inbox";
    private static final Logger LOG = Logger.getLogger(InboxPage.class);

    private static final By dialogNextButton = By.xpath("//button[@name='welcome_dialog_next']");
    private static final By dialogOkButton = By.xpath("//button[@name='ok']");
    private static final By composeButton = By.xpath("//div[contains(@class, 'T-I J-J5-Ji T-I-KE L3')]");

    private static final By mailBody = By.xpath("//div[contains(@class, 'Am Al editable LW-avf')]");
    private static final By mailRecipient = By.xpath("//textarea[contains(@name, 'to')]");
    private static final By mailSubject = By.xpath("//input[contains(@name, 'subjectbox')]");
    private static final By sendButtonLocator = By.xpath("//div[contains(@class, 'T-I J-J5-Ji aoO v7 T-I-atl L3')]");

    private static final By incommingMessageUnreadContainer = By.xpath("//tr[contains(@class, 'zA zE')]");
    private static final By incommingMessageSubject = By.xpath(".//span[contains(@class, 'bqe')]");
    private static final By incommingMessageCheckbox = By.xpath(".//div[contains(@class, 'oZ-jc T-Jo J-J5-Ji')]");
    private static final By inboxDeleteButton = By.xpath("//div[contains(@class, 'T-I J-J5-Ji nX T-I-ax7 T-I-Js-Gs mA')]");



    public InboxPage(WebDriver driver) {
        LOG.debug("creating InboxPage obj");
        this.driver = driver;
        jsExecutor = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, 20);
        PageFactory.initElements(driver, this);
    }

    public boolean checkUrl() {
        LOG.debug("verifying url. current url: " + driver.getCurrentUrl() + " should contain: " + URL_INBOX);
        return driver.getCurrentUrl().contains(URL_INBOX.substring(URL_INBOX.indexOf('/')));
    }

    public void dealWithWelcomDialog() {
        if (!driver.findElements(dialogNextButton).isEmpty()) {
            LOG.debug("dealing with welcome dialog");
            driver.findElement(dialogNextButton).click();
            driver.findElement(dialogOkButton).click();
        }
    }

    public void clickComposeMessage() {
        LOG.debug("starting new message");
        WebElement compose = wait.until(ExpectedConditions.visibilityOfElementLocated(composeButton));
        compose.click();
    }


    public void setMailText(String msgBody) {
        LOG.debug("writing email body");
        driver.findElement(mailBody).sendKeys(msgBody);
    }

    public void setRecipient(String gmailEmail) {
        LOG.debug("setting email recipient");
        WebElement mailRecipientField = driver.findElement(mailRecipient);
        if (!mailRecipientField.isDisplayed()) LOG.debug("element is NOT displayed");
        mailRecipientField.sendKeys(gmailEmail);
    }

    public void setSubject(String s) {
        LOG.debug("setting email subject");
        driver.findElement(mailSubject).sendKeys(s);
    }

    public void clickSendButton() {
        LOG.debug("clicking Send button");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(sendButtonLocator));
        button.click();
    }

    public WebElement findEmailBySubject(String subject) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> mails = getAllMails();
        LOG.debug("found unread emails on page: " + mails.size());

        LOG.debug("searching emails by subject");
        for (WebElement email : mails) {
            Util.scrollIntoView(jsExecutor, email);
            Actions act = new Actions(driver);
            act.moveToElement(email).build().perform();
            WebElement subjectEl = email.findElement(incommingMessageSubject);
            String foundSubject = subjectEl.getAttribute("innerHTML");
            LOG.debug("found subject: " + foundSubject);
            if (foundSubject.equals(subject)) {
                LOG.debug("email found in inbox. subject:[" + foundSubject + "]");
                return email;
            }
        }
        LOG.debug("email not found");
        return null;
    }

    public List<WebElement> getAllMails() {
        LOG.debug("getting all emails on page");
        wait.until(ExpectedConditions.visibilityOfElementLocated(incommingMessageUnreadContainer));
        return driver.findElements(incommingMessageUnreadContainer);
    }

    public void removeEmail(WebElement email) {
        LOG.debug("removing an email: [" + email.getText() + "]");
        WebElement checkbox = email.findElement(incommingMessageCheckbox);
        LOG.debug("clicking checkbox");
        Actions act = new Actions(driver);
        act.moveToElement(checkbox).click().build().perform();
        WebElement delete = wait.until(ExpectedConditions.visibilityOfElementLocated(inboxDeleteButton));
        LOG.debug("clicking delete button");
        delete.click();
    }
}
