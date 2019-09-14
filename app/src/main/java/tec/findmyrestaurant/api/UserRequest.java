package tec.findmyrestaurant.api;

import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import tec.findmyrestaurant.model.User;

public class UserRequest {
    public static void getUsers(Context context, final Response<User> userResponse){
        HttpClient.get(context, "users", null, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                userResponse.onSuccess(new ArrayList<User>());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Gson().fromJson(errorResponse.toString(),Message.class);
                userResponse.onFailure(message);
            }
        });
    }
    public static void authUser(Context context, User user,final  Response<User> userResponse){
        HttpClient.get(context,"users"+"/"+user.getEmail()+"/"+user.getPassword(),null,null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Message message = new Gson().fromJson(response.toString(),Message.class);
                userResponse.onSuccess(message);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Gson().fromJson(errorResponse.toString(),Message.class);
                userResponse.onFailure(message);
            }
        });
    }
}
