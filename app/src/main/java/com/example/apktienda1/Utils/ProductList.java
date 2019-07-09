package com.example.apktienda1.Utils;

import android.graphics.Bitmap;

import com.example.apktienda1.utils;

import java.util.ArrayList;

public class ProductList extends ArrayList<Producto> implements OnLoadImg {
    private int ContImgLoaded;
    OnLoadAllList olal;
    public ProductList(OnLoadAllList ol) {
        ContImgLoaded = 0;
        olal=ol;
    }


    @Override
    public void onLoadImg(int index, Bitmap img) {
        this.get(index).img=img;
        ContImgLoaded++;
        if(ContImgLoaded==this.size()){
            olal.onLoadAllList();
        }
    }
    public void addAndLoadImg(Producto p){
        this.add(p);
        TaskImg loadImage=new TaskImg();
        Query query=new Query();
        query.add("img",this.get(this.size()-1).getPicture());
        loadImage.execute(utils.HOST+utils.DOWNLOAD_PRODUCT_IMG+query.getQuery());

        loadImage.setLoadImage(this,this.size()-1);
    }
}
