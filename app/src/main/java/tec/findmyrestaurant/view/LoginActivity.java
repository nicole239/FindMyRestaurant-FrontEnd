package tec.findmyrestaurant.view;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
        loginButton.setPermissions(Arrays.asList("email", "public_profile"));

        // Callback registration
        /*try { PackageInfo info = getPackageManager().getPackageInfo("tec.findmyrestaurant", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign= Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("MYKEYHASH", sign);
                Toast.makeText(getApplicationContext(),sign, Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("MYKEYHASH",e.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("MYKEYHASH",e.toString());
        }*/


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
                                Log.v("MYEMAIL", response.toString());
                                try {
                                    register(object.getString("email"),object.getString("id"),object.getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("mierror","Canceled");
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

    private void register(final String email, final String password,final  String name){
        UserRequest.getUser(this,email,new Response<User>(){
            @Override
            public void onSuccess(User objet) {
                Log.d("LoginActivityM","El email ya esta registrado");
                login(email,password);
            }

            @Override
            public void onFailure(Message message) {
                final User user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setPassword(password);
                user.setType(User.REGULAR);
                UserRequest.registerUser(LoginActivity.this,user,new Response<User>(){
                    @Override
                    public void onSuccess(Message message) {
                        Log.d("LoginActivityM","Se pudo registrar al usuario");
                        login(email,password);
                    }

                    @Override
                    public void onFailure(Message message) {
                        Toast.makeText(LoginActivity.this,"No se pudo registrar al usario",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void login(String email, String password){
        //Login
        final User user = new User();
        user.setEmail(email);
        user.setPassword(password);

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
