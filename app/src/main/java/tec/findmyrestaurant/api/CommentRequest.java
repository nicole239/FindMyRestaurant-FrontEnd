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
import tec.findmyrestaurant.model.Comment;
import tec.findmyrestaurant.model.Restaurant;

public class CommentRequest {

    public static void getComments(Context context, int id, final Response<Comment> commentResponse){
        HttpClient.get(context,"comments/"+id,null,SessionManager.getTokenHeader(context),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Type listType = new TypeToken<List<Comment>>(){}.getType();
                List<Comment> comments = new Gson().fromJson(response.toString(),listType);
                commentResponse.onSuccess(comments);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Gson().fromJson(errorResponse.toString(),Message.class);
                commentResponse.onFailure(message);
            }
        });
    }
    public static void registerComment(Context context, Comment comment, final Response<Restaurant> commentsResponse){
        try {
            StringEntity params = new StringEntity(new Gson().toJson(comment));
            HttpClient.post(context,"comments",params,SessionManager.getTokenHeader(context),new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Message message = new Gson().fromJson(response.toString(),Message.class);
                    commentsResponse.onSuccess(message);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Message message = new Gson().fromJson(errorResponse.toString(),Message.class);
                    commentsResponse.onFailure(message);
                }
            });
        }catch (Exception e){
            Log.e("Http-error",e.getMessage());
            Message message = new Message();
            commentsResponse.onFailure(message);
        }
    }
}
