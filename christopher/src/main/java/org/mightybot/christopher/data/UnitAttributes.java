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
public enum UnitAttributes {
    COSTS_WOOD, COSTS_STONE, COSTS_IRON, UNIT_ATTACK, UNIT_DEFENSE, UNIT_DEFENSE_CAVALRY, UNIT_POP, UNIT_SPEED, UNIT_CARRY;

    @Override
    public String toString() {
        return super.toString().toLowerCase(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
