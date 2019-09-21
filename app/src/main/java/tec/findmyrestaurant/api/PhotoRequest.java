package tec.findmyrestaurant.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import cz.msebera.android.httpclient.Header;
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
}
