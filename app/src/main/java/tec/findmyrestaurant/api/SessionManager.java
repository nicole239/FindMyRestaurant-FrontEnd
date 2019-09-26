package tec.findmyrestaurant.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.message.BasicHeader;
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
    public static User getUser(Context context){
        String email = getUserEmail(context);
        User user = new User();
        user.setEmail(email);
        return user;
    }
    public static String getToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("tec.findmyrestaurant",Context.MODE_PRIVATE);
        return sharedPreferences.getString("Token",null);
    }
    public static Header[] getTokenHeader(Context context){
        return new Header[]{new BasicHeader("x-access-token",getToken(context))};
    }
}
