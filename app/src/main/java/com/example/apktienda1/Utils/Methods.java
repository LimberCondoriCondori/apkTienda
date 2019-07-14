package com.example.apktienda1.Utils;

import android.app.Activity;

import com.example.apktienda1.utils;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Methods {

    public static void LoadChats(){
        utils.CHATS_C=new ArrayList<Chat>();
        utils.CHATS_V=new ArrayList<Chat>();
        loadChatsC();
        loadChatsV();
    }
    public static void loadChatsV(){
        AsyncHttpClient client=new AsyncHttpClient();
        client.addHeader("authorization",utils.TOKEN);
        Query q=new Query();
        q.add("idVendedor",utils.idUSer);
        client.get(utils.HOST+utils.CHAT+q.getQuery(),new RequestParams(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    for(int i=0;i<response.length();i++){
                        JSONObject o=response.getJSONObject(i);
                        ArrayList<Messege> msns=new ArrayList<Messege>();
                        JSONArray arrayMsn=o.getJSONArray("msns");
                        for(int j=arrayMsn.length()-1;j>-1;j--){
                            JSONObject m=arrayMsn.getJSONObject(j);
                            msns.add(new Messege(
                                    m.getString("_id"),
                                    m.getString("idUser"),
                                    m.getString("msn"),
                                    m.getString("registerDate"),
                                    m.getString("idChat"),
                                    m.getBoolean("leido")
                            ));
                        }
                        Chat c=new Chat(
                                o.getString("_id"),//id
                                o.getString("idComprador"),//idC
                                o.getString("idVendedor"),//idV
                                o.getString("idProduct"),//idP
                                msns//listmsn
                        );
                        utils.CHATS_V.add(c);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static void loadChatsC(){
        AsyncHttpClient client=new AsyncHttpClient();

        client.addHeader("authorization",utils.TOKEN);
        Query q=new Query();
        q.add("idComprador",utils.idUSer);
        client.get(utils.HOST+utils.CHAT+q.getQuery(),new RequestParams(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    for(int i=0;i<response.length();i++){
                        JSONObject o=response.getJSONObject(i);
                        ArrayList<Messege> msns=new ArrayList<Messege>();
                        JSONArray arrayMsn=o.getJSONArray("msns");
                        for(int j=arrayMsn.length()-1;j>-1;j--){
                            JSONObject m=arrayMsn.getJSONObject(j);
                            msns.add(new Messege(
                                    m.getString("_id"),
                                    m.getString("idUser"),
                                    m.getString("msn"),
                                    m.getString("registerDate"),
                                    m.getString("idChat"),
                                    m.getBoolean("leido")
                            ));
                        }
                        Chat c=new Chat(
                                o.getString("_id"),//id
                                o.getString("idComprador"),//idC
                                o.getString("idVendedor"),//idV
                                o.getString("idProduct"),//idP
                                msns//listmsn
                        );
                        utils.CHATS_C.add(c);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static int ChatConVendedorExiste(String idProduct) {
        int r=-1;
        for(int i=0;i<utils.CHATS_C.size();i++){
            if(utils.CHATS_C.get(i).idProduct.equals(idProduct))
                return i;
        }
        return r;
    }
    public static void startSocket(final Activity a){
        try {
            utils.mSocket = IO.socket(utils.HOST_SOCKET);
        } catch (URISyntaxException e) {}

        utils.mSocket.connect();
        Emitter.Listener newMessage= new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        Messege msn=null;

                        try {

                            msn=new Messege(
                                    data.getString("_id"),
                                    data.getString("idUser"),
                                    data.getString("msn"),
                                    data.getString("registerDate"),
                                    data.getString("idChat"),
                                    data.getBoolean("leido")
                            );
                            boolean insert=false;
                            int val=existeChatId(msn.idChat);
                            if(val==-1){
                                class jsRHGetChat extends JsonHttpResponseHandler {
                                    Messege msn;
                                    public jsRHGetChat(Messege m){
                                        super();
                                        msn=m;
                                    }
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        super.onFailure(statusCode, headers, throwable, errorResponse);
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                        super.onSuccess(statusCode, headers, response);
                                        try{
                                            Chat c=new Chat(
                                                    response.getJSONObject(0).getString("_id"),
                                                    response.getJSONObject(0).getString("idComprador"),
                                                    response.getJSONObject(0).getString("idVendedor"),
                                                    response.getJSONObject(0).getString("idProduct"),
                                                    new ArrayList<Messege>()
                                            );
                                            c.addMessege(msn);
                                            if(c.idVendedor.equals(utils.idUSer)){
                                                utils.CHATS_V.add(c);
                                            }
                                            else{
                                                utils.CHATS_C.add(c);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }


                                //solicitar chat al servidor
                            }else{
                                if(utils.CHATS_C.size()>val){
                                    if(utils.CHATS_C.get(val).id.equals(msn.idChat))
                                        utils.CHATS_C.get(val).addMessege(msn);
                                }
                                if(utils.CHATS_V.size()>val&&!insert){
                                    if(utils.CHATS_V.get(val).id.equals(msn.idChat))
                                        utils.CHATS_V.get(val).addMessege(msn);

                                }
                            }
                        } catch (JSONException e) {
                            return;
                        }

                        // add the message chat



                    }
                });
            }
        };
        utils.mSocket.on(utils.idUSer, newMessage);
    }
    public static int existeChatId(String idChat){
        int r=-1;
        for(int i=0;i<utils.CHATS_C.size();i++){
            if(utils.CHATS_C.get(i).id.equals(idChat)){
                return i;
            }
        }
        for(int i=0;i<utils.CHATS_V.size();i++){
            if(utils.CHATS_V.get(i).id.equals(idChat)){
                return i;
            }
        }
        return r;
    }
}
