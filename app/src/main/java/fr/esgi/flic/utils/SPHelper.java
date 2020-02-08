package fr.esgi.flic.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class SPHelper {

    //Usage : saveObjectToSharedPreference(context, "mPreference", "mObjectKey", mObject);
    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();
    }

    public static void saveUserToSharedPreference(Context context,  Object object) {
        String preferenceFileName = "FLIC_user";
        String serializedObjectKey = "User";
        saveObjectToSharedPreference(context, preferenceFileName, serializedObjectKey, object);
    }

    //Usage : getSavedObjectFromPreference(context, "mPreference", "mObjectKey", (Type) SampleClass.class)
    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Type classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }

    public static <GenericClass> GenericClass getSavedUserFromPreference(Context context, Type classType) {
        String preferenceFileName = "FLIC_user";
        String preferenceKey = "User";
        return getSavedObjectFromPreference(context, preferenceFileName, preferenceKey, classType);
    }
}
