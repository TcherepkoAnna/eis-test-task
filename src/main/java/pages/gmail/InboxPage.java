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
    public static final String URL_INBOX = "https://mail.google.com/mail/u/0/#inbox";
    private static final Logger LOG = Logger.getLogger(InboxPage.class);

    private static final By dialogNextButton = By.xpath("//button[@name='welcome_dialog_next']");
    private static final By dialogOkButton = By.xpath("//button[@name='ok']");
    private static final By composeButton = By.xpath("//div[contains(@class, 'T-I J-J5-Ji T-I-KE L3')]");

    private static final By mailBody = By.xpath("//div[contains(@aria-label, 'Message Body')]");
    private static final By mailRecipient = By.xpath("//textarea[contains(@name, 'to')]");
    private static final By mailSubject = By.xpath("//input[contains(@name, 'subjectbox')]");
    private static final By sendButtonLocator = By.xpath("//div[contains(@class, 'T-I J-J5-Ji aoO v7 T-I-atl L3')]");
    private static final By incommingMessageUnreadContainer = By.xpath("//tr[contains(@class, 'zA zE')]");
    private static final By incommingMessageReadContainer = By.xpath("//tr[contains(@class, 'zA yO')]");
    private static final By incommingMessageSubject = By.xpath(".//span[contains(@class, 'bqe')]");
    private static final By incommingMessageDeleteButton = By.xpath(".//li[contains(@class, 'bqX bru')]");
    private static final By incommingMessageCheckbox = By.xpath(".//div[contains(@class, 'oZ-jc T-Jo J-J5-Ji')]");
    private static final By inboxDeleteButton = By.xpath("//div[contains(@class, 'T-I J-J5-Ji nX T-I-ax7 T-I-Js-Gs mA')]");

//    private static final By searchMailField = By.xpath("//input[contains(@class, 'gb_Oe')]");
//    private static final By searchMailButton = By.xpath("//button[contains(@class, 'gb_Xe gb_Ze')]");


    public InboxPage(WebDriver driver) {
        LOG.debug("creating InboxPage obj");
        this.driver = driver;
        jsExecutor = (JavascriptExecutor) driver;
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
        WebElement compose = driver.findElement(composeButton);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!compose.isDisplayed()) LOG.debug("mainmenu is NOT displayed");
        if (!compose.isEnabled()) LOG.debug("mainmenu is NOT enabled");
        WebDriverWait wait = new WebDriverWait(driver, 40);
        wait.until(ExpectedConditions.visibilityOfElementLocated(composeButton));
        compose.click();
    }


    public void getOuterHtml() {
        LOG.debug("finding some element");
        List<WebElement> list = driver.findElements(mailRecipient);
        if (list.isEmpty()) {
            LOG.debug("no elements found");
            return;
        }
        for (WebElement element : list) {
            System.out.println(element.getAttribute("outerHTML"));
            if (element == null) LOG.debug("element is null");
            if (!element.isDisplayed()) LOG.debug("element is NOT displayed");
            if (element.isEnabled()) LOG.debug("element IS enabled");

        }
    }

    public void setMailText(String msgBody) {
        LOG.debug("writing email body");
        driver.findElement(mailBody).sendKeys(msgBody);
    }

    public void setRecipient(String gmailEmail) {
        LOG.debug("setting email recipient");
        WebElement mailRecipientField = driver.findElement(mailRecipient);
//        Util.scrollIntoView(jsExecutor, mailRecipientField);
        if (!mailRecipientField.isDisplayed()) LOG.debug("element is NOT displayed");
        mailRecipientField.sendKeys(gmailEmail);
    }

    public void setSubject(String s) {
        LOG.debug("setting email subject");
        driver.findElement(mailSubject).sendKeys(s);
    }

    public void clickSendButton() {
        LOG.debug("clicking Send button");
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(sendButtonLocator));
        button.click();
    }

    public WebElement findEmailBySubject(String subject) {
//        LOG.debug("inputting search key");
//        driver.findElement(searchMailField).sendKeys(subject);
//        driver.findElement(searchMailButton).click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<WebElement> mails = getAllMails();
        LOG.debug("found emails: " + mails.size());

        LOG.debug("searching emails by subject");
        for (WebElement email : mails) {
//            email.findElement(incommingMessageCheckbox);
            Util.scrollIntoView(jsExecutor, email);
            Actions act = new Actions(driver);
            act.moveToElement(email).build().perform();
            WebElement subjectEl = email.findElement(incommingMessageSubject);
//            if (!subjectEl.isDisplayed())LOG.debug("subject not displayed");
            String foundSubject = subjectEl.getAttribute("innerHTML");
            LOG.debug("found subject: " + foundSubject);
//            System.out.println(email.getAttribute("outerHTML"));
            if (foundSubject.equals(subject)) {
                LOG.info("email found in inbox. subject:[" + foundSubject + "]");
                return email;
            }
        }
        LOG.debug("email not found");
        return null;
    }

    public List<WebElement> getAllMails() {
        LOG.debug("getting all emails on page");
        new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(incommingMessageUnreadContainer));
        return driver.findElements(incommingMessageUnreadContainer);
    }

    public void removeEmail(WebElement email) {
        LOG.debug("removing an email");

        WebElement checkbox = email.findElement(incommingMessageCheckbox);
        Actions act = new Actions(driver);
        act.moveToElement(checkbox).click().build().perform();
//        checkbox.click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(inboxDeleteButton));
        driver.findElement(inboxDeleteButton).click();
    }
}
