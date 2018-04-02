package travelfeedback.abdul.com.travelfeedback;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();

    private FirebaseAuth mAuth;
    EditText editTextUserName, editTextPassword;
    Button buttonLogin;
    String email, password;
    String instanceToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = new ProgressDialog(this);

        progressBar.setTitle("Logging in...");
        progressBar.setMessage("Please wait..");
        progressBar.setCancelable(false);
        progressBar.setIndeterminate(true);


        //init FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        initialise();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressBar.show();

               signInMethod();



            }
        });







    }

    public void initialise()
    {
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextUserName = findViewById(R.id.editTextUsername);
        buttonLogin = findViewById(R.id.buttonLogin);
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

                    progressBar.dismiss();

                    onSuccessfulSignIn();

                }
                else
                {
                    Log.println(Log.INFO, "mytag" , "Sign in failed");
                    Toast.makeText(LoginActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
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
        }
        else if (email.equals("ritvik.bhavan@gmail.com"))
        {
            name = "Ritvik";
            DatabaseReference firebaseDatabase;
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();
            HashMap<String,String> tripData = new HashMap<>();
            tripData.put("tripCity","Ladakh");
            tripData.put("dateTimeStamp","2345");
            tripData.put("token",fcmToken);
            firebaseDatabase.child("tripList").child(name).setValue(tripData);
        }
        else
        {
            //Do nothing with token since user isn't admin

        }



    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(LoginActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
