package com.example.prj_ailatrieuphu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.skydoves.progressview.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import InternetProblem.NetworkChangeListener;
import Model.Answer;
import Model.Comment;
import Model.Question;
import Model.User;
import myinterface.CommentInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,WarningExitDialog.editTextListener {
    private final int REQUEST_CALLPHONE_PERMISSION = 100;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private String [] RequestArray = {"android.permission.CALL_PHONE"};
    ArrayList<User> ds_bestUser = new ArrayList<>();
    BackgroundTask backgroundTask = new BackgroundTask(this);
    CountDownTimer countDownTimer;
    EditText edtPhoneNumber;
    Button btnAskDialog;
    ProgressBar prgTimeLimit;
    TextView txtQuestionContent,txtQuestion,txtPercentA,txtPercentB,txtPercentC,txtPercentD,txtScore;
    ProgressView progress_1,progress_2,progress_3,progress_4;
    TextView txtAnswer1,txtAnswer2,txtAnswer3,txtAnswer4,txtShowDistanceLeft,txtShowName,txtShowBestScore;
    ImageButton btn5050,btnAsk,btnRandomHelp,btnComment;
    ArrayList<Answer> ds_dapan = new ArrayList<>();
    ArrayList<Question> ds_cauhoi = new ArrayList<>();
    User bestScoreUser;
    private int question_number = 0;
    private Question mQuestion;
    private boolean trangthaiAsk = true,trangthai5050 = true,trangthaiRH = false,trangthaiComment = true;
    private boolean isTimeLimit = false;
    private int score = 0;
    private int scoreBonus = 10;
    private int numberMode = 0;
    private int countRandomhelp = 0;
    private int limitCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        Log.d("count","on Create");
        countDownTimer = new CountDownTimer(30000,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                prgTimeLimit.setProgress(prgTimeLimit.getProgress()+100);
            }

            @Override
            public void onFinish() {
                GameOver();
            }
        };

        Intent it = getIntent();
        Bundle bd = it.getBundleExtra("bundle");
        if(bd!=null) {
            ds_cauhoi = (ArrayList<Question>) bd.getSerializable("danhsachcauhoi");
            question_number = bd.getInt("vitri",1);
            isTimeLimit = bd.getBoolean("isLimitTime");
            scoreBonus = bd.getInt("scoreBonus");
            numberMode = bd.getInt("numberMode");
            Log.d("getTime",isTimeLimit + "");
            Collections.shuffle(ds_cauhoi);
        }
        else{
            TinyDB tinyDB = new TinyDB(MainActivity.this);
            ArrayList<Object> dsOb = tinyDB.getListObject("ds_cauhoi",Question.class);
            for(Object obj : dsOb){
                ds_cauhoi.add((Question) obj);
            }
            question_number = tinyDB.getInt("question_number");
            trangthai5050 = tinyDB.getBoolean("trangthai5050");
            trangthaiAsk = tinyDB.getBoolean("trangthaiAsk");
            trangthaiRH = tinyDB.getBoolean("trangthaiRH");
            trangthaiComment = tinyDB.getBoolean("trangthaiComment");
            numberMode = tinyDB.getInt("numberMode");
            score = tinyDB.getInt("score");
            scoreBonus = tinyDB.getInt("scoreBonus");
            isTimeLimit = tinyDB.getBoolean("isTimeLimit");
            countRandomhelp = tinyDB.getInt("countRandomhelp");
        }
        setQuestion(question_number - 1);
        setButtonState();
            prgTimeLimit.setVisibility(View.VISIBLE);
            countTimeLimit();
            if(isTimeLimit && bd==null) {
            GameOver();
        }
        setLimitCount();
        //Toast.makeText(MainActivity.this,"Count = "+ countRandomhelp,Toast.LENGTH_SHORT).show();
        backgroundTask.getMaxScoreUser(ds_bestUser,txtShowBestScore,txtShowName,txtShowDistanceLeft);
    }

    private void setDistanceBestCore(){
        if(bestScoreUser.getDiemso() < score){
            txtShowDistanceLeft.setText("Đã vượt qua!");
        }
        else{
            txtShowDistanceLeft.setText(String.valueOf(bestScoreUser.getDiemso() - score));
        }
        //Toast.makeText(MainActivity.this,bestScoreUser.getDiemso() + "",Toast.LENGTH_SHORT).show();
    }

    private void setLimitCount(){
        Log.d("count","setLimitCount");
        switch (numberMode){
            case 0:{
                limitCount = 20;
                break;
            }
            case 1:{
                limitCount = 25;
                break;
            }
            case 2:{
                limitCount = 15;
                break;
            }
            case 3:{
                limitCount = 10;
                break;
            }
            default:{
                limitCount = 20;
                break;
            }
        }
    }

    private void setQuestion(int i){
        Log.d("count",i + " index");
        txtAnswer1.setBackgroundResource(R.drawable.bg_blue_corner30);
        txtAnswer2.setBackgroundResource(R.drawable.bg_blue_corner30);
        txtAnswer3.setBackgroundResource(R.drawable.bg_blue_corner30);
        txtAnswer4.setBackgroundResource(R.drawable.bg_blue_corner30);
        txtAnswer1.setEnabled(true);
        txtAnswer2.setEnabled(true);
        txtAnswer3.setEnabled(true);
        txtAnswer4.setEnabled(true);
        ds_dapan.clear();
        mQuestion = ds_cauhoi.get(i);
        txtQuestion.setText(getResources().getText(R.string.cauhoi) + " fbtn" + question_number);
        txtQuestionContent.setText(ds_cauhoi.get(i).getCauhoi());
        txtAnswer1.setText(ds_cauhoi.get(i).getDanhsachdapan().get(0).getDapan());
        txtAnswer2.setText(ds_cauhoi.get(i).getDanhsachdapan().get(1).getDapan());
        txtAnswer3.setText(ds_cauhoi.get(i).getDanhsachdapan().get(2).getDapan());
        txtAnswer4.setText(ds_cauhoi.get(i).getDanhsachdapan().get(3).getDapan());
        txtScore.setText(score + "");
        ds_dapan.add(ds_cauhoi.get(i).getDanhsachdapan().get(0));
        ds_dapan.add(ds_cauhoi.get(i).getDanhsachdapan().get(1));
        ds_dapan.add(ds_cauhoi.get(i).getDanhsachdapan().get(2));
        ds_dapan.add(ds_cauhoi.get(i).getDanhsachdapan().get(3));
        if(countRandomhelp == 3){
            trangthaiRH = true;
            setButtonState();
        }
        countTimeLimit();
    }

    private void setButtonState(){
        Log.d("count","setButton");
        btn5050.setEnabled(trangthai5050);
        btnRandomHelp.setEnabled(trangthaiRH);
        btnAsk.setEnabled(trangthaiAsk);
        btnComment.setEnabled(trangthaiComment);
        if(trangthai5050 == false){
            btn5050.setImageResource(R.drawable.f50_50_used);
        }
        else{
            btn5050.setImageResource(R.drawable.f50_50_old);
        }
        if(trangthaiAsk == false){
            btnAsk.setImageResource(R.drawable.ask_used);
        }
        else{
            btnAsk.setImageResource(R.drawable.ask_old);
        }
        if(trangthaiRH == true){
            btnRandomHelp.setImageResource(R.drawable.random_help);
        }
        else{
            btnRandomHelp.setImageResource(R.drawable.no_random_help);
        }
        if(trangthaiComment == false){
            btnComment.setImageResource(R.drawable.no_comment);
        }
    }

    @Override
    protected void onStart() {
        Log.d("count","onStart");
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);
        super.onStart();

    }

    @Override
    protected void onStop() {
        Log.d("count","onStop");
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    private void Anhxa() {
        Log.d("count","Anhxa");
        txtScore = (TextView) findViewById(R.id.txtScore);
        txtQuestionContent = (TextView) findViewById(R.id.txtQuestionContent);
        prgTimeLimit = (ProgressBar) findViewById(R.id.prgTimeLimit);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        txtAnswer1 = (TextView) findViewById(R.id.txtAnswer1);
        txtAnswer2 = (TextView) findViewById(R.id.txtAnswer2);
        txtAnswer3 = (TextView) findViewById(R.id.txtAnswer3);
        txtAnswer4 = (TextView) findViewById(R.id.txtAnswer4);
        txtShowName = (TextView) findViewById(R.id.txtShowName);
        txtShowDistanceLeft = (TextView) findViewById(R.id.txtShowDistanceLeft);
        txtShowBestScore = (TextView) findViewById(R.id.txtBestScore);
        btnRandomHelp = (ImageButton) findViewById(R.id.btnRandomHelp);
        btnAsk = (ImageButton) findViewById(R.id.btnAsk);
        btn5050 = (ImageButton) findViewById(R.id.btn5050);
        btnComment = (ImageButton) findViewById(R.id.btnComment);
        txtAnswer1.setOnClickListener(this);
        txtAnswer2.setOnClickListener(this);
        txtAnswer3.setOnClickListener(this);
        txtAnswer4.setOnClickListener(this);
        btn5050.setOnClickListener(this);
        btnAsk.setOnClickListener(this);
        btnRandomHelp.setOnClickListener(this);
        btnComment.setOnClickListener(this);
    }

    private void countTimeLimit(){
        Log.d("count","countTimeLimit");
        prgTimeLimit.setProgress(0);
        if(countDownTimer!=null){
            Log.d("count","1");
            countDownTimer.cancel();
        }
        countDownTimer.start();
    }

    private void checkAnswer(TextView textView,Question question,Answer answer){
        Log.d("count","checkAnswer");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(answer.getTrangthai()==1){
                    score += scoreBonus;
                    textView.setBackgroundResource(R.drawable.bg_green_corner30);
                    countRandomhelp++;

                    nextQuestion();
                }
                else{
                    textView.setBackgroundResource(R.drawable.bg_red_corner30);
                    showAnswerCorrect();
                    GameOver();
                }
            }
        },1000);
    }

    private void nextQuestion() {
        Log.d("count","nextQuestion");
        if(question_number == ds_cauhoi.size()){
            GameOver();
        }
        else{
            question_number++;
            bestScoreUser = new User(backgroundTask.getBestUser().getId(),
                    backgroundTask.getBestUser().getDiemso(),
                    backgroundTask.getBestUser().getEmail(),
                    backgroundTask.getBestUser().getHoten(),
                    backgroundTask.getBestUser().getCheckthoigian(),
                    backgroundTask.getBestUser().getMucdo(),
                    backgroundTask.getBestUser().getDate());
            setDistanceBestCore();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setQuestion(question_number-1);
                }
            },1000);
        }
    }

    private void GameOver(){
        Log.d("count","Game Over");
            try {
                countDownTimer.cancel();
            }catch (Exception e){

            }

        //Toast.makeText(MainActivity.this,"Bạn thua rồi",Toast.LENGTH_SHORT).show();
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_gameover);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();
        TinyDB tinyDB = new TinyDB(this);
        tinyDB.putBoolean("canContinue",false);


        TextView txtScoreResult = (TextView) dialog.findViewById(R.id.txtScoreResult);
        txtScoreResult.setText("SCORE: " + String.format("%09d",score));

        Button btnManHinhChinh = (Button) dialog.findViewById(R.id.btnManHinhChinh);
        btnManHinhChinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MainActivity.this.finish();
            }
        });

        Button btnChoiLai = (Button) dialog.findViewById(R.id.btnChoiLai);
        btnChoiLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });

        Button btnRank = (Button) dialog.findViewById(R.id.btnBangXepHang);
        btnRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenRank = new Intent(MainActivity.this,RankActivity.class);
                startActivity(intenRank);
            }
        });

        Button btnLuuDiem = (Button) dialog.findViewById(R.id.btnLuuDiem);
        btnLuuDiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog1 = new Dialog(MainActivity.this);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.dialog_savescore);
                Window window = dialog1.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                dialog1.setCancelable(false);
                dialog1.show();

                EditText edtEmail = (EditText) dialog1.findViewById(R.id.edtEmail);
                EditText edtHoTen = (EditText) dialog1.findViewById(R.id.edtHoTen);
                EditText edtComment = (EditText) dialog1.findViewById(R.id.edtComment);

                Button btnNoSaveScore = (Button) dialog1.findViewById(R.id.btnNoSaveScore);
                btnNoSaveScore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });

                Button btnSaveScore = (Button) dialog1.findViewById(R.id.btnSaveScore);
                btnSaveScore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       //  Toast.makeText(MainActivity.this,new SimpleDateFormat("yyyy-MM-dd").format(new Date()),Toast.LENGTH_SHORT).show();
                        String email = edtEmail.getText().toString();
                        String hoten = edtHoTen.getText().toString();
                        String comment = edtComment.getText().toString();
                        int checkthoigian;
                        if(isTimeLimit){
                            checkthoigian = 1;
                        }
                        else{
                            checkthoigian = 0;
                        }
                        if(email.trim().isEmpty() || hoten.trim().isEmpty()){
                            Toast.makeText(MainActivity.this,"Chưa nhập email hoặc họ tên",Toast.LENGTH_SHORT).show();
                        }
                        else{
//                            Toast.makeText(MainActivity.this,"Done",Toast.LENGTH_SHORT).show();
                            String curdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            backgroundTask.insertUser(new User(0,score,email,hoten,checkthoigian,numberMode,curdate));
                            if(!comment.isEmpty()){
                                backgroundTask.insertComment(new Comment(0,hoten,comment,0,ds_cauhoi.get(question_number-1).getCauhoi()));
                            }
                            dialog1.dismiss();
                            btnLuuDiem.setEnabled(false);
                            btnLuuDiem.setBackgroundResource(R.drawable.bg_gray_corner30);
                        }
                    }
                });

            }
        });
    }

    private void showAnswerCorrect() {
        Log.d("count","showAnswerCorrect");
        if(ds_dapan.get(0).getTrangthai()==1){
            txtAnswer1.setBackgroundResource(R.drawable.bg_green_corner30);
        }
        else if(ds_dapan.get(1).getTrangthai()==1){
            txtAnswer2.setBackgroundResource(R.drawable.bg_green_corner30);
        }
        else if(ds_dapan.get(2).getTrangthai()==1){
            txtAnswer3.setBackgroundResource(R.drawable.bg_green_corner30);
        }
        else if(ds_dapan.get(3).getTrangthai()==1){
            txtAnswer4.setBackgroundResource(R.drawable.bg_green_corner30);
        }
    }

    @Override
    protected void onDestroy() {
        Log.d("count","Destroy");
        super.onDestroy();
        luuGame();

            try {
                countDownTimer.cancel();
            }catch (Exception e){

            }

    }

    private void luuGame(){
        Log.d("count","LuuGame");
        TinyDB tinyDB = new TinyDB(MainActivity.this);
        ArrayList<Object> dscauhoiObject = new ArrayList<>();
        for(Question question : ds_cauhoi){
            dscauhoiObject.add((Object) question);
        }

        tinyDB.putInt("question_number",question_number);
        tinyDB.putListObject("ds_cauhoi",dscauhoiObject);
        tinyDB.putBoolean("trangthai5050",trangthai5050);
        tinyDB.putBoolean("trangthaiAsk",trangthaiAsk);
        tinyDB.putBoolean("trangthaiRH",trangthaiRH);
        tinyDB.putBoolean("trangthaiComment",trangthaiComment);
        tinyDB.putBoolean("isTimeLimit",isTimeLimit);
        tinyDB.putInt("score",score);
        tinyDB.putInt("numberMode",numberMode);
        tinyDB.putInt("scoreBonus",scoreBonus);
        tinyDB.putBoolean("canContinue",true);
        tinyDB.putInt("countRandomhelp",countRandomhelp);
        tinyDB.putInt("limitCount",limitCount);
    }

    @Override
    public void onClick(View v) {
        Log.d("count","OnClick");
        switch (v.getId()){
            case R.id.txtAnswer1:{
                txtAnswer1.setBackgroundResource(R.drawable.bg_orange_corner30);
                checkAnswer(txtAnswer1,mQuestion,mQuestion.getDanhsachdapan().get(0));
                break;
            }
            case R.id.txtAnswer2:{
                txtAnswer2.setBackgroundResource(R.drawable.bg_orange_corner30);
                checkAnswer(txtAnswer2,mQuestion,mQuestion.getDanhsachdapan().get(1));
                break;
            }
            case R.id.txtAnswer3:{
                txtAnswer3.setBackgroundResource(R.drawable.bg_orange_corner30);
                checkAnswer(txtAnswer3,mQuestion,mQuestion.getDanhsachdapan().get(2));
                break;
            }
            case R.id.txtAnswer4:{
                txtAnswer4.setBackgroundResource(R.drawable.bg_orange_corner30);
                checkAnswer(txtAnswer4,mQuestion,mQuestion.getDanhsachdapan().get(3));
                break;
            }
            case R.id.btn5050:{
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Có chắc chắn dùng quyền trợ giúp bỏ đi 2 đáp án sai?");
                alert.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trangthai5050 = false;
                        setButtonState();
                        show5050Help();
                    }
                });
                alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
                break;
            }
            case R.id.btnAsk:{
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Có chắc chắn dùng quyền trợ giúp bỏ thống kê ý kiến khán giả?");
                alert.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trangthaiAsk = false;
                        setButtonState();
                        showAskHelp();
                    }
                });
                alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
                break;
            }
            case R.id.btnRandomHelp:{
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Có chắc chắn muốn trừ đi 30% số điểm để Random Help?");
                alert.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trangthaiRH = false;
                        countRandomhelp = 0;
                        setButtonState();
                        Random rd = new Random();
                        int rdnumber = 1 + rd.nextInt(3);
                        switch (rdnumber) {
                            case 1: {
                                show5050Help();
                                break;
                            }
                            case 2: {
                                showAskHelp();
                                break;
                            }
                            case 3: {
                                showCommentHelp();
                                break;
                            }
                        }
                        score -= (score*20/100);
                        txtScore.setText(score + "");
                    }
                });
                alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
                break;
            }
            case R.id.btnComment:{
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Bạn có chắc chắn muốn dùng quyền trợ giúp tham khảo comment không?");
                alert.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trangthaiComment = false;
                        setButtonState();
                        showCommentHelp();
                    }
                });
                alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
                break;
            }
        }
    }

    private void show5050Help(){

        int dem = 0;
        if(ds_dapan.get(0).getTrangthai()==0){
            dem++;
            txtAnswer1.setEnabled(false);
            txtAnswer1.setText("");
        }
        if(ds_dapan.get(1).getTrangthai()==0){
            dem++;
            txtAnswer2.setText("");
            txtAnswer2.setEnabled(false);
        }
        if(dem<2 && ds_dapan.get(2).getTrangthai()==0){
            dem++;
            txtAnswer3.setText("");
            txtAnswer3.setEnabled(false);
        }
        if(dem<2 && ds_dapan.get(3).getTrangthai()==0){
            dem++;
            txtAnswer4.setText("");
            txtAnswer4.setEnabled(false);
        }
    }
    private void showAskHelp(){
        int prg1 = 25,prg2 = 25,prg3 = 25,prg4 = 25;
        // Random rd = new Random();
        if(ds_dapan.get(0).getTrangthai()==1){
            prg1 = ThreadLocalRandom.current().nextInt(40,101);
            prg2 = ThreadLocalRandom.current().nextInt(0,(100 - prg1) + 1);
            prg3 = ThreadLocalRandom.current().nextInt(0,(100 - prg1 - prg2) + 1);
            prg4 = 100 - prg1 - prg2 - prg3;
        }
        else if(ds_dapan.get(1).getTrangthai()==1){
            prg2 = ThreadLocalRandom.current().nextInt(40,101);
            prg1 = ThreadLocalRandom.current().nextInt(0,(100 - prg2) + 1);
            prg3 = ThreadLocalRandom.current().nextInt(0,(100 - prg1 - prg2) + 1);
            prg4 = 100 - prg1 - prg2 - prg3;
        }
        else if(ds_dapan.get(2).getTrangthai()==1){
            prg3 = ThreadLocalRandom.current().nextInt(40,101);
            prg2 = ThreadLocalRandom.current().nextInt(0,(100 - prg3) + 1);
            prg1 = ThreadLocalRandom.current().nextInt(0,(100 - prg3 - prg2) + 1);
            prg4 = 100 - prg1 - prg2 - prg3;
        }
        else if(ds_dapan.get(3).getTrangthai()==1){
            prg4 = ThreadLocalRandom.current().nextInt(40,101);
            prg2 = ThreadLocalRandom.current().nextInt(0,(100 - prg4) + 1);
            prg3 = ThreadLocalRandom.current().nextInt(0,(100 - prg4 - prg2) + 1);
            prg1 = 100 - prg4 - prg2 - prg3;
        }
        openAskDialog(prg1,prg2,prg3,prg4);
    }
    private void showCommentHelp(){
        ArrayList<Comment> ds_comment = new ArrayList<>();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_comment);

        RecyclerView mRecycleView = (RecyclerView) dialog.findViewById(R.id.myRecycler);
        CommentAdapter commentAdapter = new CommentAdapter(ds_comment, new CommentInterface() {
            @Override
            public void onClickInterface(Comment comment) {
                Toast.makeText(MainActivity.this,"Đã click vào comment số " + comment.getId(),Toast.LENGTH_SHORT).show();
            }
        });
        backgroundTask.loadListComment(ds_comment,ds_cauhoi.get(question_number-1).getCauhoi(),commentAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(commentAdapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(this,R.drawable.custom_divider));
        mRecycleView.addItemDecoration(itemDecoration);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        dialog.getWindow().setLayout(width,height/2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }


    private void openAskDialog(int prg1,int prg2,int prg3,int prg4){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_askdialog);
        Window window = dialog.getWindow();
            if (window == null) {
                return;
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);

            btnAskDialog = (Button) dialog.findViewById(R.id.btnAskDialog);
            txtPercentA = (TextView) dialog.findViewById(R.id.txtPercentA);
            txtPercentB = (TextView) dialog.findViewById(R.id.txtPercentB);
            txtPercentC = (TextView) dialog.findViewById(R.id.txtPercentC);
            txtPercentD = (TextView) dialog.findViewById(R.id.txtPercentD);
            progress_1 = (ProgressView) dialog.findViewById(R.id.progress_1);
            progress_2 = (ProgressView) dialog.findViewById(R.id.progress_2);
            progress_3 = (ProgressView) dialog.findViewById(R.id.progress_3);
            progress_4 = (ProgressView) dialog.findViewById(R.id.progress_4);

            txtPercentA.setText(prg1 + "%");
            txtPercentB.setText(prg2 + "%");
            txtPercentC.setText(prg3 + "%");
            txtPercentD.setText(prg4 + "%");

            progress_1.setProgress(prg1);
            progress_2.setProgress(prg2);
            progress_3.setProgress(prg3);
            progress_4.setProgress(prg4);

            btnAskDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
    }

    @Override
    public void onBackPressed() {
        Log.d("count","Back Press");
    WarningExitDialog warningExitDialog = new WarningExitDialog();
    warningExitDialog.show(getSupportFragmentManager(),"ExitWarning");
//        Toast.makeText(MainActivity.this,"Đã nhấn vào backpress",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEdtPicked(String str) {
        Log.d("count","EdtPicked");
        if(str.equals("Yes")){
            GameOver();
        }
    }

}