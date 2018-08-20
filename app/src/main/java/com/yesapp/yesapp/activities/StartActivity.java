package com.yesapp.yesapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yesapp.yesapp.R;

public class StartActivity extends AppCompatActivity {


    private int onBackPressed = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

    }




    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Press Back again to Exit",Toast.LENGTH_SHORT).show();
        if(onBackPressed==1){
            onBackPressed =0;
            finish();
        }
        onBackPressed ++;
    }


    public void go(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.goToReg :
                 i = new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(i);
                break;

            case R.id.goToLogin:
                i = new Intent(StartActivity.this,LoginActivity.class);
                startActivity(i);
                break;

         default:
             break;
        }

    }
}
