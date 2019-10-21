/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.timer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mightybot.christopher.GroupTroop;
import org.mightybot.christopher.Village;
import org.mightybot.christopher.WebDriverSingleton;
import org.mightybot.christopher.execution.BBWallBashTask;
import org.mightybot.christopher.execution.LoginTask;
import org.mightybot.christopher.execution.WorkExecutioner;
import org.mightybot.christopher.utils.ChristopherUtils;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Simon
 */
public class MainBBWallBashTimerTask extends TimerTask {

    @Override
    public void run() {

        init();

        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            int offVills = ChristopherUtils.selectAllVillagesFromGroup(GroupTroop.OFF);
            if (offVills > 0) {

                List<Village> wallBashedVills = new LinkedList<>();

                Village startVill = ChristopherUtils.getCurrentVillage();
                ChristopherUtils.nextVillage();
                ChristopherUtils.waitTime(250);
                while (!ChristopherUtils.getCurrentVillage().getCoords().equals(startVill.getCoords())) {
                    Future<LinkedList<Village>> bashedVills = executorService.submit(new BBWallBashTask(wallBashedVills));
                    bashedVills.get(); // blocks until finished
                    ChristopherUtils.nextVillage();
                    ChristopherUtils.waitTime(250);
                }
                ChristopherUtils.goToVillage(null, ChristopherUtils.getCurrentVillage().getCoords());
                System.out.println("wallbash aus allen dörfern, gehe schlafen ...");
            }
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init() {
        try {
            WebDriver webDrv = WebDriverSingleton.getWebDriver();
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.INFO, "Beginne mit Wallbashing aus allen Off Dörfern:");

            webDrv.navigate().refresh();
            ChristopherUtils.waitTime(500);

            Future<String> future = executorService.submit(new LoginTask());
            future.get();
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
