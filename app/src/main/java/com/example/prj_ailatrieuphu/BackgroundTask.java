package com.example.prj_ailatrieuphu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import BangXepHangTab.PlayerRecordAdapter;
import Model.Answer;
import Model.Comment;
import Model.Question;
import Model.User;

public class BackgroundTask {
    public Context context;
    ArrayList<Question> ds_cauhoi = new ArrayList<>();
    ArrayList<Comment> ds_mycomment = new ArrayList<>();
    User bestScoreUser = new User();

    Dialog dialog;

    public BackgroundTask(Context context) {
        this.context = context;
    }

    private void showLoadingDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_screen);
    }

    String urlgetData = "https://minhducphuonganh02.000webhostapp.com/getData.php";

    public void getDataCauhoi(String mode) {
        showLoadingDialog();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            ds_cauhoi.clear();
                            JSONArray responseArray = new JSONArray(response);
                            for (int i = 0; i < responseArray.length(); i++) {
                                ArrayList<Answer> ds_dapan = new ArrayList<>();
                                JSONObject jsonObject = responseArray.getJSONObject(i);
                                JSONArray jsonArray = jsonObject.getJSONArray("mangdoituong");
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject job = jsonArray.getJSONObject(j);
                                    ds_dapan.add(new Answer(job.getString("cautraloi"), job.getInt("trangthai")));
                                }
                                if (ds_dapan.size() > 0) {
                                    ds_cauhoi.add(new Question(jsonObject.getString("cauhoi"), jsonObject.getInt("mucdo"), ds_dapan));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // dialog.dismiss();
                Log.d("error", "Error: " + error.toString());
                Toast.makeText(context, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("mucdo", mode);
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestqueue(stringRequest);
    }

    String urlgetDataComment = "https://minhducphuonganh02.000webhostapp.com/getDataComment.php";

    public void loadListComment(ArrayList<Comment> ds_comment, String cauhoi, CommentAdapter commentAdapter) {
        showLoadingDialog();
        dialog.show();
        Log.d("abc", "Join this");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgetDataComment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("abc", "Response: " + response);
                        dialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ds_comment.add(new Comment(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("user"),
                                        jsonObject.getString("content"),
                                        jsonObject.getInt("like"),
                                        jsonObject.getString("cauhoi")));
//                            Toast.makeText(context,ds_comment.get(0).getName(),Toast.LENGTH_SHORT).show();
                                Log.d("abc", ds_comment.get(i).getName());
                            }
                            Log.d("abc", "commentAdapter.notifyDataSetChanged();");
                            commentAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  dialog.dismiss();
                Log.d("error", "Error: " + error.toString());
                Toast.makeText(context, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("myques", cauhoi);
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestqueue(stringRequest);
    }

    String urlgetDataWeekUser = "https://minhducphuonganh02.000webhostapp.com/getWeekUser.php";

    public void loadListWeekUser(ArrayList<User> ds_user, PlayerRecordAdapter playerRecordAdapter) {
        showLoadingDialog();
        dialog.show();
        Log.d("abc", "Join this");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlgetDataWeekUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        try {
                            ds_user.clear();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ds_user.add(new User(
                                        jsonObject.getInt("id"),
                                        jsonObject.getInt("diemso"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("hoten"),
                                        jsonObject.getInt("checkthoigian"),
                                        jsonObject.getInt("mucdo"),
                                        jsonObject.getString("date")));
//                            Toast.makeText(context,ds_comment.get(0).getName(),Toast.LENGTH_SHORT).show();
                                // Log.d("abc",ds_comment.get(i).getName());
                            }
                            playerRecordAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   dialog.dismiss();
                Log.d("error", "Error: " + error.toString());
                Toast.makeText(context, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(context).addToRequestqueue(stringRequest);
    }

    String urlgetDataMonthUser = "https://minhducphuonganh02.000webhostapp.com/getMonthUser.php";

    public void loadListMonthUser(ArrayList<User> ds_user, PlayerRecordAdapter playerRecordAdapter) {
        showLoadingDialog();
        dialog.show();
        Log.d("abc", "Join this");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlgetDataMonthUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        try {
                            ds_user.clear();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ds_user.add(new User(
                                        jsonObject.getInt("id"),
                                        jsonObject.getInt("diemso"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("hoten"),
                                        jsonObject.getInt("checkthoigian"),
                                        jsonObject.getInt("mucdo"),
                                        jsonObject.getString("date")));
//                            Toast.makeText(context,ds_comment.get(0).getName(),Toast.LENGTH_SHORT).show();
                                // Log.d("abc",ds_comment.get(i).getName());
                            }
                            playerRecordAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  dialog.dismiss();
                Log.d("error", "Error: " + error.toString());
                Toast.makeText(context, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(context).addToRequestqueue(stringRequest);
    }

    String urlgetDataYearUser = "https://minhducphuonganh02.000webhostapp.com/getYearUser.php";

    public void loadListYearUser(ArrayList<User> ds_user, PlayerRecordAdapter playerRecordAdapter) {
        showLoadingDialog();
        dialog.show();
        Log.d("abc", "Join this");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlgetDataYearUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        try {
                            ds_user.clear();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ds_user.add(new User(
                                        jsonObject.getInt("id"),
                                        jsonObject.getInt("diemso"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("hoten"),
                                        jsonObject.getInt("checkthoigian"),
                                        jsonObject.getInt("mucdo"),
                                        jsonObject.getString("date")));
//                            Toast.makeText(context,ds_comment.get(0).getName(),Toast.LENGTH_SHORT).show();
                                // Log.d("abc",ds_comment.get(i).getName());
                            }
                            playerRecordAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // dialog.dismiss();
                Log.d("error", "Error: " + error.toString());
                Toast.makeText(context, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(context).addToRequestqueue(stringRequest);
    }

    String urlgetAllUser = "https://minhducphuonganh02.000webhostapp.com/getAllUser.php";
    String urlgetUserDate = "https://minhducphuonganh02.000webhostapp.com/getUserDate.php";
    String urlgetUserTime = "https://minhducphuonganh02.000webhostapp.com/getUserTime.php";
    String urlgetUserTimeAndDate = "https://minhducphuonganh02.000webhostapp.com/getUserTimeAndDate.php";

    public void loadListDateTimeUser(ArrayList<User> ds_user, PlayerRecordAdapter playerRecordAdapter, int choice, String timefrom, String timeto) {
        showLoadingDialog();
        //   Toast.makeText(context,timefrom + " " + timeto,Toast.LENGTH_SHORT).show();
        dialog.show();
        Log.d("abc", "Join this");
        String choiceurl = "";
        if (choice == 4) {
            choiceurl = urlgetAllUser;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, choiceurl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            //  Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                            try {
                                ds_user.clear();
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    ds_user.add(new User(
                                            jsonObject.getInt("id"),
                                            jsonObject.getInt("diemso"),
                                            jsonObject.getString("email"),
                                            jsonObject.getString("hoten"),
                                            jsonObject.getInt("checkthoigian"),
                                            jsonObject.getInt("mucdo"),
                                            jsonObject.getString("date")));
//                            Toast.makeText(context,ds_comment.get(0).getName(),Toast.LENGTH_SHORT).show();
                                    // Log.d("abc",ds_comment.get(i).getName());
                                }
                                playerRecordAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //  dialog.dismiss();
                    Log.d("error", "Error: " + error.toString());
                    Toast.makeText(context, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            });
            MySingleton.getInstance(context).addToRequestqueue(stringRequest);
        } else {
            if (choice == 1) {
                choiceurl = urlgetUserDate;
            } else if (choice == 2) {
                choiceurl = urlgetUserTime;
            } else if (choice == 3) {
                choiceurl = urlgetUserTimeAndDate;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, choiceurl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            // Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                            try {
                                ds_user.clear();
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    ds_user.add(new User(
                                            jsonObject.getInt("id"),
                                            jsonObject.getInt("diemso"),
                                            jsonObject.getString("email"),
                                            jsonObject.getString("hoten"),
                                            jsonObject.getInt("checkthoigian"),
                                            jsonObject.getInt("mucdo"),
                                            jsonObject.getString("date")));
//                            Toast.makeText(context,ds_comment.get(0).getName(),Toast.LENGTH_SHORT).show();
                                    // Log.d("abc",ds_comment.get(i).getName());
                                }
                                playerRecordAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //   dialog.dismiss();
                    Log.d("error", "Error: " + error.toString());
                    Toast.makeText(context, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("timefrom", timefrom);
                    params.put("timeto", timeto);
                    Log.d("day", timefrom + " - " + timeto);
                    return params;
                }
            };
            MySingleton.getInstance(context).addToRequestqueue(stringRequest);
        }
    }

    String urlInsertUser = "https://minhducphuonganh02.000webhostapp.com/insertUser.php";

    public void insertUser(User user) {
        showLoadingDialog();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlInsertUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        if (response.equals("1")) {
                            Toast.makeText(context, "Lưu điểm thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Lưu điểm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  dialog.dismiss();
                Log.d("error", "Error: " + error.toString());
                Toast.makeText(context, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("diemso", String.valueOf(user.getDiemso()));
                param.put("email", String.valueOf(user.getEmail()));
                param.put("hoten", user.getHoten());
                param.put("checkthoigian", String.valueOf(user.getCheckthoigian()));
                param.put("mucdo", String.valueOf(user.getMucdo()));
                param.put("date", String.valueOf(user.getDate()));
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestqueue(stringRequest);
    }

    String urlInsertComment = "https://minhducphuonganh02.000webhostapp.com/insertComment.php";

    public void insertComment(Comment comment) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlInsertComment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("1")) {
                            Toast.makeText(context, "Thêm comment thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Thêm comment thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.toString());
                Toast.makeText(context, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user", comment.getName());
                param.put("content", comment.getContent());
                param.put("cauhoi", comment.getCauhoi());
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestqueue(stringRequest);
    }

    String urlUpdateComment = "https://minhducphuonganh02.000webhostapp.com/updateComment.php";

    public void updateComment(Comment comment) {
        showLoadingDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdateComment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        if (response.equals("1")) {
                            Toast.makeText(context, "Update comment thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Update comment thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  dialog.dismiss();
                Log.d("error", "Error: " + error.toString());
                Toast.makeText(context, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("id", String.valueOf(comment.getId()));
                param.put("likenumber", String.valueOf(comment.getLike()));
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestqueue(stringRequest);
    }

    String urlgetMaxScoreUser = "https://minhducphuonganh02.000webhostapp.com/getMaxScoreUser.php";

    public void getMaxScoreUser(ArrayList<User> ds_user, TextView txtShowBestScore, TextView txtShowName, TextView txtShowDistanceLeft) {
        showLoadingDialog();
        dialog.show();
        Log.d("abc", "Join this");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlgetMaxScoreUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            bestScoreUser = new User(
                                    jsonObject.getInt("id"),
                                    jsonObject.getInt("diemso"),
                                    jsonObject.getString("email"),
                                    jsonObject.getString("hoten"),
                                    jsonObject.getInt("checkthoigian"),
                                    jsonObject.getInt("mucdo"),
                                    jsonObject.getString("date"));
//                            Toast.makeText(context,ds_comment.get(0).getName(),Toast.LENGTH_SHORT).show();
                            // Log.d("abc",ds_comment.get(i).getName());
                            txtShowName.setText(bestScoreUser.getHoten());
                            txtShowBestScore.setText(bestScoreUser.getDiemso() + "");
                            txtShowDistanceLeft.setText(String.valueOf(bestScoreUser.getDiemso()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  dialog.dismiss();
                Log.d("error", "Error: " + error.toString());
                Toast.makeText(context, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(context).addToRequestqueue(stringRequest);
    }

    public ArrayList<Question> getListCauHoi() {
        return ds_cauhoi;
    }

    public User getBestUser() {
        Log.d("abc", bestScoreUser.toString());
        return bestScoreUser;
    }
}
