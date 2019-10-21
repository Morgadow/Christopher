/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.execution;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mightybot.christopher.WebDriverSingleton;
import org.mightybot.christopher.data.LoginData;
import org.mightybot.christopher.utils.ChristopherUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Simon
 */
public class LoginTask implements Callable {

    private final WebDriver webDrv;
    private final int world;
    private final String userName;
    private final String userPassword;

    public LoginTask() {
        this(WebDriverSingleton.getWebDriver());
    }

    public LoginTask(WebDriver webDrv) {
        this.webDrv = webDrv;
        world = LoginData.getWorldNumber();
        userName = LoginData.getUserName();
        userPassword = LoginData.getUserPassword();
    }

    public LoginTask(String username, String password, int world, WebDriver webDrv) {
        this.webDrv = webDrv;
        this.world = world;
        this.userName = username;
        userPassword = password;
    }

    @Override
    public Boolean call() throws Exception { // TODO rückgabewert sinnlos, aber muss wohl so erstmal gemacht werden, konzept überlegen!

        try {
            // Falls schon eingeloggt
            if (webDrv.getCurrentUrl().contains("https://de" + world + ".")) { // dann schon eingeloggt
                Logger.getLogger(ChristopherUtils.class.getName()).log(Level.FINEST, "LOGIN | War schon in einem Account eingeloggt!");
                return true;
            }

            if (webDrv.getCurrentUrl().contains("session_expired=1")) {
                System.out.println("LOGIN | Session abgelaufen, logge neu ein!");
                logIntoSession();
                return true;
            }

            // Falls neue Marionette
            webDrv.get("https://www.die-staemme.de");
            logIntoAccount();
            logIntoSession();
            return true;

        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private void logIntoAccount() {
        // Logge in Account ein
        try {
            if (webDrv.findElements(By.cssSelector("#user")).size() != 0) {
                webDrv.findElement(By.cssSelector("#user")).sendKeys(userName);
                webDrv.findElement(By.cssSelector("#password")).sendKeys(userPassword);
                webDrv.findElement(By.cssSelector(".btn-login")).click();
                Logger.getLogger(ChristopherUtils.class.getName()).log(Level.INFO, "LOGIN | Logge in Account " + userName + " ein!");
                ChristopherUtils.waitTime(1000);
            }
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void logIntoSession() {
        // Welt auswählen und in Session einloggen
        try {
            List<WebElement> worlds = webDrv.findElements(By.className("world_button_active"));
            boolean worldAvailable = false;
            for (WebElement element : worlds) {
                if (element.getText().equals("Welt " + world)) {
                    element.click();
                    Logger.getLogger(ChristopherUtils.class.getName()).log(Level.FINER, "LOGIN | Logge in Account " + userName + " auf Welt " + world + " ein!");
                    worldAvailable = true;
                    break;
                }
            }
            if (!worldAvailable) {
                throw new IllegalArgumentException("World " + world + " is not active!");
            }
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        ChristopherUtils.waitTime(1000);
    }

    private void anotherAccount() { // Unused und nicht gedebuggt!!!
        // Notfalls umloggen wenn falscher Account
        try {
            String welcome = webDrv.findElement(By.cssSelector("div.wrap:nth-child(2) > h2:nth-child(2)")).getText().toLowerCase();
            if (!welcome.contains(userName.toLowerCase())) { // falls nicht der richtige account, ausloggen und neu einloggen
                Logger.getLogger(ChristopherUtils.class.getName()).log(Level.INFO, "LOGIN | In einem anderen Account als " + userName + " eingeloggt, logge aus");
                WorkExecutioner worker = new WorkExecutioner();
                worker.addToQueue(new LogoutTask());
                ChristopherUtils.waitTime(500);
                webDrv.findElement(By.cssSelector("#user")).sendKeys(userName);
                webDrv.findElement(By.cssSelector("#password")).sendKeys(userPassword);
                webDrv.findElement(By.cssSelector(".btn-login")).click();
                Logger.getLogger(ChristopherUtils.class.getName()).log(Level.FINEST, "LOGIN | Logge in Account " + userName + " ein!");
                ChristopherUtils.waitTime(500);
            }
        } catch (Exception ex) {
            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
