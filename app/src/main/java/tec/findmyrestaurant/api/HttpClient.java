package tec.findmyrestaurant.api;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class HttpClient {
    private static final String BASE_URL = "http://find-my-restaurant.herokuapp.com";
    private static final String CONTENT_TYPE = "application/json";

    public static void get(Context context, String route, RequestParams params, Header[] headers, AsyncHttpResponseHandler responseHandler){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(context,buildURL(route),headers,params,responseHandler);
    }
    public static void post(Context context, String route, HttpEntity entity,Header[] headers,AsyncHttpResponseHandler asyncHttpResponseHandler){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(context,buildURL(route),headers,entity,CONTENT_TYPE,asyncHttpResponseHandler);
    }

    private static String buildURL(String route){
        return BASE_URL+"/"+route;
    }
}
