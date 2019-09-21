package tec.findmyrestaurant.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import tec.findmyrestaurant.model.Calification;
import tec.findmyrestaurant.model.Restaurant;

public class CalificationRequest {

    public static void insertCalification(Context context, Calification calification, final Response<Calification> calificationResponse){
        try {
            StringEntity params = new StringEntity(new Gson().toJson(calification));
            HttpClient.post(context,"califications",params,SessionManager.getTokenHeader(context),new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Message message = new Gson().fromJson(response.toString(),Message.class);
                    calificationResponse.onSuccess(message);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Message message = new Gson().fromJson(errorResponse.toString(),Message.class);
                    calificationResponse.onFailure(message);
                }
            });
        }catch (Exception e){
            Log.e("Http-error",e.getMessage());
            Message message = new Message();
            calificationResponse.onFailure(message);
        }
    }
    public static void getCalifications(Context context, int restaurantId,final Response<Calification> calificationResponse){
        HttpClient.get(context,"califications/"+restaurantId,null,SessionManager.getTokenHeader(context),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Type listType = new TypeToken<List<Calification>>(){}.getType();
                List<Calification> califications = new Gson().fromJson(response.toString(),listType);
                calificationResponse.onSuccess(califications);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Gson().fromJson(errorResponse.toString(),Message.class);
                calificationResponse.onFailure(message);
            }
        });
    }
    public static void getCalificationsByUser(Context context, int restaurantId,String userEmail,final Response<Calification> calificationResponse){
        HttpClient.get(context,"califications/"+restaurantId+"/"+userEmail,null,SessionManager.getTokenHeader(context),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Calification calification = new Gson().fromJson(response.toString(),Calification.class);
                calificationResponse.onSuccess(calification);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Gson().fromJson(errorResponse.toString(),Message.class);
                calificationResponse.onFailure(message);
            }
        });
    }
    public static void getRestaurantCalification(Context context, int restaurantId,final Response<Calification> calificationResponse){
        HttpClient.get(context,"califications/avg/"+restaurantId,null,SessionManager.getTokenHeader(context),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Calification calification = new Gson().fromJson(response.toString(),Calification.class);
                calificationResponse.onSuccess(calification);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Gson().fromJson(errorResponse.toString(),Message.class);
                calificationResponse.onFailure(message);
            }
        });
    }
}
