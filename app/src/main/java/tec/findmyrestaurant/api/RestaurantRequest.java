package tec.findmyrestaurant.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import tec.findmyrestaurant.model.Restaurant;
import tec.findmyrestaurant.model.User;

public class RestaurantRequest {

    public static void getRestaurants(Context context, final Response<Restaurant> restaurantResponse){
        HttpClient.get(context,"restaurants",null,SessionManager.getTokenHeader(context),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Type listType = new TypeToken<List<Restaurant>>(){}.getType();
                List<Restaurant> restaurants = new Gson().fromJson(response.toString(),listType);
                restaurantResponse.onSuccess(restaurants);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Gson().fromJson(errorResponse.toString(),Message.class);
                restaurantResponse.onFailure(message);
            }
        });
    }
    public static void getRestaurant(Context context, int id,final Response<Restaurant> restaurantResponse){
        HttpClient.get(context,"restaurants/"+id,null,SessionManager.getTokenHeader(context),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Restaurant restaurant = new Gson().fromJson(response.toString(),Restaurant.class);
                restaurantResponse.onSuccess(restaurant);            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Gson().fromJson(errorResponse.toString(),Message.class);
                restaurantResponse.onFailure(message);
            }
        });
    }
    public static void registerRestaurant(Context context, Restaurant restaurant, final Response<Restaurant> restaurantResponse){
        try {
            StringEntity params = new StringEntity(new Gson().toJson(restaurant));
            HttpClient.post(context,"restaurants",params,SessionManager.getTokenHeader(context),new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Message message = new Gson().fromJson(response.toString(),Message.class);
                    restaurantResponse.onSuccess(message);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Message message = new Gson().fromJson(errorResponse.toString(),Message.class);
                    restaurantResponse.onFailure(message);
                }
            });
        }catch (Exception e){
            Log.e("Http-error",e.getMessage());
            Message message = new Message();
            restaurantResponse.onFailure(message);
        }
    }
}
