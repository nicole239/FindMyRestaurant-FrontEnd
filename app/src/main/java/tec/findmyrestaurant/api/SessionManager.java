package tec.findmyrestaurant.api;

import android.content.Context;
import android.content.SharedPreferences;

import tec.findmyrestaurant.model.User;

public class SessionManager {

    public static void saveToken(Context context, User user, String token){
        SharedPreferences sharedPreferences = context.getSharedPreferences("tec.findmyrestaurant",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("Token",token).apply();
        sharedPreferences.edit().putString("User",user.getEmail()).apply();
    }
    public static String getUserEmail(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("tec.findmyrestaurant",Context.MODE_PRIVATE);
        return  sharedPreferences.getString("User",null);
    }
    public static String getToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("tec.findmyrestaurant",Context.MODE_PRIVATE);
        return sharedPreferences.getString("Token",null);
    }
}
