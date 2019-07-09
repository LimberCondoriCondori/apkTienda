package com.example.apktienda1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.loopj.android.http.JsonHttpResponseHandler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class registerUser extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Button register=findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regitrar();
            }
        });
    }
    private void regitrar() {
        String firstname=((TextView)findViewById(R.id.firstaname)).getText().toString();
        String surname=((TextView)findViewById(R.id.surname)).getText().toString();
        String email=((TextView)findViewById(R.id.email)).getText().toString();
        String password=((TextView)findViewById(R.id.password)).getText().toString();
        String password_c=((TextView)findViewById(R.id.password_c)).getText().toString();
        String phone=((TextView)findViewById(R.id.phone)).getText().toString();
        if(password.equals(password_c)){
            AsyncHttpClient client= new AsyncHttpClient();
            RequestParams params=new RequestParams();
            params.add("firstname",firstname);
            params.add("surname",surname);
            params.add("email",email);
            params.add("password",password);
            params.add("phone",phone);

            client.post(utils.HOST+utils.REGISTER_USER,params,new JsonHttpResponseHandler(){


                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    AlertDialog alertDialog = new AlertDialog.Builder(registerUser.this).create();
                    try{
                        String msn=errorResponse.getString("msn");
                        alertDialog.setTitle("RESPONSE SERVER");
                        alertDialog.setMessage(msn);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    AlertDialog alertDialog = new AlertDialog.Builder(registerUser.this).create();
                    try{
                        alertDialog.setTitle("RESPONSE SERVER");
                        alertDialog.setMessage("Usuario Registrado");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }
                        });
                        alertDialog.show();
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            });
        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(registerUser.this).create();
            //String token=responseString;
            alertDialog.setTitle("Error!!!");
            alertDialog.setMessage("Las contraseñas no coinsiden");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }

    }

}
