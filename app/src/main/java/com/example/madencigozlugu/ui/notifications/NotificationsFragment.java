package com.example.madencigozlugu.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.madencigozlugu.MainActivity;
import com.example.madencigozlugu.R;
import com.example.madencigozlugu.alt_menu;
import com.example.madencigozlugu.databinding.FragmentNotificationsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationsFragment extends Fragment {


    EditText uMail,uName,uTel,uSifre,uID,uSirket;
    Button giris;
    private String URL = "https://madencigozlugu.com.tr/api/uyelerServis.php?token=emre";
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        uMail=(EditText) root.findViewById(R.id.uuMail);
        uName=(EditText) root.findViewById(R.id.uuName);
        uTel=(EditText) root.findViewById(R.id.uuTel);
        uSifre=(EditText) root.findViewById(R.id.uuSifre);
        uID=(EditText) root.findViewById(R.id.uuid);
        uSirket=(EditText) root.findViewById(R.id.uuSirket);

        uyebilgiCek();

        giris=(Button) root.findViewById(R.id.ugiris);

        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uMail.getText().toString().matches("") || uName.getText().toString().matches("") ||
                        uSifre.getText().toString().matches("") || uTel.getText().toString().matches("")){
                    Toast.makeText(getContext(), "BOŞ BIRAKMAYINIZ!", Toast.LENGTH_LONG).show();
                } else {
                    //--------volley put işlemi----------------

                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                    JSONObject putData = new JSONObject();
                    try {
                        putData.put("umail", uMail.getText().toString());
                        putData.put("usifre", uSifre.getText().toString());
                        putData.put("uisim", uName.getText().toString());
                        putData.put("utel", uTel.getText().toString());
                        putData.put("usirket", uSirket.getText().toString());
                        putData.put("uid", uID.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, URL,
                            putData, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

           Toast.makeText(getContext(), "BAŞARIYLA DÜZENLENDİ!\nLütfen Tekrar Giriş Yapınız...", Toast.LENGTH_LONG).show();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

                    requestQueue.add(jsonObjectRequest);

                    //------------------volley put bitimi------------
                }

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void uyebilgiCek(){
        uMail.setText(alt_menu.u_mail);
        uName.setText(alt_menu.u_isim);
        uTel.setText(alt_menu.u_tel);
        uSifre.setText(alt_menu.u_sifre);
        uID.setText(String.valueOf(alt_menu.u_id));
        uSirket.setText(String.valueOf(alt_menu.u_sirket));
    }


}