package com.example.johnpconsidine.classshare.Fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.johnpconsidine.classshare.Adapters.GroupAdapter;
import com.example.johnpconsidine.classshare.MainActivity;
import com.example.johnpconsidine.classshare.ParseClasses.Group;
import com.example.johnpconsidine.classshare.ParseClasses.ParseConstants;
import com.example.johnpconsidine.classshare.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class GroupsFragment extends ListFragment implements View.OnClickListener{
    public static final String TAG = GroupsFragment.class.getSimpleName();
    ImageButton mButton;
    private List<Group> mGroups;
    private List<String> sGroups;
    private String[][] groupClass; //2 dimensional arrays of groups and classes in one, this
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_groups, container, false);
        mButton = (ImageButton) rootview.findViewById(R.id.addGroupButton);
        mButton.setOnClickListener(this);
        //so we dont repeat group

        return rootview;
        //todo rootview

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sGroups = new ArrayList<>();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
     super.onResume();
        ParseQuery<Group> query = new ParseQuery<Group>(ParseConstants.GROUP_OBJECT);
        query.whereEqualTo(ParseConstants.USERS, ((MainActivity)getActivity()).getCurrentUsername());
        query.whereEqualTo(ParseConstants.APPROVED, true);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if (e == null) {
                    mGroups = objects;
                    groupClass = new String[objects.size()][2];
                    sGroups = new ArrayList<String>();
                    Log.v(TAG, objects.size() + "The size of these groups ");

                    //remove duplicates
                    for (int i = 0; i < mGroups.size(); i++) {
                        sGroups.add(mGroups.get(i).getGroupName() + "-" + mGroups.get(i).getNameOfClass());
                        groupClass[i][0] = mGroups.get(i).getGroupName();
                        groupClass[i][1] = mGroups.get(i).getNameOfClass();
                        Log.v(TAG, "hi");
                    }
                    ((MainActivity) getActivity()).setGroups(sGroups); //this is the name with the class attached
                    if (getListView().getAdapter() == null) {
                        GroupAdapter adapter = new GroupAdapter(
                                getListView().getContext(),
                                sGroups); //just the raw group name
                        setListAdapter(adapter);
                    }

                } else {
                    Log.e(TAG, "help" + e.getMessage());
                }
            }
        });


    }


    @Override
    public void onClick(View v) {

        ((MainActivity)getActivity()).addingFragment(2);
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ((MainActivity)getActivity()).setCurrentGroup(groupClass[position][0]); // save current group
        ((MainActivity)getActivity()).setCurrentClass(groupClass[position][1]);

        //set current grop members

        ParseQuery<Group> query = new ParseQuery<Group>(ParseConstants.GROUP_OBJECT);
        query.whereEqualTo(ParseConstants.GROUP_NAME, ((MainActivity)getActivity()).getCurrentGroup());
        query.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if (e == null) {
                    List<String> groupmembers = new ArrayList<String>();
                    //we found the groups
                    for (Group object : objects) {
                        groupmembers.add(object.getMember()); //save current groupmembers

                    }
                    ((MainActivity) getActivity()).setCurrentGroupMembers(groupmembers);
                } else {
                    Log.e(TAG, "Error getting group members " + e.getMessage());
                    //we have not found the groups
                }

                ((MainActivity) getActivity()).addingFragment(4); //go to the next fragment

            }

        });



    }
}