/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.data;

/**
 *
 * @author Simon
 */
public class LoginData {

    public static int worldNumber;
    public static String userName;
    public static String userPassword;

    public LoginData() {
    }

    @Override
    public String toString() {
        return "Login Daten: Welt: " + this.worldNumber + ", LoginID: " + this.userName + ", Passwort: " + userPassword;
    }

    public static void setWorldNumber(int worldNumber) {
        LoginData.worldNumber = worldNumber;
    }

    public static void setUserName(String userName) {
        LoginData.userName = userName;
    }

    public static void setUserPassword(String userPassword) {
        LoginData.userPassword = userPassword;
    }

    public static int getWorldNumber() {
        return worldNumber;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getUserPassword() {
        return userPassword;
    }

}
