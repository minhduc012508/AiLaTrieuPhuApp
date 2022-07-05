package BangXepHangTab;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prj_ailatrieuphu.R;

import java.util.ArrayList;

import Model.User;

public class PlayerRecordAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<User> userArrayList;
    int count = 1;

    public PlayerRecordAdapter(Context context, int layout, ArrayList<User> userArrayList) {
        this.context = context;
        this.layout = layout;
        this.userArrayList = userArrayList;
    }

    @Override
    public int getCount() {
        return userArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        ImageView imvRankNumber;
        TextView txtRankName,txtRankEmail,txtRankScore,txtRankTime;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder vh;
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            vh = new ViewHolder();
            vh.txtRankScore = (TextView) view.findViewById(R.id.txtRankScore);
            vh.txtRankEmail = (TextView) view.findViewById(R.id.txtRankEmail);
            vh.txtRankName = (TextView) view.findViewById(R.id.txtRankName);
            vh.txtRankTime = (TextView) view.findViewById(R.id.txtRankTime);
            vh.imvRankNumber = (ImageView) view.findViewById(R.id.imvRankNumber);
            view.setTag(vh);
        }
        else{
            vh = (ViewHolder) view.getTag();
        }
        User user = userArrayList.get(position);
        vh.txtRankName.setText(user.getHoten());
        vh.txtRankEmail.setText(user.getEmail());
        vh.txtRankScore.setText(user.getDiemso() + "");
        vh.txtRankTime.setText(user.getDate());
        return view;
    }
}
