package com.example.apktienda1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.apktienda1.Utils.Chat;
import com.example.apktienda1.Utils.Messege;
import com.example.apktienda1.Utils.Methods;
import com.example.apktienda1.Utils.OnLoadImg;
import com.example.apktienda1.Utils.Producto;
import com.example.apktienda1.Utils.Query;
import com.example.apktienda1.Utils.TaskImg;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class productView extends AppCompatActivity implements OnLoadImg {
    ImageView imgView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(productView.this,homeCompras.class));
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(productView.this,homeVentas.class));
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    //startActivity(new Intent(productView.this,homeCompras.class));
                    //finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadComponents();
    }
    String idProduct;
    String idVendedor;
    Double Price;
    private void loadComponents() {
        Intent i=getIntent();
        String name=i.getExtras().getString("name");
        Double price=Price=i.getExtras().getDouble("price");
        String description=i.getExtras().getString("description");
        String id=idProduct=i.getExtras().getString("id");
        String idUser=idVendedor=i.getExtras().getString("idUser");
        int cant=i.getExtras().getInt("cant");
        String picture=i.getExtras().getString("picture");
        imgView=findViewById(R.id.imgView);
        TaskImg t=new TaskImg();
        t.setLoadImage(this,-1);
        Query query=new Query();
        query.add("img",picture);
        t.execute(utils.HOST+utils.DOWNLOAD_PRODUCT_IMG+query.getQuery());
        ((TextView)findViewById(R.id.nameView)).setText(name);
        ((TextView)findViewById(R.id.priceView)).setText(""+price+" Bs.");
        ((TextView)findViewById(R.id.cantView)).setText(""+cant);
        ((TextView)findViewById(R.id.descriptionView)).setText(description);

        Button comprar=findViewById(R.id.btnComprarView);
        Button chat=findViewById(R.id.btnChatVIew);
        if(idUser.equals(utils.idUSer)){
            comprar.setVisibility(View.INVISIBLE);
            chat.setVisibility(View.INVISIBLE);

        }else{
            comprar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(productView.this,Compra.class);
                    i.putExtra("idProduct",idProduct);
                    i.putExtra("idVendedor",idVendedor);
                    i.putExtra("price",Price);
                    startActivity(i);
                }
            });
            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chatWithVendedor();
                }
            });
        }


    }
    public void chatWithVendedor(){
        int exist=Methods.ChatConVendedorExiste(idProduct);
        if(exist==-1){
            AsyncHttpClient client=new AsyncHttpClient();
            client.addHeader("authorization",utils.TOKEN);
            RequestParams params=new RequestParams();
            params.add("idVendedor",idVendedor);
            params.add("idComprador",utils.idUSer);
            params.add("idProduct",idProduct);
            client.post(utils.HOST+utils.CHAT,params,new JsonHttpResponseHandler(){
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        Chat c=new Chat(
                                response.getString("_id"),
                                response.getString("idComprador"),
                                response.getString("idVendedor"),
                                response.getString("idProduct"),
                                new ArrayList<Messege>()
                        );
                        utils.CHATS_C.add(c);
                        Intent i=new Intent(productView.this,MessegesActivity.class);
                        i.putExtra("indexChat",utils.CHATS_C.size()-1);
                        i.putExtra("idChat",c.id);
                        startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }else{
            Intent i=new Intent(productView.this,MessegesActivity.class);
            i.putExtra("indexChat",exist);
            i.putExtra("idChat",utils.CHATS_C.get(exist).id);
            startActivity(i);
        }

    }

    @Override
    public void onLoadImg(int index, Bitmap img) {
        imgView.setImageBitmap(img);
    }
}
