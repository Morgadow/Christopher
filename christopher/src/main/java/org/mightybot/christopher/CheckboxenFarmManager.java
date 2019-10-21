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
public enum CheckboxenFarmManager {
    ALL_VILLAGE_CHECKBOX, ATTACKED_CHECKBOX, FULL_HAULS_CHECKBOX, FULL_LOSSES_CHECKBOX, PARTIAL_LOSSES_CHECKBOX;
      
    // ALL_VILLAGE_CHECKBOX  Zeige nur Angriffe, die von diesem Dorf ausgingen
    // ATTACKED_CHECKBOX  Füge Berichte von Dörfern, welche du gerade angreifst, bei
    // FULL_HAULS_CHECKBOX  Nur Berichte anzeigen, in welchen deine Truppen die volle Tragekapazität ausschöpfen konnten
    // FULL_LOSSES_CHECKBOX  Berichte vollständiger Verluste beifügen
    // FULL_PARTIAL_CHECKBOX  Berichte teilweiser Verluste beifügen

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
    
    

}
