package com.example.progettomobile_07_05;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.progettomobile_07_05.Database.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity =(MainActivity) getActivity();
        FloatingActionButton fl = activity.findViewById(R.id.fab_add);
        fl.setVisibility(View.INVISIBLE);
        SearchView searchView = getActivity().findViewById(R.id.search_icon);
        searchView.setVisibility(View.INVISIBLE);

        User user = ((MainActivity)getActivity()).getActualUser();
        TextView email = activity.findViewById(R.id.emailprofile);
        TextView name = activity.findViewById(R.id.nameprofile);
        TextView surname = activity.findViewById(R.id.surnameprofile);
        TextView telephone = activity.findViewById(R.id.numbertelephoneprofile);
        if(user != null) {
            email.setText(user.getEmail());
            name.setText(user.getNameUser());
            surname.setText(user.getSurnameUser());
            telephone.setText(user.getTelephoneNumber());
        }

        activity.findViewById(R.id.btnlogout).setOnClickListener(l->{
            ((MainActivity)getActivity()).setUser(null);
            ((MainActivity)getActivity()).setRelogin(true);



            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Utilities.insertFragment((AppCompatActivity) getActivity(), new LoginFragment(), LoginFragment.class.getSimpleName());
        });

    }
}
