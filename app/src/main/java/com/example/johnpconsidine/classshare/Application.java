package com.example.johnpconsidine.classshare;

import com.example.johnpconsidine.classshare.ParseClasses.ClassRoom;
import com.example.johnpconsidine.classshare.ParseClasses.Group;
import com.example.johnpconsidine.classshare.ParseClasses.Message;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by johnpconsidine on 12/3/15.
 */
public class Application extends android.app.Application {



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getMyClasses() {
        return myClasses;
    }

    public void setMyClasses(List<String> myClasses) {
        this.myClasses = myClasses;
    }


    private List<String> myClasses;
    private String username;

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);


        ParseObject.registerSubclass(ClassRoom.class);
        ParseObject.registerSubclass(Group.class);
        ParseObject.registerSubclass(Message.class);


        Parse.initialize(this, "nZh3Jks2Nd019arpJWXeOGtS5ntVhN5zhrWvseqN", "qlBDbVD805ghlsDmxOSBKfHShcOGyWTKEciiZE8F");
        ParseUser.enableRevocableSessionInBackground();
    }


}
