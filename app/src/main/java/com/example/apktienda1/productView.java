package com.example.apktienda1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.apktienda1.Utils.OnLoadImg;
import com.example.apktienda1.Utils.Producto;
import com.example.apktienda1.Utils.Query;
import com.example.apktienda1.Utils.TaskImg;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    private void loadComponents() {
        Intent i=getIntent();
        String name=i.getExtras().getString("name");
        Double price=i.getExtras().getDouble("price");
        String description=i.getExtras().getString("description");
        String id=i.getExtras().getString("id");
        String idUser=i.getExtras().getString("idUser");
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

        }
    }


    @Override
    public void onLoadImg(int index, Bitmap img) {
        imgView.setImageBitmap(img);
    }
}
