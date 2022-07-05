package com.example.prj_ailatrieuphu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Model.Comment;
import myinterface.CommentInterface;

public class CommentListActivity extends AppCompatActivity {
    String urlgetDataComment = "https://minhducphuonganh02.000webhostapp.com/getDataComment.php";
    private CommentAdapter commentAdapter;
    private RecyclerView myRecycler;
    ArrayList<Comment> ds_comment = new ArrayList<>();
    BackgroundTask backgroundTask = new BackgroundTask(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        Anhxa();

        Intent it = getIntent();
        Bundle bundle = it.getBundleExtra("bundle");
        if(bundle!=null) {
            String cauhoi = bundle.getString("cauhoi");
            commentAdapter = new CommentAdapter(ds_comment, new CommentInterface() {
                @Override
                public void onClickInterface(Comment comment) {
                    Toast.makeText(CommentListActivity.this,"Đã click vào comment số " + comment.getId(),Toast.LENGTH_SHORT).show();
                }
            });
            backgroundTask.loadListComment(ds_comment,cauhoi,commentAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

            myRecycler.setLayoutManager(linearLayoutManager);
            myRecycler.setAdapter(commentAdapter);
            DividerItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
            itemDecoration.setDrawable(ContextCompat.getDrawable(this,R.drawable.custom_divider));
            myRecycler.addItemDecoration(itemDecoration);
        }
    }

    private void Anhxa() {
        myRecycler = (RecyclerView) findViewById(R.id.myRecycler);
    }

    private void loadList(String cauhoi){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}