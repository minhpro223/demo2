package com.example.daandroid2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterBook extends BaseAdapter {
    Context context;
    ArrayList<book>list;

    public AdapterBook(Context context, ArrayList<book> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.item_book,null);
        ImageView imgAvatar= row.findViewById(R.id.imgAvatar);
        TextView txtId=row.findViewById(R.id.txtid);
        TextView txtName=row.findViewById(R.id.txtname);
        TextView txtMaloai=row.findViewById(R.id.txtmaloai);
          book Book=list.get(position);
          txtId.setText(Book.ma+"");
          txtName.setText(Book.ten);
          txtMaloai.setText(Book.maloai+"");
        Bitmap bmAvatar= BitmapFactory.decodeByteArray(Book.getHinhanh(),0,Book.getHinhanh().length);
        if (bmAvatar == null) {
            Log.e("BaiHatAdapter", "Bitmap is null");
        } else {
            Log.e("BaiHatAdapter", "Bitmap loaded successfully");
        }
        imgAvatar.setImageBitmap(bmAvatar);
        return row;

    }


}
