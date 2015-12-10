package com.example.johnpconsidine.classshare.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.johnpconsidine.classshare.Adapters.MessageAdapter;
import com.example.johnpconsidine.classshare.MainActivity;
import com.example.johnpconsidine.classshare.ParseClasses.Message;
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
 * {@link MessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends ListFragment implements View.OnClickListener {


    public static final String TAG = MessageFragment.class.getSimpleName();
    public String firstname;
    //set up on click
    ImageButton send;
    private String CurrentGroup;
    EditText mMessage;

    @Override
    public void onClick(View v) {
        Log.v("TAG", " come to duac ");

    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_message, container, false);
        mMessage = (EditText) rootview.findViewById(R.id.messageText);
        CurrentGroup = ((MainActivity) getActivity()).getCurrentGroup();
        ((MainActivity)getActivity()).toolbarChange(CurrentGroup);
        return rootview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);





    }








    @Override
    public void onResume() {
        super.onResume();
        send = (ImageButton) getActivity().findViewById(R.id.sendButton);

        final List<String> Messages = new ArrayList<>();
        final List<String> Initials = new ArrayList<>();
        final List<Boolean> Sender = new ArrayList<>();


        //ParseQuery to get this information:
        ParseQuery<Message> query = new ParseQuery<Message>(ParseConstants.MESSAGE_OBJECT);
        query.whereEqualTo(ParseConstants.GROUP_NAME, ((MainActivity)getActivity()).getCurrentGroup());
        query.whereEqualTo(ParseConstants.CLASS_NAME, ((MainActivity)getActivity()).getCurrentClass());
        query.addAscendingOrder(ParseConstants.KEY_CREATEDAT);
        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> objects, ParseException e) {
                if (e == null) {
                    for ( Message object : objects) {
                        Log.v(TAG, "Sender info: " + object.getSenderName() + " " + ParseUser.getCurrentUser().getUsername());
                        Messages.add(object.getMessageText());
                        Initials.add(object.getSenderName());
                        Sender.add(ParseUser.getCurrentUser().getUsername().equals(object.getSenderName()));
                        Log.v(TAG, Sender.get(Sender.size()-1) + "");
                    }


                    Log.v(TAG, "Setting adapter now");
                    getListView().setDivider(null);
                    MessageAdapter adapter = new MessageAdapter(
                            getListView().getContext(), Messages, Initials, Sender);
                    setListAdapter(adapter);
                    scrollMyListViewToBottom();
                }
                else {
                    Log.e(TAG, "There was an error retrieving messages" + e.getMessage());

                }
            }
        });
        //query info later:

//        if (getListView().getAdapter() == null) {

//        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity()).hideSoftKeyboard(getActivity());
                if (mMessage.getText().toString().isEmpty()) {
                    return;
                }

                //now put Message info
                Message message = new Message();
                message.setMessageText(mMessage.getText().toString());
                message.setSenderName(((MainActivity) getActivity()).getCurrentUsername());
                //need to get a recipient // wil make a main activity variable to store current group members
                message.setGroupName(((MainActivity) getActivity()).getCurrentGroup());
                message.setMessageClass(((MainActivity) getActivity()).getCurrentClass());
                message.setMessageSpecial(false); //only a special message if someone leaves the group
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.v(TAG, "Message item saved! ");
                        } else {
                            Log.e(TAG, "There is some kind of error: " + e.getMessage());

                        }
                    }
                });
                mMessage.setText("");
                //refresh fragment
                ((MainActivity) getActivity()).addingFragment(4);


            }
        });


    }

    //for scrolling to bottom
    private void scrollMyListViewToBottom() {
        getListView().post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                getListView().setSelection(getListAdapter().getCount() - 1);
            }
        });
    }


}
