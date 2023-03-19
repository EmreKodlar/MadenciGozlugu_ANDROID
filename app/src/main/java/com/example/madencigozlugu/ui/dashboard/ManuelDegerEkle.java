package com.example.madencigozlugu.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.madencigozlugu.R;
import com.example.madencigozlugu.VeriTabani;
import com.example.madencigozlugu.alt_menu;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ManuelDegerEkle extends AppCompatActivity {

    private VeriTabani vt;

    private EditText metane,butane,propane,coe,temizHavae,tarihe;
    private Button ekleDegerr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manuel_deger_ekle);

        vt=new VeriTabani(this); // veritabanı bağlantısı
        //------ toolbar ayareları
        getSupportActionBar().setDisplayShowTitleEnabled(true); //yazıyı ekle
        getSupportActionBar().setTitle("Test amaçlı Değer Ekleme Sayfası");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // geri dön iconu. Altta onOptionsItemSelected fonk. var

        //------------------------

        metane=(EditText)findViewById(R.id.metanekle);
        butane=(EditText) findViewById(R.id.butanekle);
        propane=(EditText) findViewById(R.id.propanekle);
        coe=(EditText)findViewById(R.id.coekle);
        temizHavae=(EditText) findViewById(R.id.havaekle);
        tarihe=(EditText) findViewById(R.id.tarihekle);


        ekleDegerr=(Button) findViewById(R.id.degerEkle);

        ekleDegerr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(metane.getText().toString().matches("") || butane.getText().toString().matches("")  ){

                    Toast.makeText( getApplicationContext(), "Boş Alan Bırakmayınız!!!!!!", Toast.LENGTH_SHORT).show();

                }

                else {

                    try {


//                        SimpleDateFormat sekil = new SimpleDateFormat();
//                        Date tarihe = new Date();

                        deger_ekle(metane.getText().toString(), butane.getText().toString(),
                                   propane.getText().toString(), coe.getText().toString(),
                                    temizHavae.getText().toString(), tarihe.getText().toString());

                        Toast.makeText(getApplicationContext(), "Yeni Değeri  Başarıyla Eklediniz...", Toast.LENGTH_SHORT).show();

                    } finally {
                        vt.close();
                    }


                }
            }
        });
    }

        private   void deger_ekle(String m,String b, String p,String c,String h, String t){

        SQLiteDatabase db=vt.getWritableDatabase(); // yazılabilir db oluşturduk
        ContentValues veriler=new ContentValues(); // verileri yazacağımız content
        veriler.put("metan",m); // verileri ekledik (vt'deki sütun adı, burda belirlediğimiz ad)
        veriler.put("butan",b);
        veriler.put("propan",p);
        veriler.put("temizHava",h);
        veriler.put("co",c);
        veriler.put("userid", alt_menu.u_id);
        veriler.put("tarih",t);


        db.insertOrThrow("Degerler",null,veriler); //(tablo,null,veriler)

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // geri dön ok'u fonk.
        switch (item.getItemId()) {
            case android.R.id.home:
                //------listeyi yenile----------
                Intent intent33=new Intent(ManuelDegerEkle.this, DashboardFragment.class);

                startActivity(intent33);
                //---------------------
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}