package travelfeedback.abdul.com.travelfeedback.userHandler;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import travelfeedback.abdul.com.travelfeedback.R;

/*
* For Progress bar doc refer
* https://github.com/F0RIS/sweet-alert-dialog ---a fork of the original progress bar library which does
* not working
*
*/
public class SignUpActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, editTextPhone, editTextName, editTextResidence, editTextAge;
    Button buttonRegister;
    String email, password, name, phone, residence, age;
    private FirebaseAuth mAuth;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        initialise();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                name = editTextName.getText().toString();
                residence = editTextResidence.getText().toString();
                phone = editTextPhone.getText().toString();
                age = editTextAge.getText().toString();
                HashMap<String,String> userInfo = new HashMap<>();
                userInfo.put("name",name);
                userInfo.put("email",email); userInfo.put("residence",residence); userInfo.put("age",age);
                userInfo.put("phone",phone);

                pDialog = new SweetAlertDialog(SignUpActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();

                signUpUser(email, password, userInfo);
            }
        });


    }

    public  void initialise()
    {
        editTextAge = findViewById(R.id.editTextAge);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextResidence = findViewById(R.id.editTextResidence);

        buttonRegister = findViewById(R.id.buttonRegister);
    }

    public void signUpUser(String strEmail, String strPassword, final HashMap<String, String> userInfo )
    {
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    //successfully completed sign up
                    FirebaseUser user = mAuth.getCurrentUser();
                    addUserToFirebase(user, userInfo);

                    moveToLoginActivity();
                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "(FB error)Registration failed",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void addUserToFirebase(FirebaseUser user, HashMap<String,String> userInfo)
    {
        String userID = user.getUid();
        DatabaseReference firebaseReference;
        firebaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseReference.child("users").child(userID).child("info").setValue(userInfo);
        Toast.makeText(SignUpActivity.this,"Added to Firebase",Toast.LENGTH_SHORT).show();
    }

    public void moveToLoginActivity()
    {

        pDialog.dismissWithAnimation();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

}
