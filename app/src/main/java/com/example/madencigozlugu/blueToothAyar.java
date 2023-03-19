package com.example.madencigozlugu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;//adapter kütüphanesi
import android.bluetooth.BluetoothDevice;//cihaz kütüphanesi

import android.bluetooth.BluetoothSocket;//soket kütüphanesi


import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;




import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Set;
import java.util.UUID;

public class blueToothAyar extends AppCompatActivity {
    //Not: Arduino HC-05 Modüle Direkt olarak, telefon üzerinden erişemiyoruz.
    // Bu yüzden, uygulama içerisinde cihazı arattırıp bağlanmamız lazım!
    // Ama önce telefondan eşleştirmeyi yapmalıyız.

    Button cihazgetir, cihazbagla, digersayfa;
    TextView yazii;

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//standart bluetooth adresi
    BluetoothAdapter btAdapter; // adapterimiz
    BluetoothDevice hc05; //bluetooth cihazı
    BluetoothSocket btsoket; //bluetooth cihazı için soket
    OutputStream disariGidecekVeri; //dışarı göndereceğimiz veriler. Örneğin Arduinoya
    InputStream iceriGelecekVeri; //içeri aktaracağımız veriler. Örneğin Arduino'dan
    IntentFilter intentFilter;
    RxThread rxThread;

    //-----veri gönderme
    String DegerGonderString = "1";

    //--Sqlite işlemleri
    private VeriTabani vt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth_ayar);

        vt=new VeriTabani(this); // Sqlite veritabanı bağlantısı

        cihazbagla = (Button) findViewById(R.id.cihazbagla);
        cihazgetir = (Button) findViewById(R.id.cihazgetir);
        digersayfa = (Button) findViewById(R.id.digersayfa);
        yazii = (TextView) findViewById(R.id.yaziii);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        intentFilter=new IntentFilter();
        intentFilter.addAction(btAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(hc05.ACTION_ACL_DISCONNECTED);

        rxThread=new RxThread(); // Arduino'dan gelecek veri için rx nesnesine ihtyaç var.


        registerReceiver(Btrecevier, intentFilter);
        cihazbagla.setEnabled(false);
        digersayfa.setEnabled(false);

        if(!btAdapter.isEnabled()) // bluetooth kapalıysa aç...
        {
            Intent bluetoothBaslat = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(bluetoothBaslat,1);

        }


        cihazgetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(blueToothAyar.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    Set<BluetoothDevice> deviceList = btAdapter.getBondedDevices();
                    for (BluetoothDevice dev : deviceList) {
                        if (dev.getName().equals("HC-05")) {
                            hc05 = dev;
                            Toast.makeText(blueToothAyar.this, "HC-05 İsimli Cihaz Bulundu", Toast.LENGTH_SHORT).show();
                            btAdapter.cancelDiscovery();
                            break;
                        }
                    }
                }


            }
        });

        cihazbagla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(blueToothAyar.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    try {
                        btsoket = hc05.createRfcommSocketToServiceRecord(mUUID);
                        btsoket.connect(); // cihaza bağlandık
                        iceriGelecekVeri=btsoket.getInputStream(); // Arduino'dan verileri alıyoruz. Orada, Serial.print("Metan : "); kodunda olan vcerileri çekiyoruz.
                        disariGidecekVeri=btsoket.getOutputStream(); // Arduino'ya gidecek veriler.
                        disariGidecekVeri.write(DegerGonderString.toString().getBytes()); // Arduino'ya 1 verisi gitti. Orada, int androidDegerCek = Serial.read(); olarak veriler yakalanacak..
                        rxThread.start();
                        Toast.makeText(blueToothAyar.this, "HC-05'e Bağlanıldı", Toast.LENGTH_SHORT).show();
                        digersayfa.setEnabled(true);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        });

        digersayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iii = new Intent(blueToothAyar.this, alt_menu.class);

                startActivity(iii);
            }
        });

    }

    class RxThread extends Thread{
        public boolean isRunning;
        public byte[] iceriByte;
        public int iceriSayi;
        //--string veri alma işlemi---
        public String iceriData="";
        public byte[] readBuffer;
        public int readBufferPosition;
        public final Handler handler = new Handler();
        public final byte delimiter = 10; //This is the ASCII code for a newline character

        //---sqlite'a verilerikaydetme
        String metan,butan,propan,co,tarih,temizHava;
        // ManuelDegerEkle manueldegerNesnesi=new ManuelDegerEkle(); // ManuelDegerEkle'deki deger_ekle fonksiyonunu kullanacağız.


        RxThread(){
            isRunning=true;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

        }
        public void run(){
            while (isRunning){

                try {
                    iceriSayi= iceriGelecekVeri.available();

                    if(iceriSayi>0){
                        iceriByte=new byte[iceriSayi];
                        iceriGelecekVeri.read(iceriByte);
                        for(int i=0;i<iceriSayi;i++){
                            byte b=iceriByte[i];
                            if(b==delimiter){
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                iceriData = new String(encodedBytes, "US-ASCII");
                                readBufferPosition = 0;
/*
                                handler.post(new Runnable() {
                                    public void run() {
                                        if(!iceriData.equals("")) {
                                            yazii.setText("gelen veri : " + iceriData);

                                            System.out.println("gelen veri : " + iceriData);
                                        }
                                        else{
                                            yazii.setText("gelen VEri YOk : 0 " + iceriData);

                                            System.out.println("gelen VEri YOk : 0 " + iceriData);
                                        }
                                    }
                                });

 */
                            }
                            else {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!iceriData.equals("")) {

                                SimpleDateFormat sekil = new SimpleDateFormat();
                                Date tt = new Date();
                                tarih=sekil.format(tt);

                                String split[] = iceriData.split(",", 0); // virgüle kadar olan verileri alacağız
                                // Arduinodan gelen veriler örneğin-> 180,240,330,220,660 gibi olacak
                                // veri sıramız metan,butan,propan,co,temizHava olduğu için ona göre verileri atacağız.
                                //Tarih verisini java dan alacağız.
                                for (int j=0;j<5;j++) { // j<5 yaptık, çünkü metan,butan,propan,co,temizHava olarak 5 verimiz var
                                    metan=split[0]; // ilk değer metan
                                    butan=split[1]; // ikinci değer butan
                                    propan=split[2]; // 3. değer propan
                                    co=split[3]; // 4. değer co
                                    temizHava=split[4]; // 5. değer temiz hava
                                }
                                System.out.println("gelen veri : " + iceriData);
                                yazii.setText(" Metan: " +  metan + " Bütan: " +  butan +" Proan: " +  propan +" CO: " +  co +" Temiz Hava: " +  temizHava );
                                DegerleriSqliteEkle(metan,butan, propan,co,temizHava, tarih); // Manuel değer'deki fonksiyona göndererek kayıt ediyoruz.

                            }
                            else{
                                yazii.setText("Veriler 1 Dk. İçinde Yüklenmeye Başlayacak!");

                                System.out.println("gelen Veri Yok : 0 " + iceriData);
                            }
                        }
                    });

                    Thread.sleep(60000); // 1 dakikada bir işlemi gerçekleştirsin. // Bu süreyi istediğin gibi değiştir. Mesela 1 saat gibi 3.600.000ms
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }
        }
    }
    BroadcastReceiver Btrecevier=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cihazbagla.setEnabled(true);
                        }
                    });
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    rxThread.isRunning=false;
                    break;
            }
        }
    };
    private void DegerleriSqliteEkle(String m,String b, String p,String c,String h, String t){

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

}