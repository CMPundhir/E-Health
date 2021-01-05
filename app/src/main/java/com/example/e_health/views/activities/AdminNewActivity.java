package com.example.e_health.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.e_health.R;
import com.example.e_health.commons.models.User;
import com.example.e_health.commons.utils.CommonDialogs;
import com.example.e_health.commons.utils.Prefs;
import com.example.e_health.databinding.ActivityAdminNewBinding;
import com.example.e_health.views.fragments.ProfileFragment;
import com.example.e_health.views.fragments.admin.AppointmentsFragment;
import com.example.e_health.views.fragments.admin.DoctorsFragment;
import com.example.e_health.views.fragments.admin.TestFragment;
import com.example.e_health.views.fragments.doctor.dashboard.DashboardFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class AdminNewActivity extends AppCompatActivity {

    private ActivityAdminNewBinding binding;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Prefs.init(this);
        getSupportActionBar().setTitle("Admin");
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_doctors, R.id.navigation_test)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_admin_dashboard);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.navigation_home){
                    updateNavigationBarState(0);
                    setFragment(AppointmentsFragment.newInstance());
                    getSupportActionBar().setTitle("New Appointments");
                }else if(id == R.id.navigation_doctors){
                    updateNavigationBarState(1);
                    setFragment(DoctorsFragment.newInstance());
                    getSupportActionBar().setTitle("Doctors");
                }else if(id == R.id.navigation_test){
                    updateNavigationBarState(2);
                    setFragment(TestFragment.newInstance());
                    getSupportActionBar().setTitle("Tests");
                }
                return false;
            }
        });
        binding.navView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            CommonDialogs.myDialog(AdminNewActivity.this, "Log-Out", "Do you want to logout from app?", "Log-Out", "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    Prefs.clear();
                    Intent intent = new Intent(AdminNewActivity.this, SplashActivity.class);
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
        Menu menu = binding.navView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            if(i == index) {
                item.setChecked(true);
                break;
            }
        }
    }
}