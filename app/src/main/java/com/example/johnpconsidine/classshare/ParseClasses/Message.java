package com.example.johnpconsidine.classshare.ParseClasses;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by johnpconsidine on 12/5/15.
 */
@ParseClassName(ParseConstants.MESSAGE_OBJECT)
public class Message extends ParseObject {
    //get and set messages, BY group object ID
    //every message is a new message object

    //message has recipient,  sender, and recieveer, and content
    public String getGroupName() { return getString(ParseConstants.GROUP_NAME);}
    public void setGroupName(String groupName) { put(ParseConstants.GROUP_NAME, groupName);}

    public String getSenderName () { return getString(ParseConstants.SENDER);}
    public void setSenderName(String sendereName) {put(ParseConstants.SENDER, sendereName);}

    public String getMessageText () {return getString(ParseConstants.MESSAGE_TEXT);}
    public void setMessageText (String MessageText) {put(ParseConstants.MESSAGE_TEXT, MessageText);}

    public String getTimeMade () {return getString(ParseConstants.KEY_CREATEDAT);}

    public String getMessageClass(){ return getString(ParseConstants.CLASS_NAME); }
    public void setMessageClass(String classofMessage){  put(ParseConstants.CLASS_NAME, classofMessage); }

    public Boolean isMessageSpecial () { return getBoolean(ParseConstants.SPECIAL_MESSAGE);}
    public void setMessageSpecial (Boolean special) { put (ParseConstants.SPECIAL_MESSAGE, special);}
   // the time be mad ecan not be set


}
