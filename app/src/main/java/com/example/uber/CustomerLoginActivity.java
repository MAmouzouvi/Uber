package com.example.uber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerLoginActivity extends AppCompatActivity {

    private EditText nEmail, nPassword;
    private Button nLogin, nRegister;

    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);


        nRegister = findViewById(R.id.btnRegister);

        nRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CustomerLoginActivity.this, CustomerRegisterActivity.class);
                startActivity(intent);


            }
        });


        nLogin = findViewById(R.id.btnLogin);
        nEmail = findViewById(R.id.edtTxtEmail);
        nPassword= findViewById(R.id.edtTxtPassword);


        authProfile = FirebaseAuth.getInstance();

        //Login User
        nLogin = findViewById(R.id.btnLogin);



        nLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String email = nEmail.getText().toString();
                 String password = nPassword.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(CustomerLoginActivity.this,"Please Enter your email address", Toast.LENGTH_LONG).show();
                    nEmail.setError("Email is required");
                    nEmail.requestFocus();

                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(CustomerLoginActivity.this,"Please re-enter your email", Toast.LENGTH_LONG).show();
                    nEmail.setError("Valid email is required");
                    nEmail.requestFocus();


                } else if (TextUtils.isEmpty(password)){
                    Toast.makeText(CustomerLoginActivity.this,"Please Enter your password", Toast.LENGTH_LONG).show();
                    nPassword.setError("Password is required");
                    nPassword.requestFocus();

            } else if(password.length() <= 5){
                    Toast.makeText(CustomerLoginActivity.this,"Please a longer", Toast.LENGTH_LONG).show();
                    nPassword.setError("Password needs to have 6 or more digits");
                    nPassword.requestFocus();

                }else {
                    //registerUser(email,)
                   LoginUser(email, password);

                }
            }
        });





//
//                nAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(CustomerLoginActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(!task.isSuccessful()){
//                            Toast.makeText(CustomerLoginActivity.this,"sign in error", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });

    }

    private void LoginUser(String email, String password) {

        authProfile.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CustomerLoginActivity.this,"You are logged in now", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CustomerLoginActivity.this, CustomerProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CustomerLoginActivity.this,"something went wrong!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        nAuth.addAuthStateListener(firebaseAuthListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        nAuth.removeAuthStateListener(firebaseAuthListener);
//    }
}
