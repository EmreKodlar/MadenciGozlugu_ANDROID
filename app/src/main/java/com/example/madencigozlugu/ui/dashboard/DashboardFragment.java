package com.example.madencigozlugu.ui.dashboard;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.madencigozlugu.R;
import com.example.madencigozlugu.VeriTabani;
import com.example.madencigozlugu.databinding.FragmentDashboardBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

public class DashboardFragment extends Fragment {

    VeriTabani vt;

    //---listview
    DegerClass degerClass;

    public ListView degerlerList;
    public ArrayList<DegerClass> degerGetir;
    public DegerAdapter adapterDeger;
    //----

    Float mm,bb,pp,cc,hh;
    String tt;
    int usid,deid;

    private String URL="https://madencigozlugu.com.tr/api/degerlerServis.php?token=emre";

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //listView kullanımı----------------------------
        vt=new VeriTabani(getContext()); // veritabanı bağlantısı
        degerlerList = (ListView) root.findViewById(R.id.degerlerList);
        degerGetir = degerListele();
        adapterDeger = new DegerAdapter(getContext(),R.layout.deger_adapter,degerGetir);
        degerlerList.setAdapter(adapterDeger);
        //degerlerList.setTextFilterEnabled(true); // arama kutusuyla etkileşimli olsun diye
        //-----------------------------------------


        degerlerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(degerGetir.get(position).getTarih() + " Tarihli Değerler");
                builder.setMessage( "Metan: "+  degerGetir.get(position).getMetan() + "\n" +
                                    "Bütan: "+  degerGetir.get(position).getButan() + "\n" +
                                    "Propan: "+  degerGetir.get(position).getPropan() + "\n" +
                                    "CO: "+  degerGetir.get(position).getCo() + "\n" +
                                    "Temiz Hava: "+  degerGetir.get(position).getTemizHava() );
                builder.setPositiveButton("OK",null);
                builder.show();


            }
        });

        //Fab menu------
        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Manuel değer eklemek için gerekli class'a gider
              // Intent intentekle=new Intent(getContext(), ManuelDegerEkle.class);
            // startActivity(intentekle);
                if(isOnline()){

                    SQLiteDatabase db55= vt.getWritableDatabase();

                    String sorgu2="SELECT did, MAX(metan) as metan," +
                            "MAX(butan) as butan," +
                            "MAX(propan) as propan," +
                            "MAX(temizHava) as temizHava," +
                            "MAX(co) as co," +
                            "userid, tarih  FROM Degerler ";

                    Cursor okunanlar222=db55.rawQuery(sorgu2, null);

                    //---- değerler içerisinden en büyüğünü alıyoruz...
                    if(okunanlar222.moveToNext()) { // while olursa moveToNext() kullanılır

                          deid    = okunanlar222.getInt(0);
                          mm      = okunanlar222.getFloat(1);
                          bb      =    okunanlar222.getFloat(2);
                          pp      =      okunanlar222.getFloat(3);
                          hh    =      okunanlar222.getFloat(4);
                          cc =      okunanlar222.getFloat(5);
                          usid=      okunanlar222.getInt(6);
                          tt= okunanlar222.getString(7);

                    }

                    //--------volley post ile değerleri sunucuya ekleme işlemi----------------

                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                    JSONObject putData = new JSONObject();
                    try {
                        putData.put("metan", mm);
                        putData.put("butan", bb);
                        putData.put("propan", pp);
                        putData.put("temizHava", hh);
                        putData.put("co", cc);
                        putData.put("userid", usid);
                        putData.put("tarih", tt);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL,
                            putData, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Toast.makeText(getContext(), "Veriler Başarıyla Sunucuya Gönderildi...", Toast.LENGTH_SHORT).show();

                            //--SQLite ile localdeki verileri siliyoruz.
                            SQLiteDatabase db =vt.getWritableDatabase();

                            db.delete("Degerler", null , null);
                            //------------
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

                    requestQueue.add(jsonObjectRequest);

                    //------------------volley post bitimi------------


                }
                else
                {


                    Toast.makeText(getContext(), "İnternete Bağlı Değilsiniz!\nVerileriniz Gönderilemedi", Toast.LENGTH_SHORT).show();

                 }


            }
        });
        //----


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //-- sqlite ile veri listeleme fonksiyonu
    public ArrayList<DegerClass> degerListele(){

        ArrayList<DegerClass> veriler=new ArrayList<DegerClass>();
        SQLiteDatabase db4= vt.getWritableDatabase();

        String sorgu2="SELECT * FROM Degerler ";

        Cursor okunanlar2=db4.rawQuery(sorgu2, null);


        while(okunanlar2.moveToNext()) { // while olursa moveToNext() kullanılır

            degerClass= new DegerClass(okunanlar2.getInt(0),okunanlar2.getFloat(1),okunanlar2.getFloat(2),okunanlar2.getFloat(3)
                    ,okunanlar2.getFloat(4),okunanlar2.getFloat(5),okunanlar2.getInt(6),okunanlar2.getString(7));
            veriler.add(degerClass);
        }
        okunanlar2.close();
        db4.close();
        return veriler;
    }

    //internet var mı yok mu? Kontrolü--------------
    public static Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 8.8.8.8"); // google'a ping atıyor. cevap varsa internet vardır.
            int returnVal = p1.waitFor();
            return (returnVal == 0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    //-------------------


}
