package com.example.madencigozlugu.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.madencigozlugu.R;

import com.example.madencigozlugu.alt_menu;
import com.example.madencigozlugu.databinding.FragmentHomeBinding;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    ListView listView;
    ArrayAdapter<String> adapter;
    //Sorgu kuyruğu adında sorgumuzu çalıştıracak bir RequestQueue oluşturuyoruz.
    RequestQueue queue;
    //Yazılarımızın başlıklarını dolduracağımız bir List yapıyoruz.
    ArrayList<String> yaziBasliklari;
    //Web servis linkimiz.
    private String URL = "https://madencigozlugu.com.tr/api/degerlerServis.php?token=emre&uid="+ alt_menu.u_id;

    String temizHava,metan,butan,propan,co,tarih;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

      binding = FragmentHomeBinding.inflate(inflater, container, false);
      View root = binding.getRoot();


        //listView'i tasarımdakiyle bağlıyoruz.
        listView = (ListView) root.findViewById(R.id.listViewGecmisDegerler);

        //Öncelikle Volley içerisinden bir sorgu oluşturmasını istiyoruz.
        // Ve bu sorgu hangi context içerisinde kullanılacak bilgisini içerisinde gösteriyoruz.
        queue = Volley.newRequestQueue(getContext());
        //Verileri tutacağımız arraylistimizi oluşturduk.
        yaziBasliklari = new ArrayList<>();

        //ArrayAdapter'i hazırlıyoruz.
        adapter = new ArrayAdapter<String>( getContext() ,
                android.R.layout.simple_list_item_1, android.R.id.text1, yaziBasliklari);

        //Sorgunun gerçekleştiğinde yapacağı işlemleri yapalım.
        JsonObjectRequest getYaziRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new com.android.volley.Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arrYazilar= response.getJSONArray("Degerler");
                            for (int i = 0; i < arrYazilar.length(); i++){
                                JSONObject yazi = arrYazilar.getJSONObject(i);
                                tarih = yazi.getString("tarih");
                                metan = yazi.getString("metan");
                                butan = yazi.getString("butan");
                                propan = yazi.getString("propan");
                                temizHava = yazi.getString("temizHava");
                                co = yazi.getString("co");
                                // Log.d("YAZI BAŞLIK:",tarih);
                                yaziBasliklari.add(
                                        "Metan : " + metan +
                                                "\nBütan : " + butan +
                                                "\nPropan : " + propan +
                                                "\nCO : " + co +
                                                "\nTemiz Hava : " + temizHava +
                                                "\nTarih : " + tarih );

                                //listView için hazırladığımız adapter'i ayarlıyoruz.
                                listView.setAdapter(adapter);

                                adapter.notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );
        //Sorgu kuyruğuna gerçekleştirilmek üzere sorgumuzu veriyoruz.
        //Ve request işşlemi başlıyor.
        queue.add(getYaziRequest);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("..Detay Gösterim..");
                builder.setMessage( yaziBasliklari.get(position) );
                builder.setPositiveButton("OK",null);
                builder.show();


            }
        });






        return root;
    }



}