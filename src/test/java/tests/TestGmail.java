package tests;

import config.Config;
import org.apache.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import pages.gmail.InboxPage;
import pages.gmail.MyAccountPage;
import pages.gmail.SignInPage;

import java.util.ArrayList;
import java.util.Calendar;

public class TestGmail extends TestBase {

    private final static Logger LOG = Logger.getLogger(TestGmail.class);
    private Config config = new Config();


    @Test
    public void testSendingMail() {

        try {
            driver.get(SignInPage.URL_SIGNIN);
            SignInPage signInPage = new SignInPage(driver);
            WebDriverWait wait = new WebDriverWait(driver, 20);
            wait.until(ExpectedConditions.urlContains(SignInPage.URL_SIGNIN));
            signIn(signInPage);
            InboxPage inboxPage = getInboxPage(signInPage, wait);
            if (!inboxPage.checkUrl()) {
                switchTab();
                wait.until(ExpectedConditions.urlContains(InboxPage.URL_INBOX));
            }
            inboxPage.dealWithWelcomDialog();

            String subject = sendEmail(inboxPage);
            WebElement email = verifyEmailReceived(inboxPage, subject);
            removeEmail(inboxPage, subject, email);

        } catch (TimeoutException timeout) {
            log(timeout.getMessage());
        } catch (Exception exception) {
            log(exception.getMessage());
        }
    }

    public InboxPage getInboxPage(SignInPage signInPage, WebDriverWait wait) {
        MyAccountPage myAccountPage = signInPage.clickPasswordNext();
        wait.until(ExpectedConditions.urlContains(MyAccountPage.URL_MYACCOUNT));
        log("login successful");

        myAccountPage.clickGoogleAppsTab();
        return myAccountPage.clickGmailTab();
    }

    public void removeEmail(InboxPage inboxPage, String subject, WebElement email) {
        log("removing email");
        inboxPage.removeEmail(email);
        log("checking email doesn't exist anymore");
        email = inboxPage.findEmailBySubject(subject);
        Assert.assertNull(email);
        log("email not found");
    }

    public WebElement verifyEmailReceived(InboxPage inboxPage, String subject) {
        log("searching email by subject in unread inbox on page");
        WebElement email = inboxPage.findEmailBySubject(subject);
        Assert.assertNotNull(email);
        log("email found");
        return email;
    }

    public void signIn(SignInPage signInPage) {
        log("starting login");
        signInPage.setEmail(config.getGmailEmail());
        signInPage.clickEmailNext();
        signInPage.setPassword(config.getGmailPassword());
    }

    public String sendEmail(InboxPage inboxPage) {
        log("starting new email");
        inboxPage.clickComposeMessage();
        inboxPage.setRecipient(config.getGmailEmail());
        inboxPage.setMailText("Some message body");
        String subject = "test email, date: " + Calendar.getInstance().getTime();
        inboxPage.setSubject(subject);
        log("sending mail with subject:[" + subject + "]");
        inboxPage.clickSendButton();
        log("email sent");
        return subject;
    }

    public void switchTab() {
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        if (tabs.isEmpty()) return;
        tabs.forEach(tab -> LOG.debug(tabs.indexOf(tab) + " tab: " + tab));
        log("switching window/tab");
        driver.switchTo().window(tabs.get(1));

    }

    private void log(String message) {
        Reporter.log(message);
        LOG.info(message);
    }

}
