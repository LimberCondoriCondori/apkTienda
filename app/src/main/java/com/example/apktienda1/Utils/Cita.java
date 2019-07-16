package com.example.apktienda1.Utils;

import com.example.apktienda1.utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Cita implements OnMapReadyCallback {
    String idCompra,nameP="",date,time,cant="",lugar,estado;
    double lat;

    public Cita(String nameP, String date, String time, String cant, String lugar, String estado, double lat, double lng) {
        this.nameP = nameP;
        this.date = date;
        this.time = time;
        this.cant = cant;
        this.lugar = lugar;
        this.estado = estado;
        this.lat = lat;
        this.lng = lng;
    }

    double lng;
    public Cita(JSONObject o){
        try{
            
            date=o.getString("date");
            time=o.getString("time");
            lugar=o.getString("street");
            estado=o.getString("state");
            lat=o.getDouble("lat");
            lng=o.getDouble("long");
            idCompra=o.getString("idCompra");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadCant() {
        AsyncHttpClient client =new AsyncHttpClient();
        client.addHeader("authorization", utils.TOKEN);
        Query q=new Query();
        q.add("_id",idCompra);
        client.get(utils.HOST+utils.COMPRA+q.getQuery(),new RequestParams(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    cant=response.getJSONObject(0).getString("cantidad");
                    String idProduct=response.getJSONObject(0).getString("idProducto");
                    loadNameProduct(idProduct);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadNameProduct(String idProduct) {
        AsyncHttpClient client=new AsyncHttpClient();
        client.addHeader("authorization",utils.TOKEN);
        Query q=new Query();
        q.add("_id",idProduct);
        client.get(utils.HOST+utils.PRODUCT+q.getQuery(),new RequestParams(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try{
                    nameP=response.getJSONObject(0).getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private GoogleMap mMap;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng posicion = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(posicion).title("Lugar").zIndex(18).draggable(true));
        mMap.setMinZoomPreference(15);
    }
}
