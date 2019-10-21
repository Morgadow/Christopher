/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.execution;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mightybot.christopher.WebDriverSingleton;
import org.mightybot.christopher.mail.Mailer;
import org.mightybot.christopher.utils.ChristopherUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 *
 * @author wiesbob
 */
public class HandleBotProtection implements Callable<Boolean> {

    @Override
    public Boolean call() throws Exception {
        FirefoxOptions options = new FirefoxOptions();
        String userAgent = "Mozilla/5.0 (Linux; Android 6.0; HTC One M9 Build/MRA58K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.36";
        options.addPreference("general.useragent.override", userAgent);

        WebDriver webDriver = new FirefoxDriver(options);
        new LoginTask("Christopher", "christopher1337", 166, webDriver).call();
        ChristopherUtils.retryingFindClick(webDriver.findElement(By.cssSelector("#mobileMenu > a:nth-child(11)")));

        try {
            WebElement checkBox = webDriver.findElement(By.xpath("//*[@role='presentation']"));
            checkBox.click();
        } catch (NoSuchElementException ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.INFO, "PROTECTION | Not able to solve bot protection, now sending notification email.", ex);
            sendNotification();
            return false;
        }

        try {
            WebDriverSingleton.getWebDriver().findElement(By.xpath("//*[@role='presentation']"));

        } catch (NoSuchElementException ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.INFO, "PROTECTION | Solved bot protection, now returning...");
            return true;
        }

        Logger.getLogger(ChristopherUtils.class.getName()).log(Level.INFO, "PROTECTION | Not able to solve bot protection, now sending notification email.");
        sendNotification();
        return false;
    }

    private void sendNotification() {
        new Mailer().writeEmail("Help me, i got caught!", "Please, allmighty creator of humble little christopher, free him of his chains, so he can go after his business...");
    }

}
