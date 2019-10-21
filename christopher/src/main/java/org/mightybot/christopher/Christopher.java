/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mightybot.christopher.execution.LoginTask;
import org.mightybot.christopher.marketplace.DistributeRessourcesMainTask;
import org.mightybot.christopher.timer.AttackHandlerTimerTask;
import org.mightybot.christopher.timer.MainBBWallBashTimerTask;
import org.mightybot.christopher.timer.MainFarmTimerTask;
import org.mightybot.christopher.utils.ChristopherUtils;

/**
 *
 * @author wiesbob
 */
public class Christopher {

    public static void main(String[] args) {

        // initialize gecko driver
        ChristopherUtils.initialize();

//        while (true) {
//            ChristopherUtils.farmAllVillagesTopToBottom(166, "Christopher", "christopher1337");
////            ChristopherUtils.farmAllVillagesWithGroups(166, "Christopher", "christopher1337");
//        }
        Timer timer = new Timer();
        TimerTask FarmTask = new MainFarmTimerTask();
        TimerTask AttackHandler = new AttackHandlerTimerTask();
        TimerTask BBWallBashTask = new MainBBWallBashTimerTask();
        TimerTask DistributeRessources = new DistributeRessourcesMainTask();

        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<String> future = executorService.submit(new LoginTask());
            future.get(); // blocks until finished           ChristopherUtils.waitTime(5000);
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.WARNING, null, ex);
        }
        

        timer.scheduleAtFixedRate(FarmTask, 0, 15 * 60000);
//        timer.scheduleAtFixedRate(AttackHandler, 0, 5000);
//        timer.scheduleAtFixedRate(BBWallBashTask, 0, 4 * 60 * 6000);
//        timer.scheduleAtFixedRate(DistributeRessources, 0, 6 * 60 * 60000);
    }
}
