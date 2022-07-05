package BangXepHangTab;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:{
                return new FragmentTuan();
            }
            case 1:{
                return new FragmentThang();
            }
            case 2:{
                return new FragmentNam();
            }
            case 3:{
                return new FragmentTuyChon();
            }
            default:{
                return new FragmentTuan();
            }
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
