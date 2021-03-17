package com.nimish.moviesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText name;
    TextInputEditText email;
    TextInputEditText password;
    Button signup;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollection = db.collection("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.user_name);
        email = findViewById(R.id.user_email);
        password = findViewById(R.id.user_pwd);
        signup = findViewById(R.id.signup_btn);

        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name.getText().toString().trim();
                String userEmail = email.getText().toString().trim();
                String userPwd = password.getText().toString().trim();

                if(userName.isEmpty() || userPwd.isEmpty() || userEmail.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please fill all the details!",Toast.LENGTH_LONG).show();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    email.setError("Please enter a valid email");
                    return;
                }

                if(userPwd.length()<6){
                    password.setError("Password length should be more than 5 characters");
                    return;
                }

                registerUser(userName,userEmail, userPwd);
            }
        });

    }

    private void registerUser(String userName, String userEmail, String userPwd) {

        mAuth.createUserWithEmailAndPassword(userEmail,userPwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(userName,userEmail);
                            userCollection.add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(),"User Registered Successfully",Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(i);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),"User Registration Failed Fire",Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }else{
                            Toast.makeText(getApplicationContext(),"User Registration Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("signup error",e.toString());
            }
        });

    }
}