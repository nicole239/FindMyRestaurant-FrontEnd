package tec.findmyrestaurant.api;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class HttpClient {
    private static final String BASE_URL = "http://www.findmyrestaurant.tk";


    public static void get(Context context, String route, RequestParams params, Header[] headers, AsyncHttpResponseHandler responseHandler){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(context,buildURL(route),headers,params,responseHandler);
    }

    private static String buildURL(String route){
        return BASE_URL+"/"+route;
    }
}
