/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.marketplace;

/**
 *
 * @author Simon
 */
public class RessourcePack {

    private int wood;
    private int stone;
    private int iron;

    public RessourcePack(int wood, int stone, int iron) {
        this.wood = wood;
        this.stone = stone;
        this.iron = iron;
    }

    @Override
    public String toString() {
        String retVal = "RessourcePack:";
        if ((wood == 0) & (stone == 0) & (iron == 0)) {
            retVal = retVal + " ----";
        } else {
            if (wood != 0) {
                retVal = retVal + " Wood: " + wood;
            }
            if (stone != 0) {
                retVal = retVal + " Stone: " + stone;
            }
            if (iron != 0) {
                retVal = retVal + "Iron: " + iron;
            }
        }
        return retVal;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public void setIron(int iron) {
        this.iron = iron;
    }

    public int getWood() {
        return wood;
    }

    public int getStone() {
        return stone;
    }

    public int getIron() {
        return iron;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.wood;
        hash = 59 * hash + this.stone;
        hash = 59 * hash + this.iron;
        return hash;
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
        final RessourcePack other = (RessourcePack) obj;
        if (this.wood != other.wood) {
            return false;
        }
        if (this.stone != other.stone) {
            return false;
        }
        if (this.iron != other.iron) {
            return false;
        }
        return true;
    }

}
