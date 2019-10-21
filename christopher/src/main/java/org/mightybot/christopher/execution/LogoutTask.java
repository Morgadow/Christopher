/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.execution;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.mightybot.christopher.Christopher;
import org.mightybot.christopher.WebDriverSingleton;
import org.mightybot.christopher.utils.ChristopherUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Simon
 */
public class LogoutTask implements Runnable {

    private final WebDriver webDrv;

    public LogoutTask() {
        webDrv = WebDriverSingleton.getWebDriver();
    }

    @Override
    public void run() {
        
        // logge aus Session aus
        try {
            webDrv.findElement(By.cssSelector("a.footer-link:nth-child(7)")).click();
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ChristopherUtils.waitTime(500);

        // logge aus Startbildschirm aus
        try {
            webDrv.findElement(By.cssSelector(".small > a:nth-child(1)")).click();
            Logger.getLogger(Christopher.class.getName()).log(Level.INFO, "Aus Account komplett ausgeloggt");
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
