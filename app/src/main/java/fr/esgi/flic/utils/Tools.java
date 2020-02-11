package fr.esgi.flic.utils;

import android.util.Log;

public class Tools {

    public static String notificationSwitch(String type, String value) {
        switch (type) {
            case "headphone":
                return headphoneSwitch(value);
            case "localisation":
                return getLocalisationURL(value);
            case "state":
                return stateSwitch(value);
            default:
                Log.d("Tools", "Type de notification inconnue");
                return "Notification inconnue";
        }
    }

    public static String titleSwitch(String title) {
        switch (title) {
            case "headphone":
                return "Écouteurs";
            case "localisation":
                return "Emplacement";
            case "state":
                return "Type de mouvement";
            default:
                return "Type de notification inconnu";
        }
    }

    public static String stateSwitch(String value) {

        switch (value) {
            case "STILL":
                return "Aucun mouvement";
            case "IN_VEHICLE":
                return "En voiture";
            case "ON_BICYCLE":
                return "En vélo";
            case "ON_FOOT":
                return "À pied";
            case "RUNNING":
                return "Course à pied";
            case "TILTING":
                return "À l'envers";
            case "UNKNOWN":
                return "Impossible de détecter le mouvement";
            case "WALKING":
                return "Marche à pied";
            default:
                return "Mouvement inconnu";
        }
    }

    public static String headphoneSwitch(String value) {
        if(value.equalsIgnoreCase("yes")) {
            return "Branché";
        }
        else if(value.equalsIgnoreCase("no")) {
            return "Débranché";
        }
        else {
            return "État des écouteurs inconnu";
        }
    }

    public static String getLocalisationURL(String value) {
        if(value.length() < 29) {
            Log.d("Tools", "Can't get localisation from value : string too short");
            return "";
        }
        String latitude = value.substring(4, 14);
        String longitude = value.substring(20, 29);
        return "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
    }

    public static int min(int to_test, int min) {
        if(to_test <= min)
            return to_test;
        else
            return min;
    }
}
