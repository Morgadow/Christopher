/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.timer;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mightybot.christopher.GroupTroop;
import org.mightybot.christopher.Village;
import org.mightybot.christopher.WebDriverSingleton;
import org.mightybot.christopher.execution.CheckBotProtectionTask;
import org.mightybot.christopher.execution.FarmTopToBottomTask;
import org.mightybot.christopher.execution.LoginTask;
import org.mightybot.christopher.utils.ChristopherUtils;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Simon
 */
public class MainFarmTimerTask extends TimerTask {

    @Override
    public void run() {
        init();

        boolean onlyOff = true;

        if (onlyOff) {

            System.out.println("FARM | Farme nur OFF Dörfer:");
            int offVills = ChristopherUtils.selectAllVillagesFromGroup(GroupTroop.OFF);
            if (offVills > 0) {
//                farmCurrentSelection(GroupTroop.OFF);
                farmCurrentSelection(null);
            }

        } else {
            System.out.println("FARM | Farme OFF Dörfer:");
            int offVills = ChristopherUtils.selectAllVillagesFromGroup(GroupTroop.OFF);
            if (offVills > 0) {
                farmCurrentSelection(GroupTroop.OFF);
//            farmCurrentSelection(null);
            }

            System.out.println("FARM | Farme DEFF Dörfer:");
            int deffVills = ChristopherUtils.selectAllVillagesFromGroup(GroupTroop.DEFF_NORMAL);
            if (deffVills > 0) {
                farmCurrentSelection(GroupTroop.DEFF_NORMAL);
            }
        }

        System.out.println("FARM | Alle Dörfert gefarmt, gehe schlafen ...");
    }

    private void farmCurrentSelection(GroupTroop groupTroop) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        try {
            Village startVill = ChristopherUtils.getCurrentVillage();
            while (true) {
                System.out.println("FARM | Farme Dorf: " + ChristopherUtils.getCurrentVillage());
                Future<String> future = executorService.submit(new FarmTopToBottomTask(groupTroop));
                future.get(); // blocks until finished
                Future<Boolean> result = executorService.submit(new CheckBotProtectionTask());

                if (result.get()) {
                    WebDriverSingleton.getWebDriver().navigate().refresh();
                    ChristopherUtils.waitTime(250);
                    Future<Boolean> loginResult = executorService.submit(new LoginTask());
                    loginResult.get();
                }

                ChristopherUtils.nextVillage();
                ChristopherUtils.waitTime(250);
                if (startVill.getCoords().equals(ChristopherUtils.getCurrentVillage().getCoords())) {
                    ChristopherUtils.goToVillage(null, ChristopherUtils.getCurrentVillage().getCoords());
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init() {
        try {
            WebDriver webDrv = WebDriverSingleton.getWebDriver();
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.INFO, "Beginne mit MainFarmTimerTask:");

            webDrv.navigate().refresh();
            ChristopherUtils.waitTime(500);

            Future<String> future = executorService.submit(new LoginTask());
            future.get(); // blocks until finished            
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
