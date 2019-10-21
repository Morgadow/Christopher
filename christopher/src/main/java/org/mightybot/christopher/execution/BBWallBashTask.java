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
import org.mightybot.christopher.Village;
import org.mightybot.christopher.WebDriverSingleton;
import org.mightybot.christopher.utils.ChristopherUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Simon
 */
// TODO Abfrage für gespähte dörfer wo wallstufen angezeigt werden
public class BBWallBashTask implements Callable {

    private WebDriver webDrv;
    private Map<UnitType, Integer> bashTemplateAxe;
    private Map<UnitType, Integer> bashTemplateLight;
    private String nameTemplateAxe;
    private String nameTemplateLight;
    private String nameAttack;
    private int attackCounter;
    private List<Village> bashedVills;

    public BBWallBashTask(List<Village> bashedVills) {
        this.webDrv = WebDriverSingleton.getWebDriver();
        this.bashTemplateAxe = TroopTemplate.getBBWallBashAxe();
        this.bashTemplateLight = TroopTemplate.getBBWallBashLight();
        this.nameTemplateAxe = "BB_WALL_BASH_AXE";
        this.nameTemplateLight = "BB_WALL_BASH_LIGHT";
        this.nameAttack = "BB_Wall_Bash";
        this.attackCounter = 0;
        this.bashedVills = bashedVills;
    }

    @Override
    public List<Village> call() throws Exception {
        try {

            // falls templates leer
            if ((null == bashTemplateAxe) | (null == bashTemplateLight)) {
                System.out.println("WALLBASH | Templates sind null");
                return this.bashedVills;
            }

            ChristopherUtils.retryingFindClick(webDrv.findElement(By.id("manager_icon_farm")));
            ChristopherUtils.waitTime(250);

            ChristopherUtils.checkCheckBox(CheckboxenFarmManager.PARTIAL_LOSSES_CHECKBOX.toString(), true);
            ChristopherUtils.checkCheckBox(CheckboxenFarmManager.FULL_LOSSES_CHECKBOX.toString(), true);

            checkFarmPage(); // Seite 1
            if (!webDrv.findElements(By.cssSelector("a.paged-nav-item:nth-child(2)")).isEmpty()) { // Seite 2
                ChristopherUtils.retryingFindClick(webDrv.findElement(By.cssSelector("a.paged-nav-item:nth-child(2)")));
                checkFarmPage();
            }

            if (attackCounter > 0) {
                System.out.println("WALLBASHER | Dorf " + ChristopherUtils.getCurrentVillage().toString() + " hat " + attackCounter + " Dörfer gebasht");
            }
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "WALLBASHER | Exception in run Methode!", ex);
        }

