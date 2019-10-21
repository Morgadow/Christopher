/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.utils;

import org.mightybot.christopher.data.GlobalData;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mightybot.christopher.Christopher;
import org.mightybot.christopher.Coordinate;
import org.mightybot.christopher.GroupTroop;
import org.mightybot.christopher.Time;
import org.mightybot.christopher.UnitType;
import org.mightybot.christopher.Village;
import org.mightybot.christopher.WebDriverSingleton;
import org.mightybot.christopher.data.LoginData;
import org.mightybot.christopher.data.TroopTemplate;
import org.mightybot.christopher.data.UnitRuntime;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author wiesbob
 */
public class ChristopherUtils {

    public static void initialize() {
        ChristopherUtils.initializeGeckoDriver(); // Geckodriver

        WebDriver webDrv = WebDriverSingleton.getWebDriver(); // Webdriver
        webDrv.manage().window().maximize();

        GlobalData.setAmountIncomingAttacks(0);
        GlobalData.setAmountIncomingSupports(0);

        new TroopTemplate();
        UnitRuntime runtimes = new UnitRuntime();

        new LoginData();
        LoginData.setWorldNumber(166);
        LoginData.setUserName("Christopher");
        LoginData.setUserPassword("christopher1337");

//        Logger.setLevel(Level.ALL);
        Logger.getLogger(ChristopherUtils.class.getName()).setLevel(Level.ALL);
//        Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);

//        data.JsonReader.readConfigJSON();
//        data.JsonReader.readAllUnits();
        System.out.println("INFO | Init fertig, los gehts!");
    }

