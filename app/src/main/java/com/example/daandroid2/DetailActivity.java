package com.example.daandroid2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class DetailActivity extends AppCompatActivity {

    final String DATABASE_NAME="dbsach.sqlite";
    SQLiteDatabase database;
    final int REQUEST_TAKE_PHOTO=123;
    final int REQUEST_CHOOSE_PHOTO=123;
    ListView listView;
    ArrayList<book> list;
    AdapterBook adapter;
    EditText edtId, edtName, edtPrice, edtStyle, edtPage;
    int id_Book;
    ImageView imgSongUpdate;
    Button btnChonHinh,btnUpdateBook, btnDel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        addControls();
        addEvents();
        readData();
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolBarUpdate);
        setSupportActionBar(toolbar);
    }



    private void addControls() {
        edtId=findViewById(R.id.edtId);
        edtName=findViewById(R.id.edtName);
        edtPrice=findViewById(R.id.edtPrice);
        edtStyle=findViewById(R.id.edtStyle);
        edtPage=findViewById(R.id.edtPage);
        btnChonHinh=findViewById(R.id.btnChonHinh);
        imgSongUpdate=findViewById(R.id.imgSongUpdate);
        btnUpdateBook=findViewById(R.id.btnUpdateBook);
        //btnDel=findViewById(R.id.btnDelete);
    }
    private void addEvents() {
        btnChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoTo();
            }
        });

        btnUpdateBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin từ các EditText
                String id_book = edtId.getText().toString();
                String ten = edtName.getText().toString();
                String gia = edtPrice.getText().toString();
                String sotrang = edtPage.getText().toString();

                // Kiểm tra nếu các trường thông tin không trống
                if (id_book.isEmpty() || ten.isEmpty() || gia.isEmpty() || sotrang.isEmpty()) {
                    Toast.makeText(DetailActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Khởi tạo cơ sở dữ liệu
                database = Database.initDatabase(DetailActivity.this, DATABASE_NAME);

                // Tạo ContentValues để chuẩn bị cập nhật dữ liệu vào bảng book
                ContentValues contentValues = new ContentValues();
                contentValues.put("ten", ten);
                contentValues.put("gia", gia);
                contentValues.put("sotrang", sotrang);

                // Cập nhật thông tin sách vào bảng
                int rowsUpdated = database.update("book", contentValues, "ma = ?", new String[]{id_book});

                // Kiểm tra xem có bản ghi nào bị ảnh hưởng không
                if (rowsUpdated > 0) {
                    Toast.makeText(DetailActivity.this, "Update successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnAddBook = findViewById(R.id.btnAddBook); // Thay đổi ID nếu cần
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi hàm addBook khi người dùng nhấn nút
                addBook();
            }
        });

    }
    private void readData() {
        id_Book = getIntent().getIntExtra("id_Book", -1);
        if (id_Book == -1) {
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        }
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM book WHERE  ma=?", new String[]{String.valueOf(id_Book)});
        if (cursor != null && cursor.moveToFirst()) {

            int id_book=cursor.getInt(0);
            String ten=cursor.getString(1);
            int gia=cursor.getInt(2);
            int maloai=cursor.getInt(3);
            byte[] anh=cursor.getBlob(4);
            int sotrang= cursor.getInt(5);

            edtId.setText(id_book+"");
            edtName.setText(ten);
            edtStyle.setText(maloai+"");
            edtPrice.setText(gia+"");
            edtPage.setText(sotrang+"");


            // Lấy thông tin từ bảng genres
            stylebook style = null;
            Cursor genresCur = database.rawQuery(
                    "SELECT * FROM stylebook WHERE maloai = ?",
                    new String[]{String.valueOf(maloai)}
            );

            if (genresCur != null && genresCur.moveToFirst()) {
                String genresName = genresCur.getString(1);
                style = new stylebook(maloai, ten);
                genresCur.close();
            } else {
                style = new stylebook(maloai, "Không xác định");
            }

            if (anh != null) {
                Bitmap imgSongBm = BitmapFactory.decodeByteArray(anh, 0, anh.length);
                imgSongUpdate.setImageBitmap(imgSongBm);
            } else {

            }
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    private byte[] getByteFromImageView(ImageView imgv) {
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }

    private void addBook() {
//        String id = edtId.getText().toString().trim();
//        String name = edtName.getText().toString().trim();
//        String price = edtPrice.getText().toString().trim();
//        String page= edtPage.getText().toString().trim();
//        String style = edtStyle.getText().toString().trim();
//        byte[] picture = getByteFromImageView(imgSongUpdate);
//
//        if (name.isEmpty() ||id.isEmpty() || style.isEmpty()) {
//            Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String sqlCheckGenres = "SELECT maloai FROM stylebook WHERE tenloai = ?";
//        Cursor cursor = database.rawQuery(sqlCheckGenres, new String[]{style});
//        int maloai;
//
//        if (cursor.moveToFirst()) {
//            // Nếu thể loại đã tồn tại, lấy id_genres
//            maloai = cursor.getInt(0);
//        } else {
//
//            String sqlInsertGenres = "INSERT INTO stylebook (tenloai) VALUES (?)";
//            database.execSQL(sqlInsertGenres, new Object[]{style});
//
//
//            Cursor newCursor = database.rawQuery(sqlCheckGenres, new String[]{style});
//            if (newCursor.moveToFirst()) {
//                maloai = newCursor.getInt(0);
//            } else {
//                Toast.makeText(this, "Lỗi khi thêm thể loại mới!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            newCursor.close();
//        }
//        cursor.close();
//
//
//        String sqlInsertSong = "INSERT INTO book ( ten,gia,maloai,hinhanh,sotrang) VALUES ( ?,?, ?, ?, ?)";
//        database.execSQL(sqlInsertSong, new Object[]{name,price,picture,style,page});
//
//        Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
//
//        //edtNameSong.setText("");
//        edtStyle.setText("");
//        edtName.setText("");
//        edtId.setText("");
//        readData();
        String id = edtId.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        String page = edtPage.getText().toString().trim();
        String style = edtStyle.getText().toString().trim();

        // Lấy ảnh từ ImageView và chuyển thành byte array
        BitmapDrawable drawable = (BitmapDrawable) imgSongUpdate.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        byte[] picture = getCompressedImage(bitmap);
        Bitmap bimap=BitmapFactory.decodeByteArray(picture,0,picture.length);

        // Kiểm tra dữ liệu đầu vào
        if (id.isEmpty() || name.isEmpty() || style.isEmpty() || price.isEmpty() || page.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra và thêm thể loại nếu chưa tồn tại
        String sqlCheckStyle = "SELECT maloai FROM stylebook WHERE tenloai = ?";
        Cursor cursor = database.rawQuery(sqlCheckStyle, new String[]{style});
        int maloai;

        if (cursor.moveToFirst()) {
            // Lấy mã loại nếu tồn tại
            maloai = cursor.getInt(0);
        } else {
            // Nếu không tồn tại, thêm mới vào bảng stylebook
            String sqlInsertStyle = "INSERT INTO stylebook (tenloai) VALUES (?)";
            database.execSQL(sqlInsertStyle, new Object[]{style});

            // Lấy lại mã loại vừa thêm
            Cursor newCursor = database.rawQuery(sqlCheckStyle, new String[]{style});
            if (newCursor.moveToFirst()) {
                maloai = newCursor.getInt(0);
            } else {
                Toast.makeText(this, "Lỗi khi thêm thể loại mới!", Toast.LENGTH_SHORT).show();
                newCursor.close();
                return;
            }
            newCursor.close();
        }
        cursor.close();

        // Thêm sách vào cơ sở dữ liệu
        String sqlInsertBook = "INSERT INTO book (ma, ten, gia, maloai, hinhanh, sotrang) VALUES (?, ?, ?, ?, ?, ?)";
        database.execSQL(sqlInsertBook, new Object[]{id, name, price, maloai, picture, page});

        Toast.makeText(this, "Thêm sách thành công!", Toast.LENGTH_SHORT).show();

        // Xóa dữ liệu nhập trên giao diện
        edtId.setText("");
        edtName.setText("");
        edtPrice.setText("");
        edtPage.setText("");
        edtStyle.setText("");
        imgSongUpdate.setImageResource(R.drawable.ic_launcher_foreground); // Đặt lại ảnh mặc định

    }
    private byte[] getCompressedImage(Bitmap bitmap) {
        // Nén ảnh trước khi lưu để tránh vượt quá giới hạn của SQLite
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int newWidth = 500; // Chiều rộng ảnh sau khi resize
        int newHeight = (int) (bitmap.getHeight() * (500.0 / bitmap.getWidth())); // Giữ tỷ lệ ảnh

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream); // Nén ảnh với chất lượng 80%
        return stream.toByteArray();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= new MenuInflater(this);
        inflater.inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.styleBar)
        {
            Intent intent= new Intent(DetailActivity.this, StyleActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.allBookBar)
        {
            Intent intent= new Intent(DetailActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
       else if (id == R.id.aboutBar)        {
           Intent intent= new Intent(DetailActivity.this, About.class);
            startActivity(intent);
           return true;     }


        return super.onOptionsItemSelected(item);

    }
    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_TAKE_PHOTO);

    }
    private void choosePhoTo(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CHOOSE_PHOTO);
    }
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==REQUEST_CHOOSE_PHOTO){
                Uri imgUri=data.getData();
                try{
                    InputStream is =getContentResolver().openInputStream(imgUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgSongUpdate.setImageBitmap(bitmap);
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }

            }else if(requestCode==REQUEST_TAKE_PHOTO){
                Bitmap bimap= (Bitmap) data.getExtras().get("data");
                imgSongUpdate.setImageBitmap(bimap);

            }
        }

    }


}