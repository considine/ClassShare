package com.example.johnpconsidine.classshare.ParseClasses;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by johnpconsidine on 12/4/15.
 */
@ParseClassName(ParseConstants.CLASS_OBJECT)
public class ClassRoom extends ParseObject {
    public ClassRoom() {} //default constructor

    //set the name of the class
    public String getNameOfClass() { return getString(ParseConstants.CLASS_NAME);}
    public void setClassName(String className) {put(ParseConstants.CLASS_NAME, className);}

  //  get the name of the user
    public String getUserName() { return getString(ParseConstants.USERNAME);}
    public void setUserName(String username) { put(ParseConstants.USERNAME, username);}



    //users in the class IDS

}
