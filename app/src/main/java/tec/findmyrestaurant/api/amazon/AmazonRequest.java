package tec.findmyrestaurant.api.amazon;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.AWSRefreshableSessionCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import tec.findmyrestaurant.api.Message;
import tec.findmyrestaurant.api.Response;
import tec.findmyrestaurant.api.Utils;

public class AmazonRequest {
    public  Context context;
    private String name;
    private Uri uri;
    private Response response;
    private static  final String TAG="Cognito";
    private CognitoCachingCredentialsProvider credentialsProvider;

    public AmazonRequest(Context context, String name, Uri uri, Response response) {
        this.context = context;
        this.name = name;
        this.uri = uri;
        this.response = response;
        AWSMobileClient.getInstance().initialize(context).execute();
    }

    public void beginUpload(){
        try {
            final CognitoSettings cognitoSettings = new CognitoSettings(context);
            Log.i(TAG, "getting user pool user");
            credentialsProvider = cognitoSettings.getCredentialsProvider();
            CognitoUser currentUser = cognitoSettings.getUserPool().getCurrentUser();
            currentUser.getSessionInBackground(new AuthenticationHandler() {
                @Override
                public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                    if (userSession.isValid()) {
                        String idToken = userSession.getIdToken().getJWTToken();
                        if (idToken.length() > 0) {
                            Map<String, String> login = new HashMap<>();
                            login.put("cognito-idp.us-east-1.amazonaws.com/us-east-1_lzffzqgsa", idToken);
                            credentialsProvider.setLogins(login);
                            new RefreshAsyncTask().execute(0);
                        }
                    } else {
                        response.onFailure(new Message("Session invalid"));
                    }
                }

                @Override
                public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                }

                @Override
                public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                }

                @Override
                public void authenticationChallenge(ChallengeContinuation continuation) {
                }

                @Override
                public void onFailure(Exception exception) {
                    response.onFailure(new Message(exception.getMessage()));
                }
            });
        }catch (Exception e){
            response.onFailure(new Message(e.getMessage()));
        }
    }
    private void upload(){
        try {
            TransferUtility transferUtility = TransferUtility.builder()
                    .context(context)
                    .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                    .s3Client(new AmazonS3Client(credentialsProvider,Region.getRegion(Regions.US_EAST_1)))
                    .build();
            final TransferObserver observer = transferUtility.upload(
                    name,
                    new File(Utils.toAbsolutePath(context,uri)),
                    CannedAccessControlList.PublicRead
            );
            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (state.equals(TransferState.COMPLETED)) {
                        String URL = "https://findmyrestaurantapp-deployments-mobilehub-761975919.s3.amazonaws.com"+ "/" + name;
                        response.onSuccess(new Message(URL));
                    } else if(state.equals(TransferState.FAILED) || state.equals(TransferState.CANCELED)){
                        response.onFailure(new Message("ERROR"));
                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) { }

                @Override
                public void onError(int id, Exception ex) {
                    response.onFailure(new Message(ex.getMessage()));
                }
            });
        }catch (Exception e){
            response.onFailure(new Message(e.getMessage()));
        }
    }
    private class RefreshAsyncTask extends AsyncTask<Integer,Void,Integer>{
        @Override
        protected Integer doInBackground(Integer... integers) {
            credentialsProvider.refresh();
            return  integers[0];
        }

        @Override
        protected void onPostExecute(Integer integer) {
            upload();
        }
    }

    public static void signIn(Context context, final Response response){
        CognitoSettings cognitoSettings = new CognitoSettings(context);
        CognitoUser user = cognitoSettings.getUserPool().getUser("steven");

        final AuthenticationHandler authenticationContinuation = new AuthenticationHandler(){
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                response.onSuccess(new Message());
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                AuthenticationDetails details = new AuthenticationDetails(userId, "ABCabc123.",null);
                authenticationContinuation.setAuthenticationDetails(details);
                authenticationContinuation.continueTask();
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) { }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) { }

            @Override
            public void onFailure(Exception exception) {
                response.onFailure(new Message());
            }
        };
        user.getSessionInBackground(authenticationContinuation);
    }
}
