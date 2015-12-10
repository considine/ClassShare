package com.example.johnpconsidine.classshare.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.johnpconsidine.classshare.MainActivity;
import com.example.johnpconsidine.classshare.ParseClasses.ClassRoom;
import com.example.johnpconsidine.classshare.ParseClasses.Group;
import com.example.johnpconsidine.classshare.ParseClasses.ParseConstants;
import com.example.johnpconsidine.classshare.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BrowseClasses extends android.support.v4.app.ListFragment implements AdapterView.OnClickListener {

    private List<String> allClasses;
    private List<String> myClasses;
    ProgressBar mProgressBar;
    private Button mButton;

    private String username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_browse_classes, container, false);

        return rootview;
    }

    public void returnToMain () {
        ((MainActivity)getActivity()).addingFragment(3);
    }

    @Override
    public void onResume() {
        super.onResume();
        //query all classes:
        if (getView() != null) {
            mProgressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
            mButton = (Button) getView().findViewById(R.id.addClassButton);
            allClasses = new ArrayList<>();
            myClasses = new ArrayList<>();
            getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            username = ((MainActivity) getActivity()).getCurrentUsername();
            ParseQuery<ClassRoom> query = new ParseQuery<>(ParseConstants.CLASS_OBJECT);

            query.findInBackground(new FindCallback<ClassRoom>() {
                @Override
                public void done(List<ClassRoom> objects, ParseException e) {
                    if (e == null) {
                        HashSet<String> classHash = new HashSet<String>();
                        for (ClassRoom classRoom : objects) {
                            classHash.add(classRoom.getNameOfClass());
                            if (classRoom.getUserName().equals(username)) {
                                myClasses.add(classRoom.getNameOfClass());
                                classRoom.deleteInBackground();
                            }

                        }

                        for (String CLASS : classHash) {
                            allClasses.add(CLASS);
                        }
                        //now we must inflate allClasses

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),
                                android.R.layout.simple_list_item_checked,
                                allClasses);
                        setListAdapter(adapter);

                        addCheckMarks();
                        mProgressBar.setVisibility(View.GONE);
                        mButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.v("TAG", "Coruses");
                                resetCourses();
                            }
                        });
                    }
                }
            });
        }
    }

    private void resetCourses() {
        //resets courses, and groups
        ((MainActivity)getActivity()).setClasses(myClasses);
        ((MainActivity)getActivity()).setDrawer();
        for (String course : myClasses) {
            ClassRoom classRoom = new ClassRoom();
            classRoom.setUserName(username);
            classRoom.setClassName(course);
            classRoom.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null) {
                        Log.v("TAG", "Saved new courses");
                    }
                    else {
                        Log.e("Tag", "there was an error saving classes: "+e.getMessage());
                    }

                }
            });
        }
        //reset groups we are no longer in-> query for groups equal to our username but not eqal to any of our classes
        ParseQuery<Group> groupParseQuery = new ParseQuery<Group>(ParseConstants.GROUP_OBJECT);
        groupParseQuery.whereEqualTo((ParseConstants.USERS), username);
        groupParseQuery.whereNotContainedIn(ParseConstants.CLASS_NAME, myClasses);
        groupParseQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if (e == null) {
                    Log.v("TAG", "size of objects: "+objects.size());
                    for (Group object : objects) {
                        Log.v("TAG", "object name"+ object.getGroupName());
                        object.deleteInBackground(); //remove such groups for us!
                    }

                } else {
                    Log.e("TAg", "the error removing groups that don't apply anymore: " + e.getMessage());
                }
                returnToMain();
            }



        });



    }

    private void addCheckMarks() {
        int i =0;
        Log.v("TAG", "this at least got called!");
        Log.v("TAG", " size of allClasses: " + allClasses.size() + " size of myclasses: "+ myClasses.size());
        for (String CLASS : allClasses) {
            if (myClasses.contains(CLASS)) {
                Log.v("TAG", "why aint this working");
                getListView().setItemChecked(i, true);
            }
            i++;
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        if (getListView().isItemChecked(position)) {
            // add friend
            myClasses.add(allClasses.get(position));
        } else {
            myClasses.remove(allClasses.get(position));
        }

        //reset mainactivity classes
        //todo store on parse
    }

    @Override
    public void onClick(View v) {

    }
}