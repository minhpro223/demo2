package com.example.daandroid2;


import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;


import androidx.appcompat.app.AppCompatActivity;



import java.util.ArrayList;

public class StyleActivity extends AppCompatActivity {
    final String DATABASE_NAME="dbsach.sqlite";
    SQLiteDatabase database;
    ListView listView ;
    ArrayList<stylebook> list;
    ArrayAdapter adapter;
    EditText edtId,edtStyleName;
    Button btnAdd,btnUpdate,btnDelete;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);
//        database = Database.initDatabase(this, DATABASE_NAME);
//        Cursor cursor = database.rawQuery("SELECT * FROM stylebook", null);
//        if (cursor.moveToFirst()) { // Kiểm tra xem bảng có dữ liệu không
//            Toast.makeText(this, "Dữ liệu đầu tiên: " + cursor.getString(1), Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Không có dữ liệu trong bảng stylebook", Toast.LENGTH_SHORT).show();
//        }
//        cursor.close();
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        addcontrol();
        addEvents();
        readdata();



        // Xử lý sự kiện khi người dùng chọn mục trong ListView

    }
    private stylebook selectedStyle;

    private void addcontrol() {
        edtId = findViewById(R.id.edtId);
        edtStyleName = findViewById(R.id.edtStyleName);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        listView = findViewById(R.id.listView);
        list=new ArrayList<>();
        listView =findViewById(R.id.listView);
        adapter =new ArrayAdapter<>(StyleActivity.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }
    private void addEvents() {
//        listView.setOnItemClickListener((adapterView, view, position, id) -> {
//            Cursor itemCursor = (Cursor) adapterView.getItemAtPosition(position);
//
//            // Kiểm tra itemCursor trước khi lấy dữ liệu
//            if (itemCursor != null) {
//                // Lấy dữ liệu từ Cursor (sử dụng column index trực tiếp)
//                int maloai = itemCursor.getInt(0); // Cột 0 là "maloai"
//                String tenloai = itemCursor.getString(1); // Cột 1 là "tenloai"
//
//                // Hiển thị dữ liệu lên các trường nhập
//                edtId.setText(String.valueOf(maloai));
//                edtStyleName.setText(tenloai);
//
//                // Thông báo đã chọn
//                Toast.makeText(this, "Đã chọn: " + tenloai, Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Không thể lấy dữ liệu từ mục đã chọn", Toast.LENGTH_SHORT).show();
//            }
//        });
        btnAdd.setOnClickListener(v -> addStyle());
        btnDelete.setOnClickListener(v -> deleteStyle());
        btnUpdate.setOnClickListener(v -> updateStyle());
        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedStyle = list.get(position);
           edtId.setText(valueOf(selectedStyle.getId()));
            edtStyleName.setText(selectedStyle.getTenloai());
        });
    }


    private void readdata() {
        database=Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor=database.rawQuery("SELECT * From stylebook",null);
        list.clear();
        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);
            int id=cursor.getInt(0);
            String tenloai=cursor.getString(1);
           list.add(new stylebook(id,tenloai));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void addStyle() {
        String idText = edtId.getText().toString().trim();
        String tenStyle = edtStyleName.getText().toString().trim();
        if (idText.isEmpty() || tenStyle.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            int maloai = Integer.parseInt(idText);
            // Thêm vào cơ sở dữ liệu
            ContentValues values = new ContentValues();
            values.put("maloai", maloai);
            values.put("tenloai", tenStyle);

            long result = database.insert("stylebook", null, values);
            if (result != -1) {
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();

                // Cập nhật lại danh sách
                // Xóa dữ liệu nhập
                edtId.setText("");
                edtStyleName.setText("");
            } else {
                Toast.makeText(this, "Lỗi: ID đã tồn tại", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID phải là số", Toast.LENGTH_SHORT).show();
        }
        readdata();

    }

    private void deleteStyle() {
        String idText = edtId.getText().toString().trim();

        if (idText.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập ID để xóa", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int maloai = Integer.parseInt(idText);

            // Tạo hộp thoại xác nhận
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa mục với ID " + maloai + " không?")
                    .setCancelable(false) // Không thể hủy bỏ bằng cách nhấn ra ngoài hộp thoại
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Xóa dòng có ID tương ứng
                            int result = database.delete("stylebook", "maloai = ?", new String[]{String.valueOf(maloai)});

                            if (result > 0) {
                                Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();

                                // Cập nhật lại danh sách
                                readdata();
                                // Xóa dữ liệu nhập
                                edtId.setText("");
                                edtStyleName.setText("");
                            } else {
                                Toast.makeText(getApplicationContext(), "Lỗi: Không tìm thấy ID để xóa", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Không", null) // Nếu người dùng chọn "Không", hộp thoại sẽ bị đóng
                    .show();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID phải là số", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateStyle() {
//        String idText = edtId.getText().toString().trim();
//        String tenStyle = edtStyleName.getText().toString().trim();
//
//        if (idText.isEmpty() || tenStyle.isEmpty()) {
//            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            int maloai = Integer.parseInt(idText);
//
//            // Cập nhật dữ liệu trong cơ sở dữ liệu
//            ContentValues values = new ContentValues();
//            values.put("tenloai", tenStyle);
//
//            int result = database.update("stylebook", values, "maloai = ?", new String[]{String.valueOf(maloai)});
//            if (result > 0) {
//                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
//                readdata(); // Cập nhật lại danh sách
//            } else {
//                Toast.makeText(this, "ID không tồn tại", Toast.LENGTH_SHORT).show();
//            }
//        } catch (NumberFormatException e) {
//            Toast.makeText(this, "ID phải là số", Toast.LENGTH_SHORT).show();
//        }
        if(selectedStyle==null)
        {
            Toast.makeText(this, "Vui lòng chọn thể loại cần sửa", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = edtStyleName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Tên thể loại không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        String sql = "UPDATE stylebook SET tenloai = ? WHERE maloai = ?";
        database.execSQL(sql, new Object[]{name, selectedStyle.getId()});

        Toast.makeText(this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
        readdata();
        edtStyleName.setText(""); // Xóa nội dung nhập
        selectedStyle = null; // Hủy chọn
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
            Intent intent= new Intent(StyleActivity.this, StyleActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.allBookBar)
        {
            Intent intent= new Intent(StyleActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.aboutBar)
        {
            Intent intent= new Intent(StyleActivity.this, About.class);
            startActivity(intent);
            return true;
       }


        return super.onOptionsItemSelected(item);

    }

}