    public static void initializeGeckoDriver() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            System.setProperty("webdriver.gecko.driver", Paths.get("").toAbsolutePath().toString() + "/resources/drivers/firefox/windows/geckodriver.exe");
            Logger.getLogger(Christopher.class.getName()).log(Level.INFO, "Took geckodriver from: {0}/resources/drivers/firefox/windows/geckodriver.exe", Paths.get("").toAbsolutePath().toString());
        }
        if (System.getProperty("os.name").startsWith("Linux")) {
            System.setProperty("webdriver.gecko.driver", Paths.get("").toAbsolutePath().toString() + "/resources/drivers/firefox/linux/geckodriver");
            Logger.getLogger(Christopher.class.getName()).log(Level.INFO, "Took geckodriver from: {0}/resources/drivers/firefox/windows/geckodriver", Paths.get("").toAbsolutePath().toString());
        }
    }

    public static void checkCheckBox(String checkboxName, boolean check) {

        WebDriver webDrv = WebDriverSingleton.getWebDriver();

        try {
            WebElement checkboxElement = webDrv.findElement(By.cssSelector("#" + checkboxName));
            boolean isChecked = checkboxElement.isSelected();

            if (isChecked != check) {
                ChristopherUtils.retryingFindClick(checkboxElement);
                ChristopherUtils.waitTime(250);
                if (checkboxElement.isSelected() != check) {
                    Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, null, "Warnung | Checkbox " + checkboxName + " konnte nicht umgeschalten werden!");
                }
            }
        } catch (Exception ex) {
            System.out.println("FEHLER | Konnte checkbox " + checkboxName + " nicht finden!");
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    public static void nextVillage() {
        WebDriver webDrv = WebDriverSingleton.getWebDriver();
        try {
            ChristopherUtils.scrollToTop();
            ChristopherUtils.waitTime(250);
            if (!webDrv.findElements(By.cssSelector(".arrowRight")).isEmpty()) {
                webDrv.findElement(By.cssSelector(".arrowRight")).click();
                return;
            }
            if (!webDrv.findElements(By.cssSelector(".groupRight")).isEmpty()) {
                webDrv.findElement(By.cssSelector(".groupRight")).click();
                return;
            }
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "WARNUNG | Konnte kein Dorf weiterschalten, wahrscheinlich nur eins vorhanden!");

        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("FEHLER | Konnte kein Dorf weiterschalten, wahrscheinlich nur eins vorhanden!");
        }
    }

    public static void previousVillage() {
        WebDriver webDrv = WebDriverSingleton.getWebDriver();
        try {
            ChristopherUtils.scrollToTop();
            ChristopherUtils.waitTime(250);
            if (!webDrv.findElements(By.cssSelector(".arrowLeft")).isEmpty()) {
                webDrv.findElement(By.cssSelector(".arrowLeft")).click();
                return;
            }
            if (!webDrv.findElements(By.cssSelector(".groupLeft")).isEmpty()) {
                webDrv.findElement(By.cssSelector(".groupLeft")).click();
                return;
            }
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "WARNUNG | Konnte kein Dorf weiterschalten, wahrscheinlich nur eins vorhanden!");
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("FEHLER | Konnte kein Dorf zurückschalten, wahrscheinlich nur eins vorhanden!");
        }
    }

    public static double calculateDistance(Coordinate coordsA, Coordinate coordsB) {
        int xdistance = abs(coordsA.getXcoord() - coordsB.getXcoord());
        int ydistance = abs(coordsA.getYcoord() - coordsB.getYcoord());
        return sqrt(xdistance * xdistance + ydistance * ydistance);
    }

    public static Time calculateUnitRuntime(Coordinate coordsA, Coordinate coordsB, int unitSpeed) {
        double doubleTime = ChristopherUtils.calculateDistance(coordsA, coordsB) * unitSpeed;
        int hours = (int) (doubleTime / 60);
        int minutes = (int) (doubleTime - hours * 60);
        int seconds = (int) ((doubleTime - hours * 60 - minutes) * 60);
        int milliseconds = (int) ((doubleTime - (hours * 60 + minutes + seconds / 60)) * 60000);
        return new Time(hours, minutes, seconds, milliseconds);
    }

    public static void setTemplatesFarmManager(String template, Map<UnitType, Integer> units) {
        WebDriver webDrv = WebDriverSingleton.getWebDriver();

        if (null == units) {
//            System.out.println("FEHLER | Übergebenes Template is null!");
            return;
        }

        try {
            if (webDrv.findElements(By.cssSelector("#content_value > h3:nth-child(2)")).isEmpty()) { // falls noch nicht im farmmanager
                webDrv.findElement(By.id("manager_icon_farm")).click();
                ChristopherUtils.waitTime(500);
            }

            if ("A".equals(template)) {
                WebElement tableA = webDrv.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[2]/table[3]/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/div[2]/div/form[1]/table"));
                // TODO vielleicht xpath hier anders lösen

                boolean haveToSave = false;
//                for (Entry<UnitType, Integer> entry : units.entrySet()) {
                for (Map.Entry<UnitType, Integer> entry : units.entrySet()) {
                    try {
                        int valTemp = Integer.parseInt(tableA.findElement(By.name(entry.getKey().toString().toLowerCase())).getAttribute("value"));
                        if (valTemp != entry.getValue()) {
                            tableA.findElement(By.name(entry.getKey().toString())).clear();
                            tableA.findElement(By.name(entry.getKey().toString())).sendKeys(entry.getValue().toString());
                            haveToSave = true;
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Christopher.class.getName()).log(Level.WARNING, "Einheit " + entry.getKey() + " kann nicht im Farmmanager gesetzt werden!", ex);
                    }
                }
                if (haveToSave) {
                    tableA.findElement(By.className("btn")).click();
                }
            }
            if ("B".equals(template)) {
                WebElement tableB = webDrv.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[2]/table[3]/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/div[2]/div/form[2]/table"));
                // TODO vielleicht xpath hier anders lösen

                boolean haveToSave = false;
//                for (Entry<UnitType, Integer> entry : units.entfor (Map.Entry<UnitType, Integer> entry : units.entrySet()) {rySet()) {
                for (Map.Entry<UnitType, Integer> entry : units.entrySet()) {
                    try {
                        int valTemp = Integer.parseInt(tableB.findElement(By.name(entry.getKey().toString().toLowerCase())).getAttribute("value"));
                        if (valTemp != entry.getValue()) {
                            tableB.findElement(By.name(entry.getKey().toString())).clear();
                            tableB.findElement(By.name(entry.getKey().toString())).sendKeys(entry.getValue().toString());
                            haveToSave = true;
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Christopher.class.getName()).log(Level.WARNING, "Einheit " + entry.getKey() + " kann nicht im Farmmanager gesetzt werden!", ex);
                    }
                }
                if (haveToSave) {
                    tableB.findElement(By.className("btn")).click();
                }

            }
            if ("A" != template & "B" != template) {
                Logger.getLogger(Christopher.class.getName()).log(Level.SEVERE, "Input template has to be 'A' or 'B'!");
            }
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void scrollToTop() {
        WebDriver webDrv = WebDriverSingleton.getWebDriver();
        try {
            ((JavascriptExecutor) webDrv)
                    .executeScript("window.scrollTo(0, 0)");
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void scrollToBottom() {
        WebDriver webDrv = WebDriverSingleton.getWebDriver();
        try {
            ((JavascriptExecutor) webDrv)
                    .executeScript("window.scrollTo(0, document.body.scrollHeight)");
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void scrollToElement(WebElement element) {
        WebDriver webDrv = WebDriverSingleton.getWebDriver();
        try {
            ((JavascriptExecutor) webDrv).executeScript(
                    "arguments[0].scrollIntoView();", element);
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void scrollByCoordinates(int x, int y) {
        WebDriver webDrv = WebDriverSingleton.getWebDriver();
        try {
            ((JavascriptExecutor) webDrv).executeScript("window.scrollBy(" + x + "," + y + ")");
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void waitTime(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int randomWaitTime(int minMilliseconds, int maxMilliseconds) {
        int randomTime = (int) (Math.random() * (maxMilliseconds - minMilliseconds) + minMilliseconds);
        try {
            Thread.sleep(randomTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return randomTime;
    }

    public static void renameVillage(Village vill, Coordinate coord) {
        // TODO
        WebDriver webDrv = WebDriverSingleton.getWebDriver();
        String genericName = "Christopher";

//        try {            
        if (null == vill & null == coord) { //dafür muss man sich schon in der dorfansicht befinden
            webDrv.findElement(By.cssSelector("area.tooltip-delayed:nth-child(1)")).click();
            String villCord = webDrv.findElement(By.cssSelector("b.nowrap")).getAttribute("value");
//                String newName = "|" +;
            webDrv.findElement(By.cssSelector("area.tooltip-delayed:nth-child(1)")).click();
            ChristopherUtils.scrollToBottom();
            webDrv.findElement(By.cssSelector("#content_value > form:nth-child(9) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(1) > input:nth-child(1)")).clear();
        }

//            } 
//              if (null!=vill&null==coord)
//                int xname = vill.getXcoord() - vill.getXcoord() / 100;
//                int yname = vill.getYcoord() - vill.getYcoord() / 100;
//                String newName = "|" + vill.getContinent() + ":" + xname + ":" + yname + "| " + genericName;
//
//                webDrv.findElement(By.cssSelector("area.tooltip-delayed:nth-child(1)")).click();
//                ChristopherUtils.scrollToBottom();
//                webDrv.findElement(By.cssSelector("#content_value > form:nth-child(9) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(1) > input:nth-child(1)")).clear();
//                webDrv.findElement(By.cssSelector("#content_value > form:nth-child(9) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(1) > input:nth-child(1)")).sendKeys(newName);
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, null, ex);
//        }
    }

    public static void goToVillage(String villName, Coordinate villCoords) {
        WebDriver webDrv = WebDriverSingleton.getWebDriver();
        try {
            if (villName != null & villCoords == null) { // falls schon im richtigen dorf, dann einfach noch auf dorfansicht gehen
                if (ChristopherUtils.getCurrentVillage().getName().equals(villName)) {
                    if (webDrv.findElements(By.cssSelector("area.tooltip-delayed:nth-child(1)")).isEmpty()) {
                        webDrv.findElement(By.cssSelector("a.nowrap")).click();
                    }
                    return;
                }
                webDrv.findElement(By.cssSelector("td.menu-item:nth-child(2) > a:nth-child(1)")).click(); // auf übersicht gehen
                webDrv.findElement(By.cssSelector("#overview_menu > tbody > tr > td:nth-child(1) > a")).click(); // auf kombiniert gehen, da sind alle dörfer
                ChristopherUtils.waitTime(250);
                webDrv.findElement(By.xpath("//*[contains(text(), 'alle')]")).click(); // alle dörfer wählen
                ChristopherUtils.waitTime(250);
                webDrv.findElement(By.partialLinkText(villName)).click(); // TODO testen, vielleicht über xpath tabelle in denen dörfer gespeichert werden und dann durchehen
                ChristopherUtils.waitTime(250);
            }
            if (villName == null & villCoords != null) { // falls schon im richtigen dorf, dann einfach noch auf dorfansicht gehen
                if (ChristopherUtils.getCurrentVillage().getCoords().equals(villCoords)) {
                    if (webDrv.findElements(By.cssSelector("area.tooltip-delayed:nth-child(1)")).isEmpty()) {
                        webDrv.findElement(By.cssSelector("a.nowrap")).click();
                    }
                    return;
                }
                webDrv.findElement(By.cssSelector("td.menu-item:nth-child(2) > a:nth-child(1)")).click(); // auf übersicht gehen
                webDrv.findElement(By.cssSelector("#overview_menu > tbody > tr > td:nth-child(1) > a")).click(); // auf kombiniert gehen, da sind alle dörfer
                ChristopherUtils.waitTime(250);
                webDrv.findElement(By.xpath("//*[contains(text(), 'alle')]")).click(); // alle dörfer wählen
                ChristopherUtils.waitTime(250);
                webDrv.findElement(By.partialLinkText(villCoords.toString())).click(); // TODO testen, vielleicht über xpath tabelle in denen dörfer gespeichert werden und dann durchehen
                ChristopherUtils.waitTime(250);
            }
            // TODO wenn beides gegeben ist, schauen ob das zum selben dorf gehört!
            if ((villName == null & villCoords == null) | (villName != null & villCoords != null)) {
                Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, null, "Fehler | Genau eine der beiden Input Möglichkeiten verwenden!");
            }
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    public static Village getCurrentVillage() {
        try {
            WebDriver webDrv = WebDriverSingleton.getWebDriver();
            String coordWrap = webDrv.findElement(By.cssSelector("b.nowrap")).getText();
            String nameWrap = webDrv.findElement(By.cssSelector("a.nowrap")).getText();
            String[] coords = coordWrap.replace("(", "").replace(")", "").split(" ")[0].split("\\|");
            return new Village(nameWrap, Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, null, ex);
        }
        return null;
    }

    public static int selectAllVillagesFromGroup(GroupTroop group) {
        try {
            WebDriver webDrv = WebDriverSingleton.getWebDriver();
            if (webDrv.findElements(By.cssSelector("#overview_menu")).isEmpty()) { // auf übersicht gehen
                ChristopherUtils.retryingFindClick(webDrv.findElement(By.cssSelector("td.menu-item:nth-child(2) > a:nth-child(1)")));
                ChristopherUtils.waitTime(250);
            }
            if (webDrv.findElements(By.cssSelector("td.selected > a:nth-child(1)")).isEmpty()) { // kombiniert
                ChristopherUtils.retryingFindClick(webDrv.findElement(By.cssSelector("#overview_menu > tbody > tr > td:nth-child(1) > a")));
            }
            ChristopherUtils.waitTime(250);
            webDrv.findElement(By.xpath("//*[contains(text(), '" + group.toString() + "')]")).click(); // gruppe wählen
            ChristopherUtils.waitTime(250);
            List<WebElement> villages = webDrv.findElements(By.className("quickedit-label"));
            if (villages.size() > 0) { // dörfer in gruppe
                if (!webDrv.findElements(By.cssSelector(".jump_link > img:nth-child(1)")).isEmpty()) { // dann ist das nicht die aktuelle gruppe
                    ChristopherUtils.retryingFindClick(webDrv.findElement(By.cssSelector(".jump_link > img:nth-child(1)"))); // pfeil "erstes dorf der Gruppe"
                    ChristopherUtils.waitTime(250);
                }
            }
            ChristopherUtils.retryingFindClick(webDrv.findElement(By.cssSelector("a.nowrap"))); // auf dorfansicht
            ChristopherUtils.waitTime(250);
            return villages.size();
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public static void selectAllVillages() {
        WebDriver webDrv = WebDriverSingleton.getWebDriver();
        try {
            webDrv.findElement(By.cssSelector("td.menu-item:nth-child(2) > a:nth-child(1)")).click(); // auf übersicht gehen
            webDrv.findElement(By.cssSelector("#overview_menu > tbody > tr > td:nth-child(1) > a")).click(); // auf kombiniert gehen, da sind alle dörfer
            ChristopherUtils.waitTime(250);
            webDrv.findElement(By.xpath("//*[contains(text(), 'alle')]")).click(); // alle dörfer wählen
            ChristopherUtils.waitTime(250);
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    public static GroupTroop getGroupFromCurrentVillage() {
        try {
            WebDriver webDrv = WebDriverSingleton.getWebDriver();
            List<WebElement> groups = webDrv.findElements(By.cssSelector(".p_groups"));
            if (webDrv.findElements(By.cssSelector("#show_groups > h4:nth-child(1)")).isEmpty()) { // Falls nicht in der Dorfansicht können die Gruppen nicht ausgelesen werden
                ChristopherUtils.scrollToTop();
                ChristopherUtils.waitTime(125);
                webDrv.findElement(By.cssSelector("a.nowrap")).click();
                ChristopherUtils.waitTime(125);
            }

            if (groups.size() > 1) { // mehr als eine gruppe, return null und warnung
                Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "WARNUNG | Dorf " + ChristopherUtils.getCurrentVillage() + " hat mehr als eine Gruppe gesetzt!");
                for (int i = 0; i < groups.size(); i++) {
                    System.out.println("\tGesetzt sind: " + (i + 1) + ".: " + groups.get(i).getText());
                }
                return null;
            }
            if (groups.isEmpty()) { // auch wenn nur eine gruppe ist die list leer, ist ein anderes webelement
                try {
                    String groupName = webDrv.findElement(By.cssSelector(".p_groups")).getText();
                    for (GroupTroop element : GroupTroop.values()) {
                        if (element.toString().toUpperCase().equals(groupName)) {
                            return element;
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "WARNUNG | Dorf " + ChristopherUtils.getCurrentVillage() + " hat keine Gruppe gesetzt!", "WARNUNG | Dorf " + ChristopherUtils.getCurrentVillage() + " hat keine Gruppe gesetzt!");
                    return null;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return null;
    }

    public static void switchTabs() {
        WebDriver webDrv = WebDriverSingleton.getWebDriver();

        String mainWindow = webDrv.getWindowHandle();
        ChristopherUtils.waitTime(250);
        Set<String> handles = webDrv.getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(mainWindow)) {
                webDrv.switchTo().window(handle);
                System.out.println(webDrv.getCurrentUrl().toString());
            }
            ChristopherUtils.waitTime(125);
            webDrv.switchTo().window(mainWindow);
        }
    }

    public static void switchToTab(int number) {
        WebDriver webDrv = WebDriverSingleton.getWebDriver();

        String mainWindow = webDrv.getWindowHandle();
        ChristopherUtils.waitTime(125);
//        List<String> handles = (List<String>) webDrv.getWindowHandles();
//        LinkedHashSet<String> handles = (List<String>) webDrv.getWindowHandles();
        LinkedHashSet<String> handles = (LinkedHashSet<String>) webDrv.getWindowHandles();
        List<String> tabs = new ArrayList<>(handles);
        ChristopherUtils.waitTime(125);

        if (handles.size() < number) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "Es sind nur " + handles.size() + " Tabs vorhanden, kann nicht in Tab " + number + " wechseln!");
            return;
        }
        webDrv.switchTo().window(tabs.get(number));
    }

    public static void openNewTab() {
        try {
            WebDriver webDrv = WebDriverSingleton.getWebDriver();
            String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL, Keys.RETURN);
            webDrv.findElement(By.cssSelector("a.nowrap")).sendKeys(selectLinkOpeninNewTab);
            ChristopherUtils.waitTime(500);
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean retryingFindClick(WebElement button) {
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
