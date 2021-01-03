package com.example.e_health.views.fragments.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_health.R;
import com.example.e_health.databinding.FragmentHomeBinding;
import com.example.e_health.views.fragments.AppointmentBookFragment;
import com.example.e_health.views.fragments.TestBookFragment;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.page_1){
                updateNavigationBarState(0);
                setFragment(AppointmentBookFragment.newInstance());
            }else if(id == R.id.page_2){
                updateNavigationBarState(1);
                setFragment(TestBookFragment.newInstance());
            }
            return false;
        });
        binding.bottomNavigation.setSelectedItemId(R.id.page_1);
        return root;
    }

    private void updateNavigationBarState(int index){
        Menu menu = binding.bottomNavigation.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            if(i == index) {
                item.setChecked(true);
                break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(binding.container.getId(), fragment);
        transaction.commit();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}