package com.example.johnpconsidine.classshare.Fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.johnpconsidine.classshare.Adapters.ClassGroupAdapter;
import com.example.johnpconsidine.classshare.MainActivity;
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


public class GroupsByClass extends ListFragment implements AdapterView.OnClickListener{
    public static final String TAG = GroupsByClass.class.getSimpleName();
    public List<String> aUsers;
    public List<String> aCreators;
    public String mCurrentClass;
    public String mCurrentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_groups_by_class, container, false);
        return rootview;




    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        aUsers = new ArrayList<>();
        mCurrentUser = ((MainActivity) getActivity()).getCurrentUsername();
        //parsequery for groups that have a sender same as ParseUser.
        ParseQuery<Group> groupQuery = new ParseQuery<>(ParseConstants.GROUP_OBJECT);
        groupQuery.whereEqualTo(ParseConstants.CLASS_NAME, mCurrentClass); //todo remember to set this
        // need to find groups this user is not in!!!! -> new query?
        groupQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if (e == null) {

                   HashSet<String> nGroups = new HashSet<String>();
                    //inflate array adapter with appropriate infformation
                    aUsers = new ArrayList<String>();

                    for (Group group : objects) {
                        nGroups.add(group.getGroupName());
                    }
                    //iterate through hashmap:
                    //make String arrays:


                    String[] sGroups = new String[nGroups.size()];
                    nGroups.toArray(sGroups);


                    List<String> unapproved = ((MainActivity) getActivity()).getUnapprovedGroups();
                    for (int i=0; i<sGroups.length; i++) {
                        if (!unapproved.contains(sGroups[i])) {
                            aUsers.add(sGroups[i]);
                        }
                    }



                    if (aUsers.size() == 0) {
                        Log.v(TAG, aUsers.size() + "aUses size");
                        noGroups();
                    } else {
                        Log.v(TAG, " Ausers size: " + aUsers.size());
                    }

                    //would like to remove all entries in aUsers that are in unnaproved groups
                    ClassGroupAdapter classGroupAdapter = new ClassGroupAdapter(getListView().getContext(), aUsers);
                    setListAdapter(classGroupAdapter);

                } else {
                    Log.e(TAG, "there was some sort of error getting the classes pertaining to those groups");

                }
            }
        });


    }
    //create back dialog
    public void noGroups() {

        //change to toast
        Toast.makeText(getActivity(),"There are no groups in " + mCurrentClass + " that you are not in!",Toast.LENGTH_LONG ).show();
        ((MainActivity) getActivity()).addingFragment(3);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentClass = ((MainActivity) getActivity()).getCurrentClass();
        ((MainActivity) getActivity()).toolbarChange(mCurrentClass);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);
        //Want to send request to join the group!

        Group group = new Group();
        group.setApproved(false); //needs approval
        group.setGroupName(aUsers.get(position));
        group.setNameOfClass(mCurrentClass);
        group.setMembers(mCurrentUser);
        group.setCreator("alreadyCreated");
        group.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.v(TAG, "Group saved!");
                    Toast.makeText(getActivity(), "A request to join group " + aUsers.get(position) + " was made.", Toast.LENGTH_LONG).show();
                    ((MainActivity) getActivity()).queryUnapproved();
                } else {
                    Log.e(TAG, "There was an error requesting a group ");
                }
            }
        });



    }
}