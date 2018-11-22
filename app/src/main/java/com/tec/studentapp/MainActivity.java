package com.tec.studentapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.tec.entities.Estudiante;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    static TextView carnet;
    static EditText passwordlogin ;
    static Estudiante estudiante;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        estudiante = null;

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intentt = new Intent(MainActivity.this, BarcodeScanner.class);
                startActivityForResult(intentt, 1);
                setFacebookData(loginResult);
            }


            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"c mamo",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(),"CANELADO PA",Toast.LENGTH_SHORT).show();
            }
        });



        // variables del XML
        passwordlogin = (EditText) findViewById(R.id.passwordlogin);
        final RelativeLayout iniciarsesion = (RelativeLayout) findViewById(R.id.iniciarsesion);
        carnet = (TextView) findViewById(R.id.carnet);
        carnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(MainActivity.this, BarcodeScanner.class);
                startActivityForResult(registerIntent, 1);
            }
        });

        final TextView registrase = (TextView) findViewById(R.id.registrarse);
        registrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registration = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(registration);
            }
        });

        iniciarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(carnet.getText().toString().equals(RegistrationActivity.carnet.getText().toString()) && passwordlogin.getText().toString().equals(RegistrationActivity.resultpass)){
                    Intent mapa = new Intent(MainActivity.this, StudentMapActivity.class);
                    MainActivity.this.startActivity(mapa);
              //  }else{
//                    Toast.makeText(getApplicationContext(),"Carnet o contraseña invalida",Toast.LENGTH_SHORT).show();

            //    }
            }
        });
    }

    private void goTestSesion(){
        Intent intent = new Intent(this, TestSesion.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String resultado = data.getStringExtra("resultado");
                carnet.setText(resultado);
                Intent mapa = new Intent(MainActivity.this, StudentMapActivity.class);
                MainActivity.this.startActivity(mapa);
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void setFacebookData(final LoginResult loginResult)
    {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            Log.i("Response",response.toString());
                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");
                            Log.i("Response",firstName);

                            Toast.makeText(getApplicationContext(),"Bienvenido " + firstName +" "+ lastName ,Toast.LENGTH_SHORT).show();
                            carnet.setText(carnet.getText()+firstName+" "+lastName); //de aqui se saca el nombre de face
                            String nombreCompleto = firstName + " "+lastName;
                            estudiante = new Estudiante(nombreCompleto,null,carnet.getText().toString(),0,0);
                            Profile profile = Profile.getCurrentProfile();
                            String id = profile.getId();
                            String link = profile.getLinkUri().toString();
                            Log.i("Link",link);
                            if (Profile.getCurrentProfile()!=null)
                            {
                                Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                            }

                            Log.i("Login"+ "FirstName", firstName);
                            Log.i("Login" + "LastName", lastName);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
