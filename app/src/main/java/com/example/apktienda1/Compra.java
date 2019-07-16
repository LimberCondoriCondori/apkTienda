package com.example.apktienda1;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.session.MediaSession;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class Compra extends AppCompatActivity implements OnMapReadyCallback {
    private MapView map;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private LatLng mainposition;
    private TextView street;
    Intent i;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(Compra.this,homeCompras.class));
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(Compra.this,homeVentas.class));
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    startActivity(new Intent(Compra.this,ChatsActivity.class));
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        map = findViewById(R.id.mapCita);
        map.onCreate(savedInstanceState);
        map.onResume();
        i=getIntent();
        ((TextView)findViewById(R.id.nameProductC)).setText(i.getExtras().getString("nameProduct"));
        Button comprar=findViewById(R.id.btnComprarP);
        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCompra();
            }
        });
        MapsInitializer.initialize(this);
        map.getMapAsync(this);
        geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        street = findViewById(R.id.streetCita);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //-19.5597641,-65.7633884
        //-19.5730936,-65.7559122
        LatLng potosi = new LatLng(-19.5730936, -65.7559122);
        mainposition = potosi;
        mMap.addMarker(new MarkerOptions().position(potosi).title("Lugar").zIndex(18).draggable(true));
        mMap.setMinZoomPreference(15);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(potosi));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                mainposition = marker.getPosition();
                String street_string = getStreet(marker.getPosition().latitude, marker.getPosition().longitude);
                street.setText(street_string);
            }
        });
    }

    public String getStreet (Double lat, Double log) {
        List<Address> addresses;
        String result = "";
        try {
            addresses = geocoder.getFromLocation(lat, log, 1);
            result += addresses.get(0).getThoroughfare();

            /*for (int i = 0; i < addresses.size(); i++) {
                if (addresses.get(i).getThoroughfare()!=null)
                result += addresses.get(i).getThoroughfare() + ",";
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }
    String idCompra;
    private void saveCompra(){
        final String idComprador=utils.idUSer;
        String idVendedor=i.getExtras().getString("idVendedor");
        String idProducto=i.getExtras().getString("idProducto");
        Double price=i.getExtras().getDouble("price");
        int cantidad=Integer.parseInt(((TextView)findViewById(R.id.cantCompra)).getText().toString());
        double pagoTotal=price*cantidad;
        AsyncHttpClient client =new AsyncHttpClient();
        client.addHeader("authorization",utils.TOKEN);
        RequestParams params=new RequestParams();
        params.add("idVendedor",idVendedor);
        params.add("idComprador",idComprador);
        params.add("idProducto",idProducto);
        params.add("cantidad",""+cantidad);
        params.add("pagoTotal",""+pagoTotal);
        client.post(utils.HOST+utils.COMPRA,params,new JsonHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                     idCompra=response.getString("id");
                     saveCita();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void saveCita(){
        AsyncHttpClient client=new AsyncHttpClient();
        client.addHeader("authorization",utils.TOKEN);
        RequestParams params=new RequestParams();
        params.add("idCompra",idCompra);
        params.add("date",((TextView)findViewById(R.id.fechaCita)).getText().toString());
        params.add("time",((TextView)findViewById(R.id.timeCita)).getText().toString());
        params.add("street",((TextView)findViewById(R.id.streetCita)).getText().toString());
        params.add("lat", String.valueOf(mainposition.latitude));
        params.add("long", String.valueOf(mainposition.longitude));
        client.post(utils.HOST+utils.CITA,params,new JsonHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    final AlertDialog a=new AlertDialog.Builder(Compra.this).create();
                    a.setTitle("SERVER!!!");
                    a.setMessage(errorResponse.getString("msn"));
                    a.setButton(10,"OK.",new AlertDialog.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            a.dismiss();
                        }
                    });
                    a.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                final AlertDialog a=new AlertDialog.Builder(Compra.this).create();
                a.setTitle("SERVER!!!");
                try {
                    String msn=response.getString("msn");
                    a.setMessage("compra y "+msn);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                a.setButton(android.app.AlertDialog.BUTTON_NEUTRAL,"OK",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(Compra.this,homeCompras.class));
                        Compra.this.finish();
                    }
                });

                a.show();
            }
        });
    }
}
