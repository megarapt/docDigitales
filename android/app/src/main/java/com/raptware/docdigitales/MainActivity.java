package com.raptware.docdigitales;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private TextView tStatus;
    private EditText tfEmail;
    private EditText tfPassword;
    private Button bLogin;
    private Button bSignUp;

    public static String userMail;
    public static JSONObject userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        JNI.StartDataBase(this.getFilesDir() + "data.db");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tStatus = (TextView) findViewById(R.id.tvStatus);
        tfEmail = (EditText) findViewById(R.id.tfEmail);
        tfPassword = (EditText) findViewById(R.id.tfPassword);
        bLogin = (Button) findViewById(R.id.bLogin);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    if(JNI.Login(tfEmail.getText().toString(),tfPassword.getText().toString())){
                        userMail=tfEmail.getText().toString();
                        JSONArray jsonArray;
                        try {
                            jsonArray = new JSONArray(JNI.GetUserInfo(MainActivity.userMail));
                            userInfo = jsonArray.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(MainActivity.this,ManagementActivity.class);
                        finish();
                        startActivity(intent);
                    }else {
                        tStatus.setText(getResources().getString(R.string.error_wrongLogin));
                        tfEmail.requestFocus();
                    }
                }
            }
        });
        bSignUp = (Button) findViewById(R.id.bSignup);

        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRegisterActivity();
            }
        });

    }

    private void gotoRegisterActivity(){
        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    private boolean validate() {
        boolean result = true;
        if(tfEmail.getText().length()==0||tfEmail.getText().toString().trim().length()==0||tfPassword.getText().length()==0||tfPassword.getText().toString().trim().length()==0) {
            result = false;
        }
        return result;
    }
}
