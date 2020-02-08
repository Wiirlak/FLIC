package fr.esgi.flic.utils;

import android.util.Log;

public class Tools {

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
        String latitude = value.substring(4, 14);
        String longitude = value.substring(20, 29);
        // Log.d("getLocalisationURL", "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude);
        return "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
    }
}
