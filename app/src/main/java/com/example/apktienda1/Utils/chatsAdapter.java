package com.example.apktienda1.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.apktienda1.MessegesActivity;
import com.example.apktienda1.R;
import com.example.apktienda1.utils;
import java.util.ArrayList;

public class chatsAdapter extends BaseAdapter {
    private Context CONTEXT;

    private ArrayList<Chat> lista;
    public chatsAdapter(Context cont,ArrayList<Chat> l){
        CONTEXT=cont;
        lista=l;
    }
    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            LayoutInflater inflater=(LayoutInflater) CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.view_model_chat,null);
        }
        TextView fullname=(TextView) view.findViewById(R.id.fullnameM);
        TextView productname=(TextView)view.findViewById(R.id.productNameM);
        ImageButton btnChatM=(ImageButton) view.findViewById(R.id.btnChatM);

        if(utils.idUSer.equals(lista.get(i).idComprador))
            fullname.setText(lista.get(i).fullNameV);
        else
            fullname.setText(lista.get(i).fullNameC);

        productname.setText(lista.get(i).nameP);
        btnChatM.setOnClickListener(new OnClicM(i,lista.get(i).id));
        return view;
    }
    private class OnClicM implements View.OnClickListener {
        int Index;
        String idChat;
        public OnClicM(int i,String id){
            Index=i;
            idChat=id;
        }
        @Override
        public void onClick(View view) {
            Intent i=new Intent(CONTEXT, MessegesActivity.class);
            i.putExtra("indexChat",Index);
            i.putExtra("idChat",idChat);
            CONTEXT.startActivity(i);
        }
    }
}
