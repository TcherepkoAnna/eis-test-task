package tests;

import config.Config;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
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
        driver.get(SignInPage.URL_SIGNIN);
        SignInPage signInPage = new SignInPage(driver);
        Assert.assertTrue(signInPage.checkUrl());
        signInPage.setEmail(config.getGmailEmail());
        signInPage.clickEmailNext();
        signInPage.setPassword(config.getGmailPassword());

        MyAccountPage myAccountPage = signInPage.clickPasswordNext();
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.urlContains(MyAccountPage.URL_MYACCOUNT));
        myAccountPage.clickGoogleAppsTab();

        InboxPage inboxPage = myAccountPage.clickGmailTab();
        inboxPage.checkUrl();
//        inboxPage.dealWithWelcomDialog();
//        inboxPage.clickComposeMessage();

        switchTab();

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        inboxPage.clickComposeMessage();
//        inboxPage.getOuterHtml();
        inboxPage.setRecipient(config.getGmailEmail());
        inboxPage.setMailText("Some message body");
        String subject = "test email, date: " + Calendar.getInstance().getTime();
        inboxPage.setSubject(subject);
        LOG.info("sending mail with subject:[" + subject +"]");
        inboxPage.clickSendButton();
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        WebElement email = inboxPage.findEmailBySubject(subject);
        Assert.assertNotNull(email);
        inboxPage.removeEmail(email);
        email = inboxPage.findEmailBySubject(subject);
        Assert.assertNull(email);







    }

    public void switchTab() {
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        if (tabs.isEmpty()) return;
        tabs.forEach(tab -> LOG.debug(tabs.indexOf(tab) + " tab: " + tab));
        LOG.debug("switching window/tab");
        driver.switchTo().window(tabs.get(1));
//        driver.close();
//        driver.switchTo().window(tabs.get(0));
    }


}
