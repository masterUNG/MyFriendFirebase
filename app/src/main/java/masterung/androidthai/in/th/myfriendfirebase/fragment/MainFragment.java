package masterung.androidthai.in.th.myfriendfirebase.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import masterung.androidthai.in.th.myfriendfirebase.R;
import masterung.androidthai.in.th.myfriendfirebase.ServiceActivity;
import masterung.androidthai.in.th.myfriendfirebase.utility.MyAlert;

public class MainFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Check Status Login
        checkStatusLogin();

//        Register Controller
        registerController();

//        Login Controller
        loginController();

    }   // Main Method

    private void loginController() {
        Button button = getActivity().findViewById(R.id.buttonLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText emailEditText = getView().findViewById(R.id.editTextEmail);
                EditText passwordEditText = getView().findViewById(R.id.editTextPassword);

                String emailString = emailEditText.getText().toString().trim();
                String passwordString = passwordEditText.getText().toString().trim();
                final MyAlert myAlert = new MyAlert(getActivity());

                if (emailString.isEmpty() || passwordString.isEmpty()) {
//                    Have Space
                    myAlert.myNormalDialog(getString(R.string.title_have_space),
                            getString(R.string.message_have_space));
                } else {
//                    No Space
//                    Authentication by Firebase
                    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signInWithEmailAndPassword(emailString, passwordString)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
//                                        Login Status
                                        Toast.makeText(getActivity(),
                                                "Welcome " + firebaseAuth.getCurrentUser().getDisplayName().toString(),
                                                Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(getActivity(), ServiceActivity.class));
                                        getActivity().finish();

                                    } else {
//                                        Cannot Login
                                        myAlert.myNormalDialog("Cannot Login",
                                                task.getException().getMessage().toString());
                                    }

                                }
                            });



                }   // if


            }
        });
    }


    private void checkStatusLogin() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
//            Login
            Log.d("7AprilV1", "Login Status");
            Log.d("7AprilV1", "displayName ==> " + firebaseUser.getDisplayName());

//            Intent To Service
            startActivity(new Intent(getActivity(), ServiceActivity.class));
            getActivity().finish();

        } else {
//            Logout
            Log.d("7AprilV1", "LogOut Status");
        }
    }


    private void registerController() {

        TextView textView = getView().findViewById(R.id.txtNewRegister);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentMainFragement, new RegisterFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }
}
