package com.example.johnpconsidine.classshare.Fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    ListView mListView;
    TextView mText;
    public ProgressBar progressbar;
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
    //before create view, we want the groups loaded, then we can create view
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).toolbarChange(getString(R.string.my_groups));
        sGroups = new ArrayList<>();


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
     super.onResume();
        if (getView() != null) {
            progressbar = (ProgressBar) getView().findViewById(R.id.progressBar);
            mListView = (ListView) getListView();
            mText = (TextView) getView().findViewById(R.id.emptyText);
            //set visibilities
            progressbar.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mText.setVisibility(View.GONE);


            ParseQuery<Group> query = new ParseQuery<Group>(ParseConstants.GROUP_OBJECT);
            query.whereEqualTo(ParseConstants.USERS, ((MainActivity) getActivity()).getCurrentUsername());
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

                        List<String> groupListNoHiphens = new ArrayList<String>();
                        for (int i = 0; i < mGroups.size(); i++) {
                            sGroups.add(mGroups.get(i).getGroupName() + "(" + mGroups.get(i).getNameOfClass()+")");
                            groupClass[i][0] = mGroups.get(i).getGroupName();
                            groupListNoHiphens.add(groupClass[i][0]);
                            groupClass[i][1] = mGroups.get(i).getNameOfClass();
                            Log.v(TAG, "hi");
                        }
                        ((MainActivity)getActivity()).setJustGroups(groupListNoHiphens);
                        ((MainActivity) getActivity()).setGroups(sGroups); //this is the name with the class attached
                        ((MainActivity)getActivity()).queryNotifications(); // now that sGroups is set
                        //lets make the list view visible now:
                        mListView.setVisibility(View.VISIBLE);
                        progressbar.setVisibility(View.GONE);
                        if (sGroups.isEmpty()) {
                            mText.setVisibility(View.VISIBLE);
                        }
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