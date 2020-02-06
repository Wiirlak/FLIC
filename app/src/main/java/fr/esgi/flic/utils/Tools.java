package fr.esgi.flic.utils;

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
}
