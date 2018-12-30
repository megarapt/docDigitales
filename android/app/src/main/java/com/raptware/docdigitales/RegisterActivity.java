package com.raptware.docdigitales;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private TextView tStatus;
    private EditText tfFullName;
    private EditText tfEmail;
    private EditText tfRFC;
    private EditText tfEnterpirseName;
    private EditText tfPassword;
    private EditText tfConfirmPassword;
    private Button bSignup;
    private Button bLogin;

    private Drawable DefaultEditTextBg=null;
    private int DefaultPaddingLeft=0;

    private AlertDialog.Builder adbSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tStatus = (TextView) findViewById(R.id.tvStatus);
        tfFullName = (EditText) findViewById(R.id.tfFullName);
        tfEmail = (EditText) findViewById(R.id.tfEmail);
        tfRFC = (EditText) findViewById(R.id.tfRFC);
        tfEnterpirseName = (EditText) findViewById(R.id.tfEnterpirseName);
        tfPassword = (EditText) findViewById(R.id.tfPassword);
        tfConfirmPassword = (EditText) findViewById(R.id.tfConfirmPassword);
        bSignup = (Button) findViewById(R.id.bSignup);

        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    if(!JNI.CreateRegister(tfFullName.getText().toString(),tfEmail.getText().toString(),tfRFC.getText().toString(),tfEnterpirseName.getText().toString(),tfPassword.getText().toString())){
                        tStatus.setText(getResources().getString(R.string.error_emailExist));
                        tfEmail.setBackground(getResources().getDrawable(R.drawable.edittext_error));
                        tfEmail.requestFocus();
                    }else{
                        adbSuccess.create().show();
                    }
                }
            }
        });
        bLogin = (Button) findViewById(R.id.bLogin);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DefaultEditTextBg = tfFullName.getBackground();
        DefaultPaddingLeft = tfFullName.getPaddingLeft();

        //Validación durante la escritura
        tfFullName.addTextChangedListener(new Filters.LettersAndSpaceWatcher());
        tfRFC.addTextChangedListener(new Filters.RFCWatcher());
        tfEnterpirseName.addTextChangedListener(new Filters.LettersAndSpaceWatcher());

        adbSuccess = new AlertDialog.Builder(this);
        adbSuccess.setMessage(getResources().getString(R.string.message_registerSucced));
        adbSuccess.setCancelable(false);

        adbSuccess.setPositiveButton(
                getResources().getString(R.string.login_button),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private boolean validate(){
        boolean result=true;
        EditText focusField=null;
        //Reseteamos los marcos para asumir que no hay errores
        tfFullName.setBackground(DefaultEditTextBg);
        tfEmail.setBackground(DefaultEditTextBg);
        tfRFC.setBackground(DefaultEditTextBg);
        tfEnterpirseName.setBackground(DefaultEditTextBg);
        tfPassword.setBackground(DefaultEditTextBg);
        tfConfirmPassword.setBackground(DefaultEditTextBg);
        //Padding solo para evitar overlaps
        tfFullName.setPadding(DefaultPaddingLeft,tfFullName.getPaddingTop(),DefaultPaddingLeft,tfFullName.getPaddingBottom());
        tfEmail.setPadding(DefaultPaddingLeft,tfFullName.getPaddingTop(),DefaultPaddingLeft,tfFullName.getPaddingBottom());
        tfRFC.setPadding(DefaultPaddingLeft,tfFullName.getPaddingTop(),DefaultPaddingLeft,tfFullName.getPaddingBottom());
        tfEnterpirseName.setPadding(DefaultPaddingLeft,tfFullName.getPaddingTop(),DefaultPaddingLeft,tfFullName.getPaddingBottom());
        tfPassword.setPadding(DefaultPaddingLeft,tfFullName.getPaddingTop(),DefaultPaddingLeft,tfFullName.getPaddingBottom());
        tfConfirmPassword.setPadding(DefaultPaddingLeft,tfFullName.getPaddingTop(),DefaultPaddingLeft,tfFullName.getPaddingBottom());
        //Remover error
        tStatus.setText("");
        //Revisamos campos vacios
        if(tfFullName.getText().length()==0||tfFullName.getText().toString().trim().length()==0){
            tfFullName.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            focusField=tfFullName;
            result=false;
        }
        if(tfEmail.getText().length()==0||tfEmail.getText().toString().trim().length()==0){
            tfEmail.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            if(focusField==null){
                focusField=tfEmail;
            }
            result=false;
        }
        if(tfRFC.getText().length()==0||tfRFC.getText().toString().trim().length()==0){
            tfRFC.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            if(focusField==null){
                focusField=tfRFC;
            }
            result=false;
        }
        if(tfEnterpirseName.getText().length()==0||tfEnterpirseName.getText().toString().trim().length()==0){
            tfEnterpirseName.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            if(focusField==null){
                focusField=tfEnterpirseName;
            }
            result=false;
        }
        if(tfPassword.getText().length()==0||tfPassword.getText().toString().trim().length()==0){
            tfPassword.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            if(focusField==null){
                focusField=tfPassword;
            }
            result=false;
        }
        if(tfConfirmPassword.getText().length()==0||tfConfirmPassword.getText().toString().trim().length()==0){
            tfConfirmPassword.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            if(focusField==null){
                focusField=tfConfirmPassword;
            }
            result=false;
        }
        //Al menos un campo está vacio y hay que reportar el error
        if(!result){
            tStatus.setText(getResources().getString(R.string.error_emptyField));
            focusField.requestFocus();
            return result;
        }

        //Checar si el e-mail tiene un patrón valido
        if(!Patterns.EMAIL_ADDRESS.matcher(tfEmail.getText().toString()).matches()){
            tStatus.setText(getResources().getString(R.string.error_emailFormat));
            tfEmail.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            tfEmail.requestFocus();
            result=false;
        }

        //Checar si el RFC tiene 12 o 13 caracteres
        else if(tfRFC.getText().toString().length()<12){
            tStatus.setText(getResources().getString(R.string.error_RFCSize));
            tfRFC.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            tfRFC.requestFocus();
            result=false;
        }

        //Checar si los Passwords coinciden
        else if(!tfPassword.getText().toString().equals(tfConfirmPassword.getText().toString())){
            tStatus.setText(getResources().getString(R.string.error_PasswordMissmatch));
            tfPassword.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            tfConfirmPassword.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            tfPassword.requestFocus();
            result=false;
        }

        return result;
    }
}
