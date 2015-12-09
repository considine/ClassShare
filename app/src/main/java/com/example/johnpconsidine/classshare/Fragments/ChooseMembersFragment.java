package com.example.johnpconsidine.classshare.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.johnpconsidine.classshare.MainActivity;
import com.example.johnpconsidine.classshare.ParseClasses.ClassRoom;
import com.example.johnpconsidine.classshare.ParseClasses.Group;
import com.example.johnpconsidine.classshare.ParseClasses.ParseConstants;
import com.example.johnpconsidine.classshare.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChooseMembersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChooseMembersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseMembersFragment extends ListFragment implements AdapterView.OnClickListener {
    private static final String TAG = ChooseClassFragment.class.getSimpleName();
    private String currentClass;
    protected List<ClassRoom> mClasses;
    private String[] aUsers;
    private List<String> mGroupMembers;
    ImageButton mButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroupMembers = new ArrayList<>();
        currentClass = ((MainActivity)getActivity()).getCurrentClass(); //sets the class we're talkin
        ParseQuery<ClassRoom> query = new ParseQuery<ClassRoom>(ParseConstants.CLASS_OBJECT);
        query.whereEqualTo(ParseConstants.CLASS_NAME, currentClass);
        query.whereNotEqualTo(ParseConstants.USERNAME, ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ClassRoom>() {
            @Override
            public void done(List<ClassRoom> objects, ParseException e) {
                if (e == null) {
                    mClasses = objects;
                    Log.v(TAG, objects.size() + " is the size of the objects ");
                    aUsers = new String[objects.size()];
                    getActivity().setProgressBarIndeterminateVisibility(false);
                    int i = 0;
                    for (ClassRoom CLASS : mClasses) {
                        aUsers[i] = CLASS.getUserName();
                        i++;
                    }

                    //todo remove self from this list
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_list_item_checked,
                            aUsers);
                    setListAdapter(adapter);
                } else {
                    Log.e(TAG, "there was an errro: " + e.getMessage());
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_choose, container, false);
        mButton = (ImageButton) rootview.findViewById(R.id.createGroupButton);
        mButton.setOnClickListener(this);
        ImageView imageView = ((MainActivity) getActivity()).getNotifications();
        imageView.setOnClickListener(this);

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        if (getListView().isItemChecked(position)) {
            // add friend
            mGroupMembers.add(aUsers[position]);
        } else {
            mGroupMembers.remove(aUsers[position]);
        }




    }

    @Override
    public void onClick(View v) {
        if (mGroupMembers.isEmpty()) {
            Toast.makeText(getActivity(), R.string.no_groupmembers, Toast.LENGTH_LONG).show();
            return;
        }

        //get the groupname


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Almost Done");
        builder.setMessage(R.string.please_create_group);
        final EditText input = new EditText(getActivity());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = input.getText().toString();

                //CHECK VALIDIDTY
                if (groupName.length() < 1) {
                    Toast.makeText(getActivity(), R.string.no_group_name, Toast.LENGTH_LONG).show();
                } else {
                    //todo check if gruop exists
                    for (String individual : ((MainActivity) getActivity()).getGroups()) {
                        if ((groupName + "-" + currentClass).equals(individual)) {
                            Toast.makeText(getActivity(), "That Group Already exists", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    mGroupMembers.add(ParseUser.getCurrentUser().getUsername());
                    int i = 0;
                    for (String member : mGroupMembers) {
                        i++;
                        Group group = new Group();
                        if (member.equals(ParseUser.getCurrentUser().getUsername())) {
                            group.setApproved(true);
                        } else {
                            group.setApproved(false);
                        }
                        group.setMembers(member);
                        group.setNameOfClass(currentClass);
                        group.setGroupName(groupName);
                        group.setApproved(true);
                        group.setCreator(((MainActivity)getActivity()).getCurrentUsername());
                        group.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.v(TAG, "GROup created");
                                    //return to GroupsFragment

                                } else {
                                    Log.v(TAG, "An unfortunate error this late..." + e.getMessage());
                                }
                            }
                        });
                    }

                }
                ((MainActivity) getActivity()).addingFragment(3);
            }


        });
        builder.show();

    }




}


