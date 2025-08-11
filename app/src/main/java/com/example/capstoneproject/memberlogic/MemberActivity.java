package com.example.capstoneproject.memberlogic;

import android.os.Bundle;
// Import NavGraph if you haven't already, though setGraph directly takes the resource ID
// import androidx.navigation.NavGraph;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.capstoneproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MemberActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this); // You can enable this if you manage insets correctly
        setContentView(R.layout.member_layout);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main); // Ensure this ID is correct for member_layout.xml

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            // --- Get username from Intent and prepare arguments for NavGraph ---
            String username = getIntent().getStringExtra("USERNAME_EXTRA");
            if (username == null) {
                username = "Member"; // Default fallback username
            }
            Bundle startArgs = new Bundle();
            startArgs.putString("username", username); // Key must match argument name in nav_graph

            // --- Set the graph for NavController with start destination arguments ---
            // This line replaces the graph set via XML (app:navGraph) but ensures args are passed.
            navController.setGraph(R.navigation.member_nav_graph, startArgs);


            // Define top-level destinations for BottomNavigationView.
            // These IDs should match the menu item IDs in your member_bottom_nav_menu.xml
            // and the destination fragment IDs in your member_nav_graph.xml
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.memberHomeFragment, R.id.memberReceiptsFragment, R.id.memberAccountFragment)
                    .build();

            // If you have a Toolbar in MemberActivity that you want to integrate:
            // Toolbar memberToolbar = findViewById(R.id.member_toolbar); // Assuming you add a Toolbar
            // setSupportActionBar(memberToolbar);
            // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

            NavigationUI.setupWithNavController(navView, navController);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.member), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Adjust padding as needed, especially if using EdgeToEdge and a Toolbar
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // If you add NavigationUI.setupActionBarWithNavController, you'll need this for Up navigation
    /*
    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            // You might need to reconstruct or pass the AppBarConfiguration if it's complex
            return NavigationUI.navigateUp(navController, (DrawerLayout) null) // Pass null if no drawer
                   || super.onSupportNavigateUp();
        }
        return super.onSupportNavigateUp();
    }
    */
}
