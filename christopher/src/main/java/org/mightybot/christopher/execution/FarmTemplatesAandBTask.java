/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.execution;

import static java.awt.SystemColor.window;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mightybot.christopher.CheckboxenFarmManager;
import org.mightybot.christopher.UnitType;
import org.mightybot.christopher.WebDriverSingleton;
import org.mightybot.christopher.utils.ChristopherUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

/**
 *
 * @author wiesbob
 */
/**
 * Farmt erst Template A dann Template B solange wie Truppen vorhanden sind,
 * Dörfer werden nicht doppelt angegriffen
 */
public class FarmTemplatesAandBTask implements Runnable {

    private Map<UnitType, Integer> unitsHome;
    private Map<UnitType, Integer> templateA;
    private Map<UnitType, Integer> templateB;
    private WebDriver webDrv;

    public FarmTemplatesAandBTask() {
        webDrv = WebDriverSingleton.getWebDriver();
        templateA = new HashMap<>();
        templateB = new HashMap<>();
        unitsHome = new HashMap<>();
    }

    @Override
    public void run() {
        webDrv.findElement(By.id("manager_icon_farm")).click();
        ChristopherUtils.scrollToBottom();
        if (Integer.parseInt(webDrv.findElement(By.id("farm_pagesize")).getAttribute("value")) != 100) {
            webDrv.findElement(By.id("farm_pagesize")).clear();
            webDrv.findElement(By.id("farm_pagesize")).sendKeys("100");
            webDrv.findElement(By.cssSelector("#plunder_list_nav > form:nth-child(2) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(3) > input:nth-child(1)")).click();
        }
        ChristopherUtils.scrollToTop();

        ChristopherUtils.checkCheckBox(CheckboxenFarmManager.ATTACKED_CHECKBOX.toString(), false);
        ChristopherUtils.checkCheckBox(CheckboxenFarmManager.ALL_VILLAGE_CHECKBOX.toString(), false);
        ChristopherUtils.checkCheckBox(CheckboxenFarmManager.PARTIAL_LOSSES_CHECKBOX.toString(), true);
        ChristopherUtils.checkCheckBox(CheckboxenFarmManager.FULL_LOSSES_CHECKBOX.toString(), false);

        readUnitsHome();
        readTemplates();

        List<WebElement> villagesToPlunder = webDrv.findElement(By.id("plunder_list")).findElements(By.cssSelector("[id^=village_]"));
        boolean unitsAvailable = true;

        for (int i = 0; i < villagesToPlunder.size() && unitsAvailable; i++) {
            WebElement village = villagesToPlunder.get(i);
            unitsAvailable = false;
            ChristopherUtils.randomWaitTime(50, 500);
            
            if (farmTemplate(templateA)) {
                village.findElement(By.cssSelector("[class*=farm_icon_a]")).click();
                unitsAvailable = true;
            } else if (farmTemplate(templateB)) {
                village.findElement(By.cssSelector("[class*=farm_icon_b]")).click();
                unitsAvailable = true;
            }
        }
//        for (WebElement village : villagesToPlunder) {
//            WebElement currentVillageBtnA = village.findElement(By.cssSelector("[class*=farm_icon_a]"));
//            WebElement currentVillageBtnB = village.findElement(By.cssSelector("[class*=farm_icon_b]"));
//            if (!currentVillageBtnA.getAttribute("class").contains("farm_icon farm_icon_a farm_icon_disabled start_locked")) {
//                currentVillageBtnA.click();
//            } else if (!currentVillageBtnB.getAttribute("class").contains("farm_icon farm_icon_b farm_icon_disabled start_locked")) {
//                currentVillageBtnB.click();
//            }
//
//        }
    }

    private void readUnitsHome() throws NumberFormatException {
        WebElement unitsHomeTable = webDrv.findElement(By.id("units_home"));
        unitsHome.put(UnitType.SPEAR, Integer.parseInt(unitsHomeTable.findElement(By.id("spear")).getText()));
        unitsHome.put(UnitType.SWORD, Integer.parseInt(unitsHomeTable.findElement(By.id("sword")).getText()));
        unitsHome.put(UnitType.AXE, Integer.parseInt(unitsHomeTable.findElement(By.id("axe")).getText()));
        unitsHome.put(UnitType.SPY, Integer.parseInt(unitsHomeTable.findElement(By.id("spy")).getText()));
        unitsHome.put(UnitType.LIGHT, Integer.parseInt(unitsHomeTable.findElement(By.id("light")).getText()));
        unitsHome.put(UnitType.HEAVY, Integer.parseInt(unitsHomeTable.findElement(By.id("heavy")).getText()));
    }

    private void readTemplates() {
        WebElement tableA = webDrv.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[2]/table[3]/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/div[2]/div/form[1]/table"));
        WebElement tableB = webDrv.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[2]/table[3]/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/div[2]/div/form[2]/table"));

        for (UnitType element : unitsHome.keySet()) {
            templateA.put(element, Integer.parseInt(tableA.findElement(By.name(element.toString())).getAttribute("value")));
            templateB.put(element, Integer.parseInt(tableB.findElement(By.name(element.toString())).getAttribute("value")));
        }
    }

    private boolean farmTemplate(Map<UnitType, Integer> template) {
        boolean enoughUnits = false;

        for (Entry<UnitType, Integer> unit : unitsHome.entrySet()) {
            enoughUnits = (unit.getValue() - template.get(unit.getKey())) >= 0;
            if (!enoughUnits) {
                break;
            }
        }

        if (enoughUnits) {
            for (Entry<UnitType, Integer> unit : template.entrySet()) {
                unitsHome.put(unit.getKey(), unitsHome.get(unit.getKey()) - unit.getValue());
            }
        }
        return enoughUnits;
    }

}
