package com.example.apktienda1;

import android.content.Intent;
import android.os.Bundle;

import com.example.apktienda1.Utils.OnLoadAllList;
import com.example.apktienda1.Utils.ProductList;
import com.example.apktienda1.Utils.Producto;
import com.example.apktienda1.Utils.ProductosVentaAdapter;
import com.example.apktienda1.Utils.Query;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class homeVentas extends AppCompatActivity implements OnLoadAllList {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(homeVentas.this,homeCompras.class));
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(homeVentas.this,homeVentas.class));
                    finish();
                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_ventas);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setSelectedItemId(R.id.navigation_dashboard);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadComponets();
    }
    private ListView listViewP;
    ProductList list;
    private void loadComponets() {
        Button btnNuevo=findViewById(R.id.btnNuevoP);
        btnNuevo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),NuevoProducto.class));
                finish();
            }
        });

        listViewP=findViewById(R.id.listViewPV);
        list=new ProductList(this);
        cargarProductos();
        //listViewP.setAdapter(new ProductosVentaAdapter(this,list));
    }
    public void cargarProductos(){
        AsyncHttpClient client=new AsyncHttpClient();
        client.addHeader("authorization",utils.TOKEN);
        Query query=new Query();
        query.add("idUser",utils.idUSer);
        client.get(utils.HOST+utils.PRODUCT+"user"+query.getQuery(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for(int i=0;i<response.length();i++) {
                    JSONObject jsonO = null;
                    try {
                        jsonO = response.getJSONObject(i);
                        Producto p = new Producto(
                                jsonO.getString("name"),
                                jsonO.getString("description"),
                                jsonO.getString("idUser"),
                                jsonO.getString("_id"),
                                jsonO.getString("picture"),
                                Double.parseDouble(jsonO.getString("price").toString()),
                                Integer.parseInt(jsonO.getString("cant"))

                        );
                        list.addAndLoadImg(p);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ProductosVentaAdapter pva=new ProductosVentaAdapter(homeVentas.this,list);
                listViewP.setAdapter(pva);

            }

        });
    }

    @Override
    public void onLoadAllList() {
        ProductosVentaAdapter pva=new ProductosVentaAdapter(homeVentas.this,list);
        listViewP.setAdapter(pva);
    }
}
