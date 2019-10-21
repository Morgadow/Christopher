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
public class TroopTemplate {

    public static Map<UnitType, Integer> BBWallBashAxe;
    public static Map<UnitType, Integer> BBWallBashLight;
    public static Map<UnitType, Integer> AGwithSupport;
    public static Map<UnitType, Integer> FarmTemplateAOFF;
    public static Map<UnitType, Integer> FarmTemplateBOFF;
    public static Map<UnitType, Integer> FarmTemplateADEFF;
    public static Map<UnitType, Integer> FarmTemplateBDEFF;

    public TroopTemplate() {

        BBWallBashAxe = new HashMap<>();
        BBWallBashLight = new HashMap<>();
        AGwithSupport = new HashMap<>();
        FarmTemplateAOFF = new HashMap<>();
        FarmTemplateBOFF = new HashMap<>();
        FarmTemplateADEFF = new HashMap<>();
//        FarmTemplateBDEFF = new HashMap<>();
        FarmTemplateBDEFF = null;

        BBWallBashAxe.put(UnitType.AXE, 50);
        BBWallBashAxe.put(UnitType.RAM, 5);

        BBWallBashLight.put(UnitType.LIGHT, 5);
        BBWallBashLight.put(UnitType.RAM, 5);

        AGwithSupport.put(UnitType.AXE, 150);
        AGwithSupport.put(UnitType.SPY, 1);
        AGwithSupport.put(UnitType.SNOB, 1);

        FarmTemplateAOFF.put(UnitType.SPEAR, 0);
        FarmTemplateAOFF.put(UnitType.SWORD, 0);
        FarmTemplateAOFF.put(UnitType.AXE, 50);
        FarmTemplateAOFF.put(UnitType.SPY, 0);
        FarmTemplateAOFF.put(UnitType.LIGHT, 0);
        FarmTemplateAOFF.put(UnitType.HEAVY, 0);

        FarmTemplateBOFF.put(UnitType.SPEAR, 0);
        FarmTemplateBOFF.put(UnitType.SWORD, 0);
        FarmTemplateBOFF.put(UnitType.AXE, 0);
        FarmTemplateBOFF.put(UnitType.SPY, 0);
        FarmTemplateBOFF.put(UnitType.LIGHT, 4);
        FarmTemplateBOFF.put(UnitType.HEAVY, 0);

        FarmTemplateADEFF.put(UnitType.SPEAR, 0);
        FarmTemplateADEFF.put(UnitType.SWORD, 0);
        FarmTemplateADEFF.put(UnitType.AXE, 0);
        FarmTemplateADEFF.put(UnitType.SPY, 0);
        FarmTemplateADEFF.put(UnitType.LIGHT, 0);
        FarmTemplateADEFF.put(UnitType.HEAVY, 6);

//        FarmTemplateBDEFF.put(UnitType.SPEAR, 100);
//        FarmTemplateBDEFF.put(UnitType.SWORD, 100);
//        FarmTemplateBDEFF.put(UnitType.AXE, 0);
//        FarmTemplateBDEFF.put(UnitType.SPY, 0);
//        FarmTemplateBDEFF.put(UnitType.LIGHT, 0);
//        FarmTemplateBDEFF.put(UnitType.HEAVY, 0);
    }

    public static Map<UnitType, Integer> getBBWallBashAxe() {
        return BBWallBashAxe;
    }

    public static Map<UnitType, Integer> getBBWallBashLight() {
        return BBWallBashLight;
    }

    public static Map<UnitType, Integer> getAGwithSupport() {
        return AGwithSupport;
    }

    public static Map<UnitType, Integer> getFarmTemplateAOFF() {
        return FarmTemplateAOFF;
    }

    public static Map<UnitType, Integer> getFarmTemplateBOFF() {
        return FarmTemplateBOFF;
    }

    public static Map<UnitType, Integer> getFarmTemplateADEFF() {
        return FarmTemplateADEFF;
    }

    public static Map<UnitType, Integer> getFarmTemplateBDEFF() {
        return FarmTemplateBDEFF;
    }

}
