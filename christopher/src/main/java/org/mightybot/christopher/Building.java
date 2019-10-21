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
public enum Building {
    MAIN, BARRACKS, STABLE, GARAGE, SNOB, SMITH, PLACE, MARKET, WOOD, STONE, IRON, FARM, STORAGE, HIDE, WALL;
    
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
