package travelfeedback.abdul.com.travelfeedback;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText editTextUserName, editTextPassword;
    Button buttonLogin;
    String email, password;
    String instanceToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //init FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        initialise();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    addTokenToFirebase(instanceToken);
                    Log.println(Log.INFO, "mytag" , "Token obtained:"+instanceToken);
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(LoginActivity.this,"Signed in!",Toast.LENGTH_SHORT).show();
                    onSuccessfulSignIn();
                }
                else
                {
                    Log.println(Log.INFO, "mytag" , "Sign in failed");
                    Toast.makeText(LoginActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onSuccessfulSignIn()
    {
        Intent intent = new Intent(this, CityPickerFinal.class);
        startActivity(intent);

    }

    public void addTokenToFirebase(String fcmToken)
    {
        DatabaseReference firebaseDatabase;
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("myTag" , "Writing FCM Token to Firebase");
        firebaseDatabase.child("adminToken").child("token").setValue(fcmToken);
        Log.d("myTag" , "Written FCM Token to Firebase");
    }
}
