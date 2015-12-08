package com.example.johnpconsidine.classshare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.johnpconsidine.classshare.Adapters.NavDrawerAdapter;
import com.example.johnpconsidine.classshare.Fragments.ChooseClassFragment;
import com.example.johnpconsidine.classshare.Fragments.ChooseMembersFragment;
import com.example.johnpconsidine.classshare.Fragments.GroupsFragment;
import com.example.johnpconsidine.classshare.Fragments.MessageFragment;
import com.example.johnpconsidine.classshare.ParseClasses.ClassRoom;
import com.example.johnpconsidine.classshare.ParseClasses.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public final static String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private ListView mNavList;
    private List<String> mItems;
    private List<String> mClasses;

    private String currentUsername;
    public String getCurrentUsername() {
        return currentUsername;
    }

    public List<String> getCurrentGroupMembers() {
        return currentGroupMembers;
    }

    public void setCurrentGroupMembers(List<String> currentGroupMembers) {
        this.currentGroupMembers = currentGroupMembers;
    }

    private List<String> currentGroupMembers;

    public List<String> getGroups() {
        return mGroups;
    }

    public void setGroups(List<String> groups) {
        mGroups = groups;
    }

    private List<String> mGroups;

    public ImageView getCreateImage() {
        return createImage;
    }



    public ImageView createImage;
    public String getCurrentClass() {
        return currentClass;
    }
    public void setCurrentClass(String arg) {currentClass = arg;}

    private String currentClass; //

    public String getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(String currentGroup) {
        this.currentGroup = currentGroup;
    }

    private String currentGroup;
    private int mDrawerSize;
    private ActionBarDrawerToggle drawerListener;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser == null) {//todo change
            navigateToLogin();
            //remove flags
        }
        else {
            currentUsername = currentUser.getUsername();
        }
        mNavList = (ListView) findViewById(R.id.navdrawerList);
        mGroups = new ArrayList<>();
        currentGroupMembers = new ArrayList<>();


        createImage = (ImageView) findViewById(R.id.createGroup);
        createImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingFragment(2);
            }
        });
        createImage.setVisibility(View.GONE);
        //initialize items: mItems is all items in nav drawer. mClasses is all classes
        mItems = new ArrayList<>();
        mClasses = new ArrayList<>();
        currentClass = null; //this is for adding students to your group

        //query for classes that this user is in
        //todo: You are not connected to the internet
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_OBJECT);
        query.whereEqualTo(ParseConstants.USERNAME, currentUsername);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    List<String> tempList = new ArrayList<String>();
                    for (int i = 0; i < objects.size(); i++) {

                        tempList.add(objects.get(i).getString(ParseConstants.CLASS_NAME)); //ad class to mClases

                    }
                    mClasses = tempList;

                } else {

                }
                setDrawer();
            }
        });

        //Send to login page:



        //set up layout
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_drawer); //todo change
        mToolbar.setTitle(""); //we'll use the preset title
        setSupportActionBar(mToolbar);

        // mItems = getResources().getStringArray(R.array.menu_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        //mListView = (ListView) findViewById(R.id.drawerList);
        //test




        mDrawerSize = mItems.size();
        //set array adapter:



        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        //get user info

        //adding fragment
        addingFragment(0);

    }

    public void addingFragment(int which) {

        //make sure the keyboard is down
        hideSoftKeyboard(this);
        //remove image logo

        android.support.v4.app.FragmentManager mFragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        GroupsFragment mGroupsFragment = new GroupsFragment();
        ChooseMembersFragment mChooseMembersFragment = new ChooseMembersFragment();
        MessageFragment mMessageFragment = new MessageFragment();
        ChooseClassFragment chooseClassFragment = new ChooseClassFragment();
        switch (which) {
            case (0):
                mFragmentTransaction.add(R.id.fragmentHolder, mGroupsFragment);
                break;

            case (1):
                mFragmentTransaction.replace(R.id.fragmentHolder, mChooseMembersFragment);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addingFragment(2);
                    }
                });
                break;
            case (2):
                mToolbar.setNavigationIcon(R.drawable.ic_fast_rewind_black_24dp);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addingFragment(3);
                    }
                });
                mFragmentTransaction.replace(R.id.fragmentHolder, chooseClassFragment);
                break;
            case(3):
                mToolbar.setNavigationIcon(R.mipmap.ic_drawer);
                mToolbar.setNavigationOnClickListener(null);
                mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                    }
                };
                mFragmentTransaction.replace(R.id.fragmentHolder, mGroupsFragment);
                break;

            case(4):
                mToolbar.setNavigationIcon(R.drawable.ic_fast_rewind_black_24dp);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addingFragment(3);
                    }
                });
                mFragmentTransaction.replace(R.id.fragmentHolder, mMessageFragment);

                break;
        }
        mFragmentTransaction.commit();

    }

    public void addingFragment(String className){ //for switching to add friends
        currentClass = className;
        Log.v(TAG, currentClass);
        addingFragment(1);
    }
    private void setDrawer() {

        mItems.clear();
        mItems.add(getString(R.string.add_class)); // option to add a class
        for (int i = 0; i < mClasses.size(); i++) {
            mItems.add(mClasses.get(i));
        }

        mItems.add(getString(R.string.logout));
        Log.v(TAG, mClasses.size() + " is the size of mClasses ");

//        NavDrawerAdapter adapter = new NavDrawerAdapter(
//                    this,
//                    mItems); //just the raw group name
//            mListView.setAdapter(adapter);
//        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mItems);
       // mListView.setAdapter(adapter);

        NavDrawerAdapter Adapter = new NavDrawerAdapter(getApplicationContext(), mItems);
        mNavList.setAdapter(Adapter);
        mNavList.setOnItemClickListener(this);


    }
    private void navigateToLogin() {
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            // the case that the user has selected "Add a class"
            //create Dialog that takes an input
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.create_class);
            final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                //NEW CLASS
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String className = input.getText().toString();
                    //CHECK VALIDIDTY
                    if (className.length() < 9) {
                        errorCreate(dialog);
                        return;
                    }
                    for (int i = 0; i < 3; i++) {
                        char ch = className.toCharArray()[i];
                        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
                            //this is valid
                        } else {
                            errorCreate(dialog);
                        }
                    }
                    for (int i = 4; i < className.length(); i++) {
                        char ch = className.toCharArray()[i];
                        if (ch >= '0' && ch <= '9') {
                            //this is valid
                        } else {
                            errorCreate(dialog);
                        }
                    }
                    if (className.toCharArray()[3] != '-') {
                        //this wont fly
                        errorCreate(dialog);
                    } else {
                        makeClass(className);
                    }
                    //CHECK VALIDIDTY END

                    //make sure
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        }

        if (position == 1) { //todo change to drawersize
            ParseUser.logOut();
            navigateToLogin();
        }
    }

    private void errorCreate(DialogInterface dialog) {
        Toast.makeText(MainActivity.this, R.string.invalid_name, Toast.LENGTH_LONG).show();
        dialog.cancel();
    }

    public void makeClass(final String className) {

        //see if you're in the class:
        if (mItems.contains(className)) {
            Toast.makeText(MainActivity.this, "The class exists", Toast.LENGTH_LONG).show();
            return;

        }
        Log.v("Exists", "About to search for a class");
        //first must search Parse classes to see if classes exist
        ParseQuery<ClassRoom> query = new ParseQuery<ClassRoom>(ParseConstants.CLASS_OBJECT);
        query.whereEqualTo(ParseConstants.CLASS_NAME, className);
        query.findInBackground(new FindCallback<ClassRoom>() {
            @Override
            public void done(List<ClassRoom> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        //class does not exist

                    } else {
                        //todo increment class size

                    }
                    makeClassHelper(className);
                }

            }
        });

        //If class doesn't exist:





    }

    private void makeClassHelper(String className) {
        ClassRoom classRoom = new ClassRoom();
        classRoom.setClassName(className);
        classRoom.setUserName(ParseUser.getCurrentUser().getUsername());
        classRoom.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.v(TAG, "The class was successfully added");
                } else {
                    Log.v(TAG, "the class was not added cause: " + e.getMessage());
                }
            }
        });

        mClasses.add(className);
        setDrawer();
    }


    public void hideSoftKeyboard(Activity activity) {

        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }



}