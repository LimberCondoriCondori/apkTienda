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

import com.example.apktienda1.Utils.OnLoadImg;
import com.example.apktienda1.Utils.Producto;
import com.example.apktienda1.Utils.Query;
import com.example.apktienda1.Utils.TaskImg;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class EditarProducto extends AppCompatActivity implements OnLoadImg {
    private int CODE_CAMERA=110;
    private int CODE_GALERY=111;

    private String path=null;
    private Button guardar;
    private Button cancelar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(EditarProducto.this,homeCompras.class));
                    finish();
                    return true;
                case R.id.navigation_dashboard:

                    startActivity(new Intent(EditarProducto.this,homeVentas.class));
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    //startActivity(new Intent(EditarProducto.this,homeCompras.class));
                    //finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadComponents();

    }
    private Producto p;
    private void loadComponents() {
        Intent i=getIntent();
        String id=i.getExtras().getString("id");
        AsyncHttpClient client =new AsyncHttpClient();
        Query query=new Query();
        query.add("_id",id);
        client.get(utils.HOST+utils.PRODUCT+query.getQuery(),new RequestParams(),new JsonHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject o=response.getJSONObject(0);
                    p=new Producto(
                            o.getString("name"),
                            o.getString("description"),
                            o.getString("idUser"),
                            o.getString("_id"),
                            o.getString("picture"),
                            Double.parseDouble(o.getString("price")),
                            Integer.parseInt(o.getString("cant"))
                    );
                    show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public ImageView img;
    TextView name,description,price,cant;

    private void show(){
        name=findViewById(R.id.nameEP);
        description=findViewById(R.id.descriptionEP);
        price=findViewById(R.id.priceEP);
        cant=findViewById(R.id.cantEP);
        img=findViewById(R.id.imgEP);
        name.setText(p.getName());
        description.setText(p.getDescription());
        price.setText(""+p.getPrice());
        cant.setText(""+p.getCant());
        TaskImg t=new TaskImg();
        String urlDownloadImg=utils.HOST+utils.DOWNLOAD_PRODUCT_IMG;
        Query query=new Query();
        query.add("img",p.getPicture());
        urlDownloadImg=urlDownloadImg+query.getQuery();
        t.execute(urlDownloadImg);
        t.setLoadImage(this,-1);
        guardar=findViewById(R.id.btnGuardarEP);
        cancelar=findViewById(R.id.btnCancelarEP);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditarProducto.this,homeVentas.class));
                finish();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(EditarProducto.this).create();
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

                save();
            }
        });
    }

    @Override
    public void onLoadImg(int index, Bitmap img) {
        this.img.setImageBitmap(img);
        p.setImg(img);
    }
    private void save() {
        String name=this.name.getText().toString();
        String description=this.description.getText().toString();
        String price=this.price.getText().toString();
        String cant=this.cant.getText().toString();
        AsyncHttpClient client= new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.add("idUser",utils.idUSer);
        params.add("name",name);
        params.add("description",description);
        params.add("price",price);
        params.add("cant",cant);
        //token
        Query query=new Query();
        query.add("id",p.getId());
        String par="";
        client.addHeader("authorization", utils.TOKEN);
        client.patch(utils.HOST+utils.PRODUCT+query.getQuery(),params,new JsonHttpResponseHandler(){

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                AlertDialog alertDialog = new AlertDialog.Builder(EditarProducto.this).create();
                String msn="";
                try{
                    msn=errorResponse.getString("msn");
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                alertDialog.setTitle("RESPONSE SERVER");
                alertDialog.setMessage(msn);
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
                try{
                    //imagen para el producto
                    Toast t=Toast.makeText(EditarProducto.this,"Producto creado exitosamente", Toast.LENGTH_SHORT);
                    t.show();
                    uploadImage(p.getId());

                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void uploadImage(String id) {
        if(path!=null){
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
                Toast t=Toast.makeText(EditarProducto.this,"No se Pudo Subir la Imagen del Producto",Toast.LENGTH_LONG);
                t.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast t=Toast.makeText(EditarProducto.this,"Imagen del Producto subido exitosamente",Toast.LENGTH_LONG);
                t.show();
                startActivity(new Intent(EditarProducto.this,homeVentas.class));
                finish();
            }

        });}else{
            startActivity(new Intent(getApplicationContext(),homeVentas.class));
            finish();
        }
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
            this.img.setImageBitmap(BitmapFactory.decodeFile(path));
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
