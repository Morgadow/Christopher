/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher;

/**
 *
 * @author wiesbob
 */
public enum UnitType {
    SPEAR, SWORD, AXE, SPY, LIGHT, HEAVY, RAM, CATAPULT, SNOB;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
    
    
}
