package com.birdicomputers.login;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.birdicomputers.login.ui.reminders.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_gallery, R.id.nav_home, R.id.link_github)
                .setDrawerLayout(drawer)
                .build();
        AccentColors();
        navigationView.setItemTextColor(NavigationItemColors());
        navigationView.setItemIconTintList(NavigationItemIconColors());
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(item -> {
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            int id = item.getItemId();
            if(id == R.id.link_github){
                Intent browserIntent =
                        new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/drive/folders/1h0VDRkP_5lemcR02NomK4ADjVgSUlrAo?usp=sharing"));
                startActivity(browserIntent);
                drawer.closeDrawer(GravityCompat.START);
            }
            return handled;
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = null;
        try{
            assert user != null;
            email = user.getEmail();
        }
        catch (Exception ex){
            Log.d("TAG","Error getting email from Firebase");
        }
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.UserEmail);
        navUsername.setText(email);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> MainActivity.super.onBackPressed())
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    public void AccentColors(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTheme)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorTheme, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorTheme));
        }
    }
    public ColorStateList NavigationItemColors(){
        int[][] state = new int[][] {
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

        int[] color = new int[] {
                Color.parseColor("#a1a5ab"),
                Color.parseColor("#FFFFFF"),
                Color.parseColor("#a1a5ab"),
                Color.parseColor("#FFFFFF"),
        };

        ColorStateList colorStateList1 = new ColorStateList(state, color);

        // FOR NAVIGATION VIEW ITEM ICON COLOR
        int[][] states = new int[][] {
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed

        };
        int[] colors = new int[] {
                Color.parseColor("#a1a5ab"),
                Color.parseColor("#FFFFFF"),
                Color.parseColor("#a1a5ab"),
                Color.parseColor("#FFFFFF"),
        };
        ColorStateList colorStateList2 = new ColorStateList(states, colors);
        return colorStateList1;
    }

    public ColorStateList NavigationItemIconColors(){
        // FOR NAVIGATION VIEW ITEM ICON COLOR
        int[][] states = new int[][] {
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed

        };
        int[] colors = new int[] {
                Color.parseColor("#a1a5ab"),
                Color.parseColor("#FFFFFF"),
                Color.parseColor("#a1a5ab"),
                Color.parseColor("#FFFFFF"),
        };
        ColorStateList colorStateList2 = new ColorStateList(states, colors);
        return colorStateList2;
    }
}
