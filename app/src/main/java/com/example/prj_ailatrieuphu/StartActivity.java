package com.example.prj_ailatrieuphu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import InternetProblem.NetworkChangeListener;
import Model.Answer;
import Model.Question;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnNewGame,btnContinue,btnSetting,btnExit,btnRank;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    Switch switchTimeLimit,switchMode;
    RadioGroup radioGroup;
    boolean isLimitTime = true,isModeOn = false;
    int numberMode=0;
    int scoreBonus=10;
    ArrayList<Question> ds_cauhoi = new ArrayList<>();
    public BackgroundTask backgroundTask = new BackgroundTask(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Anhxa();
        setSettingState();
        backgroundTask.getDataCauhoi(String.valueOf(numberMode));
    }

    @Override
    protected void onStart() {

        TinyDB tinyDB = new TinyDB(this);
//        boolean canContinue = tinyDB.getBoolean("canContinue");
//        if(canContinue) {
//            btnContinue.setVisibility(View.VISIBLE);
//        }
//        else{
//            btnContinue.setVisibility(View.GONE);
//        }
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);


        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    private void Anhxa(){
        btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnExit = (Button) findViewById(R.id.btnExit);
        btnRank = (Button) findViewById(R.id.btnShowRank);
        btnNewGame.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnRank.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNewGame:{
                ds_cauhoi = backgroundTask.getListCauHoi();
                Log.d("list",ds_cauhoi.size() + "");
                if(ds_cauhoi.size()==0){
                    Toast.makeText(StartActivity.this,"Không thể bắt đầu trò chơi, kiểm tra lại kết nối!",Toast.LENGTH_SHORT).show();
                }else {
                    Intent it = new Intent(StartActivity.this, MainActivity.class);
                    Bundle bd = new Bundle();
                    bd.putSerializable("danhsachcauhoi", (Serializable) ds_cauhoi);
                    bd.putInt("vitri", 1);
                    bd.putBoolean("isLimitTime", isLimitTime);
                    bd.putInt("scoreBonus", scoreBonus);
                    bd.putInt("numberMode", numberMode);
                    it.putExtra("bundle", bd);
                    startActivity(it);
                }
                break;
            }
            case R.id.btnExit:{
                showExitDialog();
                break;
            }
//            case R.id.btnContinue:{
//                Intent it = new Intent(StartActivity.this,MainActivity.class);
//                startActivity(it);
//                break;
//            }
            case R.id.btnSetting:{
                Dialog dialog = new Dialog(this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_setting);
                Window window = dialog.getWindow();
                if(window == null){
                    return;
                }
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.show();
//                switchTimeLimit = (Switch) dialog.findViewById(R.id.switchTimeLimit);
//                //setSettingState();
//                //switchTimeLimit.setChecked(isLimitTime);
//                switchTimeLimit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if(isChecked == true){
//                            isLimitTime = true;
//                        }
//                        else{
//                            isLimitTime = false;
//                        }
//                        TinyDB tinyDB = new TinyDB(StartActivity.this);
//                        tinyDB.putBoolean("isLimitTime",isLimitTime);
//                    }
//                });
                switchMode = (Switch) dialog.findViewById(R.id.switchMode);
                switchMode.setChecked(isModeOn);
                switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        TinyDB tinyDB = new TinyDB(StartActivity.this);
                        if(isChecked == true){
                            isModeOn = true;
                            radioGroup.setVisibility(View.VISIBLE);
                            setModeState(tinyDB,1,5);
                        }
                        else{
                            isModeOn = false;
                            radioGroup.setVisibility(View.GONE);
                            setModeState(tinyDB,0,10);
                        }

                        tinyDB.putBoolean("isModeOn",isModeOn);
                    }
                });
                RadioButton rdMode1,rdMode2,rdMode3;
                rdMode1 = (RadioButton) dialog.findViewById(R.id.rdMode1);
                rdMode2 = (RadioButton) dialog.findViewById(R.id.rdMode2);
                rdMode3 = (RadioButton) dialog.findViewById(R.id.rdMode3);
                radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
                if(isModeOn){
                    radioGroup.setVisibility(View.VISIBLE);
                    if(numberMode == 1){
                        rdMode1.setChecked(true);
                    }
                    if(numberMode == 2){
                        rdMode2.setChecked(true);
                    }
                    if(numberMode == 3){
                        rdMode3.setChecked(true);
                    }
                }
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        TinyDB tinyDB = new TinyDB(StartActivity.this);
                        switch (checkedId){
                            case R.id.rdMode1:{
                                setModeState(tinyDB,1,5);
                                break;
                            }
                            case R.id.rdMode2:{
                                setModeState(tinyDB,2,15);
                                break;
                            }
                            case R.id.rdMode3:{
                                setModeState(tinyDB,3,20);
                                break;
                            }
                        }
                    }
                });
                break;
            }
            case R.id.btnShowRank:{
                Intent intentRank = new Intent(StartActivity.this,RankActivity.class);
                startActivity(intentRank);
            }
        }
    }

    private void setModeState(TinyDB tinyDB,int nM,int sB){
        numberMode = nM;
        tinyDB.putInt("numberMode",numberMode);
        scoreBonus = sB;
        tinyDB.putInt("scoreBonus",scoreBonus);
        backgroundTask.getDataCauhoi(String.valueOf(numberMode));
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
      //  super.onBackPressed();
    }

    private void setSettingState(){
        TinyDB tinyDB = new TinyDB(StartActivity.this);
       // isLimitTime = tinyDB.getBoolean("isLimitTime");
        isModeOn = tinyDB.getBoolean("isModeOn");
        numberMode = tinyDB.getInt("numberMode");
        scoreBonus = tinyDB.getInt("scoreBonus");
    }

    private void showExitDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Chắc chắn muốn thoát?");
        alert.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent it = new Intent(getApplicationContext(),StartActivity.class);
                startActivity(it);

                Intent it2 = new Intent(Intent.ACTION_MAIN);
                it2.addCategory(Intent.CATEGORY_HOME);
                startActivity(it2);
                StartActivity.this.finish();
            }
        });
        alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }
}