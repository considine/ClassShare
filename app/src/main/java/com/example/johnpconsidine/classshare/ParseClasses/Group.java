package com.example.johnpconsidine.classshare.ParseClasses;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by johnpconsidine on 12/4/15.
 */

@ParseClassName(ParseConstants.GROUP_OBJECT)
public class Group extends ParseObject {
    //unique group for every class-> group relationship

    public String getMember() { return getString(ParseConstants.USERS);}
    public void setMembers(String member) { put(ParseConstants.USERS, member);} //only called at the creation of a group

    public String getNameOfClass () { return getString(ParseConstants.CLASS_NAME);}
    public void setNameOfClass(String nameOfClass) {put(ParseConstants.CLASS_NAME, nameOfClass);}

    public Boolean getApproved () {return getBoolean(ParseConstants.APPROVED);}
    public void setApproved (Boolean confirmed) {put(ParseConstants.APPROVED, confirmed);}

    public String getGroupName () {return getString(ParseConstants.GROUP_NAME);}
    public void setGroupName(String groupName) {put(ParseConstants.GROUP_NAME, groupName);}
}
