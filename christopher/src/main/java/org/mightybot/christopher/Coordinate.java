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
public class Coordinate {

    private final int xcoord;
    private final int ycoord;

    public Coordinate (int x, int y) {
        xcoord = x;
        ycoord = y;
//        System.out.println("TODO | coord hashcode und equals umschreiben! Coordinate");
    }

    @Override
    public String toString() {
        return "(" + xcoord + "|" + ycoord + ")";
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
        final Coordinate other = (Coordinate) obj;
        if (this.xcoord != other.xcoord) {
            return false;
        }
        if (this.ycoord != other.ycoord) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode(); 
    }

    public int getXcoord() {
        return xcoord;
    }

    public int getYcoord() {
        return ycoord;
    }

    
}
