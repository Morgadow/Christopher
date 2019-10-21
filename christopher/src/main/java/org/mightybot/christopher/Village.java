/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Simon
 */
public class Village {

    public String name;
    public final Coordinate coords;
    public final Continent continent;
    public Map<UnitType, Integer> units;
    public Map<Building, Integer> buildings;

    // TODO !!!
    public Village(String villName, int xCoord, int yCoord) {
        name = villName;
        coords = new Coordinate(xCoord, yCoord);
        int xcont = xCoord / 100;
        int ycont = yCoord / 100;
        continent = new Continent(ycont * 10 + xcont);
        units = new HashMap<>();
        buildings = new HashMap<>();
    }

    @Override
    public String toString() {
        return "Village: " + name + " at coords: " + coords + " on Continent: " + continent;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Village other = (Village) obj;
        if (!Objects.equals(this.coords, other.coords)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getContinent() {
        return continent.getNumber();
    }

    public int getXcoord() {
        return coords.getXcoord();
    }

    public int getYcoord() {
        return coords.getYcoord();
    }
    
    public Coordinate getCoords() {
        return coords;
    }
}
