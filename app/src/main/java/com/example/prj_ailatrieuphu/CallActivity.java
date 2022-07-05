package com.example.prj_ailatrieuphu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class CallActivity extends AppCompatActivity {
    private String [] RequestArray = {"android.permission.READ_CONTACTS"};
    Button btnStartCall;
    ImageView imvPhoneBook;
    EditText edtPhoneNumber;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_calldialog);
        Anhxa();
        ActivityResultLauncher myRes = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @SuppressLint("Range")
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()==Activity.RESULT_OK){
                            Intent data = result.getData();
                            Uri contactData = data.getData();

                            Cursor cur = getContentResolver().query(contactData, null, null, null, null);

                            if (cur.getCount() > 0) {// thats mean some resutl has been found

                                if (cur.moveToNext()) {

                                    @SuppressLint("Range") String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                                    @SuppressLint("Range") String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                    Log.e("Names", name);

                                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                                        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                                        while (phones.moveToNext()) {
                                            @SuppressLint("Range") String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                            edtPhoneNumber.setText(phoneNumber);
                                        }
                                        phones.close();
                                    }

                                }
                            }
                            cur.close();
                        }
                    }
                });
        btnStartCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenumber;
                phonenumber = edtPhoneNumber.getText().toString();
                if(!phonenumber.isEmpty()){
                    Intent it = new Intent();
                    it.setAction(Intent.ACTION_CALL);
                    it.setData(Uri.parse("tel:" + phonenumber));
                    startActivity(it);
                }
            }
        });

        imvPhoneBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!AllPermissionGranted()){
                    Intent it = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",getPackageName(),null);
                    it.setData(uri);
                    startActivity(it);
                }else {
                    Intent it = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    myRes.launch(it);
                }
            }
        });
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 99:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();

                    Cursor cur = getContentResolver().query(contactData, null, null, null, null);

                    if (cur.getCount() > 0) {// thats mean some resutl has been found

                        if (cur.moveToNext()) {

                            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                            @SuppressLint("Range") String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            Log.e("Names", name);

                            if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                                while (phones.moveToNext()) {
                                    @SuppressLint("Range") String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    edtPhoneNumber.setText(phoneNumber);
                                }
                                phones.close();
                            }

                        }
                    }
                    cur.close();
                }
                break;
        }
    }

    private void Anhxa(){
        btnStartCall = (Button) findViewById(R.id.btnStartCall);
        imvPhoneBook = (ImageView) findViewById(R.id.imvPhoneBook);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
    }

    private boolean AllPermissionGranted(){
        for(String permission : RequestArray){
            if(ContextCompat.checkSelfPermission(CallActivity.this,permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
}
