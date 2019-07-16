package com.example.apktienda1.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.apktienda1.MessegesActivity;
import com.example.apktienda1.R;
import com.example.apktienda1.utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CitasAdapter extends BaseAdapter{
    private Context CONTEXT;
    Bundle saveInstance;
    private ArrayList<Cita> lista;
    public CitasAdapter(Context cont,ArrayList<Cita> l,Bundle s){
        CONTEXT=cont;
        lista=l;
        saveInstance=s;
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
        if(view==null){
            LayoutInflater inflater=(LayoutInflater) CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.view_model_cita,null);
        }

        TextView nameP=view.findViewById(R.id.nameCP);
        TextView date=view.findViewById(R.id.dateCP);
        TextView time=view.findViewById(R.id.timeCP);
        TextView cant=view.findViewById(R.id.cantCP);
        TextView lugar=view.findViewById(R.id.lugarCP);
        TextView estado=view.findViewById(R.id.estadoCP);

        /*MapView map=view.findViewById(R.id.mapview_view_citas);
        map.onCreate(saveInstance);
        map.onResume();
        map.getMapAsync(lista.get(i));
    */
        nameP.setText(lista.get(i).nameP);
        date.setText(lista.get(i).date);
        time.setText(lista.get(i).time);
        cant.setText(lista.get(i).cant);
        lugar.setText(lista.get(i).lugar);
        estado.setText(lista.get(i).estado);

        return view;
    }

}
