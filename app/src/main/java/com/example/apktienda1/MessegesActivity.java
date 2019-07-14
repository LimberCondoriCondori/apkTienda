package com.example.apktienda1;

import android.content.Intent;
import android.os.Bundle;

import com.example.apktienda1.Utils.Messege;
import com.example.apktienda1.Utils.msnsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MessegesActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(MessegesActivity.this,homeCompras.class));
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(MessegesActivity.this,homeVentas.class));
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    startActivity(new Intent(MessegesActivity.this,ChatsActivity.class));
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messeges);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadComponents();
    }
    Intent i;
    ImageButton send;
    ListView list;
    TextView nameUser;
    TextView nameProduct;
    TextView message;
    String idChat;
    int indexChat;
    private void loadComponents() {
        i=getIntent();
        idChat=i.getExtras().getString("idChat");
        indexChat=i.getExtras().getInt("indexChat");
        send=findViewById(R.id.btnSendM);
        message=findViewById(R.id.msntxtM);
        class onClicSend implements View.OnClickListener{
            Messege m;

            public onClicSend(Messege m) {
                this.m = m;
            }

            @Override
            public void onClick(View view) {
                m.msn=message.getText().toString();
                utils.mSocket.emit("msnserver",m.getJSONobjetMsnSend());
            }
        }
        send.setOnClickListener(new onClicSend(new Messege(utils.idUSer,"",idChat)));
        list=findViewById(R.id.listViewMsns);
        nameUser=findViewById(R.id.nameOtherUser);
        nameProduct=findViewById(R.id.nameProductMsn);
        loadMsns();
    }

    private void loadMsns() {
        if(utils.CHATS_V.size()>indexChat){
            if(utils.CHATS_V.get(indexChat).id.equals(idChat)){
                nameUser.setText(utils.CHATS_V.get(indexChat).fullNameC);
                nameProduct.setText(utils.CHATS_V.get(indexChat).nameP);
                //adapter
                list.setAdapter(new msnsAdapter(this,utils.CHATS_V.get(indexChat)));
            }
        }
        if(utils.CHATS_C.size()>indexChat){
            if(utils.CHATS_C.get(indexChat).id.equals(idChat)){
                nameUser.setText(utils.CHATS_C.get(indexChat).fullNameV);
                nameProduct.setText(utils.CHATS_C.get(indexChat).nameP);
                //adapter

                list.setAdapter(new msnsAdapter(this,utils.CHATS_C.get(indexChat)));
            }
        }
    }

}
