package BangXepHangTab;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prj_ailatrieuphu.BackgroundTask;
import com.example.prj_ailatrieuphu.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Model.User;

public class FragmentTuyChon extends Fragment {
    View view;
    BackgroundTask backgroundTask;
    PlayerRecordAdapter playerRecordAdapter;
    ArrayList<User> userArrayList;
    Switch switchDayPick,switchTimePick;
    EditText edtNgayTu,edtNgayDen,edtGioTu,edtGioDen;
    ListView lvListRankTuyChon;
    Button btnShowResult;
    String timefrom,timeto;
    int mhour,mminute;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tuychon,container,false);
        Anhxa();

        switchDayPick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    edtNgayTu.setVisibility(View.VISIBLE);
                    edtNgayDen.setVisibility(View.VISIBLE);
                }
                else{
                    edtNgayTu.setVisibility(View.GONE);
                    edtNgayDen.setVisibility(View.GONE);
                }
            }
        });

        switchTimePick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    edtGioDen.setVisibility(View.VISIBLE);
                    edtGioTu.setVisibility(View.VISIBLE);
                }
                else{
                    edtGioTu.setVisibility(View.GONE);
                    edtGioDen.setVisibility(View.GONE);
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int thishour = calendar.get(Calendar.HOUR);
        final int thisminute = calendar.get(Calendar.MINUTE);


        edtNgayTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar internalCalendar = Calendar.getInstance();
                                internalCalendar.set(year,month,dayOfMonth,0,0);
                                edtNgayTu.setText(DateFormat.format("yyyy-MM-dd",internalCalendar));
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        edtNgayDen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar internalCalendar = Calendar.getInstance();
                                internalCalendar.set(year,month,dayOfMonth,0,0);
                                edtNgayDen.setText(DateFormat.format("yyyy-MM-dd",internalCalendar));
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        edtGioTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        view.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mhour = hourOfDay;
                                mminute = minute;
                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.set(0,0,0,mhour,mminute);
                                edtGioTu.setText(DateFormat.format("HH:mm:ss",calendar1));
                            }
                        },thishour,thisminute,true
                );
                timePickerDialog.updateTime(thishour,thisminute);
                timePickerDialog.show();
            }
        });
        edtGioDen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        view.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mhour = hourOfDay;
                                mminute = minute;
                                Calendar calendar2 = Calendar.getInstance();
                                calendar2.set(0,0,0,mhour,mminute);
                                edtGioDen.setText(DateFormat.format("HH:mm:ss",calendar2));
                            }
                        },thishour,thisminute,true
                );
                timePickerDialog.updateTime(thishour,thisminute);
                timePickerDialog.show();
            }
        });

        btnShowResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchTimePick.isChecked() && switchDayPick.isChecked()){
//                    Toast.makeText(view.getContext(),"Time",Toast.LENGTH_SHORT).show();
                    if(checkTimeAndDate()){
                        backgroundTask.loadListDateTimeUser(userArrayList,playerRecordAdapter,3,timefrom,timeto);
                    }
                }
                else if(switchTimePick.isChecked() && !switchDayPick.isChecked()){
                   // Toast.makeText(view.getContext(),"Date",Toast.LENGTH_SHORT).show();
                    if(checkTime()){
                        backgroundTask.loadListDateTimeUser(userArrayList,playerRecordAdapter,2,timefrom,timeto);
                    }
                }
                else if(!switchTimePick.isChecked() && switchDayPick.isChecked()){
                   // Toast.makeText(view.getContext(),"Both",Toast.LENGTH_SHORT).show();
                    if(checkDate()){
                        backgroundTask.loadListDateTimeUser(userArrayList,playerRecordAdapter,1,timefrom,timeto);
                    }
                }
                else{
                  //  Toast.makeText(view.getContext(),"no",Toast.LENGTH_SHORT).show();
                    backgroundTask.loadListDateTimeUser(userArrayList,playerRecordAdapter,4,timefrom,timeto);
                }
            }
        });

        return view;
    }

    private boolean checkDate(){
            try{
                timefrom = edtNgayTu.getText().toString();
                timeto = edtNgayDen.getText().toString();
                if(timefrom.isEmpty() || timeto.isEmpty()){
                    Toast.makeText(view.getContext(),"Ngày nhập không được bỏ chống",Toast.LENGTH_SHORT).show();
                    return false;
                }
                else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = sdf.parse(timefrom);
                    Date date2 = sdf.parse(timeto);
                    if (getKhoangCach(date1, date2) < 0) {
                        Toast.makeText(view.getContext(), "Ngày bắt đầu và kết thúc không hợp lí", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    else{
                        return true;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(view.getContext(),"Ngày nhập sai định dạng",Toast.LENGTH_SHORT).show();
                return false;
            }
    }
    private boolean checkTime(){
        try{
            timefrom = edtGioTu.getText().toString();
            timeto = edtGioDen.getText().toString();
            if(timefrom.isEmpty() || timeto.isEmpty()){
                Toast.makeText(view.getContext(),"Thời gian nhập không được bỏ chống",Toast.LENGTH_SHORT).show();
                return false;
            }
            else {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date date1 = sdf.parse(timefrom);
                Date date2 = sdf.parse(timeto);
                if (getKhoangCach(date1, date2) < 0) {
                    Toast.makeText(view.getContext(), "Thời gian bắt đầu, kết thúc không hợp lí", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(view.getContext(),"Thời gian nhập sai định dạng",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private boolean checkTimeAndDate(){
        try{
            timefrom = edtNgayTu.getText().toString() + " " + edtGioTu.getText().toString();
            timeto = edtNgayDen.getText().toString() + " " + edtGioDen.getText().toString();
            if(timefrom.isEmpty() || timeto.isEmpty()){
                Toast.makeText(view.getContext(),"Thời gian nhập không được bỏ chống",Toast.LENGTH_SHORT).show();
                return false;
            }
            else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date1 = sdf.parse(timefrom);
                Date date2 = sdf.parse(timeto);
                if (getKhoangCach(date1, date2) < 0) {
                    Toast.makeText(view.getContext(), "Thời gian bắt đầu, kết thúc không hợp lí", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(view.getContext(),"Thời gian nhập sai định dạng",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private float getKhoangCach(Date date1,Date date2){
        long diff = 0;
        try{
            diff = date2.getTime() - date1.getTime();
        }catch(Exception e){

        }
        return diff;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void Anhxa() {
        backgroundTask = new BackgroundTask(view.getContext());
        switchDayPick = (Switch) view.findViewById(R.id.switchDayPick);
        switchTimePick = (Switch) view.findViewById(R.id.switchTimePick);
        edtNgayTu = (EditText) view.findViewById(R.id.edtNgayTu);
        edtNgayDen = (EditText) view.findViewById(R.id.edtNgayDen);
        edtGioTu = (EditText) view.findViewById(R.id.edtGioTu);
        edtGioDen = (EditText) view.findViewById(R.id.edtGioDen);
        btnShowResult = (Button) view.findViewById(R.id.btnShowResult);
        lvListRankTuyChon = (ListView) view.findViewById(R.id.lvListRankTuyChon);

        userArrayList = new ArrayList<>();
        playerRecordAdapter = new PlayerRecordAdapter(view.getContext(),R.layout.line_rank,userArrayList);
        lvListRankTuyChon.setAdapter(playerRecordAdapter);
    }
}
