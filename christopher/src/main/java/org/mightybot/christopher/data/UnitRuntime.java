/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.data;

import java.util.HashMap;
import java.util.Map;
import org.mightybot.christopher.UnitType;

/**
 *
 * @author Simon
 */
public class UnitRuntime {

    public static Map<UnitType, Integer> UnitRunTimes;

    public UnitRuntime() {
        UnitRunTimes = new HashMap<>();
        
        UnitRunTimes.put(UnitType.SPEAR, 18);
        UnitRunTimes.put(UnitType.SWORD, 22);
        UnitRunTimes.put(UnitType.AXE, 18);
        UnitRunTimes.put(UnitType.LIGHT, 10);
        UnitRunTimes.put(UnitType.HEAVY, 11);
        UnitRunTimes.put(UnitType.SPY, 9);
        UnitRunTimes.put(UnitType.RAM, 30);
        UnitRunTimes.put(UnitType.CATAPULT, 30); 
        UnitRunTimes.put(UnitType.SNOB, 35);
    }

    public static Map<UnitType, Integer> getUnitRunTimes() {
        return UnitRunTimes;
    }
    
}
