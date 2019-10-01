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
import tec.findmyrestaurant.model.Restaurant;

public class SearchRequest {
    private double radius=-1, latitude=-1, longitude=-1, calification=-1;
    private String name=null;
    private int foodType=-1;
    private String price="0";

    public SearchRequest withPrice(String price){
        this.price=price;
        return this;
    }

    /**
     *
     * @param latitude Center latitude of the point
     * @param longitude Center longitude of the point
     * @param radius Radius around the center in km
     * @return The request
     */
    public SearchRequest withLocation(double latitude, double longitude, double radius){
        this.latitude=latitude;
        this.longitude=longitude;
        this.radius=radius;
        return this;
    }
    public SearchRequest setName(String name){
        this.name=name;
        return this;
    }
    public SearchRequest calificationAtLeast(double calification){
        this.calification=calification;
        return this;
    }
    public SearchRequest setFoodType(int foodType){
        this.foodType=foodType;
        return this;
    }
    public void request(Context context,final Response<Restaurant> restaurantResponse){
        try {
            JSONObject object = new JSONObject();
            if(name!=null)
                object.put("name",name);
            if(foodType!=-1)
                object.put("food",foodType);
            if(calification!=-1)
                object.put("calification",calification);
            if(longitude!=-1 && latitude != -1 && radius !=-1){
                object.put("lat",latitude);
                object.put("lng",longitude);
                object.put("r",radius);
            }
            if(price!="0")
                object.put("price",price);
            StringEntity params = new StringEntity(object.toString());
            HttpClient.post(context,"restaurants/search",params,SessionManager.getTokenHeader(context),new JsonHttpResponseHandler(){
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
        }catch (Exception e){
            Log.e("Http-error",e.getMessage());
            Message message = new Message();
            restaurantResponse.onFailure(message);
        }
    }
    public static void test(){
    }
}
