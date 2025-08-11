package com.example.capstoneproject.adminlogic;

// Import DialogInterface for the dialog callbacks
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
// Import MaterialAlertDialogBuilder for the dialog
import com.example.capstoneproject.LoginActivity;
import com.example.capstoneproject.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {
    private static final String TAG = "AdminActivity";

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private NavController navController;
    private ActionBarDrawerToggle toggle;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_layout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.admin);
        navigationView = findViewById(R.id.nav_view);

        Fragment hostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (!(hostFragment instanceof NavHostFragment)) {
            throw new IllegalStateException("Activity " + this + " does not have a NavHostFragment");
        }
        NavHostFragment navHostFragment = (NavHostFragment) hostFragment;
        navController = navHostFragment.getNavController();

        String username = getIntent().getStringExtra("USERNAME_EXTRA");
        if (username == null) {
            username = "Admin";
        }
        Bundle startArgs = new Bundle();
        startArgs.putString("username", username);

        navController.setGraph(R.navigation.admin_nav_graph, startArgs);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.adminHomeFragment, R.id.adminNewMemberFragment
        )
                .setOpenableLayout(drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_admin_sign_out) {
                Log.d(TAG, "Admin Sign Out selected from drawer, showing confirmation.");
                // Show confirmation dialog instead of signing out directly
                showAdminSignOutConfirmationDialog();
                drawerLayout.closeDrawer(GravityCompat.START); // Close drawer after initiating dialog
                return true;
            }

            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return handled;
        });
    }

    private void showAdminSignOutConfirmationDialog() {
        new MaterialAlertDialogBuilder(this) // 'this' refers to AdminActivity (Context)
                .setTitle("Confirm Sign Out") // Use string resources
                .setMessage("Are you sure you want to sign out?") // Use string resources
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    Log.d(TAG, "Admin sign out cancelled.");
                })
                .setPositiveButton("Sign Out", (dialog, which) -> {
                    Log.d(TAG, "Admin sign out confirmed.");
                    performAdminSignOut();
                })
                .show();
    }

    private void performAdminSignOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        Toast.makeText(AdminActivity.this, "Signed out successfully.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (NavigationUI.onNavDestinationSelected(item, navController)) {
            return true;
        }
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
