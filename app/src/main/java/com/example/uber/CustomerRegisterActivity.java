package com.example.uber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firestore.v1.TransactionOptions;

public class CustomerRegisterActivity extends AppCompatActivity {
    private EditText nEmail, nPassword, nFistName, nLastName;
    private Button nRegister;
    private static final String TAG = "CustomerRegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);


        nEmail = findViewById(R.id.edtTxtEmail);
        nPassword = findViewById(R.id.edtTxtPassword);
        nFistName = findViewById(R.id.edtTxtFirstName);
        nLastName = findViewById(R.id.edtTxtLastName);


        nRegister = findViewById(R.id.btnRegister);
        Toast.makeText(CustomerRegisterActivity.this,"You can register now", Toast.LENGTH_LONG).show();

        nRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Obtain the data from the user
                String firstName = nFistName.getText().toString();
                String lastName = nLastName.getText().toString();
                String email = nEmail.getText().toString();
                String password = nPassword.getText().toString();

                if(TextUtils.isEmpty(firstName)){
                    Toast.makeText(CustomerRegisterActivity.this,"Please Enter your first name", Toast.LENGTH_LONG).show();
                    nFistName.setError("First Name is required");
                    nFistName.requestFocus();

                } else if (TextUtils.isEmpty(lastName)){
                    Toast.makeText(CustomerRegisterActivity.this,"Please Enter your last name", Toast.LENGTH_LONG).show();
                    nLastName.setError("Last Name is required");
                    nLastName.requestFocus();
                } else if (TextUtils.isEmpty(email)){
                    Toast.makeText(CustomerRegisterActivity.this,"Please Enter your email address", Toast.LENGTH_LONG).show();
                    nEmail.setError("email is required");
                    nEmail.requestFocus();

                } else if (TextUtils.isEmpty(password)){
                    Toast.makeText(CustomerRegisterActivity.this,"Please Enter a password", Toast.LENGTH_LONG).show();
                    nPassword.setError("Password is required");
                    nPassword.requestFocus();

                }else if(password.length() <= 6){
                    Toast.makeText(CustomerRegisterActivity.this,"Please a longer", Toast.LENGTH_LONG).show();
                    nPassword.setError("Password needs to have 7 or more digits");
                    nPassword.requestFocus();

                }else {
                    //registerUser(email,)
                    registerUser(firstName, lastName, email, password);
                }
    }

});
    }

    private void registerUser(String firstName, String lastName, String email, String password) {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        // register user
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CustomerRegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    // Enter User data into the firebase realtime database
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(firstName, lastName, email);

                    // Getting User reference from database for Registered Users
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registers Users").child("Customers");
                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){


                                //send verification email

                                firebaseUser.sendEmailVerification();
                                Toast.makeText(CustomerRegisterActivity.this, "Account successfully created. Please very email in inbox", Toast.LENGTH_LONG).show();


//                    // open user profile after successful registration
//
//                    Intent intent = new Intent(CustomerRegisterActivity.this, CustomerProfileActivity.class);
//
//                    // the flags is to prevent the user from returning back to register activity when they pressed back button after registration
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();   // to Close Register Activity

                            } else {
                                Toast.makeText(CustomerRegisterActivity.this, "User registration failed. Please try againx", Toast.LENGTH_LONG).show();
                            }




                            }
                    });




                } else {
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        nPassword.setError("Your password is too weak. ");
                        nPassword.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        nEmail.setError("Your email is invalid or already in use. Please re-enter ");
                        nEmail.requestFocus();


                    }catch(FirebaseAuthUserCollisionException e){
                        nEmail.setError("Your email is already registered. Please use another one");
                        nEmail.requestFocus();

                    }catch(Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(CustomerRegisterActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }
            }
        });


    }
}
