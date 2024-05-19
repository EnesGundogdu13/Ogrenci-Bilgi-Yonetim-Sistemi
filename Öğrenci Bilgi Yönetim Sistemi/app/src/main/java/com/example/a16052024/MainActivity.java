package com.example.a16052024;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase OkulDB;

    Button ekle, sil, duzenle;
    EditText ad;
    EditText soyad, Id;
    ListView list;
    ArrayList<String> ogrenciler = new ArrayList<>();
    ArrayAdapter<String> baglama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ekle = findViewById(R.id.BTNekle);
        ad = findViewById(R.id.PTad);
        soyad = findViewById(R.id.PTsoyad);
        Id = findViewById(R.id.PTid);
        sil = findViewById(R.id.BTNsil);
        duzenle = findViewById(R.id.BTNguncelle);
        list = findViewById(R.id.LVlist);

        OkulDB= this.openOrCreateDatabase("OkulDb", MODE_PRIVATE, null);
        String ogrenciTablo = "CREATE TABLE IF NOT EXISTS ogrenci(numara INTEGER PRIMARY KEY, ad  VARCHAR, soyad VARCHAR)";
        OkulDB.execSQL(ogrenciTablo);

        Cursor OgrenciVerileri = OkulDB.rawQuery("SELECT numara, ad, soyad FROM ogrenci", null);

        int numaraSirasi = OgrenciVerileri.getColumnIndex("numara");
        int adSirasi = OgrenciVerileri.getColumnIndex("ad");
        int soyadSirasi = OgrenciVerileri.getColumnIndex("soyad");
        while (OgrenciVerileri.moveToNext()){
            int gelenNumara = OgrenciVerileri.getInt(numaraSirasi);
            String gelenAd = OgrenciVerileri.getString(adSirasi);
            String gelenSoyad = OgrenciVerileri.getString(soyadSirasi);
            ogrenciler.add(gelenNumara + " " + gelenAd + " " + gelenSoyad);
        }
        OgrenciVerileri.close();
        baglama = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, ogrenciler);
        list.setAdapter(baglama);

        ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ogrenciEkle = "INSERT INTO ogrenci(ad,soyad) VALUES (?,?)";
                SQLiteStatement eklemeSorgusu = OkulDB.compileStatement(ogrenciEkle);
                eklemeSorgusu.bindString(1, ad.getText().toString());
                eklemeSorgusu.bindString(2, soyad.getText().toString());
                eklemeSorgusu.execute();
                ad.setText("");
                soyad.setText("");
                Id.setText("");
                Toast.makeText(MainActivity.this, "Eklendi!", Toast.LENGTH_SHORT).show();
            }
        });

        duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ogrenciDuzenle = "UPDATE ogrenci SET ad = ?, soyad = ? WHERE numara = ?";
                SQLiteStatement eklemeSorgusu = OkulDB.compileStatement(ogrenciDuzenle);
                eklemeSorgusu.bindString(1, ad.getText().toString());
                eklemeSorgusu.bindString(2, soyad.getText().toString());
                eklemeSorgusu.bindString(3, Id.getText().toString());
                eklemeSorgusu.execute();
                ad.setText("");
                soyad.setText("");
                Id.setText("");
                Toast.makeText(MainActivity.this, "Güncellendi!", Toast.LENGTH_SHORT).show();
            }
        });

        sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ogrenciEkle = "Delete From ogrenci Where numara = ?";
                SQLiteStatement eklemeSorgusu = OkulDB.compileStatement(ogrenciEkle);
                eklemeSorgusu.bindString(1, Id.getText().toString());
                eklemeSorgusu.execute();
                ad.setText("");
                soyad.setText("");
                Id.setText("");
                Toast.makeText(MainActivity.this, "Silindi!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}