package com.municipal.complaintsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.navigation.NavigationView;
import com.municipal.complaintsapp.fragment.AddComplaintFragment;
import com.municipal.complaintsapp.fragment.ChangePasswordFragment;
import com.municipal.complaintsapp.fragment.ComplaintListFragment;
import com.municipal.complaintsapp.fragment.UserProfileFragment;

import java.io.File;

import okhttp3.MediaType;


public class ComplaintListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

        public DrawerLayout drawerLayout;
        public ActionBarDrawerToggle actionBarDrawerToggle;

        public NavigationView navigationView;

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_complaints_list);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);




            drawerLayout = findViewById(R.id.my_drawer_layout);
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            displaySelectedScreen(R.id.nav_view_complaints);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.navigation_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    drawerLayout.openDrawer(GravityCompat.START);
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
           displaySelectedScreen(item.getItemId());

            return true;
        }

        private void displaySelectedScreen(int id){
            if(id == R.id.nav_view_complaints){
                getSupportActionBar().setTitle("Complaint History");
                ComplaintListFragment complaintListFragment = new ComplaintListFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, complaintListFragment).commit();
            }

            if (id == R.id.nav_add_complaints) {
                getSupportActionBar().setTitle("Register Your Complaints");
                AddComplaintFragment addComplaintFragment = new AddComplaintFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, addComplaintFragment).commit();

            }

            if(id == R.id.nav_change_password){
                getSupportActionBar().setTitle("Change Password");
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, changePasswordFragment).commit();
            }

            if(id == R.id.nav_account){
                getSupportActionBar().setTitle("User Details");
                UserProfileFragment userProfileFragment = new UserProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, userProfileFragment).commit();

            }


            if(id == R.id.nav_logout){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

// Set the dialog title
                builder.setTitle("Logout Confirmation");

// Set the dialog message
                builder.setMessage("Are you sure you want to logout?");

// Add the "Yes" button
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Yes button
                        // Handle logout here
                    }
                });

// Add the "No" button
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

// Create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();


            }
            //when item is selected then it will close the drawer
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.my_drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        }


    }




