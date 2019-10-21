/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.utils;

import org.mightybot.christopher.WebDriverSingleton;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Simon
 */
public enum ScreenType {
    WELCOME, OVERVIEW, OVERVIEW_VILLAGES, MAP, REPORT, MAIL, RANKING, ALLY, INFO_PLAYER, PREMIUM, SETTINGS, MEMO;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
    
    public static ScreenType getScreenType() {

        WebDriver webDrv = WebDriverSingleton.getWebDriver();
        String[] currURLArr = webDrv.getCurrentUrl().split("&");

        int index = 0;
        for (String element : currURLArr) {
            if (element.contains("screen")) {
                String screenPart = currURLArr[index];
                int screenIndex = index;
                System.out.println(screenPart);
                System.out.println(screenIndex);
                String[] buffer = screenPart.split("=");
                System.out.println(buffer);
                String screen = buffer[buffer.length - 1];
                System.out.println(screen);
                for (ScreenType type : ScreenType.values()) {
                    if (type.toString() == screen) {

                        return type;
                    }
                }
            }
        }
        return null;
    }
    
}
