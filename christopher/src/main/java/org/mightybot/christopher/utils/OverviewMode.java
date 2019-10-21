/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.utils;

import org.mightybot.christopher.WebDriverSingleton;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Simon
 */
public enum OverviewMode {
    COMBINED, PROD, TRADER, UNITS, COMMANDS, INCOMINGS, BUILDINGS, TECH, GROUPS, AM;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
    
    public static void goToOverviewMode(OverviewMode mode) {

        WebDriver webDrv = WebDriverSingleton.getWebDriver();

//        webDrv.findElement(By.id("overview_villages")).click();
//        webDrv.findElement(By.id("icon header overview")).click();
        webDrv.findElement(By.xpath("/html/body/table/tbody/tr[1]/td[2]/div/table/tbody/tr/td/table/tbody/tr/td[2]/a")).click();
//        webDrv.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[2]/table[3]/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[9]/a")).click();

        webDrv.findElement(By.id("overview_villages&mode=" + mode.toString())).click();
    }
}
