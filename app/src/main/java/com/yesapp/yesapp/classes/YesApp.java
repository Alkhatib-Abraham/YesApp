package com.yesapp.yesapp.classes;


import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

//this class is for offline features
public class YesApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
