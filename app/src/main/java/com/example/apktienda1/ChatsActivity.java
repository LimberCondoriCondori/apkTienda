package com.example.apktienda1;

import android.content.Intent;
import android.os.Bundle;

import com.example.apktienda1.Utils.Chat;
import com.example.apktienda1.Utils.chatsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatsActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(ChatsActivity.this,homeCompras.class));
                    finish();
                    return true;
                case R.id.navigation_dashboard:

                    startActivity(new Intent(ChatsActivity.this,homeVentas.class));
                    finish();
                    return true;
                case R.id.navigation_notifications:

                    startActivity(new Intent(ChatsActivity.this,ChatsActivity.class));
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_notifications);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        titulo=findViewById(R.id.tituloChats);
        RadioButton rbCC=findViewById(R.id.btnChatsC);
        rbCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titulo.setText("Chats Comprador");
                loadComponent(utils.CHATS_C);
            }

        });
        RadioButton rbCV=findViewById(R.id.btnchatsV);
        rbCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titulo.setText("Chats Vendedor");
                loadComponent(utils.CHATS_V);
            }
        });
        titulo.setText("Chats Comprador");
        loadComponent(utils.CHATS_C);
    }
    TextView titulo;
    ListView list;
    private void loadComponent(ArrayList<Chat> chats) {

        list=findViewById(R.id.listViewChats);
        list.setAdapter(new chatsAdapter(this,chats));
    }

}
