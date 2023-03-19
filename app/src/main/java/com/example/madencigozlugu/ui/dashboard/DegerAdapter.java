package com.example.madencigozlugu.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.madencigozlugu.R;
import com.example.madencigozlugu.VeriTabani;

import java.util.ArrayList;

public class DegerAdapter extends ArrayAdapter<DegerClass> {

    VeriTabani vt;
    private Context context;
    int res;

    Float temizHava2,metan2,butan2,propan2,co2;
    String tarih2;
    int userid2,did2;


    public DegerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DegerClass> objects) {
        super(context, resource, objects);
        this.context = context;
        this.res= resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        vt=new VeriTabani(getContext()); // veritabanı bağlantısı
        //kisi bilgileri getir
        did2=getItem(position).getDid();
        userid2 = getItem(position).getUserid();
        temizHava2 = getItem(position).getTemizHava();
        metan2 = getItem(position).getMetan();
        butan2=getItem(position).getButan();
        propan2 = getItem(position).getPropan();
        co2 = getItem(position).getCo();
        tarih2=getItem(position).getTarih();

        //Bilgileri ile birlikte yeni bir kişi oluşturun

        DegerClass kitt=new DegerClass( did2, metan2,  butan2,  propan2, temizHava2,   co2,  userid2,  tarih2 );

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(res,parent,false);

        TextView metana = convertView.findViewById(R.id.metan);
        TextView butana = convertView.findViewById(R.id.butan);
        TextView propana = convertView.findViewById(R.id.propan);

        TextView temizHavaa = convertView.findViewById(R.id.temizHava);
        TextView coa = convertView.findViewById(R.id.co);
        TextView tariha = convertView.findViewById(R.id.tarih);
        TextView dida = convertView.findViewById(R.id.dida);
        TextView userida = convertView.findViewById(R.id.userida);


        dida.setText("Değer ID : " + String.valueOf(did2));
        metana.setText("Metan Değeri : " + String.valueOf(metan2));
        butana.setText("Bütan Değeri : " + String.valueOf(butan2));
        propana.setText("Propan Değeri : " + String.valueOf(propan2));
        coa.setText("CO Değeri : " + String.valueOf(co2));
        temizHavaa.setText("Temiz Hava Değeri : " + String.valueOf(temizHava2));


        userida.setText("USERID: " + String.valueOf(userid2));

        tariha.setText("Tarih : " + tarih2);

        return  convertView;
    }
}
