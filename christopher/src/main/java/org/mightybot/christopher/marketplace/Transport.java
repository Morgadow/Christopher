/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.marketplace;

import java.util.Objects;
import org.mightybot.christopher.Village;

/**
 *
 * @author Simon
 */
public class Transport {

    private Village sourceVill;
    private Village destVill;
    private RessourcePack ressources;

    public Transport(Village sourceVill, Village destVill, RessourcePack ressources) {
        this.sourceVill = sourceVill;
        this.destVill = destVill;
        this.ressources = ressources;
    }

    @Override
    public String toString() {
        return "Transport from " + sourceVill + " to " + destVill + ", " + ressources + '}';
    }

    public void setSourceVill(Village sourceVill) {
        this.sourceVill = sourceVill;
    }

    public void setDestVill(Village destVill) {
        this.destVill = destVill;
    }

    public void setRessources(RessourcePack ressources) {
        this.ressources = ressources;
    }

    public Village getSourceVill() {
        return sourceVill;
    }

    public Village getDestVill() {
        return destVill;
    }

    public RessourcePack getRessources() {
        return ressources;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.sourceVill);
        hash = 43 * hash + Objects.hashCode(this.destVill);
        hash = 43 * hash + Objects.hashCode(this.ressources);
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
        final Transport other = (Transport) obj;
        if (!Objects.equals(this.sourceVill, other.sourceVill)) {
            return false;
        }
        if (!Objects.equals(this.destVill, other.destVill)) {
            return false;
        }
        if (!Objects.equals(this.ressources, other.ressources)) {
            return false;
        }
        return true;
    }

}
