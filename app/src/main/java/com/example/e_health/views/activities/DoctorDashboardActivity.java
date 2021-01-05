package com.example.e_health.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.e_health.R;
import com.example.e_health.commons.enums.UserStatus;
import com.example.e_health.commons.models.User;
import com.example.e_health.commons.utils.CommonDialogs;
import com.example.e_health.commons.utils.Prefs;
import com.example.e_health.databinding.ActivityDoctorDashboardBinding;
import com.example.e_health.views.fragments.ProfileFragment;
import com.example.e_health.views.fragments.doctor.dashboard.DashboardFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;


public class DoctorDashboardActivity extends AppCompatActivity {

    private ActivityDoctorDashboardBinding binding;
    private User user;
    private BottomNavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDoctorDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Prefs.init(this);
        user = Prefs.getUser();
        getSupportActionBar().setTitle("Dr. "+user.getName());
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_doctor_dashboard);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.navigation_home){
                    updateNavigationBarState(0);
                    setFragment(DashboardFragment.newInstance("0"));
                    //getSupportActionBar().setTitle("New Appointments");
                }else if(id == R.id.navigation_profile){
                    updateNavigationBarState(1);
                    setFragment(ProfileFragment.newInstance());
                    //getSupportActionBar().setTitle("Previous Appointments");
                }
                return false;
            }
        });

        getSupportActionBar().setTitle(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        if(user.getStatus()==null || user.getStatus().equals(UserStatus.APPROVAL_PENDING.name())){
            CommonDialogs.myDialog(this, "Your Profile Approval Pending", "Hello "+user.getName()+", \nWe are happy to work with you to provide better medical services to Patients. Currently Your profile is under Processing for Approval. We will revert you shortly on Profile Approval Status.\nThanks to Join E-Health.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            CommonDialogs.myDialog(DoctorDashboardActivity.this, "Log-Out", "Do you want to logout from app?", "Log-Out", "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    Prefs.clear();
                    Intent intent = new Intent(DoctorDashboardActivity.this, SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.container.getId(), fragment);
        transaction.commit();
    }

    private void updateNavigationBarState(int index){
        Menu menu = navView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            if(i == index) {
                item.setChecked(true);
                break;
            }
        }
    }

}