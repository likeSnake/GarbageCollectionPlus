package net.supertool.general.utility.free.garbagecollectionplus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.util.Random;

public class MainAct extends AppCompatActivity implements View.OnClickListener{
    private Button bt_startScan;
    private TextView rubbishSize;
    private String rubbish="345.39";
    private RelativeLayout cleaner_layout,delete_layout;
    private ImageView rubbish_gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.my_title));

        initUI();
        initData();
        initListener();
    }
    public void initUI(){
        bt_startScan = findViewById(R.id.bt_startScan);
        rubbishSize = findViewById(R.id.rubbishSize);
        cleaner_layout = findViewById(R.id.cleaner_layout);
        delete_layout = findViewById(R.id.delete_layout);
        rubbish_gif = findViewById(R.id.rubbish_gif);

    }
    public void initData(){

        rubbish = getRubbish();
        rubbishSize.setText(rubbish);

        Glide.with(MainAct.this)
                .asGif()
                .load(R.drawable.rubbish_gif)
                .apply(RequestOptions.centerInsideTransform())
                .into(rubbish_gif);
    }
    public void initListener(){
        bt_startScan.setOnClickListener(this);
        cleaner_layout.setOnClickListener(this);
        delete_layout.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cleaner_layout:
            case R.id.delete_layout:
            case R.id.bt_startScan:
                Intent intent = new Intent(MainAct.this, ClearAct.class);
                intent.putExtra("rubbish",rubbish);
                startActivity(intent);
        }
    }

    public String getRubbish(){

        Random random = new Random();
        double number = 100 + random.nextDouble() * 900; // 生成100到1000之间的浮点数
        DecimalFormat decimalFormat = new DecimalFormat("#.00"); // 保留两位小数
        String formattedNumber = decimalFormat.format(number);
        return formattedNumber;

    }
}