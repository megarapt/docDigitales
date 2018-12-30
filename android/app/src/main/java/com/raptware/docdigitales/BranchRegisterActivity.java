package com.raptware.docdigitales;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BranchRegisterActivity extends AppCompatActivity {
    private static boolean edit=false;
    private static int edit_id;

    private TextView tStatus;
    private EditText tfBranch;
    private EditText tfStreet;
    private EditText tvColony;
    private EditText tfNumber;
    private EditText tfPostCode;
    private EditText tfCity;
    private EditText tfCountry;
    private Button bSave;

    private Drawable DefaultEditTextBg=null;

    private AlertDialog.Builder adbSuccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tStatus = (TextView) findViewById(R.id.tvStatus);
        tfBranch = (EditText) findViewById(R.id.tfBranch);
        tfStreet = (EditText) findViewById(R.id.tfStreet);
        tvColony = (EditText) findViewById(R.id.tvColony);
        tfNumber = (EditText) findViewById(R.id.tfNumber);
        tfPostCode = (EditText) findViewById(R.id.tfPostCode);
        tfCity = (EditText) findViewById(R.id.tfCity);
        tfCountry = (EditText) findViewById(R.id.tfCountry);
        bSave = (Button) findViewById(R.id.bSave);

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    if(!edit) {
                        if (JNI.CreateBranch(MainActivity.userMail, tfBranch.getText().toString(), tfStreet.getText().toString(), tvColony.getText().toString(), tfNumber.getText().toString(), tfPostCode.getText().toString(), tfCity.getText().toString(), tfCountry.getText().toString()) > 0) {
                            adbSuccess.create().show();
                        } else {
                            //tStatus.setText("Registro no salvado");//el usuario no debe ver esto
                        }
                    }else{
                        edit=false;
                        if (JNI.SaveBranch(""+edit_id, tfBranch.getText().toString(), tfStreet.getText().toString(), tvColony.getText().toString(), tfNumber.getText().toString(), tfPostCode.getText().toString(), tfCity.getText().toString(), tfCountry.getText().toString())) {
                            adbSuccess.create().show();
                        } else {
                            //tStatus.setText("Registro no salvado");//el usuario no debe ver esto
                        }
                    }
                }
            }
        });

        //Validación durante la escritura
        tfNumber.addTextChangedListener(new Filters.NumberWatcher());
        tfPostCode.addTextChangedListener(new Filters.NumberWatcher());

        DefaultEditTextBg = tfBranch.getBackground();

        if(edit){
            try {
                JSONArray branches=new JSONArray(JNI.GetBranchByID(""+edit_id));
                JSONObject item=branches.getJSONObject(0);
                tfBranch.setText(item.getString("sucursal"));
                tfStreet.setText(item.getString("calle"));
                tvColony.setText(item.getString("colonia"));
                tfNumber.setText(item.getString("numero"));
                tfPostCode.setText(item.getString("cpostal"));
                tfCity.setText(item.getString("ciudad"));
                tfCountry.setText(item.getString("pais"));

                adbSuccess = new AlertDialog.Builder(this);
                adbSuccess.setMessage(getResources().getString(R.string.message_branchSaved));
                adbSuccess.setCancelable(false);

                adbSuccess.setPositiveButton(
                        getResources().getString(R.string.menu_home),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            adbSuccess = new AlertDialog.Builder(this);
            adbSuccess.setMessage(getResources().getString(R.string.message_branchSucced));
            adbSuccess.setCancelable(true);

            adbSuccess.setPositiveButton(
                    getResources().getString(R.string.menu_home),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    });

            adbSuccess.setNegativeButton(
                    getResources().getString(R.string.menu_registerBranch),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            tStatus.setText("");
                            tfBranch.setText("");
                            tfStreet.setText("");
                            tvColony.setText("");
                            tfNumber.setText("");
                            tfPostCode.setText("");
                            tfCity.setText("");
                            tfCountry.setText("");
                        }
                    });
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public static void SetEditID(int id){
        edit=true;
        edit_id=id;
    }
    private boolean validate() {
        boolean result = true;
        EditText focusField = null;
        //Reseteamos los marcos para asumir que no hay errores
        tfBranch.setBackground(DefaultEditTextBg);
        tfStreet.setBackground(DefaultEditTextBg);
        tvColony.setBackground(DefaultEditTextBg);
        tfNumber.setBackground(DefaultEditTextBg);
        tfPostCode.setBackground(DefaultEditTextBg);
        tfCity.setBackground(DefaultEditTextBg);
        tfCountry.setBackground(DefaultEditTextBg);
        //Remover error
        tStatus.setText("");
        //Revisamos campos vacios
        if(tfBranch.getText().length()==0||tfBranch.getText().toString().trim().length()==0){
            tfBranch.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            focusField=tfBranch;
            result=false;
        }
        if(tfStreet.getText().length()==0||tfStreet.getText().toString().trim().length()==0){
            tfStreet.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            focusField=tfStreet;
            result=false;
        }
        if(tvColony.getText().length()==0||tvColony.getText().toString().trim().length()==0){
            tvColony.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            focusField=tvColony;
            result=false;
        }
        if(tfNumber.getText().length()==0||tfNumber.getText().toString().trim().length()==0){
            tfNumber.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            focusField=tfNumber;
            result=false;
        }
        if(tfPostCode.getText().length()==0||tfPostCode.getText().toString().trim().length()==0){
            tfPostCode.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            focusField=tfPostCode;
            result=false;
        }
        if(tfCity.getText().length()==0||tfCity.getText().toString().trim().length()==0){
            tfCity.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            focusField=tfCity;
            result=false;
        }
        if(tfCountry.getText().length()==0||tfCountry.getText().toString().trim().length()==0){
            tfCountry.setBackground(getResources().getDrawable(R.drawable.edittext_error));
            focusField=tfCountry;
            result=false;
        }
        //Al menos un campo está vacio y hay que reportar el error
        if(!result){
            tStatus.setText(getResources().getString(R.string.error_emptyField));
            focusField.requestFocus();
        }

        return result;
    }
}
