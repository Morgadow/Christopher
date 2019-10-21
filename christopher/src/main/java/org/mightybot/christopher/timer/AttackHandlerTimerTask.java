/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.timer;

import org.mightybot.christopher.data.GlobalData;
import org.mightybot.christopher.data.UnitRuntime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mightybot.christopher.Coordinate;
import org.mightybot.christopher.Time;
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
public class AttackHandlerTimerTask extends TimerTask {

    @Override
    public void run() {

        WebDriver webDrv = WebDriverSingleton.getWebDriver();

        System.out.println("gefunden: " + webDrv.findElement(By.cssSelector("#incomings_amount")).getText());
        System.out.println("gespeichert: " + GlobalData.getAmountIncomingAttacks());

        if (!"".equals(webDrv.findElement(By.cssSelector("#incomings_amount")).getText())) { // wenn eintreffender Angriff
            ChristopherUtils.waitTime(125);
            if (GlobalData.getAmountIncomingAttacks() > Integer.parseInt(webDrv.findElement(By.cssSelector("#incomings_amount")).getText())) { // weniger angriffe als gespeichert
                System.out.println("ATTACKHANDLER | Aktuell " + webDrv.findElement(By.cssSelector("#incomings_amount")).getText() + " Angriffe, waren davor " + GlobalData.getAmountIncomingAttacks());
                GlobalData.setAmountIncomingAttacks(Integer.parseInt(webDrv.findElement(By.cssSelector("#incomings_amount")).getText()));
            }
            if (GlobalData.getAmountIncomingAttacks() < Integer.parseInt(webDrv.findElement(By.cssSelector("#incomings_amount")).getText())) { // neue Angriffe reingekommen
                System.out.println("ATTACKHANDLER | " + (Integer.parseInt(webDrv.findElement(By.cssSelector("#incomings_amount")).getText()) - GlobalData.getAmountIncomingAttacks()) + " neue Angriffe gefunden!");
                GlobalData.setAmountIncomingAttacks(Integer.parseInt(webDrv.findElement(By.cssSelector("#incomings_amount")).getText()));
                renameAttacks(webDrv);
            }
            System.out.println("ATTACKHANDLER | " + webDrv.findElement(By.cssSelector("#incomings_amount")).getText() + " Angriffe gefunden!");
        }
    }

    private void renameAttacks(WebDriver webDrv) {
        try {
            if (!webDrv.findElements(By.cssSelector("#incomings_table > tbody > tr:nth-child(1) > th:nth-child(1)")).isEmpty()) { // wenn noch nicht in der angriffsübersicht
                webDrv.findElement(By.cssSelector("#incomings_amount")).click();
                ChristopherUtils.waitTime(125);
            }
            webDrv.findElement(By.xpath("//*[contains(text(), 'alle')]")).click(); // alle dörfer wählen
            if (webDrv.findElements(By.cssSelector("td.selected:nth-child(3) > a:nth-child(1)")).isEmpty()) { // falls nicht "Nicht-Ignorierte" angewählt ist
                webDrv.findElement(By.cssSelector(("table.vis:nth-child(6) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(3) > a:nth-child(1)"))).click();
                ChristopherUtils.waitTime(125);
            }
            if (webDrv.findElements(By.cssSelector("td.selected:nth-child(2) > a:nth-child(1)")).isEmpty()) { // falls nicht "Angriffe" gewählt sind
                webDrv.findElement(By.cssSelector("table.vis:nth-child(7) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > a:nth-child(1)")).click();
                ChristopherUtils.waitTime(125);
            }

            List<WebElement> incomingTable = webDrv.findElement(By.cssSelector("#incomings_table")).findElements(By.className("quickedit-label"));

            for (int i = 0; i < incomingTable.size(); i++) {
                if (incomingTable.get(i).getText().equals("Angriff")) {

                    // extract coords from start village and target village
                    String destRaw = webDrv.findElement(By.cssSelector("tr.nowrap:nth-child(" + (i + 2) + ") > td:nth-child(2) > a:nth-child(1)")).getText(); // Ziel
                    String sourceRaw = webDrv.findElement(By.cssSelector("tr.nowrap:nth-child(" + (i + 2) + ") > td:nth-child(3) > a:nth-child(1)")).getText(); // Herkunft

                    String[] destBuf = destRaw.split(" ");
                    String[] dest = destBuf[destBuf.length - 2].replace("(", "").replace(")", "").split(" ")[0].split("\\|");
                    String[] sourceBuf = sourceRaw.split(" ");
                    String[] source = sourceBuf[sourceBuf.length - 2].replace("(", "").replace(")", "").split(" ")[0].split("\\|");

                    // extract remaining attack time
                    String[] remTimeBuf = webDrv.findElement(By.cssSelector("tr.nowrap:nth-child(" + (i + 2) + ") > td:nth-child(7) > span:nth-child(1)")).getText().split(":");
                    Time remTime = new Time(Integer.parseInt(remTimeBuf[0]), Integer.parseInt(remTimeBuf[1]), Integer.parseInt(remTimeBuf[2]), 0);

                    // extract attacker name and source village name
                    String attacker = webDrv.findElement(By.cssSelector("tr.nowrap:nth-child(" + (i + 2) + ") > td:nth-child(4) > a:nth-child(1)")).getText();
                    sourceBuf = Arrays.copyOfRange(sourceBuf, 0, sourceBuf.length - 2);
                    String attackVill = String.join(" ", sourceBuf);

                    Map<UnitType, Integer> unitRuntimes = UnitRuntime.getUnitRunTimes();

                    for (Map.Entry<UnitType, Integer> unit : unitRuntimes.entrySet()) {

                        Time calculatedTime = ChristopherUtils.calculateUnitRuntime(new Coordinate(Integer.parseInt(source[0]), Integer.parseInt(source[1])), new Coordinate(Integer.parseInt(dest[0]), Integer.parseInt(dest[1])), unit.getValue());
                        int diff = (calculatedTime.getHour() - remTime.getHour()) * 60 + (calculatedTime.getMinute() - remTime.getMinute()) + (calculatedTime.getSecond() - remTime.getSecond()) / 60;

//                        int duration = calculatedTime.getHour() * 60 + calculatedTime.getMinute() + calculatedTime.getSecond();
//                        if (diff < 0.05*duration) {
                        if (Math.abs(diff) < 5) {
                            // Rename attack: UNITTYPE, Attacker, attacking Village
                            String nameAttack = (unit.getKey()).toString().toUpperCase() + ", " + attacker + ", " + attackVill;

                            webDrv.findElement(By.cssSelector("tr.nowrap:nth-child(" + (i + 2) + ") > td:nth-child(1) > span:nth-child(3) > span:nth-child(1) > a:nth-child(2)")).click();
                            webDrv.findElement(By.cssSelector(".quickedit-edit > input:nth-child(1)")).clear();
                            webDrv.findElement(By.cssSelector(".quickedit-edit > input:nth-child(1)")).sendKeys(nameAttack);
                            webDrv.findElement(By.cssSelector(".quickedit-edit > input:nth-child(2)")).click();

                            System.out.println("ATTACKHANDLER | Unit: " + unit.getKey().toString().toUpperCase() + " Diff: " + diff + " Renamed attack to: " + nameAttack);
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
