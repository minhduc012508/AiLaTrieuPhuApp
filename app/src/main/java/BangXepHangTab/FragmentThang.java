package BangXepHangTab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prj_ailatrieuphu.BackgroundTask;
import com.example.prj_ailatrieuphu.R;

import java.util.ArrayList;

import Model.User;

public class FragmentThang extends Fragment {
    View view;
    PlayerRecordAdapter playerRecordAdapter;
    ArrayList<User> userArrayList;
    ListView lvListRankThang;
    BackgroundTask backgroundTask;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trongthang,container,false);
        Anhxa();

        playerRecordAdapter = new PlayerRecordAdapter(view.getContext(),R.layout.line_rank,userArrayList);
        lvListRankThang.setAdapter(playerRecordAdapter);

        Log.d("cycle","onCreateView");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        backgroundTask.loadListMonthUser(userArrayList,playerRecordAdapter);
    }

    private void Anhxa() {
        backgroundTask = new BackgroundTask(view.getContext());
        userArrayList = new ArrayList<>();
        lvListRankThang = (ListView) view.findViewById(R.id.lvListRankThang);
    }
}
