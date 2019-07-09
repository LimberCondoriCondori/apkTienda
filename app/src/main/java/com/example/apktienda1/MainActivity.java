package com.example.apktienda1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    public Button login;
    public Button registerUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loginClient();
            }
        });
        registerUser= findViewById(R.id.register);
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(getApplicationContext(),registerUser.class));
            }
        });


    }
    public void loginClient(){
        TextView email=findViewById(R.id.email);
        TextView password= findViewById(R.id.password);
        AsyncHttpClient client= new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.add("email",email.getText().toString());
        params.add("password",password.getText().toString());

        client.post(utils.HOST+utils.LOGIN,params,new JsonHttpResponseHandler(){

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                //String token=responseString;
                alertDialog.setTitle("RESPONSE SERVER");
                alertDialog.setMessage("fallo");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                try{
                    String token=response.getString("token");
                    String idUser=response.getString("idUser");
                    alertDialog.setTitle("RESPONSE SERVER");
                    alertDialog.setMessage("Sesion Iniciada");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(getApplicationContext(),homeCompras.class));
                        }
                    });
                    alertDialog.show();
                    utils.TOKEN="data "+token;
                    utils.idUSer=idUser;
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        //email.setText(Data.TOKEN);
    }
}
