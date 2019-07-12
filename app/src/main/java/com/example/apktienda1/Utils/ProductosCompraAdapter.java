package com.example.apktienda1.Utils;


import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apktienda1.homeCompras;
import com.example.apktienda1.homeVentas;
import com.example.apktienda1.R;
import com.example.apktienda1.productView;
import com.example.apktienda1.utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ProductosCompraAdapter extends BaseAdapter {
    private Context CONTEXT;

    private ArrayList<Producto> lista;
    public ProductosCompraAdapter(Context cont,ArrayList<Producto> l){
        CONTEXT=cont;
        lista=l;
    }
    private int ProductPosition;
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
            view=inflater.inflate(R.layout.producto,null);
        }
        TextView name=(TextView) view.findViewById(R.id.namep);
        TextView price=(TextView)view.findViewById(R.id.preciop);
        ImageView img=(ImageView)view.findViewById(R.id.imgp);
        name.setText(lista.get(i).getName());
        price.setText(""+lista.get(i).getPrice()+" Bs.");
        if(img!=null) {
            img.setImageBitmap(lista.get(i).getImg());
        }
        OnClicList onClicProducView=new OnClicList(lista.get(i));
        img.setOnClickListener(onClicProducView);
        return view;
    }
    private class OnClicList implements View.OnClickListener {
        Producto producto;
        public OnClicList(Producto p){
            producto=p;
        }
        @Override
        public void onClick(View view) {
            Intent i=new Intent(CONTEXT.getApplicationContext(), productView.class);
            i.putExtra("id",producto.getId());
            i.putExtra("idUser",producto.getIdUser());
            i.putExtra("name",producto.getName());
            i.putExtra("description",producto.getDescription());
            i.putExtra("picture",producto.getPicture());
            i.putExtra("price",producto.getPrice());
            i.putExtra("cant",producto.getCant());
           // i.putExtra("img",producto.getImg());
            CONTEXT.getApplicationContext().startActivity(i);
        }
    }
}
