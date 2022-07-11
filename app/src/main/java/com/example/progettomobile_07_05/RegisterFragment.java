package com.example.progettomobile_07_05;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.progettomobile_07_05.Database.CardItem;
import com.example.progettomobile_07_05.Database.Database;
import com.example.progettomobile_07_05.Database.User;
import com.example.progettomobile_07_05.Database.UserDAO;
import com.example.progettomobile_07_05.Database.UserRepository;
import com.example.progettomobile_07_05.ViewModel.ListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    private UserRepository repository;
    private ListViewModel listViewModel;

    boolean addUser = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_register, container, false);
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

        repository = new UserRepository(getActivity().getApplication());

        MainActivity activity =(MainActivity) getActivity();
        FloatingActionButton fl = activity.findViewById(R.id.fab_add);
        fl.setVisibility(View.INVISIBLE);
        SearchView searchView = activity.findViewById(R.id.search_icon);
        searchView.setVisibility(View.INVISIBLE);


        EditText mail = (EditText) view.findViewById(R.id.emailregister);
        EditText password = (EditText) view.findViewById(R.id.passwordregister);
        EditText repassword = (EditText) view.findViewById(R.id.repasswordregister);
        EditText name = (EditText) view.findViewById(R.id.nameregister);
        EditText surname = (EditText) view.findViewById(R.id.surnameregister);
        EditText telephoneNumber = (EditText) view.findViewById(R.id.numbertelephoneregister);


        Button registerBtn = (Button) view.findViewById(R.id.registerbutton);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser = false;


                listViewModel = new ViewModelProvider(activity).get(ListViewModel.class);
                listViewModel.getUsers().observe(activity, new Observer<List<User>>() {
                    @Override
                    public void onChanged(List<User> u) {
                        if (addUser == false) {
                            addUser = true;


                            List<String> mailList = new ArrayList<>();
                            for (User p : u) {
                                mailList.add(p.getEmail());
                            }
                            if(mail.getText().toString().matches("") || password.getText().toString().matches("") || name.getText().toString().matches("") || surname.getText().toString().matches("") || telephoneNumber.getText().toString().matches("")){
                                Toast.makeText(getActivity(), "Completare tutti i campi", Toast.LENGTH_SHORT).show();
                            }
                            else if (!isValidEmail(mail.getText().toString())){
                                Toast.makeText(getActivity(), "Email non corretta", Toast.LENGTH_SHORT).show();
                            }
                            else if (!password.getText().toString().matches(repassword.getText().toString())){
                                Toast.makeText(getActivity(), "Le password non coincidono", Toast.LENGTH_SHORT).show();
                            }
                            else if (!isNumeric(telephoneNumber.getText().toString()) || telephoneNumber.getText().toString().length() != 10){
                                Toast.makeText(getActivity(), "Inserire un numero di cellulare corretto", Toast.LENGTH_SHORT).show();
                            }
                            else if (mailList.contains(mail.getText().toString())) {
                                Toast.makeText(getActivity(), "Email gia esistente", Toast.LENGTH_SHORT).show();

                            } else {
                                Sha1Hex sha1Hex = new Sha1Hex();
                                String passwordHash = "";
                                try {
                                    passwordHash = sha1Hex.makeSHA1Hash(password.getText().toString());
                                    //Log.d("hash", passwordHash);
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }


                                User newUser = new User(mail.getText().toString(), passwordHash, name.getText().toString(), surname.getText().toString(), telephoneNumber.getText().toString());
                                repository.addUser(newUser);
                                Toast.makeText(getActivity(), "Registrazione effettuata", Toast.LENGTH_SHORT).show();
                                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                                navigationView.getMenu().getItem(0).setChecked(true);
                                List<User> listUser = new ArrayList<>();
                                listUser.add(newUser);
                                ((MainActivity)getActivity()).setUser(getUser(listUser, mail.getText().toString()));
                                ((MainActivity)getActivity()).setActualPage(R.id.nav_home);
                                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                                Utilities.insertFragment((AppCompatActivity) getActivity(), new HomeFragment(), HomeFragment.class.getSimpleName());
                            }

                        }
                    }

                });

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

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    public static boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
