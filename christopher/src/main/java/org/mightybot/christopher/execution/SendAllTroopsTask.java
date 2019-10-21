/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.execution;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.mightybot.christopher.Village;
import org.mightybot.christopher.WebDriverSingleton;
import org.mightybot.christopher.utils.ChristopherUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Simon
 */
/**
 * Soll als task einfach bei aufruf alle truppen von einem dorf zu einem anderen
 * schicken, keine timings, keine eingestellten truppen
 *
 */
public class SendAllTroopsTask implements Runnable {

    private static Village sourceVill;
    private static Village destVill;
    private static String sendMode; // "attack" oder "support"

    public SendAllTroopsTask(Village source, Village destination, String mode) {

        sourceVill = source;
        destVill = destination;
        sendMode = mode;
        if (sendMode != "attack" & sendMode != "support") {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, "FEHLER | sendMode kann nur entweder 'attack' oder 'support' sein!");
            return;
        }
    }

    @Override
    public void run() {
        WebDriver webDrv = WebDriverSingleton.getWebDriver();

        if (ChristopherUtils.getCurrentVillage().coords != sourceVill.coords) {
            ChristopherUtils.goToVillage(null, sourceVill.coords);
        }

//        webDrv.findElement(By.cssSelector("area.tooltip-delayed:nth-child(2)")).click(); // versammlungsplatz TODO, nicht über schnellleiste
        try {
            webDrv.findElement(By.cssSelector("li.quickbar_item:nth-child(5) > span:nth-child(1) > a:nth-child(1)")).click();
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, "FEHLER | Schnellleistensymbol für Versammlungsplatz wahrscheinlich nicht mehr vorhanden!", ex);
        }

        webDrv.findElement(By.cssSelector(".target-input-field")).sendKeys(destVill.coords.getXcoord() + "|" + destVill.coords.getYcoord());
        webDrv.findElement(By.cssSelector("#selectAllUnits")).click();
        webDrv.findElement(By.cssSelector("#target_" + sendMode)).click();
        webDrv.findElement(By.cssSelector("#troop_confirm_go")).click();

        if ("attack" == sendMode) {
            System.out.println("BEFEHL | Angriff mit allen Truppen auf " + destVill.getName() + " " + destVill.coords.toString() + " abgeschickt.");
//                        Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, "FEHLER | sendMode kann nur entweder 'attack' oder 'support' sein!");
        } else if ("support" == sendMode) {
            System.out.println("BEFEHL | Unterstützung mit allen Truppen auf " + destVill.getName() + " " + destVill.coords.toString() + " abgeschickt.");
//                        Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, "FEHLER | sendMode kann nur entweder 'attack' oder 'support' sein!");
        }

    }
}
