package com.example.apktienda1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apktienda1.Utils.Query;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    public Button login;
    public Button registerUser;

    GoogleApiClient client;
    private int GOOGLE_CODE=11235;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //icono accion bar
         getSupportActionBar().setDisplayShowHomeEnabled(true);
         getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions options=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        client=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,options)
                .build();
        SignInButton loginWhitGoogle;
        loginWhitGoogle = (SignInButton)findViewById(R.id.signInButton);
        loginWhitGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=Auth.GoogleSignInApi.getSignInIntent(client);
                startActivityForResult(intent,GOOGLE_CODE);
            }
        });


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
               startActivity(new Intent(getApplicationContext(),registerUser.class));
                finish();
            }
        });


    }



    public void loginClient(){
        AsyncHttpClient client= new AsyncHttpClient();
        RequestParams params=new RequestParams();
        String url="";

        TextView email = findViewById(R.id.email);
        TextView password = findViewById(R.id.password);
        url = utils.HOST + utils.LOGIN;
        params.add("email", email.getText().toString());
        params.add("password", password.getText().toString());

        client.post(url,params,new JsonHttpResponseHandler(){

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
                            finish();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GOOGLE_CODE){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){

                String firstname=result.getSignInAccount().getGivenName();
                String surname=result.getSignInAccount().getFamilyName();
                String email=result.getSignInAccount().getEmail();
                String pass=result.getSignInAccount().getId();

                loginWithGoogle(firstname,surname,email,pass);

            }else{
                Toast.makeText(this,""+result.isSuccess(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loginWithGoogle(String fn,String sn,String e,String p) {
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.add("email",e);
        params.add("firstname",fn);
        params.add("surname",sn);
        params.add("password",p);
        client.post(utils.HOST+utils.LOGIN_WITH_GOOGLE,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try{
                    utils.idUSer=response.getString("idUser");
                    utils.TOKEN="data "+response.getString("token");
                    AlertDialog al=new AlertDialog.Builder(MainActivity.this).create();
                    al.setTitle("RESPONSE SERVER");
                    al.setMessage("Sesion Iniciada");
                    al.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent m=new Intent(getApplicationContext(),homeCompras.class);
                            //m.finishOnTaskLaunch(true);
                            startActivity(m);
                            finish();
                        }
                    });
                    al.show();
                }catch(JSONException jse){
                    jse.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
