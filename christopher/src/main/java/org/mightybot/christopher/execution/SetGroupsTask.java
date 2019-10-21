/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.execution;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.mightybot.christopher.GroupTroop;
import org.mightybot.christopher.WebDriverSingleton;
import org.mightybot.christopher.utils.ChristopherUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Simon
 */
public class SetGroupsTask implements Runnable {

    @Override
    public void run() {

        WebDriver webDrv = WebDriverSingleton.getWebDriver();

        webDrv.findElement(By.cssSelector("td.menu-item:nth-child(2) > a:nth-child(1)")).click();
        webDrv.findElement(By.cssSelector("td.selected:nth-child(9) > a:nth-child(1)")).click();
        webDrv.findElement(By.cssSelector("td.selected:nth-child(2) > a:nth-child(1)")).click();

        for (GroupTroop element : GroupTroop.values()) {
            try {
                if (webDrv.findElement(By.cssSelector("#add_new_group_name")).findElements(By.name(element.toString().toUpperCase())).isEmpty()) { // TODO abfrage ob gruppe vorhanden, geht noch nicht
                    webDrv.findElement(By.cssSelector("#add_new_group_name")).clear();
                    webDrv.findElement(By.cssSelector("#add_new_group_name")).sendKeys(element.toString().toUpperCase());
                    webDrv.findElement(By.cssSelector("#add_group_form > input:nth-child(3)")).click();
                    ChristopherUtils.randomWaitTime(250, 1000);
                }
            } catch (Exception ex) {
                Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "Fehler | Konnte Gruppe " + element.toString().toUpperCase() + " nicht hinzufügen!", ex);
            }
        }
    }
}
