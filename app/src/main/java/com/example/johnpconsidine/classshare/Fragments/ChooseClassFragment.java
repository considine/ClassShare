package com.example.johnpconsidine.classshare.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.johnpconsidine.classshare.MainActivity;
import com.example.johnpconsidine.classshare.ParseClasses.ClassRoom;
import com.example.johnpconsidine.classshare.ParseClasses.ParseConstants;
import com.example.johnpconsidine.classshare.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChooseClassFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChooseClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseClassFragment extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public String[] nClasses;
    protected List<ClassRoom> mClasses;
    ParseUser mCurrentUser;
    public static final String TAG = ChooseClassFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_choose_class, container, false);
        getActivity().setProgressBarIndeterminateVisibility(true);
        ParseQuery<ClassRoom> query = new ParseQuery<ClassRoom>(ParseConstants.CLASS_OBJECT);
        query.orderByAscending(ParseConstants.KEY_CREATEDAT);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ClassRoom>() {
            @Override
            public void done(List<ClassRoom> objects, ParseException e) {
                if (e == null) {
                    mClasses = objects;

                    HashSet<String> classes = new HashSet<String>();

                    //remove duplicates
                    for (ClassRoom user : mClasses) {
                        classes.add(user.getNameOfClass());
                    }
                    //convert array
                    nClasses = new String[classes.size()];
                    classes.toArray(nClasses);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_list_item_1,
                            nClasses);
                    setListAdapter(adapter);


                } //success

                else {
                    Log.e(TAG, e.getMessage());

                    //DIALOG
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(e.getMessage());
                    builder.setTitle("could not find classes");
                    builder.setPositiveButton(android.R.string.ok, null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            };
        });
        return rootview;
    }
    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();




    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ((MainActivity)getActivity()).addingFragment(nClasses[position]);
    }
}

