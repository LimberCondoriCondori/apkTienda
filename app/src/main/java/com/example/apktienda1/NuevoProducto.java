package com.example.apktienda1;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apktienda1.Utils.Query;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class NuevoProducto extends AppCompatActivity {
    private TextView mTextMessage;

    private int CODE_CAMERA=110;
    private int CODE_GALERY=111;
    private ImageView IMG;
    private String path=null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_producto);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        reviewPermissions();
        Button guardar=findViewById(R.id.btnSaveNP);
        IMG=findViewById(R.id.imgNP);
        IMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(NuevoProducto.this).create();
                //String token=responseString;
                alertDialog.setTitle("SELECT");
                alertDialog.setMessage("Seleccione el metodo");
                alertDialog.setButton("CAMARA",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),CODE_CAMERA);
                    }
                });
                alertDialog.setButton2("GALERIA",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(Intent.createChooser(intent,"Seleccione la Aplicacion"),CODE_GALERY);
                    }
                });
                alertDialog.show();

            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(path!=null)
                    save();
                else {
                    Toast t = Toast.makeText(NuevoProducto.this, "Producto creado exitosamente", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
    }

    private void save() {
        String name=((TextView)findViewById(R.id.nameNP)).getText().toString();
        String description=((TextView)findViewById(R.id.descriptionNP)).getText().toString();
        String price=((TextView)findViewById(R.id.priceNP)).getText().toString();
        String cant=((TextView)findViewById(R.id.cantNP)).getText().toString();
        AsyncHttpClient client= new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.add("idUser",utils.idUSer);
        params.add("name",name);
        params.add("description",description);
        params.add("price",price);
        params.add("cant",cant);
        //token
        String par="";
        client.addHeader("authorization", utils.TOKEN);
        client.post(utils.HOST+utils.PRODUCT,params,new JsonHttpResponseHandler(){


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();

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
                AlertDialog alertDialog = new AlertDialog.Builder(NuevoProducto.this).create();
                try{
                    String id=response.getString("id");
                    //imagen para el producto
                    Toast t=Toast.makeText(NuevoProducto.this,"Producto creado exitosamente", Toast.LENGTH_SHORT);
                    t.show();
                    uploadImage(id);

                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void uploadImage(String id) {

        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        File file=new File(path);
        try {
            params.put("img", file);
        }catch (Exception e){
            e.printStackTrace();
        }
        Query query=new Query();
        query.add("id",id);


        client.addHeader("authorization",utils.TOKEN);
        client.post(utils.HOST+utils.UPLOAD_PRODUCT_IMG+query.getQuery(),params,new JsonHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast t=Toast.makeText(NuevoProducto.this,"No se Pudo Subir la Imagen del Producto",Toast.LENGTH_LONG);
                t.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast t=Toast.makeText(NuevoProducto.this,"Imagen del Producto subido exitosamente",Toast.LENGTH_LONG);
                t.show();
                startActivity(new Intent(NuevoProducto.this,homeVentas.class));
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){

            if(requestCode==CODE_CAMERA){
                Bitmap imgbitmap=(Bitmap)data.getExtras().get("data");

                path=saveToInternalStorage(imgbitmap);
            }
            if(requestCode==CODE_GALERY){
                Uri uri=data.getData();
                path=getRealPathFromURI(uri);
                //File f=new File();
                //path=f.getPath();
            }
            IMG.setImageBitmap(BitmapFactory.decodeFile(path));
        }
    }
    public String getRealPathFromURI(Uri uri) {
        String result;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
            cursor.close();
            return result;
        }
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        result = cursor.getString(idx);
        cursor.close();
        return result;
    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String path = directory.getAbsolutePath() + "/profile.jpg";
        return path;
        //return directory.getAbsolutePath();
    }
    private boolean reviewPermissions() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }

        if(this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        requestPermissions(new String [] {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        return false;
    }

}
