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
import tec.findmyrestaurant.model.User;

public class PhotoRequest {
    public static void getPhotos(Context context, int idRestaurant,final Response<String> photoResponse){
        HttpClient.get(context, "photos/"+idRestaurant, null, SessionManager.getTokenHeader(context), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Type listType = new TypeToken<List<String>>(){}.getType();
                List<String> photos = new Gson().fromJson(response.toString(),listType);
                photoResponse.onSuccess(photos);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Gson().fromJson(errorResponse.toString(),Message.class);
                photoResponse.onFailure(message);
            }
        });
    }
    public static void getPhotoID(Context context,final Response photoResponse){
        HttpClient.get(context, "photos/", null, SessionManager.getTokenHeader(context), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Message message = new Gson().fromJson(response.toString(),Message.class);
                photoResponse.onSuccess(message);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Gson().fromJson(errorResponse.toString(),Message.class);
                photoResponse.onFailure(message);
            }
        });
    }
    public static void uploadPhoto(Context context,int id,String url,final Response response){
        try {
            JSONObject object = new JSONObject();
            object.put("url",url);
            object.put("idrestaurant",id);
            StringEntity params = new StringEntity(object.toString());
            HttpClient.post(context,"photos",params,SessionManager.getTokenHeader(context),new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject responseJSON) {
                    Message message = new Gson().fromJson(responseJSON.toString(),Message.class);
                    response.onSuccess(message);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Message message = new Gson().fromJson(errorResponse.toString(),Message.class);
                    response.onFailure(message);
                }
            });
        }catch (Exception e){
            Log.e("Http-error",e.getMessage());
            Message message = new Message();
            response.onFailure(message);
        }
    }
}
