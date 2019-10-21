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
public class Unit {
    private final String name;
    private final int costs_wood;
    private final int costs_stone;
    private final int costs_iron;
    private final int unit_attack;
    private final int unit_defense;
    private final int unit_defense_cavalry;
    private final int unit_pop;
    private final int unit_speed;
    private final int unit_carry;
    
    public Unit (String uname, int woodcost, int stonecost, int ironcost, int attack, int defense, int cavdefense, int population, int speed, int carry) {
        name = uname;
        costs_wood = woodcost;
        costs_stone = stonecost;
        costs_iron = ironcost;
        unit_attack = attack;
        unit_defense = defense;
        unit_defense_cavalry = cavdefense;
        unit_pop = population;
        unit_speed = speed;
        unit_carry = carry;        
    }

    
    
    
    
}
