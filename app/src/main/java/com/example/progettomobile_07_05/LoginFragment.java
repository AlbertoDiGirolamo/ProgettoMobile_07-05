package com.example.progettomobile_07_05;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.progettomobile_07_05.Database.User;
import com.example.progettomobile_07_05.ViewModel.ListViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment {
    private ListViewModel listViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item= menu.findItem(R.id.openfilter);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity =(MainActivity) getActivity();

        FloatingActionButton fl = activity.findViewById(R.id.fab_add);
        fl.setVisibility(View.INVISIBLE);
        SearchView searchView = activity.findViewById(R.id.search_icon);
        searchView.setVisibility(View.INVISIBLE);


        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_home).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_message).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_myproduct).setVisible(false);

        EditText mail = (EditText) view.findViewById(R.id.maillogin);
        EditText password = (EditText) view.findViewById(R.id.passwordlogin);

        Button loginBtn = (Button) view.findViewById(R.id.loginbutton);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listViewModel = new ViewModelProvider(activity).get(ListViewModel.class);
                listViewModel.getUsers().observe(activity, new Observer<List<User>>() {

                    @Override
                    public void onChanged(List<User> u) {
                        List<String> mailList = new ArrayList<>();
                        List<String> passwordList = new ArrayList<>();
                        for (User p : u) {
                            mailList.add(p.getEmail());
                            passwordList.add(p.getPassword());
                        }
                        if(mailList.contains(mail.getText().toString()) && (mailList.indexOf(mail.getText().toString()) == passwordList.indexOf(password.getText().toString()))){
                            Toast.makeText(getActivity(), "Accesso effettuato", Toast.LENGTH_SHORT).show();
                            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                            navigationView.getMenu().getItem(0).setChecked(true);

                            ((MainActivity)getActivity()).setUser(getUser(u, mail.getText().toString()));
                            Utilities.insertFragment((AppCompatActivity) getActivity(), new HomeFragment(), HomeFragment.class.getSimpleName());
                        }
                        else{
                            Toast.makeText(getActivity(), "Credenziali errate", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        TextView linkToRegister = (TextView) view.findViewById(R.id.linkregister);
        linkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.insertFragment((AppCompatActivity) getActivity(), new RegisterFragment(), RegisterFragment.class.getSimpleName());
            }
        });




    }
    public User getUser(List<User> userList, String emailUser){

        for (User u: userList){
            if(u.getEmail().matches(emailUser)){
                return u;
            }
        }
        return null;
    }
}



















