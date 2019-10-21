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
public class GlobalData {

    public static int amountIncomingAttacks;
    public static int amountIncomingSupports;


    public GlobalData() {
    }

    public static void setAmountIncomingAttacks(int amountIncomingAttacks) {
        GlobalData.amountIncomingAttacks = amountIncomingAttacks;
    }

    public static void setAmountIncomingSupports(int amountIncomingSupports) {
        GlobalData.amountIncomingSupports = amountIncomingSupports;
    }

    public static int getAmountIncomingAttacks() {
        return amountIncomingAttacks;
    }

    public static int getAmountIncomingSupports() {
        return amountIncomingSupports;
    }


}
