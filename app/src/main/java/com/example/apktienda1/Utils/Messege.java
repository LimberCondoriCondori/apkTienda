package com.example.apktienda1.Utils;

import com.example.apktienda1.utils;
import com.loopj.android.http.AsyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Messege {
    public String id,idUser,msn,registerDate,idChat;
    boolean leido=false;
    public Messege(String id, String idUser, String msn, String registerDate,String idChat,boolean leido) {
        this.id = id;
        this.idUser = idUser;
        this.msn = msn;
        this.registerDate = registerDate;
        this.idChat=idChat;
        this.leido=leido;
    }

    public Messege(String idUser, String msn, String idChat) {
        this.idUser = idUser;
        this.msn = msn;
        this.idChat = idChat;
        id=null;
        registerDate=null;
    }

    public JSONObject getJSONobjetMsnSend(){
        JSONObject o=new JSONObject();

        try {
            o.put("idUser",idUser);
            o.put("msn",msn);
            o.put("idChat",idChat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return o;
    }
    public Messege(JSONObject o){
        try{
            id=o.getString("_id");
            idUser=o.getString("idUser");
            idChat=o.getString("idChat");
            registerDate=o.getString("registerDate");
            leido=o.getBoolean("leido");
            msn=o.getString("msn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
