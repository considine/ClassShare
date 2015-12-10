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
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.johnpconsidine.classshare.Adapters.NavDrawerAdapter;
import com.example.johnpconsidine.classshare.Fragments.BrowseClasses;
import com.example.johnpconsidine.classshare.Fragments.ChooseClassFragment;
import com.example.johnpconsidine.classshare.Fragments.ChooseMembersFragment;
import com.example.johnpconsidine.classshare.Fragments.GroupsByClass;
import com.example.johnpconsidine.classshare.Fragments.GroupsFragment;
import com.example.johnpconsidine.classshare.Fragments.MessageFragment;
import com.example.johnpconsidine.classshare.ParseClasses.ClassRoom;
import com.example.johnpconsidine.classshare.ParseClasses.Group;
import com.example.johnpconsidine.classshare.ParseClasses.Message;
import com.example.johnpconsidine.classshare.ParseClasses.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView toolbarText;
    public final static String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private ListView mNavList;
    private List<String> mItems;
    private String[][] requestPairs;

    public List<String> getClasses() {
        return mClasses;
    }


    public void setClasses(List<String> classes) {
        mClasses = classes;
    }

    private List<String> mClasses;
    public List<String> getUnapprovedGroups() {
        return mUnapprovedGroups;
    }

    public void setUnapprovedGroups(List<String> unapprovedGroups) {
        mUnapprovedGroups = unapprovedGroups;
    }

    private List<String> mUnapprovedGroups;

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

    public void setJustGroups(List<String> justGroups) {
        this.justGroups = justGroups;
    }

    private List<String> justGroups;

    public ImageView getNotifications() {
        return notifications;
    }



    public ImageView notifications;
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
        currentGroupMembers = new ArrayList<>();
        toolbarText = (TextView) findViewById(R.id.toolbarTitle);

        notifications = (ImageView) findViewById(R.id.createGroup);
        notifications.setVisibility(View.GONE);

        //initialize items: mItems is all items in nav drawer. mClasses is all classes
        mItems = new ArrayList<>();
        mClasses = new ArrayList<>();
        mGroups = new ArrayList<>();
        justGroups = new ArrayList<>();
        currentClass = null; //this is for adding students to your group
        queryNotifications();
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
       // mToolbar.setMenu();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_drawer); //todo change
        mToolbar.setTitle(""); //we'll use the preset title
        groupMenuOn();

        //setSupportActionBar(mToolbar);

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
    public void groupMenuOn () {
        mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.leaveGroup) {
                    leaveGroup();
                }


                return false;
            }
        });

    }

    private void leaveGroup() {
        //query currentgroup, and this user (current user) and remove them from the group
        ParseQuery<Group> groupParseQuery = new ParseQuery<Group>(ParseConstants.GROUP_OBJECT);
        groupParseQuery.whereEqualTo(ParseConstants.USERS, currentUsername);
        groupParseQuery.whereEqualTo(ParseConstants.GROUP_NAME, currentGroup);
        groupParseQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if (e == null) {
                    //no issues
                    for (Group object : objects) {
                        mGroups.remove(object.getGroupName());
                        object.deleteInBackground(); //leave the group
                        //todo remove from current group

                    }


                } else {
                    Log.e(TAG, "Error leaving the group ");

                }
            }
        });

        Message message = new Message();
        message.setSenderName(getString(R.string.admin_sender));
        message.setMessageSpecial(true);
        message.setMessageText(currentUsername + " has left this group ");
        message.setGroupName(currentGroup);
        message.setMessageClass(getCurrentClass());
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null) {
                    addingFragment(3);
                }
                else {
                    Toast.makeText(MainActivity.this, "Error leaving group", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void groupMenuOff() {
        mToolbar.getMenu().clear();
    }
    public void queryNotifications() {
        Log.v(TAG, "code4" + mGroups.size());

        for (String group : mGroups) {
            Log.v(TAG, "The group is " + group );
        }
        ParseQuery<Group> query = new ParseQuery<Group>(ParseConstants.GROUP_OBJECT);
        query.whereEqualTo(ParseConstants.APPROVED, false); //not yet approved
        query.whereContainedIn(ParseConstants.GROUP_NAME, justGroups);
        query.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if (e==null) {
                    requestPairs = new String[3][objects.size()];
                    int i =0;
                    for (Group object : objects) {
                        requestPairs[0][i] = object.getMember();
                        requestPairs[1][i] = object.getGroupName();
                        requestPairs[2][i] = object.getNameOfClass();
                        i++;
                        object.deleteInBackground();
                    }
                    if (objects.size() > 0) {
                        notifications.setVisibility(View.VISIBLE);
                        notifications.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                notifications.setVisibility(View.GONE);
                                setNotifications(); // shows notification icon only when we have any!
                            }
                        });
                    }

                }
                else {
                    Log.e(TAG, "error getting notifications ");
                }
            }
        });



    }

    private void setNotifications() {
        int i =0;
        for (String groupname : requestPairs[0]) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final String tempGroupname = requestPairs[1][i];
            final String tempUsername = requestPairs[0][i];
            final String tempClassname = requestPairs[2][i];
            builder.setTitle(R.string.add_to_group);
            builder.setMessage(tempUsername + " would like to join " + tempGroupname);
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Message message = new Message();
                    message.setSenderName(getString(R.string.admin_sender));
                    message.setGroupName(tempGroupname);
                    message.setMessageText(tempUsername + " has joined this group");
                    message.setMessageSpecial(true);
                    message.setMessageClass(tempClassname);
                    message.saveInBackground(); //to nofity the group that someone has joined
                    Group group = new Group();
                    group.setGroupName(tempGroupname);
                    group.setMembers(tempUsername);
                    group.setNameOfClass(tempClassname);
                    group.setApproved(true);
                    group.setCreator(getString(R.string.already_created_group));
                    group.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "error adding new group member! ");
                            }
                        }
                    });
                }
            });
            builder.setNegativeButton("Ignore", null);
            builder.show();

            i++;

        }
        requestPairs = new String[3][10]; //just to delete the amount
    }


    public void addingFragment(int which) {

        //make sure the keyboard is down
        hideSoftKeyboard(this);
        //remove image logo
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawers();
        }
        if (mToolbar != null) {
            groupMenuOff();
        }
        android.support.v4.app.FragmentManager mFragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        GroupsFragment mGroupsFragment = new GroupsFragment();
        ChooseMembersFragment mChooseMembersFragment = new ChooseMembersFragment();
        MessageFragment mMessageFragment = new MessageFragment();
        ChooseClassFragment chooseClassFragment = new ChooseClassFragment();
        GroupsByClass groupsByClass = new GroupsByClass();;
        BrowseClasses browseClasses = new BrowseClasses();
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
                groupMenuOn();
                mFragmentTransaction.replace(R.id.fragmentHolder, mMessageFragment);

                break;
            case(5):
                mToolbar.setNavigationIcon(R.drawable.ic_fast_rewind_black_24dp);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addingFragment(3);
                    }
                });

                //query unapproved groups:
                mFragmentTransaction.replace(R.id.fragmentHolder, groupsByClass);
                break;
            case(6):
                mToolbar.setNavigationIcon(R.drawable.ic_fast_rewind_black_24dp);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addingFragment(3);
                    }
                });

                //query unapproved groups:
                mFragmentTransaction.replace(R.id.fragmentHolder, browseClasses);
                break;
        }
        mFragmentTransaction.commit();

    }

    public void queryUnapproved() {
        mUnapprovedGroups = new ArrayList<>();
        ParseQuery<Group> query = new ParseQuery<Group>(ParseConstants.GROUP_OBJECT);
        query.whereEqualTo(ParseConstants.USERS, currentUsername);
        query.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if (e == null) {
                    HashSet<String> tempClass = new HashSet<String>();

                    for (Group group : objects) {
                        tempClass.add(group.getGroupName());
                    }
                    Log.v(TAG, "The size of objects is: " + objects.size());
                    Log.v(TAG, "The size of tempClass is: " + tempClass.size());

                    String tempArrayClass[] = new String[tempClass.size()];
                    tempClass.toArray(tempArrayClass);
                    for (int i = 0; i < tempArrayClass.length; i++) {
                        mUnapprovedGroups.add(tempArrayClass[i]);
                    }
                    Log.v(TAG, "The size of mUnapprobed here i:s " + mUnapprovedGroups.size());

                    //now we have mUnapproved Groups, go to next fragment


                    addingFragment(5);
                } else {
                    Log.e(TAG, "The error was querying unapproved: " + e.getMessage());
                }
            }
        });

    }

    public void addingFragment(String className){ //for switching to add friends
        currentClass = className;
        Log.v(TAG, currentClass);
        addingFragment(1);
    }
    public void setDrawer() {

        mItems.clear();
        mItems.add(currentUsername);
        mItems.add(getString(R.string.add_class)); // option to add a class
        mItems.add(getString(R.string.search_class));
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
        mDrawerLayout.closeDrawers();
        if (position == 0) {
            //ido nothing
        }
        else if (position == 1) {

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
                    for (int i = 6; i < className.length(); i++) {
                        char ch = className.toCharArray()[i];
                        if (ch >= '0' && ch <= '9') {
                            //this is valid
                        } else {
                            errorCreate(dialog);
                        }
                    }
                    if (className.toCharArray()[3] != '-' && className.toCharArray()[4] != '-') {
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

        else if (position == 2) {
            //Search for clas
            addingFragment(6);
            Log.v(TAG, "search for classses");
        }
        else if (position == mItems.size()-1) { //todo change to drawersize
            ParseUser.logOut();
            navigateToLogin();
        }
        else {
            currentClass = mItems.get(position);
            queryUnapproved();

        }

    }


    private void errorCreate(DialogInterface dialog) {
        Toast.makeText(MainActivity.this, R.string.invalid_name, Toast.LENGTH_LONG).show();
        dialog.cancel();
    }

    public void makeClass(final String className) {

        //see if you're in the class:
        if (mItems.contains(className)) {
            Toast.makeText(MainActivity.this, "You are already in course " + className, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MainActivity.this, "That course already exists, you have been added to it ", Toast.LENGTH_LONG).show();
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

    public void toolbarChange (String newName) {
        if (toolbarText != null) {
            toolbarText.setText(newName);
        }
    }


}