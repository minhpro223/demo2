package com.example.daandroid2;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final String DATABASE_NAME="dbsach.sqlite";
    SQLiteDatabase database;

    ListView listView;
    ArrayList<book> list;
    AdapterBook adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       database = Database.initDatabase(this, DATABASE_NAME);
       Cursor cursor = database.rawQuery("SELECT * FROM book", null);
        if (cursor.moveToFirst()) { // Kiểm tra xem bảng có dữ liệu không
           Toast.makeText(this, "Dữ liệu đầu tiên: " + cursor.getString(1), Toast.LENGTH_SHORT).show();
      } else {
           Toast.makeText(this, "Không có dữ liệu trong bảng book", Toast.LENGTH_SHORT).show();
      }
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
       cursor.close();
        addControls();
        readData();
        addEvents();
    }

    private void addEvents() {
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Lấy thông tin sách tại vị trí được chọn
//                book selectedBook = list.get(position);
//
//                // Tạo Intent để chuyển sang DetailActivity
//                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//
//                // Truyền dữ liệu của sách sang DetailActivity
//                intent.putExtra("BOOK_ID", selectedBook.getMa());
//                intent.putExtra("BOOK_NAME", selectedBook.getTen());
//                intent.putExtra("BOOK_PRICE", selectedBook.getGia());
//                intent.putExtra("BOOK_MALOAI",getTenLoaiByMaloai(selectedBook.getMaloai()));
//                intent.putExtra("BOOK_IMAGE", selectedBook.getHinhanh());
//                intent.putExtra("BOOK_PAGE", selectedBook.getSotrang());
//
//                // Bắt đầu Activity chi tiết
//                startActivity(intent);
//            }
       // });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                book books= list.get(position);
                Intent intent= new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id_Book",books.getMa());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Hiển thị hộp thoại xác nhận
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có chắc chắn muốn xóa mục này không?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xóa mục trong cơ sở dữ liệu
                        deleteItem(position);
                    }
                });
                builder.setNegativeButton("Hủy", null);
                builder.show();
                return true;
            }
        });

    }
    private void deleteItem(int position) {
        // Lấy thông tin mục cần xóa từ danh sách
        book Book = list.get(position); // `bookList` là danh sách dữ liệu (ArrayList<Book>)
        int bookId = Book.getMa(); // ID của mục trong cơ sở dữ liệu

        // Xóa khỏi cơ sở dữ liệu
        String sqlDelete = "DELETE FROM book WHERE ma = ?";
        database.execSQL(sqlDelete, new Object[]{bookId});

        // Xóa khỏi danh sách và cập nhật giao diện
        list.remove(position);
        adapter.notifyDataSetChanged();

        // Hiển thị thông báo
        Toast.makeText(MainActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
    }


    public String getTenLoaiByMaloai(stylebook maloai) {
        String tenloai = "Không xác định"; // Giá trị mặc định
        Cursor cursor = database.rawQuery(
                "SELECT tenloai FROM stylebook WHERE maloai = ?",
                new String[]{String.valueOf(maloai)}
        );

        if (cursor != null && cursor.moveToFirst()) {
            tenloai = cursor.getString(1); // Lấy giá trị của cột tenloai
            cursor.close();
        }
        return tenloai;
    }

    private void addControls() {
       listView=(android.widget.ListView)findViewById(R.id.lvBook);
        list=new ArrayList<>();
        adapter=new AdapterBook(this,list);
        listView.setAdapter(adapter);
    }
    private void readData(){
        database =Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor=database.rawQuery("SELECT * From book",null);
        list.clear();
        for (int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);
            int id_book=cursor.getInt(0);
            String ten=cursor.getString(1);
            int maloai=cursor.getInt(3);
            byte[] anh=cursor.getBlob(4);

            stylebook stylebooks = null;
            Cursor genresCur = database.rawQuery(
                    "SELECT * FROM stylebook WHERE maloai = ?",
                    new String[]{String.valueOf(maloai)}
            );

            if (genresCur != null && genresCur.moveToFirst()) {
                String  tenloai= genresCur.getString(1);
                stylebooks = new stylebook(maloai, tenloai);
                genresCur.close();
            } else {
                stylebooks = new stylebook(maloai, "Không xác định");
            }


            list.add(new book(id_book,ten,stylebooks,anh));
//            list.add(new book(id,100,maloai,ten,19,anh));
        }
        cursor.close();
        adapter.notifyDataSetChanged();

       // Toast.makeText(this,cursor.getString(1),Toast.LENGTH_SHORT).show();
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
            Intent intent= new Intent(MainActivity.this, StyleActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.allBookBar)
        {
            Intent intent= new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
      else if (id == R.id.aboutBar)
        {
          Intent intent= new Intent(MainActivity.this, About.class);
           startActivity(intent);
            return true;
       }
        return super.onOptionsItemSelected(item);

    }
}