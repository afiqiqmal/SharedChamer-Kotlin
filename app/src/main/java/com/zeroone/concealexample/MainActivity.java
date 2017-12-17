package com.zeroone.concealexample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;

import com.facebook.crypto.CryptoConfig;
import com.google.gson.reflect.TypeToken;
import com.zeroone.conceal.ConcealCrypto;
import com.zeroone.conceal.ConcealPrefRepository;
import com.zeroone.conceal.model.CryptoFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    String NAME_KEY = "user_name";
    String AGE_KEY = "user_age";
    String EMAIL_KEY = "user_email";
    String USER_DETAIL = "user_detail";
    String TASK_DETAIL = "task_detail";
    String IMAGE_KEY = "user_image";
    String FILE_KEY = "user_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConcealPrefRepository.applicationInit(getApplication());

        concealPrefRepository.clearPrefs();


        //FIRST TEST
        concealPrefRepository.putString(NAME_KEY, "HAFIQ IQMAL");
        concealPrefRepository.putInt(AGE_KEY, 24);
        concealPrefRepository.putModel(USER_DETAIL, Data.getUser(this));
        concealPrefRepository.putModel(TASK_DETAIL, Data.getTaskData(this));

        Log.d("FIRST TEST", concealPrefRepository.getString(NAME_KEY));
        Log.d("FIRST TEST", concealPrefRepository.getString(AGE_KEY));
        Log.d("FIRST TEST", concealPrefRepository.getModel(USER_DETAIL, User.class).toString());
        Log.d("FIRST TEST", concealPrefRepository.getModel(TASK_DETAIL, new TypeToken<ArrayList<Task>>(){}.getType()).toString());
        Log.d("FIRST TEST SIZE", ""+concealPrefRepository.getPrefsSize());

        concealPrefRepository.clearPrefs();

        //SECOND TEST
        new ConcealPrefRepository.Editor()
                .putString(NAME_KEY, "Hafiq Iqmal")
                .putInt(AGE_KEY, 24)
                .putString(EMAIL_KEY, "hafiqiqmal93@gmail.com")
                .apply();

        Log.d("SECOND TEST", concealPrefRepository.getString(NAME_KEY));
        Log.d("SECOND TEST",concealPrefRepository.getString(AGE_KEY));
        Log.d("SECOND TEST SIZE", ""+concealPrefRepository.getPrefsSize());



        concealPrefRepository.clearPrefs();

        //add user details preferences
        new ConcealPrefRepository.UserPref("PREFIX").setFirstName("Firstname").setLastName("Lasname").setEmail("hello@gmail.com").apply();

        //get user details
        Log.d("THIRD_TEST",new ConcealPrefRepository.UserPref("PREFIX").getFirstName());
        Log.d("THIRD_TEST",new ConcealPrefRepository.UserPref().setDefault("No Data").getLastName());
        Log.d("THIRD_TEST",new ConcealPrefRepository.UserPref("PREFIX").setDefault("No Data").getEmail());
        Log.d("THIRD_TEST TEST SIZE", ""+concealPrefRepository.getPrefsSize());



        concealPrefRepository.clearPrefs();


        ConcealPrefRepository.UserPref userPref = new ConcealPrefRepository.UserPref("PREFIX", "No Data");
        userPref.setUserName("afiqiqmal");
        userPref.setEmail("afiqiqmal@example.com");
        userPref.apply();


        //get user details
        Log.d("FOURTH_TEST",userPref.getUserName());
        Log.d("FOURTH_TEST",userPref.getEmail());



        ConcealPrefRepository.DevicePref devicePref = new ConcealPrefRepository.DevicePref("PREFIX", "No Data");
        devicePref.setDeviceId("ABC123123123");
        devicePref.setDeviceOS("android");
        devicePref.apply();


        //get user details
        Log.d("FIFTH_TEST",devicePref.getDeviceId());
        Log.d("FIFTH_TEST",devicePref.getDeviceOs());



        Map<String,String> map =concealPrefRepository.getAllSharedPrefData();
        for(Map.Entry<String,?> entry : map.entrySet()){
            try {
                Log.d("VIEW_ALL",entry.getKey()+" :: "+entry.getValue().toString());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        Log.d("VIEW ALL SIZE", ""+concealPrefRepository.getPrefsSize());


        ConcealCrypto concealCrypto = new ConcealCrypto.CryptoBuilder(this)
                .setEnableCrypto(true) //default true
                .setKeyChain(CryptoConfig.KEY_256) // CryptoConfig.KEY_256 or CryptoConfig.KEY_128
                .createPassword("Mac OSX")
                .create();

        String test = "Hello World";
        String cipher =  concealCrypto.obscure(test); //encrypt
        Log.d("CYRPTO TEST E", cipher);
        String dec = concealCrypto.deObscure(cipher); //decrypt
        Log.d("CYRPTO TEST D", dec);

        test = "Hello World Iteration";
        cipher =  concealCrypto.obscureWithIteration(test,4); //encrypt with 4 times
        Log.d("CYRPTO TEST E", cipher);
        dec = concealCrypto.deObscureWithIteration(cipher,4); //decrypt with 4 times
        Log.d("CYRPTO TEST D", dec);



        cipher =  concealCrypto.aesEncrypt("Hello World is World Hello Aes Cryption");
        Log.d("AES E", cipher);
        dec = concealCrypto.aesDecrypt(cipher);
        Log.d("AES D", dec);


    }
}
