package tec.findmyrestaurant.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import tec.findmyrestaurant.model.User;

public class UserRequest {
    public static void getUsers(Context context, final Response<User> userResponse){
        HttpClient.get(context, "users", null, SessionManager.getTokenHeader(context), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Type listType = new TypeToken<List<User>>(){}.getType();
                List<User> users = new Gson().fromJson(response.toString(),listType);
                userResponse.onSuccess(users);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Gson().fromJson(errorResponse.toString(),Message.class);
                userResponse.onFailure(message);
            }
        });
    }
    public static void getUser(Context context,String email,final Response<User> userResponse){
        HttpClient.get(context, "users/"+email, null, SessionManager.getTokenHeader(context), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = new Gson().fromJson(response.toString(),User.class);
                userResponse.onSuccess(user);
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
    public static void registerUser(Context context, User user, final Response<User> userResponse){
        try {
            StringEntity params = new StringEntity(new Gson().toJson(user));
            HttpClient.post(context,"users",params,SessionManager.getTokenHeader(context),new JsonHttpResponseHandler(){
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
        }catch (Exception e){
            Log.e("Http-error",e.getMessage());
            Message message = new Message();
            userResponse.onFailure(message);
        }

    }
    public static void forgotPassword(Context context, String email, final Response userResponse){
        try{
            HttpClient.get(context, "users/recover/forgot/"+email, null,null, new JsonHttpResponseHandler() {
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
        }catch (Exception e){

        }
    }
    public static void newPassword(Context context, String email,String code, String password, final Response userResponse){
        try{
            JSONObject params = new JSONObject();
            params.put("email",email);
            params.put("code",code);
            params.put("password",password);
            StringEntity entity = new StringEntity(params.toString());
            HttpClient.put(context, "users/recover/newpassword/", entity,null, new JsonHttpResponseHandler() {
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
        }catch (Exception e){

        }
    }
}

