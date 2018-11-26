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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tec.entities.Conductor;
import com.tec.entities.Estudiante;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistrationActivity extends AppCompatActivity {
    static TextView carnet;
    EditText nombre;
    EditText password;
    static String resultpass;
    Button ubicacion;
    static Estudiante estudiante;
    String urlInicioSesion = "http://172.18.210.63:8080/registro-estudiante";
    Gson gson = new Gson();

    OkHttpClient client = new OkHttpClient();
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
                    Estudiante estud = new Estudiante(nombre.getText().toString(), carnet.getText().toString(), password.getText().toString(),0,0);
                    registro(estud);

                    //getManualRegistrationInfo();
                    //finish();
                    //Toast.makeText(getApplicationContext(),"Binvenido :"+nombre.getText().toString(), Toast.LENGTH_SHORT).show();
                    //se manda al mapa
                    // Intent mapa = new Intent(MainActivity.this, MapsActivity.class);
                    //MainActivity.this.startActivity(mapa);
                    //getManualRegistrationInfo();
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

    public void registro(Estudiante estudiante) {
        String json = gson.toJson(estudiante);
        final JsonParser jsonParser = new JsonParser();
        final JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        jsonObject.remove("amigos");
        json = jsonObject.toString();

        RequestBody requestBody = new FormBody.Builder()
                .add("json", json)
                .build();

        Request request = new Request.Builder()
                .url(this.urlInicioSesion)
                .addHeader("Content-Type", "text/plain")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    JsonObject json = jsonParser.parse(myResponse).getAsJsonObject();
                    boolean exito = json.getAsJsonPrimitive("exitoso").getAsBoolean();

                    if (exito) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegistrationActivity.this, "Bienvenido!",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegistrationActivity.this,
                                        StudentMapActivity.class);
                                RegistrationActivity.this.startActivity(intent);
                            }
                        });

                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegistrationActivity.this,
                                        "Información inválida o usuario ya existe", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }
        });
    }
}
