package com.exponents.externalstoragewriter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_WRITE_STORAGE = 112;

    EditText savableText;
    Button SaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hook();
        useEffect();
    }

    private void hook(){
        savableText = findViewById(R.id.my_text);
        SaveButton = findViewById(R.id.save_button);
    }

    private void useEffect(){
        SaveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.save_button){
            if(savableText.getText().toString().isEmpty()){
                Toast.makeText(this, "Save give some content", Toast.LENGTH_SHORT).show();
                return;
            }

            String state = Environment.getExternalStorageState();

            if(Environment.MEDIA_MOUNTED.equals(state)){
                if(checkPermission()){
                    File sdcard = Environment.getExternalStorageDirectory();
                    File dir = new File(sdcard.getAbsolutePath() + "/text/");
                        boolean mkdir = dir.mkdir();

                        File file = new File(dir, "sample.txt");
                        FileOutputStream os;

                        try {
                            os = new FileOutputStream(file);
                            os.write(savableText.getText().toString().getBytes());
                            os.close();

                            Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show();
                            savableText.setText("");
                        }
                        catch (Exception e){
                            Toast.makeText(this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }


                } else {
                    requestPermission();
                }
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.MANAGE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to create files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
        }
    }
}