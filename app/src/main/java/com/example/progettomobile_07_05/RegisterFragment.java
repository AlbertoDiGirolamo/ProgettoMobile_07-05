package com.example.progettomobile_07_05;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.progettomobile_07_05.Database.User;
import com.example.progettomobile_07_05.Database.UserRepository;

public class RegisterFragment extends Fragment {

    private UserRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new UserRepository(getActivity().getApplication());


        TextView mail = (TextView) view.findViewById(R.id.emailregister);
        TextView password = (TextView) view.findViewById(R.id.passwordregister);
        TextView repassword = (TextView) view.findViewById(R.id.repasswordregister);
        TextView name = (TextView) view.findViewById(R.id.nameregister);
        TextView surname = (TextView) view.findViewById(R.id.surnameregister);
        TextView telephoneNumber = (TextView) view.findViewById(R.id.numbertelephoneregister);


        Button registerBtn = (Button) view.findViewById(R.id.registerbutton);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.d("room", String.valueOf(repository.getUserFromEmail(mail.getText().toString()).getValue()));
                if (repository.getUserFromEmail(mail.getText().toString()).getValue() == null) {
                    repository.addUser(new User(mail.getText().toString(),password.getText().toString(),name.getText().toString(),surname.getText().toString(),telephoneNumber.getText().toString()));
                    Toast.makeText(getActivity(), "REGISTER OK", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "REGISTER FAILED", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
