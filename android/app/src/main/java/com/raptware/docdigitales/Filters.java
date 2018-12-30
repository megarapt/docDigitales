package com.raptware.docdigitales;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class Filters {
    private static String filter_LettersAndSpace="abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ áéíóúÁÉÍÓÚüÜ";
    private static String filter_RFC="ABCDEFGHIJKLMNÑOPQRSTUVWXYZ0123456789";
    private static String filter_Numbers="0123456789";
    //** TODO: Check why filters don't work in every device
    /*public static InputFilter[] LettersAndSpace=new InputFilter[] {
            new InputFilter() {
                public CharSequence filter(CharSequence src, int start,
                                           int end, Spanned dst, int dstart, int dend) {
                    Log.d("filter","src= "+src);
                    Log.d("filter","start= "+start);
                    Log.d("filter","end= "+end);
                    Log.d("filter","dst= "+dst);
                    Log.d("filter","dstart= "+dstart);
                    Log.d("filter","dend= "+dend);
                    if(src.equals("")){ // backspace
                        Log.d("filter","return backspace");
                        return src;
                    }
                    if(src.length()<dst.length()){//backspace también pero en este caso aceptaremos src
                        Log.d("filter","return backspace 2");
                        if(src.length()<1){
                            return ""; //backspace
                        }
                        return null; //acepta src
                    }
                    if(src.toString().matches("["+filter_LettersAndSpace+"]+")){
                        Log.d("filter","matches");
                        if(src.length()>dst.length()+1) { //seguro cambio la palabra por el autocorrector
                            Log.d("filter","return autocorrector 1");
                            return null; //acepta src
                        }
                        for(int i=0;i<dend;i++){ //aun si es valido, hay que revisar si se usó el hint del autocorrector
                            if (i<dend && dst.charAt(i) != src.charAt(i)){
                                Log.d("filter","return autocorrector 2");
                                return null;
                            }
                        }
                        return src;
                    }
                    String validsrc="";
                    for(int i=0;i<src.length();i++) {
                        if (filter_LettersAndSpace.indexOf(src.charAt(i)) >= 0) {
                            validsrc += src.charAt(i);
                        }
                    }
                    String newsrc="";
                    for (int i = dstart; i < validsrc.length(); i++) {
                        if (filter_LettersAndSpace.indexOf(validsrc.charAt(i)) >= 0) {
                            newsrc += validsrc.charAt(i);
                        }
                    }
                    /*if(dstart==dend && dstart>0) {
                        for (int i = dstart; i < validsrc.length(); i++) {
                            if (filter_LettersAndSpace.indexOf(validsrc.charAt(i)) >= 0) {
                                newsrc += validsrc.charAt(i);
                            }
                        }
                    }else{
                        for (int i = dstart; i < dend; i++) {
                            if (filter_LettersAndSpace.indexOf(dst.charAt(i)) >= 0) {
                                newsrc += dst.charAt(i);
                            }
                        }
                    }*/
/*                    Log.d("filter","return "+newsrc);
                    return newsrc;
                }
            }
    };
    public static InputFilter[] RFCFilter=new InputFilter[] {
            new InputFilter() {
                public CharSequence filter(CharSequence src, int start,
                                           int end, Spanned dst, int dstart, int dend) {

                    if(src.equals("")){ // backspace
                        return src;
                    }
                    if(src.length()<dst.length()){//backspace también pero en este caso aceptaremos src
                        if(src.length()<1){
                            return ""; //backspace
                        }
                        return null; //acepta src
                    }
                    if(src.toString().matches("["+filter_RFC+"]+")){
                        if(src.length()>dst.length()+1) { //seguro cambio la palabra por el autocorrector
                            return null; //acepta src
                        }
                        for(int i=0;i<dend;i++){ //aun si es valido, hay que revisar si se usó el hint del autocorrector
                            if (i<dend && dst.charAt(i) != src.charAt(i)){
                                return null;
                            }
                        }
                        return src;
                    }
                    String validsrc="";
                    for(int i=0;i<src.length();i++) {
                        if (filter_RFC.indexOf(src.charAt(i)) >= 0) {
                            validsrc += src.charAt(i);
                        }
                    }
                    String newsrc="";
                    for (int i = dstart; i < validsrc.length(); i++) {
                        if (filter_RFC.indexOf(validsrc.charAt(i)) >= 0) {
                            newsrc += validsrc.charAt(i);
                        }
                    }
                    return newsrc;
                }
            }
    };
    public static InputFilter[] NumberOnly=new InputFilter[] {
            new InputFilter() {
                public CharSequence filter(CharSequence src, int start,
                                           int end, Spanned dst, int dstart, int dend) {
                    if(src.equals("")){ // for backspace
                        return src;
                    }
                    if(src.toString().matches("[0-9]+")){
                        return src;
                    }
                    String newsrc="";
                    for(int i=0;i<src.length();i++){
                        if(filter_Numbers.indexOf(src.charAt(i))>=0){
                            newsrc+=src.charAt(i);
                        }
                    }
                    return newsrc;
                }
            }
    };*/
    static class LettersAndSpaceWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public synchronized void afterTextChanged(Editable editable) {
            Log.d("afterTextChanged","editable= "+editable.toString());
            if (TextUtils.isEmpty(editable))
                return;

            Log.d("afterTextChanged","editable length "+editable.toString().length());
            for (int i = 0; i < editable.toString().length(); i++) {
                Log.d("afterTextChanged",i+": "+editable.charAt(i));
                if (filter_LettersAndSpace.indexOf(editable.charAt(i)) < 0) {
                    editable.delete(i, i + 1);
                    i--;
                }
            }
        }
    }
    static class RFCWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public synchronized void afterTextChanged(Editable editable) {
            Log.d("afterTextChanged","editable= "+editable.toString());
            if (TextUtils.isEmpty(editable))
                return;

            Log.d("afterTextChanged","editable length "+editable.toString().length());
            for (int i = 0; i < editable.toString().length(); i++) {
                Log.d("afterTextChanged",i+": "+editable.charAt(i));
                if (filter_RFC.indexOf(editable.charAt(i)) < 0) {
                    Log.d("afterTextChanged","toUpperCase= "+(""+editable.charAt(i)).toUpperCase());
                    if (filter_RFC.indexOf((""+editable.charAt(i)).toUpperCase()) >= 0) {
                        editable.replace(i,i+1,(""+editable.charAt(i)).toUpperCase());
                    }else {
                        editable.delete(i, i + 1);
                        i--;
                    }
                }
            }
        }
    }
    static class NumberWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public synchronized void afterTextChanged(Editable editable) {
            Log.d("afterTextChanged","editable= "+editable.toString());
            if (TextUtils.isEmpty(editable))
                return;

            Log.d("afterTextChanged","editable length "+editable.toString().length());
            for (int i = 0; i < editable.toString().length(); i++) {
                Log.d("afterTextChanged",i+": "+editable.charAt(i));
                if (filter_Numbers.indexOf(editable.charAt(i)) < 0) {
                    editable.delete(i, i + 1);
                    i--;
                }
            }
        }
    }
}
