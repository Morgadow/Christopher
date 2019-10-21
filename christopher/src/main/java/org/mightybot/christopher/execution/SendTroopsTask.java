/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.execution;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mightybot.christopher.UnitType;
import org.mightybot.christopher.Date;
import org.mightybot.christopher.Time;
import org.mightybot.christopher.Village;
import org.mightybot.christopher.WebDriverSingleton;
import org.mightybot.christopher.utils.ChristopherUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Simon
 */
public class SendTroopsTask implements Runnable {

    private static Village sourceVill;
    private static Village destVill;
    private static String sendMode; // "attack" oder "support"
    private static Map<UnitType, Integer> unitsToSend;

    public SendTroopsTask(Village source, Village destination, String mode, Map<UnitType, Integer> units) {

        sourceVill = source;
        destVill = destination;
        sendMode = mode;
        if (sendMode != "attack" & sendMode != "support") {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, "FEHLER | sendMode kann nur entweder 'attack' oder 'support' sein!");
            return;
        }
        unitsToSend = units;
    }

    @Override
    public void run() {

        WebDriver webDrv = WebDriverSingleton.getWebDriver();

        if (ChristopherUtils.getCurrentVillage().coords != sourceVill.coords) {
            ChristopherUtils.goToVillage(null, sourceVill.coords);
        }
        if (!webDrv.getCurrentUrl().contains("screen=overview")) { // falls nicht in der dorfansicht in dorfansicht gehen // TODO geht nicht
            webDrv.findElement(By.cssSelector("a.nowrap")).click();
        }
        
//        webDrv.findElement(By.cssSelector("area.tooltip-delayed:nth-child(2)")).click(); // versammlungsplatz TODO, nicht über schnellleiste
        try {
            webDrv.findElement(By.cssSelector("li.quickbar_item:nth-child(5) > span:nth-child(1) > a:nth-child(1)")).click(); // versammlungsplatz
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, "FEHLER | Schnellleistensymbol für Versammlungsplatz wahrscheinlich nicht mehr vorhanden!", ex);
        }
        
        ChristopherUtils.waitTime(250);
        checkIfEnoughUnits();

        webDrv.findElement(By.cssSelector(".target-input-field")).sendKeys(destVill.coords.getXcoord() + "|" + destVill.coords.getYcoord());
        selectUnits();
        webDrv.findElement(By.cssSelector("#target_" + sendMode)).click();
        webDrv.findElement(By.cssSelector("#troop_confirm_go")).click();

        if ("attack" == sendMode) {
            System.out.println("BEFEHL | Angriff mit eingestellten Truppen nach " + destVill.getName() + " " + destVill.coords.toString() + " abgeschickt.");
        } else if ("support" == sendMode) {
            System.out.println("BEFEHL | Unterstützung mit eingestellten Truppen nach " + destVill.getName() + " " + destVill.coords.toString() + " abgeschickt.");
        }
    }

    private static void selectUnits() {
        WebDriver webDrv = WebDriverSingleton.getWebDriver();
        for (Map.Entry<UnitType, Integer> unit : unitsToSend.entrySet()) {
            try {
                webDrv.findElement(By.cssSelector("#unit_input_" + unit.getKey())).clear();
                webDrv.findElement(By.cssSelector("#unit_input_" + unit.getKey())).sendKeys((unit.getValue()).toString());
            } catch (Exception ex) {
                Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, "Einheit" + unit.getKey() + " kann nicht gesetzt werden!", ex);
            }
        }
    }

    private static void checkIfEnoughUnits() {
        WebDriver webDrv = WebDriverSingleton.getWebDriver();
        for (Map.Entry<UnitType, Integer> unit : unitsToSend.entrySet()) {
            if (Integer.parseInt(webDrv.findElement(By.cssSelector("#units_entry_all_" + unit.getKey())).getText().replace("(", "").replace(")", "")) < unitsToSend.get(unit.getKey())) {
                Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "Einheit" + unit.getKey() + " ist nur " + Integer.parseInt(webDrv.findElement(By.cssSelector("#units_entry_all_" + unit.getKey())).getText()) + " mal vorhanden, soll aber " + unitsToSend.get(unit) + " mal geschickt werden!");
            }
        }
    }
}

// TODO unused
//    private static void renameSendOrder(String orderName) {
//        WebDriver webDrv = WebDriverSingleton.getWebDriver();
//        webDrv.findElement(By.cssSelector("#default_name_span > a > img")).click();
//        webDrv.findElement(By.cssSelector("#new_attack_name")).sendKeys(orderName);
//        webDrv.findElement(By.cssSelector("#attack_name_btn")).click();
//    }

