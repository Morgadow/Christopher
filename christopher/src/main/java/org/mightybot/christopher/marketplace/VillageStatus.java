/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.marketplace;

import org.mightybot.christopher.Village;

/**
 *
 * @author Simon
 */
public class VillageStatus {
    private final Village village;
    private RessourcePack ressources;
    private int meanRessources;
    private RessourcePack diffFromMean;
    private int carrier;

    public VillageStatus(Village village, RessourcePack ressources, int meanRessources, RessourcePack diffFromMean, int carrier) {
        this.village = village;
        this.ressources = ressources;
        this.meanRessources = meanRessources;
        this.diffFromMean = diffFromMean;
        this.carrier = carrier;
    }

    public void setRessources(RessourcePack ressources) {
        this.ressources = ressources;
    }

    public void setMeanRessources(int meanRessources) {
        this.meanRessources = meanRessources;
    }

    public void setDiffFromMean(RessourcePack diffFromMean) {
        this.diffFromMean = diffFromMean;
    }

    public void setCarrier(int carrier) {
        this.carrier = carrier;
    }

    public Village getVillage() {
        return village;
    }

    public RessourcePack getRessources() {
        return ressources;
    }

    public int getMeanRessources() {
        return meanRessources;
    }

    public RessourcePack getDiffFromMean() {
        return diffFromMean;
    }

    public int getCarrier() {
        return carrier;
    }

    
    
}
