/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.marketplace;

import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mightybot.christopher.Village;
import org.mightybot.christopher.WebDriverSingleton;
import org.mightybot.christopher.execution.LoginTask;
import org.mightybot.christopher.utils.ChristopherUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Simon
 */
// TODO Unterstützung für mehr als 100 Dörfer einbauen, passen nicht mehr auf eine Seite
public class DistributeRessourcesMainTask extends TimerTask {

    @Override
    public void run() {

        init(); // login und so Sachen

        goToProductionOverview();
        setPageSize(100); // 100 Dörfer pro Seite

        List<VillageStatus> statusQuo = readStatusQuo();
        if (statusQuo == null) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "DISTRIBUTERESSOURCES | statusQuo ist null!");
            return;
        }
        
        List<Transport> transports = calculateTransports(statusQuo);
        if (transports == null) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "DISTRIBUTERESSOURCES | transports ist null!");
            return;
        }

    }

    private void init() {
        try {
            WebDriver webDrv = WebDriverSingleton.getWebDriver();
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.INFO, "Beginne mit Ausgleichen der Rohstoffe:");

            webDrv.navigate().refresh(); // falls session abgelaufen
            ChristopherUtils.waitTime(500);

            Future<String> future = executorService.submit(new LoginTask());
            future.get(); // blocks until finished            
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "DISTRIBUTERESSOURCES | Exception beim Init!", ex);
        }
    }

    private static void goToProductionOverview() {
        try {
            WebDriver webDrv = WebDriverSingleton.getWebDriver();
            ChristopherUtils.retryingFindClick(webDrv.findElement(By.cssSelector("td.menu-item:nth-child(2) > a:nth-child(1)"))); // übersichten seite
            if (webDrv.findElements(By.cssSelector("td.selected > a:nth-child(1)")).size() == 0) {
                ChristopherUtils.retryingFindClick(webDrv.findElement(By.cssSelector("#overview_menu > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > a:nth-child(1)")));
                ChristopherUtils.waitTime(250);
            }
            webDrv.findElement(By.xpath("//*[contains(text(), 'alle')]")).click(); // alle dörfer wählen
            ChristopherUtils.waitTime(250);
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "DISTRIBUTERESSOURCES | Exception bei goToProductionOverview!", ex);
        }
    }

    private static void setPageSize(int pageSize) {
        try {
            String pageSizeStr = String.valueOf(pageSize);
            WebDriver webDrv = WebDriverSingleton.getWebDriver();
            if (!webDrv.findElement(By.id("#page_size")).getAttribute("value").equals(pageSizeStr)) {
                webDrv.findElement(By.id("#page_size")).clear();
                webDrv.findElement(By.id("#page_size")).sendKeys(pageSizeStr);
                ChristopherUtils.retryingFindClick(webDrv.findElement(By.cssSelector(".btn")));
            }
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "DISTRIBUTERESSOURCES | Exception beim Setzen der setPageSize!", ex);
        }
    }

    private List<VillageStatus> readStatusQuo() {
        try {
            WebDriver webDrv = WebDriverSingleton.getWebDriver();
            List<WebElement> villages = webDrv.findElement(By.xpath("//*[@id=\"production_table\"]")).findElements(By.cssSelector("[id^=village_]"));

            List<VillageStatus> statusQuo = new LinkedList<>();

            for (int i = 0; i < villages.size(); i++) {

                // get village name and or coords
                String villRaw = webDrv.findElement(By.cssSelector("TODO")).getText(); // TODO anpassen
                String[] vill = villRaw.replace("(", "").replace(")", "").split(" ")[0].split("\\|");
                Village currVill = new Village(null, Integer.parseInt(vill[0]), Integer.parseInt(vill[1]));

                // get ressources
                RessourcePack currRes = new RessourcePack(wood, stone, iron);

                // calculate mean ressources
                int meanRes = (wood + stone + iron) / 3;

                // calculate diffs 
                RessourcePack currDiffs = new RessourcePack(wood - meanRes, stone - meanRes, iron - meanRes);

                // get availablecarrier
                int carrier = Integer.parseInt(webDrv.findElement(By.cssSelector("TODO")).getText()); // TODO anpassen

                // hinzufügen zu Liste
                statusQuo.add(new VillageStatus(currVill, currRes, meanRes, currDiffs, carrier));
            }
            return statusQuo;
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "DISTRIBUTERESSOURCES | Exception beim Auslesen von Status Quo!", ex);
        }
        return null;
    }

    private List<Transport> calculateTransports(List<VillageStatus> statusQuo) {
        try {

        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, "DISTRIBUTERESSOURCES | Exception beim Berechnen der besten Transporte!", ex);
        }
        return null;
    }

}
