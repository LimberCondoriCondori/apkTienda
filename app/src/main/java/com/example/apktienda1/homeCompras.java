package com.example.apktienda1;

import android.content.Intent;
import android.os.Bundle;

import com.example.apktienda1.Utils.OnLoadAllList;
import com.example.apktienda1.Utils.ProductList;
import com.example.apktienda1.Utils.Producto;
import com.example.apktienda1.Utils.ProductosCompraAdapter;
import com.example.apktienda1.Utils.ProductosVentaAdapter;
import com.example.apktienda1.Utils.Query;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class homeCompras extends AppCompatActivity implements OnLoadAllList {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(homeCompras.this,homeCompras.class));
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(homeCompras.this,homeVentas.class));
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    startActivity(new Intent(homeCompras.this,ChatsActivity.class));
                    return true;
            }
            return false;
        }
    };

    GridView gridListView;
    ProductList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_compras);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_home);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        RadioButton rbtec=findViewById(R.id.btnbuscartecnologia);
        rbtec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProducts("tecnologia");
            }
        });
        RadioButton rbelec=findViewById(R.id.btnbuscarelectro);
        rbelec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProducts("electrodomesticos");
            }
        });
        RadioButton rbvestir=findViewById(R.id.btnbuscarvestir);
        rbvestir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProducts("vestir");
            }
        });
        gridListView=findViewById(R.id.gridLawoutProducts);
        loadProducts("");
    }

    private void loadProducts(String categoria) {
        list=new ProductList(this);
        AsyncHttpClient client=new AsyncHttpClient();
        Query query=new Query();
        if(!categoria.equals("")){
            query.add("categoria",categoria);
        }

        client.get(utils.HOST+utils.PRODUCT+query.getQuery(),new RequestParams(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for(int i=0;i<response.length();i++){
                    try {
                        JSONObject jsonO;
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
                        if(!p.getIdUser().equals(utils.idUSer))
                            list.addAndLoadImg(p);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

                ProductosCompraAdapter pva=new ProductosCompraAdapter(homeCompras.this,list);
                gridListView.setAdapter(pva);
            }
        });
    }

    @Override
    public void onLoadAllList() {
        ProductosCompraAdapter pva=new ProductosCompraAdapter(homeCompras.this,list);
        gridListView.setAdapter(pva);
    }
}
