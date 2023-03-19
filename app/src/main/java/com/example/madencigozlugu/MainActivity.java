package com.example.madencigozlugu;



import androidx.appcompat.app.AppCompatActivity;


import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    TextView mail_et;
    TextView password_et;
    Button giris_btn;
    String URL = "https://madencigozlugu.com.tr/api/loginServis.php?token=emre";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //------toolbar kaldırma

        getSupportActionBar().hide();

        //----


        mail_et = (TextView) findViewById(R.id.mail_et);
        password_et = (TextView) findViewById(R.id.password_et);
        giris_btn = (Button) findViewById(R.id.giris_btn);

        giris_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                    if (mail_et.getText().toString().matches("") || password_et.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "BOŞ BIRAKMAYINIZ!", Toast.LENGTH_LONG).show();
                    } else {
                        //--------volley post (login) işlemi----------------

                        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                        JSONObject postData = new JSONObject();
                        try {
                            postData.put("umail", mail_et.getText().toString());
                            postData.put("usifre", password_et.getText().toString());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    boolean giris = response.getBoolean("success"); // burası api de tanımlanan success=true

                                    if (giris == true) {

                                        JSONArray arrayJson = response.getJSONArray("Uyeler");

                                        JSONObject bilgiler = arrayJson.getJSONObject(0);

                                        alt_menu.u_isim = bilgiler.getString("uisim");
                                        alt_menu.u_tel = bilgiler.getString("utel");
                                        alt_menu.u_mail = bilgiler.getString("umail");
                                        alt_menu.u_sifre = bilgiler.getString("usifre");
                                        alt_menu.u_id = bilgiler.getInt("uid");
                                        alt_menu.u_sirket = bilgiler.getInt("usirket");

                                        Toast.makeText(getApplicationContext(),
                                                "HOŞ GELDİNİZ SAYIN " + bilgiler.getString("uisim"), Toast.LENGTH_LONG).show();

                                        Intent i = new Intent(MainActivity.this, blueToothAyar.class);

                                        startActivity(i);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "HATALI GİRİŞ!", Toast.LENGTH_LONG).show();
                                    }


                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(getApplicationContext(), "BAĞLANTI HATASI!\nİnternetinizi Kontrol Edniz...", Toast.LENGTH_LONG).show();
                            }
                        });

                        requestQueue.add(jsonObjectRequest);

                        //------------------volley post bitimi------------
                    }



        }});

    }



}


