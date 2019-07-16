package com.example.apktienda1.Utils;

import com.example.apktienda1.utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Chat {
    public String id,idComprador,idVendedor, idProduct;
    public String fullNameC,fullNameV,nameP;
    private ArrayList<Messege> msns;
    public boolean alerta=false;
    public Chat(String id,String idComprador, String idVendedor, String idProducto,ArrayList<Messege> msn) {
        this.id=id;
        this.idComprador = idComprador;
        this.idVendedor = idVendedor;
        this.idProduct = idProducto;
        msns=msn;
        fullNameC=fullNameV=nameP=null;
        checMsnNoLeidos();
        loadNames();
    }
    public void loadNames(){
        AsyncHttpClient client =new AsyncHttpClient();
        Query q=new Query();
        q.add("_id",idComprador);
        client.addHeader("authorization", utils.TOKEN);
        client.get(utils.HOST+utils.USER+q.getQuery(),new RequestParams(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    fullNameC=response.getJSONObject(0).getString("firstname")+" "+response.getJSONObject(0).getString("surname");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        AsyncHttpClient clientv =new AsyncHttpClient();
        Query qv=new Query();
        qv.add("_id",idVendedor);
        clientv.addHeader("authorization", utils.TOKEN);
        clientv.get(utils.HOST+utils.USER+qv.getQuery(),new RequestParams(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    fullNameV=response.getJSONObject(0).getString("firstname")+" "+response.getJSONObject(0).getString("surname");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        //nameP="ejemplo1";

        AsyncHttpClient clientp =new AsyncHttpClient();
        Query qp=new Query();
        qp.add("_id",idProduct);
        clientp.addHeader("authorization", utils.TOKEN);
        clientp.get(utils.HOST+utils.PRODUCT+qp.getQuery(),new RequestParams(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    nameP=response.getJSONObject(0).getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void addMessege(Messege m){
        msns.add(m);
        if(!m.leido&&(!utils.idUSer.equals(m.idUser))){
            alerta=true;
        }
    }
    public ArrayList<Messege> getMsns() {

        for(int i=msns.size()-1;i>=0;i--){
            if(!msns.get(i).leido) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put("id", msns.get(i).id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                utils.mSocket.emit("showMsn", jo);
            }
        }
        alerta=false;
        return msns;
    }
    public void checMsnNoLeidos(){
        for(int i=0;i<msns.size();i++)
            if(!msns.get(i).leido){
                alerta=true;
            }
    }
}
