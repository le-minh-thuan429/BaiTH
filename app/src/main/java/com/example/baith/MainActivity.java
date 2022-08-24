package com.example.baith;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ArrayList<CauHoi> listCauHoi;
    TextView txtDiem, txtCauHoi,socau,huongdan;
    ImageView imgtt;
    Button[]bt;
    Button btreset;
    int currentQuestion=0, diem=0;
    int Socau=1;
    MediaPlayer mediaPlayer;
    int causai=0;

    final String DATABASE_NAME = "BaiTapCauhoi.db";
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listCauHoi = new ArrayList<CauHoi>();

        LoadCauHoi();

        txtDiem = (TextView)findViewById(R.id.txtDiem);
        txtCauHoi = (TextView)findViewById(R.id.txtquestion);
        socau = (TextView)findViewById(R.id.socau);
        huongdan = (TextView)findViewById(R.id.huongdan);

        txtDiem.setText("0");
        bt=new Button[4];
        bt[0]=(Button)findViewById(R.id.btlc1);
        bt[1]=(Button)findViewById(R.id.btlc2);
        bt[2]=(Button)findViewById(R.id.btlc3);
        bt[3]=(Button)findViewById(R.id.btlc4);
        btreset = (Button)findViewById(R.id.btReset);

        for(int i=0;i<bt.length;i++) {
            bt[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.btlc1:
                            KiemTra(bt[0].getText().toString().trim(),0);
                            break;
                        case R.id.btlc2:
                            KiemTra(bt[1].getText().toString().trim(),1);
                            break;
                        case R.id.btlc3:
                            KiemTra(bt[2].getText().toString().trim(),2);
                            break;
                        case R.id.btlc4:
                            KiemTra(bt[3].getText().toString().trim(),3);
                            break;
                        case R.id.btReset:
                            reset();
                            break;
                    }
                }
            });
        }
        btreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        //Tạo Animation cho ảnh
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.rotate);
        txtDiem.setAnimation(animation);
        animation.start();
        // Hiển thị câu hỏi
        ViewQuestion();
    }
    public void oppen(){
        database = DatabaseHandler.initDatabase(this, DATABASE_NAME);
    }
    public void close(){
        database.close();
    }
    //Hiển thị câu hỏi lên các View
    public void ViewQuestion()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bt[0].setBackgroundResource(R.drawable.button);
                bt[1].setBackgroundResource(R.drawable.button);
                bt[2].setBackgroundResource(R.drawable.button);
                bt[3].setBackgroundResource(R.drawable.button);

            }
        }, 1000);
        socau.setText("Câu :"+Socau);
        Socau++;
        if(currentQuestion>=listCauHoi.size())
        {
            huongdan.setText("Chúc mừng bạn đã hoàn thành bài Test!");
            socau.setText("");
            txtCauHoi.setText("");
            amthanhhoanthanh();
            for(int i=0;i<bt.length;i++) {
                bt[i].setVisibility(View.INVISIBLE);
            }

            AlertDialog.Builder v=new AlertDialog.Builder(MainActivity.this);
            v.setTitle("Kết quả");
            v.setMessage("Điểm: "+diem+"\nSố câu sai: "+causai);
            v.create().show();
        }
        else {
            txtCauHoi.setText(listCauHoi.get(currentQuestion).getCauhoi());
            String[]luaChon=listCauHoi.get(currentQuestion).getLuachon();

            for(int i=0;i<luaChon.length;i++) {
                bt[i].setText(luaChon[i]);
                bt[i].setVisibility(View.VISIBLE);
            }
            for(int i=luaChon.length;i<bt.length;i++) {
                bt[i].setVisibility(View.INVISIBLE);
            }

        }
    }
    public void KiemTra(String traLoi,int a){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.slide);
        txtCauHoi.setAnimation(animation);
        animation.start();
        if(listCauHoi.get(currentQuestion).getDapan().equals(traLoi))
        {
            diem = diem+10;
            txtDiem.setText(diem +"");
            amthanhtrachondung();

        }
        else{
            Toast.makeText(this,"Bạn chọn sai",Toast.LENGTH_SHORT).show();
            amthanhtrachonsai();
            bt[a].setBackgroundResource(R.drawable.state);
            causai++;

        }

        currentQuestion++;
        ViewQuestion();
    }
    // Load ngân hàng câu hỏi
    public void LoadCauHoi()
    {
        CauHoi question;
        oppen();
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM  tblToan", null);
            cursor.moveToFirst();
            do {
                String Luachon1 = cursor.getString(2).trim();
                String Luachon2 = cursor.getString(3).trim();;
                String Luachon3 = cursor.getString(4).trim();;
                String Luachon4 = cursor.getString(5).trim();;
                question = new CauHoi(cursor.getString(1), new String[]{Luachon1, Luachon2, Luachon3, Luachon4}, cursor.getString(6));
                listCauHoi.add(question);
            } while (cursor.moveToNext());
        }catch (Exception exception){
            exception.toString();
        }
      close();

    }
    private void amthanhtrachondung() {

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.anvaodechon);
        mediaPlayer.start();
    }
    private void amthanhtrachonsai() {

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.fail);
        mediaPlayer.start();
    }
    private void amthanhhoanthanh() {

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.hoanthanh);
        mediaPlayer.start();
    }


    public void reset()
    {
        diem=0;
        txtDiem.setText("0");
        currentQuestion=0;
        Socau=1;
        huongdan.setText("Bạn hãy chọn đáp án đúng ");
        causai=0;
        ViewQuestion();
    }

}