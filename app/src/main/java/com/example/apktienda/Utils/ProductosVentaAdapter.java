package com.example.apktienda.Utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apktienda.homeVentas;
import com.example.apktienda.utils.*;
import com.example.apktienda.R;
import com.example.apktienda.utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ProductosVentaAdapter extends BaseAdapter {
    private Context CONTEXT;

    private ArrayList<Producto> lista;
    public ProductosVentaAdapter(Context cont,ArrayList<Producto> l){
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
            view=inflater.inflate(R.layout.producto_vendedor,null);
        }
        TextView name=(TextView) view.findViewById(R.id.namePV);
        TextView cant=(TextView)view.findViewById(R.id.cantPV);
        ImageView img=(ImageView)view.findViewById(R.id.imgPV);
        Button btnEdit=view.findViewById(R.id.editPV);
        Button btnDelete=view.findViewById(R.id.deletePV);
        Query query=new Query();
        query.add("img",lista.get(i).getPicture());
        name.setText(lista.get(i).getName());
        cant.setText(""+lista.get(i).getCant());
        if(img!=null)
            img.setImageBitmap(lista.get(i).getImg());

        /*
        Editar Producto
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //enviar a la actividad editar con la id del producto

            }
        });*/
        OnClicList onClicEdit=new OnClicList(lista.get(i).getId());
        btnDelete.setOnClickListener(onClicEdit);
        return view;
    }
    private class OnClicList implements View.OnClickListener {
        String idP;
        public OnClicList(String id){
            idP=id;
        }
        @Override
        public void onClick(View view) {
            AsyncHttpClient client=new AsyncHttpClient();
            client.addHeader("authorization",utils.TOKEN);
            Query query1=new Query();
            query1.add("id",idP);
            client.delete(utils.HOST+utils.PRODUCT+query1.getQuery(),new JsonHttpResponseHandler(){
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Toast t=Toast.makeText(CONTEXT,"Producto Eliminado!!!!",Toast.LENGTH_LONG);
                    t.show();
                    CONTEXT.startActivity(new Intent(CONTEXT, homeVentas.class));
                }
            });
        }
    }
}
