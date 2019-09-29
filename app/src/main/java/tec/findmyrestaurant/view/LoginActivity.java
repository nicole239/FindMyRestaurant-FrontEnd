package tec.findmyrestaurant.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import tec.findmyrestaurant.R;
import tec.findmyrestaurant.api.Message;
import tec.findmyrestaurant.api.Response;
import tec.findmyrestaurant.api.SessionManager;
import tec.findmyrestaurant.api.UserRequest;
import tec.findmyrestaurant.api.amazon.AmazonRequest;
import tec.findmyrestaurant.api.amazon.CognitoSettings;
import tec.findmyrestaurant.model.User;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;

    private AWSCredentialsProvider credentialsProvider;
    private AWSConfiguration configuration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.btnLoginFacebook);
        loginButton.setPermissions("email");

        // Callback registration

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                final User user = new User();
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    user.setEmail(object.getString("email"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email");
                request.setParameters(parameters);
                request.executeAsync();

                SessionManager.saveToken(LoginActivity.this, user,accessToken.getToken());
                Intent intent = new Intent(LoginActivity.this, TabbedActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(),exception.toString(),Toast.LENGTH_LONG).show();
                Log.e("mierror",exception.toString());
            }
        });
    }

    public void btnRegister_Click(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
        startActivity(intent);
    }
    public void btnLogin_Click(View view){
        AmazonRequest.signIn(this,new Response());
        TextInputLayout txtEmail = findViewById(R.id.txtEmail);
        TextInputLayout txtPassword = findViewById(R.id.txtPassword);

        final User user = new User();
        user.setEmail(txtEmail.getEditText().getText().toString());
        user.setPassword(txtPassword.getEditText().getText().toString());

        UserRequest.authUser(getApplicationContext(),user, new Response<User>(){
            @Override
            public void onSuccess(Message message) {
                if(message.isAuth()) {
                    SessionManager.saveToken(LoginActivity.this, user,message.getToken());
                    Intent intent = new Intent(LoginActivity.this, TabbedActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Message message) {
                Toast.makeText(getApplicationContext(),"Email or password incorrect",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void btnForgotPassword(View view){
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
    public void test(){
        final CognitoUserAttributes userAttributes = new CognitoUserAttributes();
        final SignUpHandler signUpHandler = new SignUpHandler() {
            @Override
            public void onSuccess(CognitoUser user, SignUpResult signUpResult) {
                int b=7;
            }

            @Override
            public void onFailure(Exception exception) {
                int g7=0;            }
        };

        userAttributes.addAttribute("given_name","steven");
        userAttributes.addAttribute("email","steven.moya.quinones@gmail.com");
        CognitoSettings cognitoSettings = new CognitoSettings(this);
        cognitoSettings.getUserPool().signUpInBackground("steven","ABCabc123.",userAttributes,null,signUpHandler);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
