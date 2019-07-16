package com.example.apktienda1;

import android.content.Intent;
import android.os.Bundle;

import com.example.apktienda1.Utils.Cita;
import com.example.apktienda1.Utils.CitasAdapter;
import com.example.apktienda1.Utils.Query;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ViewCitas extends AppCompatActivity {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(ViewCitas.this,homeCompras.class));
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(ViewCitas.this,homeVentas.class));
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    startActivity(new Intent(ViewCitas.this,ChatsActivity.class));
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_citas);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_dashboard);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        savedInstanceState=savedInstanceState;
        loadComponents();
    }
    Bundle saveInstance;
    ListView list;
    ArrayList<Cita> citas;
    private void loadComponents() {
        list=findViewById(R.id.listViewCitas);
        citas=new ArrayList<Cita>();
        getCompras();

        list.setAdapter(new CitasAdapter(this,citas,saveInstance));
    }
    public void getCompras(){
        AsyncHttpClient client =new AsyncHttpClient();
        client.addHeader("authorization",utils.TOKEN);
        Query q =new Query();
        q.add("idVendedor",utils.idUSer);
        client.get(utils.HOST+utils.COMPRA+q.getQuery(),new RequestParams(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    for(int i=0;i<response.length();i++){
                        String idCompra=response.getJSONObject(i).getString("_id");
                        //getcita(idCompra);
                        AsyncHttpClient clientc =new AsyncHttpClient();
                        clientc.addHeader("authorization",utils.TOKEN);
                        Query q =new Query();
                        q.add("idCompra",idCompra);
                        clientc.get(utils.HOST+utils.CITA+q.getQuery(),new RequestParams(),new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {


                                try {
                                    //System.out.println("tamaño "+response.length()+"<-------------------------------------------------------------");
                                    Cita e=new Cita(response.getJSONObject(0));
                                    e.loadCant();
                                    citas.add(e);
                                    CitasAdapter ca=new CitasAdapter(ViewCitas.this,citas,saveInstance);
                                    list.setAdapter(ca);
                                } catch (JSONException e) {

                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void getcita(String idCompra){

    }

}
