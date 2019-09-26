package tec.findmyrestaurant.api.amazon;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class CognitoSettings {
    private final String userPoolId="us-east-1_lzffzqgsa";
    private final String clientId="15o4p8alna8tog193ivrrk3i54";
    private final String clientSecret= "129bvush1o1ef2jlf1ro1rqkehpg8k4md7vitgi09v986rbp02lg";
    private final Regions cognitoRegion = Regions.US_EAST_1;
    private final String identityPoolId="us-east-1:134444ec-73a2-4ae1-bf88-037d70221ef3";
    private Context context;

    public CognitoSettings(Context context) {
        this.context = context;
    }

    public String getUserPoolId() {
        return userPoolId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Regions getCognitoRegion() {
        return cognitoRegion;
    }

    public CognitoUserPool getUserPool(){
        return new CognitoUserPool(context,userPoolId,clientId,clientSecret,cognitoRegion);
    }
    public CognitoCachingCredentialsProvider getCredentialsProvider(){
        return new CognitoCachingCredentialsProvider(context.getApplicationContext(),identityPoolId,cognitoRegion);
    }
}
