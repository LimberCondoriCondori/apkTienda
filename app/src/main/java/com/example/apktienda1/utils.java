package com.example.apktienda1;

import android.graphics.Bitmap;

import com.example.apktienda1.Utils.Chat;
import com.github.nkzawa.socketio.client.Socket;

import java.util.ArrayList;

public class utils {
    public static String HOST = "http://192.168.43.117:8001";
    public static String HOST_SOCKET=HOST.substring(0,HOST.length()-4)+"8002";
    public static String USER = "/users/";
    public static String LOGIN = "/users/login";
    public static String LOGIN_WITH_GOOGLE = "/users/login_with_google";
    public static String PRODUCT="/product/";
    public static String UPLOAD_PRODUCT_IMG="/product/uploadImg";
    public static String DOWNLOAD_PRODUCT_IMG="/product/downloadImg";
    public static String COMPRA="/compra/";
    public static String CITA="/cita/";
    public static String CHAT="/chat/";
    public static String TOKEN = "";
    public static String idUSer="";

    public static ArrayList<Chat> CHATS_V;
    public static ArrayList<Chat> CHATS_C;
    public static Socket mSocket;
}