package com.yash.assignment2_yash;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    private NavigationView navigationView;
    private DashboardFragment dashboardFragment = null;
    private LocationFragment locationFragment = null;
    private ArrayList<String> arrItemIds = null;
    private ArrayList<String> arrTitles = null;

    public DrawerLayout drawerLayout;
    public FragmentManager fragmentManager;
    public SharedPreferences preferences;
    public YVSQLiteHelper sqLiteHelper;
    public LoginFragment loginFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean isLogin = preferences.getBoolean("isLogin", false);
        fragmentManager = getSupportFragmentManager();
        sqLiteHelper = new YVSQLiteHelper(this);

        arrItemIds = new ArrayList<String>();
        arrTitles = new ArrayList<String>();

        if (isLogin) {

            arrItemIds.add(String.valueOf(R.id.nav_dashboard));
            arrTitles.add(getString(R.string.home_title));

            // Set the home as default
            dashboardFragment = new DashboardFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content, dashboardFragment, getString(R.string.home_title))
                    .commit();

            locationFragment = new LocationFragment();

            setNavigationViewUser();

        } else {

            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            loginFragment = new LoginFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.drawer_layout, loginFragment, getString(R.string.login_title))
                    .commit();
        }

        processExtraData(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqLiteHelper.close();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            if (arrItemIds.size() > 1 && arrTitles.size() > 1) {
                arrItemIds.remove(arrItemIds.size() - 1);
                arrTitles.remove(arrTitles.size() - 1);

                int prevItemId = Integer.parseInt(arrItemIds.get(arrItemIds.size() - 1));
                navigationView.setCheckedItem(prevItemId);

                String prevTitle = arrTitles.get(arrTitles.size() - 1);
                getSupportActionBar().setTitle(prevTitle);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_notify) {
            notifyUser();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processExtraData(intent);
    }

    private void processExtraData(Intent intent) {
        Boolean isNotify = intent.getBooleanExtra("isNotify", false);
        if (isNotify) {
            Log.d("Notification", "Fired!");

            arrItemIds.clear();
            arrTitles.clear();

            arrItemIds.add(String.valueOf(R.id.nav_dashboard));
            arrTitles.add(getString(R.string.home_title));

            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            navigationView.setCheckedItem(R.id.nav_dashboard);
            getSupportActionBar().setTitle(getString(R.string.home_title));
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title = item.getTitle().toString();

        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = null;
        String fragmentTag = null;

        if (id == R.id.nav_dashboard) {
            fragment = dashboardFragment;
            fragmentTag = getString(R.string.home_title);
        } else if (id == R.id.nav_location) {
            fragment = locationFragment;
            fragmentTag = getString(R.string.location_title);
        } else if (id == R.id.nav_logout) {

            new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_AlertDialog))
                    .setTitle("Message")
                    .setMessage("Are you sure, you want to logout ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            getSupportActionBar().setTitle(getString(R.string.home_title));
                            navigationView.setCheckedItem(R.id.nav_dashboard);
                            drawerLayout.closeDrawer(GravityCompat.START);

                            arrItemIds.clear();
                            arrTitles.clear();

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.commit();

                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                            if (loginFragment == null) {
                                loginFragment = new LoginFragment();
                            }
                            if (!loginFragment.isAdded()) {
                                fragmentManager.beginTransaction()
                                        .add(R.id.drawer_layout, loginFragment, getString(R.string.login_title))
                                        .commit();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int prevItemId = Integer.parseInt(arrItemIds.get(arrItemIds.size() - 1));
                            navigationView.setCheckedItem(prevItemId);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return true;

        } else {
            fragment = new ComingSoonFragment();
            fragmentTag = getString(R.string.coming_soon_text)+"Nav";
        }

        getSupportActionBar().setTitle(title);

        // Insert the fragment by replacing any existing fragment
        if (fragment != null && fragmentTag != null) {
            if (fragment.isAdded()) {
                fragmentManager.beginTransaction()
                        .replace(R.id.content, fragment, fragmentTag)
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .add(R.id.content, fragment, fragmentTag)
                        .addToBackStack(fragmentTag)
                        .commit();
            }
        } else {
            Toast.makeText(this, "Something went wrong. Please try again ...", Toast.LENGTH_LONG);
        }

        arrItemIds.add(String.valueOf(id));
        arrTitles.add(title);

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // FragmentInterationListener Methods

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLoginSuccess() {
        Log.d("Login", "Success!");

        if (dashboardFragment == null) {
            dashboardFragment = new DashboardFragment();
        }

        if (arrItemIds.size() == 0 && arrItemIds.size() == 0) {
            arrItemIds.add(String.valueOf(R.id.nav_dashboard));
            arrTitles.add(getString(R.string.home_title));
        }

        SignUpFragment signUpFragment = (SignUpFragment) fragmentManager.findFragmentByTag(getString(R.string.signup_title));
        if (signUpFragment != null) {
            fragmentManager.beginTransaction()
                    .remove(signUpFragment)
//                    .remove(loginFragment)
                    .replace(R.id.content, dashboardFragment, this.getString(R.string.home_title))
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .remove(loginFragment)
                    .replace(R.id.content, dashboardFragment, this.getString(R.string.home_title))
                    .commit();
        }

        if (locationFragment == null) {
            locationFragment = new LocationFragment();
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLogin", true);
        editor.commit();

        setNavigationViewUser();
    }

    @Override
    public void onSignUpSuccess() {
        Log.d("Sign Up", "Success!");
        onLoginSuccess();
    }

    // Button Actions

    public void buttonClicked(View view) {

        String title = null;
        switch (view.getId()) {
            case R.id.button1:
                title = getString(R.string.button_title1);
                break;
            case R.id.button2:
                title = getString(R.string.button_title2);
                break;
            case R.id.button3:
                title = getString(R.string.button_title3);
                break;
            case R.id.button4:
                title = getString(R.string.button_title4);
                break;
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);

        String fragmentTag = getString(R.string.coming_soon_text)+"Dashboard";
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        if (fragment == null) {
            fragment = new ComingSoonFragment();
        }
        if (fragment.isAdded()) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content, fragment, fragmentTag)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .add(R.id.content, fragment, fragmentTag)
                    .addToBackStack(fragmentTag)
                    .commit();
        }

        arrItemIds.add(String.valueOf(R.id.nav_dashboard));
        arrTitles.add(title);
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) this.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(), 0);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setNavigationViewUser() {

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        View headerView = navigationView.getHeaderView(0);
        TextView txtVwUsername = (TextView) headerView.findViewById(R.id.nav_username);
        final String userName = preferences.getString("user_name", "");
        txtVwUsername.setText(userName);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                getSupportActionBar().setTitle(userName.substring(0, 1).toUpperCase() + userName.substring(1).toLowerCase());

                String fragmentTag = getString(R.string.coming_soon_text)+"User";
                Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
                if (fragment == null) {
                    fragment = new ComingSoonFragment();
                }
                if (fragment.isAdded()) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.content, fragment, fragmentTag)
                            .commit();
                } else {
                    fragmentManager.beginTransaction()
                            .add(R.id.content, fragment, fragmentTag)
                            .addToBackStack(fragmentTag)
                            .commit();
                }

                arrItemIds.add(String.valueOf(R.id.nav_dashboard));
                arrTitles.add(userName);
            }
        });
    }

    private void notifyUser() {

        // prepare intent which is triggered if the notification is selected
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("isNotify", true);

        // Use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification, the addAction re-use the same intent to keep the example short
        Notification notification  = new Notification.Builder(this)
                .setContentTitle("Update Available")
                .setContentText("Improved Productivity & Cost Effectiveness for Healthcare Service Provider")
                .setSmallIcon(R.mipmap.ic_action_refresh)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
//                .addAction(R.drawable.icon, "Call", pIntent)
//                .addAction(R.drawable.icon, "More", pIntent)
//                .addAction(R.drawable.icon, "And more", pIntent)
                .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
