package fr.esgi.flic.utils;

public class StateUtils {
    public static String returnStateToString(int state) {

        switch (state) {
            case 0:
                return "IN VEHICULE";
            case 1:
                return "ON BICYCLE";
            case 2:
                return "ON FOOT";
            case 3:
                return "STILL";
            case 5:
                return "TILTING";
            case 7:
                return "WALKING";
            case 8:
                return "RUNNING";
            default:
                break;
        }


        return "";
    }
}
