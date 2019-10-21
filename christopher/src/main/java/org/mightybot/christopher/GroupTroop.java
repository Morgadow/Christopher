/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher;

/**
 *
 * @author Simon
 */
public enum GroupTroop {
    OFF, OFF_NOFARM, OFF_AG, OFF_AG_NOFARM, DEFF_NORMAL, DEFF_NORMAL_NOFARM, DEFF_SKAV, DEFF_SKAV_NOFARM, SPY, FARM, RAM;  
    
    // OFF Off mit der gefarmt werden kann (Standard)
    // OFF_noFarm Off mit der aktuell nicht gefarmt werden soll
    // OFF_AG Off mit 4 AGs
    // OFF_AG_noFarm Off mit 4 AGs mit der aktuell nicht gefarmt werden soll
    // DEFF_normal Deff mit ausgewogen Infanterie und Skav (Standard)
    // DEFF_normal_noFarm Deff mit der aktuell nicht gefarmt werden soll
    // DEFF_skav Schnelle Deff mit viel Skav (Hinterland)
    // DEFF_skav_noFarm Schnelle Deff mit viel Skav mit der aktuell nicht gefarmt werden soll(Hinterland)
    // SPY Nur Späher
    // FARM Nur Leichte Kavallerie
    // RAM Nur Rammen

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

}