        System.out.println("attackCounter: " + attackCounter); // TODO löschen
        return this.bashedVills;
    }

    private void checkFarmPage() {
        try {
            // jedes dorf durchgehen und wenn wall > 0 ODER gelbes oder rotes symbol dann bashen
            List<WebElement> villagesToPlunder = webDrv.findElement(By.id("plunder_list")).findElements(By.cssSelector("[id^=village_]"));

            for (int i = 0; i < villagesToPlunder.size(); i++) {
                WebElement village = villagesToPlunder.get(i);

                Village currentVill = getVillageName(village.findElement(By.cssSelector("td:nth-child(4) > a:nth-child(1)")).getText());

                // gelbes ODER rotes symbol
                try {
                    String image = village.findElement(By.cssSelector("td:nth-child(2) > img:nth-child(1)")).getAttribute("src");
                    if ((image.contains("/dots/yellow.png")) | (image.contains("/dots/red.png"))) {
                        System.out.println("dorf gefunden dot");
                        if (!(bashedVills.contains(currentVill))) {
//                            ChristopherUtils.scrollToElement(village.findElement(By.cssSelector("td:nth-child(2) > img:nth-child(1)")));
                            ChristopherUtils.scrollToTop();
                            ChristopherUtils.scrollByCoordinates(0, i * 50);
                            boolean retVal = sendAttack(village.findElement(By.cssSelector("td:nth-child(12) > a:nth-child(1) > img:nth-child(1)")));
                            if (!retVal) { // keine truppen mehr über
                                break;
                            } else {
                                bashedVills.add(currentVill); // zur liste mit angegriffenen Dörfern hinzufügen
                            }
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "WALLBASHER | Exception beim Abchecken ob Verluste bei letztem Angriff!", ex);
                }

                // Wallstufe > 0
                try {
                    String wallSize = village.findElement(By.cssSelector("td:nth-child(7)")).getText();
                    if (!"?".equals(wallSize) && Integer.parseInt(wallSize) > 0) { // wall größer 0
                        System.out.println("dorf gefunden wall");
                        if (!(bashedVills.contains(currentVill))) {
                            boolean retVal = sendAttack(village.findElement(By.cssSelector("td:nth-child(12) > a:nth-child(1) > img:nth-child(1)")));
                            if (!retVal) { // keine truppen mehr über
                                break;
                            } else {
                                bashedVills.add(currentVill); // zur liste mit angegriffenen Dörfern hinzufügen
                            }
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "WALLBASHER | Exception beim Abchecken welche WallStufe!", ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "WALLBASHER | Exception beim Bearbeiten der Farmmanager Seite!", ex);
        }
    }

    private boolean sendAttack(WebElement villageToBash) {
        try {
            ChristopherUtils.retryingFindClick(villageToBash); // Versammlungsplatz symbol klicken
            ChristopherUtils.waitTime(125);

            List<WebElement> templates = webDrv.findElements(By.cssSelector("#troop_template_selector"));

//            if (checkIfEnoughUnits(bashTemplateAxe)) {
//                for (WebElement element : templates) {
//                    if (element.getText().equals(nameTemplateAxe)) {
//                        element.click();
//                        ChristopherUtils.retryingFindClick(element);
//                        break;
//                    }
//                }
//
//            } else if (checkIfEnoughUnits(bashTemplateLight)) {
//                for (WebElement element : templates) {
//                    if (element.getText().equals(nameTemplateLight)) {
//                        element.click();
//                        ChristopherUtils.retryingFindClick(element);
//                        break;
//                    }
//                }
//            } else {
//                return false; // not enough units
//            }
            WebElement buffer = webDrv.findElement(By.xpath("//*[contains(text(), '" + nameTemplateAxe + "')]"));

            if (checkIfEnoughUnits(bashTemplateAxe)) {
                ChristopherUtils.retryingFindClick(webDrv.findElement(By.xpath("//*[contains(text(), '" + nameTemplateAxe + "')]"))); // select template
            } else if (checkIfEnoughUnits(bashTemplateLight)) {
                ChristopherUtils.retryingFindClick(webDrv.findElement(By.xpath("//*[contains(text(), '" + nameTemplateLight + "')]"))); // select template
            } else {
                return false;
            }

            ChristopherUtils.retryingFindClick(webDrv.findElement(By.cssSelector("#target_attack"))); // attack
            ChristopherUtils.waitTime(125);
            renameAttack();
            ChristopherUtils.retryingFindClick(webDrv.findElement(By.cssSelector("#troop_confirm_go"))); // confirm attack
            attackCounter++;
            return true;

        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "WALLBASHER | Exception beim Abschicken des Angriffs!", ex);
        }
        return false;
    }

    private void renameAttack() {
        try {
            ChristopherUtils.retryingFindClick(webDrv.findElement(By.cssSelector("#default_name_span > a:nth-child(2) > img:nth-child(1)"))); // btn umbenennen
            webDrv.findElement(By.cssSelector("#new_attack_name")).clear();
            webDrv.findElement(By.cssSelector("#new_attack_name")).sendKeys(nameAttack);
            ChristopherUtils.retryingFindClick(webDrv.findElement(By.cssSelector("#attack_name_btn"))); // ok btn
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "WALLBASHER | Exception beim Umbenennen des Angriffs!", ex);
        }
    }

    private boolean checkIfEnoughUnits(Map<UnitType, Integer> template) {
        try {
            for (Map.Entry<UnitType, Integer> unit : template.entrySet()) {
                if (Integer.parseInt(webDrv.findElement(By.cssSelector("#units_entry_all_" + unit.getKey())).getText().replace("(", "").replace(")", "")) < template.get(unit.getKey())) {
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "WALLBASHER | Exception beim Überprüfen ob genug Einheuten vorhanden!", ex);
        }
        return false;
    }

    private Village getVillageName(String villRaw) {
        try {
            String[] vill = villRaw.replace("(", "").replace(")", "").split(" ")[0].split("\\|");
            return new Village(null, Integer.parseInt(vill[0]), Integer.parseInt(vill[1]));
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "WALLBASHER | Exception Auslesen der Dorfkoordinaten!", ex);
        }
        return null;
    }

}
