/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher;

import org.mightybot.christopher.execution.HandleBotProtection;
import org.mightybot.christopher.execution.LoginTask;
import org.mightybot.christopher.utils.ChristopherUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 *
 * @author wiesbob
 */
public class Trying {

    public static void main(String[] args) throws Exception {

        
        new HandleBotProtection().call();
//        FirefoxOptions options = new FirefoxOptions();
//        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36 OPR/60.0.3255.170";
//        options.addPreference("general.useragent.override", userAgent);
//
//        WebDriver webDriver = new FirefoxDriver(options);
//        //ChristopherUtils.initialize();
//        new LoginTask("Christopher", "christopher1337", 166, webDriver).call();
//        ChristopherUtils.retryingFindClick(webDriver.findElement(By.id("manager_icon_farm")));
//
//        try {
//            webDriver.findElement(By.cssSelector("#recaptcha-anchor-label"));
//        } catch (NoSuchElementException ex) {
//        }
        Thread.sleep(122222222);
    }
}
