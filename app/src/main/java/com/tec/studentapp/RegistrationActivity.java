package com.tec.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.tec.entities.Estudiante;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class RegistrationActivity extends AppCompatActivity {
    static TextView carnet;
    EditText nombre;
    EditText password;
    static String resultpass;
    Button ubicacion;
    static Estudiante estudiante;

    //public static String carnet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        estudiante = null;

        carnet = (TextView) findViewById(R.id.carnet);
        nombre = (EditText) findViewById(R.id.nombre);
        password = (EditText) findViewById(R.id.password);

        carnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(RegistrationActivity.this, BarcodeScanner.class);
                startActivityForResult(registerIntent, 1);

                //RegistrationActivity.this.startActivity(registerIntent);
            }
        });

        final RelativeLayout registrarse = (RelativeLayout) findViewById(R.id.registrarse);
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!carnet.getText().toString().isEmpty()&&!nombre.getText().toString().isEmpty()&&!password.getText().toString().isEmpty()) {
                    resultpass = password.getText().toString();
                    MainActivity.carnet.setText(carnet.getText().toString());
                    MainActivity.passwordlogin.setText(password.getText().toString());
                    getManualRegistrationInfo();
                    finish();
                    //Toast.makeText(getApplicationContext(),"Binvenido :"+nombre.getText().toString(), Toast.LENGTH_SHORT).show();
                    //se manda al mapa
                    // Intent mapa = new Intent(MainActivity.this, MapsActivity.class);
                    //MainActivity.this.startActivity(mapa);
                    getManualRegistrationInfo();
                }else{
                    Toast.makeText(getApplicationContext(),"Ingresar todos los datos",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public String[] getManualRegistrationInfo(){
        //FALTA INCLUIR UBICACION
        String[] resultado = new String[3];
        estudiante = new Estudiante(nombre.getText().toString(),password.getText().toString(),carnet.getText().toString(),0,0);

        resultado[0] = nombre.getText().toString();
            resultado[1] = password.getText().toString();
            resultado[2] = carnet.getText().toString();
            return resultado;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String resultado = data.getStringExtra("resultado");
                carnet.setText(resultado);
                MainActivity.carnet.setText(resultado);
            }
        }
    }
}
