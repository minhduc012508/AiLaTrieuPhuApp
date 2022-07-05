package com.example.prj_ailatrieuphu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class WarningExitDialog extends DialogFragment {
    public interface editTextListener{
        public void onEdtPicked(String str);
    }
    editTextListener editTextListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            editTextListener = (WarningExitDialog.editTextListener) context;
        }catch (Exception e){

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder bd = new AlertDialog.Builder(getActivity());
        bd.setTitle("Cảnh báo xác nhận thoát bạn sẽ thua?");
        bd.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editTextListener.onEdtPicked("Yes");
            }
        });
        bd.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editTextListener.onEdtPicked("No");
            }
        });
        return bd.create();
    }
}
