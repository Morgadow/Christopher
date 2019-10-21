/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.execution;

import org.mightybot.christopher.data.TroopTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mightybot.christopher.CheckboxenFarmManager;
import org.mightybot.christopher.GroupTroop;
import org.mightybot.christopher.UnitType;
import org.mightybot.christopher.WebDriverSingleton;
import org.mightybot.christopher.utils.ChristopherUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Simon
 */
/**
 * Soll einmal von oben nach unten so viele Dörfer wie möglich mit den
 * eingestellten templates angreifen wenn group==null: farmt mit eingestellten
 * templates
 */
public class FarmTopToBottomTask implements Callable {

    private Map<UnitType, Integer> unitsHome;
    private Map<UnitType, Integer> templateA;
    private Map<UnitType, Integer> templateB;
    private GroupTroop group;
    private WebDriver webDrv;

    public FarmTopToBottomTask(GroupTroop groupTroop) {
        webDrv = WebDriverSingleton.getWebDriver();
        templateA = new HashMap<>();
        templateB = new HashMap<>();
        unitsHome = new HashMap<>();
        group = groupTroop;
    }

    @Override
    public Integer call() throws Exception {
        int i = 0, j = 0;
        try {

            ChristopherUtils.retryingFindClick(webDrv.findElement(By.id("manager_icon_farm")));
            ChristopherUtils.waitTime(250);

            boolean enablePage2 = true; // hier wieder entfernen

            setFarmPageSize(); // farmpage 100 einträge
            sortByDistance(); // entfernung aufsteigend ist richtig

            ChristopherUtils.checkCheckBox(CheckboxenFarmManager.ATTACKED_CHECKBOX.toString(), true);
            ChristopherUtils.checkCheckBox(CheckboxenFarmManager.ALL_VILLAGE_CHECKBOX.toString(), false);
            ChristopherUtils.checkCheckBox(CheckboxenFarmManager.PARTIAL_LOSSES_CHECKBOX.toString(), true);
            ChristopherUtils.checkCheckBox(CheckboxenFarmManager.FULL_LOSSES_CHECKBOX.toString(), false);

            readUnitsHome();
            readTemplates();

            List<WebElement> villagesToPlunder = webDrv.findElement(By.id("plunder_list")).findElements(By.cssSelector("[id^=village_]"));

            // Farme Seite 1
            int numAttacksA = howManyTimesCanAttack(templateA);
            i = farmTemplate(0, numAttacksA, "A", villagesToPlunder);
            System.out.println("FARM | Can farm " + numAttacksA + " villages with Template A and did farm " + i + " villages.");

            reduceUnitsHome(templateA, numAttacksA);

            int numAttacksB = howManyTimesCanAttack(templateB);
            i = farmTemplate(numAttacksA, numAttacksA + numAttacksB, "B", villagesToPlunder);
            System.out.println("FARM | Can farm " + numAttacksB + " villages with Template B and did farm " + (i - numAttacksA) + " villages.");

            // Farme Seite 2
            if (enablePage2) {
                if ((numAttacksA + numAttacksB) > 110) {
                    if (!webDrv.findElements(By.cssSelector("a.paged-nav-item:nth-child(2)")).isEmpty()) {
                        webDrv.findElement(By.cssSelector("a.paged-nav-item:nth-child(2)")).click();
                        ChristopherUtils.waitTime(250);
                        villagesToPlunder = webDrv.findElement(By.id("plunder_list")).findElements(By.cssSelector("[id^=village_]"));
                        readUnitsHome();
                        numAttacksB = howManyTimesCanAttack(templateB);
                        j = farmTemplate(0, numAttacksB, "B", villagesToPlunder);
                        System.out.println("FARM | Page 2: Can farm " + numAttacksB + " villages with Template B and did farm " + j + " villages.");
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "exception beim farmen", ex);
        }
        return i + j;
    }

    private int farmTemplate(int startCounter, int endCounter, String template, List<WebElement> villagesToPlunder) {
        int i = 0;
        for (i = startCounter; (i < endCounter) & (i < villagesToPlunder.size()); i++) {
            WebElement village = villagesToPlunder.get(i);
            ChristopherUtils.randomWaitTime(25, 200);
            try {
                retryingFindClick(village.findElement(By.cssSelector("[class*=farm_icon_" + template.toLowerCase() + "]")));
            } catch (Exception ex) {
                System.out.println("Konnte nicht klicken, überspringe: " + i);
            }
//            village.findElement(By.cssSelector("[class*=farm_icon_" + template.toLowerCase() + "]")).click();
            if ((i % 5) == 0) { // ein bisschen runterscrollen, ein feld ist ca 25 pt hoch, 5x 25 plus ränder ~ 150
                ChristopherUtils.scrollByCoordinates(0, 150);
            }
        }
        return i;
    }

    private int howManyTimesCanAttack(Map<UnitType, Integer> template) {
        // berechnet im vorfeld wie viele angriffe losgeschickt werden können
        int numAttacks = 9999;

        for (UnitType element : template.keySet()) {
            if (template.get(element) != 0) {
                int buf = unitsHome.get(element) / template.get(element);
                if (buf < numAttacks) {
                    numAttacks = buf;
                }
            }
        }
        return numAttacks;
    }

    private void reduceUnitsHome(Map<UnitType, Integer> template, int numAttacks) {
        for (Map.Entry<UnitType, Integer> unit : template.entrySet()) {
            unitsHome.put(unit.getKey(), unitsHome.get(unit.getKey()) - unit.getValue() * numAttacks);
        }
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
        try {
            
            if (group != null) {

                if ((GroupTroop.OFF == group) | (GroupTroop.OFF_AG == group)) {
                    ChristopherUtils.setTemplatesFarmManager("A", TroopTemplate.getFarmTemplateAOFF());
                    ChristopherUtils.setTemplatesFarmManager("B", TroopTemplate.getFarmTemplateBOFF());
                } else if (GroupTroop.DEFF_NORMAL == group) {
                    ChristopherUtils.setTemplatesFarmManager("A", TroopTemplate.getFarmTemplateADEFF());
                    ChristopherUtils.setTemplatesFarmManager("B", TroopTemplate.getFarmTemplateBDEFF());
                } else {
                    System.out.println("FARM | Kann keine Templates zuweisen, keine richtige Gruppe!");
                }
                ChristopherUtils.waitTime(250);
            }
            
            WebElement tableA = webDrv.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[2]/table[3]/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/div[2]/div/form[1]/table"));
            WebElement tableB = webDrv.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[2]/table[3]/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/div[2]/div/form[2]/table"));

            for (UnitType element : unitsHome.keySet()) {
                templateA.put(element, Integer.parseInt(tableA.findElement(By.name(element.toString())).getAttribute("value")));
                templateB.put(element, Integer.parseInt(tableB.findElement(By.name(element.toString())).getAttribute("value")));
            }
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "FARM | debug exception beim template auslesen/setzen", ex);
        }
    }

    private void sortByDistance() {
        try {
            List<WebElement> villagesToPlunder = webDrv.findElement(By.id("plunder_list")).findElements(By.cssSelector("[id^=village_]"));
            WebElement sortVillA = villagesToPlunder.get(0);
            for (int i = 1; i < 10; i++) {
                WebElement sortVillB = villagesToPlunder.get(i);
                if (Double.parseDouble(sortVillA.findElement(By.cssSelector("td:nth-child(8)")).getText()) > Double.parseDouble(sortVillB.findElement(By.cssSelector("td:nth-child(8)")).getText())) {
                    webDrv.findElement(By.cssSelector("#plunder_list > tbody:nth-child(1) > tr:nth-child(1) > th:nth-child(8) > a:nth-child(1) > img:nth-child(1)")).click();
                }
                break;
            }

            // falls jetzt entfernung absteigend nochmal nachschauen
            villagesToPlunder = webDrv.findElement(By.id("plunder_list")).findElements(By.cssSelector("[id^=village_]"));
            sortVillA = villagesToPlunder.get(0);
            for (int i = 1; i < 10; i++) {
                WebElement sortVillB = villagesToPlunder.get(i);
                if (Double.parseDouble(sortVillA.findElement(By.cssSelector("td:nth-child(8)")).getText()) > Double.parseDouble(sortVillB.findElement(By.cssSelector("td:nth-child(8)")).getText())) {
                    webDrv.findElement(By.cssSelector("#plunder_list > tbody:nth-child(1) > tr:nth-child(1) > th:nth-child(8) > a:nth-child(1) > img:nth-child(1)")).click();
                }
                break;
            }

        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "FARM | debug exception beim nach entfernung, nähestes zuerst", ex);
        }
    }

    private void setFarmPageSize() {
        try {
            if (Integer.parseInt(webDrv.findElement(By.id("farm_pagesize")).getAttribute("value")) != 100) {
                ChristopherUtils.scrollToBottom();
                webDrv.findElement(By.id("farm_pagesize")).clear();
                webDrv.findElement(By.id("farm_pagesize")).sendKeys("100");
                webDrv.findElement(By.cssSelector("#plunder_list_nav > form:nth-child(2) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(3) > input:nth-child(1)")).click();
                ChristopherUtils.scrollToTop();
            }
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "FARM | debug exception beim umstellen der seitenlänge", ex);
        }
    }

    public boolean retryingFindClick(WebElement button) {
        boolean result = false;
        int attempts = 0;
        while (attempts < 2) {
            try {
                button.click();
                result = true;
                break;
//            } catch (org.openqa.selenium.StaleElementReferenceException e) {
            } catch (Exception e) { // org.openqa.selenium.ElementClickInterceptedException wird dann hoffentlich auch abgefangen
            }
            attempts++;
        }
        return result;
    }

}
