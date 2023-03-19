package com.example.madencigozlugu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public  class VeriTabani extends SQLiteOpenHelper {

    public static final String VERITABANI_ADI = "Madenci";
    private static final int VERITABANI_VERSIYONU = 11;

    //Tablo isimleri
    public static final String Degerler = "Degerler"; // Tablo1

    //sütun isimleri

    public static final String did = "did"; // Tablo1 // burası hep String.Çünkü sütun isimlerini yazıyoruz.
    public static final String metan = "metan";
    public static final String butan = "butan";
    public static final String propan = "propan";
    public static final String temizHava = "temizHava";
    public static final String co = "co";
    public static final String userid = "userid";
    public static final String tarih = "tarih";

    // Tablo oluştur

    String tablo1="CREATE TABLE "+
            Degerler +" (" + did + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            metan +" FLOAT,"+
            butan +" FLOAT," +
            propan +" FLOAT," +
            temizHava +" FLOAT," +
            co +" FLOAT,"+
            userid +" INTEGER," +
            tarih +" Text)";

    public  VeriTabani(Context context) { // önemli!!!
        super(context, VERITABANI_ADI, null, VERITABANI_VERSIYONU);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) { // tablolar burda oluşturuluyor


        // İlk tablomuzun adı Degerler
        sqLiteDatabase.execSQL(tablo1);

        //İkinci Tablo Kitaplar
        //...

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int eskiVersiyon, int yeniVersiyon) { // tablo düzenlemeleri burada

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Degerler");

        onCreate(sqLiteDatabase);
    }
}
