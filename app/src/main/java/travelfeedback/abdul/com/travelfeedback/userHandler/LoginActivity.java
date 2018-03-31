package travelfeedback.abdul.com.travelfeedback.userHandler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import travelfeedback.abdul.com.travelfeedback.feedbackModule.CityPickerFinal;
import travelfeedback.abdul.com.travelfeedback.R;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressBar;
    SweetAlertDialog pDialog;

    private FirebaseAuth mAuth;
    EditText editTextUserName, editTextPassword;
    Button buttonLogin;
    String email, password;
    String instanceToken;

    TextView textViewSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //init FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        initialise();

        pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog.show();
               signInMethod();
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    public void initialise()
    {
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextUserName = findViewById(R.id.editTextUsername);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignUp = findViewById(R.id.textViewSignUp);
    }

    public void signInMethod()
    {
        email = editTextUserName.getText().toString();
        password = editTextPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    //sign in successful!
                    Log.println(Log.INFO, "mytag" , "Sign in successful");
                    instanceToken = FirebaseInstanceId.getInstance().getToken();
                    Log.println(Log.INFO, "mytag" , "Token obtained:"+instanceToken);
                    FirebaseUser user = mAuth.getCurrentUser();
                    addTokenToFirebase(instanceToken,user);
                    Toast.makeText(LoginActivity.this,"Signed in!",Toast.LENGTH_SHORT).show();

                    pDialog.dismissWithAnimation();
                    onSuccessfulSignIn();
                }
                else
                {
                    Log.println(Log.INFO, "mytag" , "Sign in failed");
                    Toast.makeText(LoginActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                    pDialog.dismissWithAnimation();
                }
            }
        });

    }

    public void onSuccessfulSignIn()
    {
        Intent intent = new Intent(this, CityPickerFinal.class);
        startActivity(intent);

    }

    public void addTokenToFirebase(String fcmToken,FirebaseUser signedInUser)
    {
        String email = signedInUser.getEmail();
        String userID = signedInUser.getUid();
        String name=" ";
        if(email.equals("abdulwasay50@gmail.com") || email.equals("akshayphadnis1994@gmail.com"))
        {
            HashMap<String,String> tokenDetails = new HashMap<>();
            //admins
            if(email.equals("abdulwasay50@gmail.com"))
            {
                name="Abdul";

            }
            else if(email.equals("akshayphadnis1994@gmail.com"))
            {
                name = "Akshay";
            }
            tokenDetails.put("adminName",name);
            tokenDetails.put("token",fcmToken);

            DatabaseReference firebaseDatabase;
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();
            Log.d("myTag" , "Writing FCM Token to Firebase");
            //firebaseDatabase.child("adminToken").child("token").setValue(fcmToken);

            firebaseDatabase.child("adminToken").child(name).setValue(tokenDetails);
            Log.d("myTag" , "Written FCM Token to Firebase");

            //now for user
            HashMap<String,String> adminInfo = new HashMap<>();
            adminInfo.put("name",name); adminInfo.put("residence","Pune");
            firebaseDatabase.child("users").child(userID).child("info").setValue(adminInfo);
            firebaseDatabase.child("users").child(userID).child("token").setValue(fcmToken);
        }
        /*else if (email.equals("ritvik.bhavan@gmail.com"))
        {
            name = "Ritvik";
            DatabaseReference firebaseDatabase;
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();
            HashMap<String,String> tripData = new HashMap<>();
            tripData.put("tripCity","Ladakh");
            tripData.put("dateTimeStamp","2345");
            tripData.put("token",fcmToken);
            firebaseDatabase.child("tripList").child(name).setValue(tripData);
        }*/
        else
        {
            //user is not admin
            DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance().getReference();

            databaseReference.child("users").child(userID).child("token").setValue(fcmToken);

        }



    }
}
