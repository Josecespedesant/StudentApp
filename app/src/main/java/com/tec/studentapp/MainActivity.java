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
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class MainActivity extends AppCompatActivity {
    TextView carnet;
    private static final String TAG = "MainActivity";
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.tec.studentapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(carnet.getText().length() == 10){
                    Toast.makeText(getApplicationContext(),"ak7",Toast.LENGTH_SHORT).show();
                    Intent intentt = new Intent(MainActivity.this, RegistrationActivity.class);
                    startActivity(intentt);
                    //goTestSesion();
                }else {
                    Toast.makeText(getApplicationContext(),"Scanear carnet primero",Toast.LENGTH_LONG).show();

                }

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


        try {

            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(
                    "com.tec.driverapp",//give your package name here
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.d(TAG, "Hash ASLKJDNKJHNLASDOHASHIPDASDPOHIJASDPHO : " + Base64.encodeToString(md.digest(), Base64.NO_WRAP));//Key hash is printing in Log
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, e.getMessage(), e);
        }
        //final EditText nombreusuariologin = (EditText) findViewById(R.id.nombreusuariologin);
      //  final EditText contraseñalogin = (EditText) findViewById(R.id.passwordlogin);
      //  final TextView registrate = (TextView) findViewById(R.id.registratetext);
       //final ImageView linkedin = (ImageView) findViewById(R.id.linkedimg);
     //   final RelativeLayout iniciarsesion = (RelativeLayout) findViewById(R.id.iniciarsesion);
        carnet = (TextView) findViewById(R.id.carnet);
        carnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(MainActivity.this, BarcodeScanner.class);
                startActivityForResult(registerIntent, 1);

                //RegistrationActivity.this.startActivity(registerIntent);
            }
        });


     /*   registrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                MainActivity.this.startActivity(registerIntent);
            }


        });*/
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
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
