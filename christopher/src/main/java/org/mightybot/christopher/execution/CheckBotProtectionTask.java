/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.execution;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mightybot.christopher.WebDriverSingleton;
import org.mightybot.christopher.utils.ChristopherUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

/**
 *
 * @author wiesbob
 */
public class CheckBotProtectionTask implements Callable<Boolean> {

    @Override
    public Boolean call() throws Exception {
        try {
            if (WebDriverSingleton.getWebDriver().findElements(By.cssSelector("#recaptcha-anchor-label")).size() > 0) {
                WebDriverSingleton.getWebDriver().findElement(By.cssSelector("#recaptcha-anchor-label"));
            } else {
                return false;
            }
        } catch (NoSuchElementException ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.INFO, "PROTECTION | bot-protection has not been recognized", ex);
            return false;
        }
        Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "PROTECTION | bot-protection has been recognized, starting handle task now...");
        Callable handler = new HandleBotProtection();
        Future<Boolean> result = Executors.newSingleThreadExecutor().submit(handler);
        if (result.get()) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "PROTECTION | bot-protection has been successfully solved.");
        } else {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "PROTECTION | bot-protection could not be solved, e-mail notification has been sent");
        }
        return true;

    }

}
