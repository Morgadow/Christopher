package org.mightybot.christopher.data;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package data;
//
//import java.io.File;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.HashMap;
//
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import jdk.nashorn.internal.parser.JSONParser;
//import org.mightybot.christopher.Unit;
//import org.mightybot.christopher.utils.ChristopherUtils;
//
///**
// *
// * @author Simon
// */
//public class JsonReader {
//
//    public static void readConfigJSON() {
//
//        String file = "";
//        
//
//
//
//
//
//
//    public static void readUnitAttributes() {
//        
//        String folder = System.getProperty("user.dir");
//        Path filePath = Paths.get(System.getProperty("user.dir") + "/resources/troops.json");
//        String file = "/ressources/troops.json";
//        
//        HashMap<String,Object> result = new ObjectMapper().readValue(file, HashMap.class);
//    }
//    
//    
//
//    public static boolean StringToBool(String string) {
//        try {
//            if (string.toUpperCase() == "TRUE") {
//                return true;
//            }
//            if (string.toUpperCase() == "FALSE") {
//                return false;
//            }
//        } catch (Exception ex) {
//            System.out.println("FEHLER | Konnte " + string + " nicht in einen boolean wandeln!");
//            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
//    }
//
//    // TODO löschen, java hat sowas bereits
//    public static int StringToInt(String string) {
//        try {
//            return Integer.parseInt(string);
//        } catch (Exception ex) {
//            System.out.println("FEHLER | Konnte " + string + " nicht in einen Integer wandeln!");
//            Logger.getLogger(ChristopherUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return 0;
//    }
//
//}
