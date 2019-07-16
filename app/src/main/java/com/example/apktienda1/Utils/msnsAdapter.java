package com.example.apktienda1.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.apktienda1.MessegesActivity;
import com.example.apktienda1.R;
import com.example.apktienda1.utils;

import java.util.ArrayList;

public class msnsAdapter extends BaseAdapter {
    private Context CONTEXT;
    private Chat c;
    private ArrayList<Messege> lista;
    public msnsAdapter(Context cont,Chat c){
        CONTEXT=cont;
        this.c=c;
        lista=c.getMsns();
    }
    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(lista.get(i).idUser.equals(utils.idUSer)){
            //if(view==null){
                LayoutInflater inflater=(LayoutInflater) CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view=inflater.inflate(R.layout.view_model_msn_your,null);
            //}
            TextView name=(TextView) view.findViewById(R.id.nametxtMyour);
            TextView msn=(TextView)view.findViewById(R.id.msntxtMyour);
            TextView date=(TextView) view.findViewById(R.id.datetxtMyour);
            if(c.idVendedor.equals(lista.get(i).idUser)) {
                name.setText(c.fullNameV);
            }
            else
                name.setText(c.fullNameC);

            msn.setText(lista.get(i).msn);
            date.setText(lista.get(i).registerDate);
            return view;
        }
        else{
            //if(view==null){
                LayoutInflater inflater=(LayoutInflater) CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view=inflater.inflate(R.layout.view_model_msn_his,null);
            //}
            TextView name=(TextView) view.findViewById(R.id.nametxtMhis);
            TextView msn=(TextView)view.findViewById(R.id.msntxtMhis);
            TextView date=(TextView) view.findViewById(R.id.datetxtMhis);
            if(c.idVendedor.equals(lista.get(i).idUser))
                name.setText(c.fullNameV);
            else
                name.setText(c.fullNameC);

            msn.setText(lista.get(i).msn);
            date.setText(lista.get(i).registerDate);
            return view;
        }
    }
}
