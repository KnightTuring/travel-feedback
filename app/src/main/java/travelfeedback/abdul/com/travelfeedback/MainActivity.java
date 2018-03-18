package travelfeedback.abdul.com.travelfeedback;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText editTextUserName, editTextHotelReview, editTextTransportReview, editTextOverallRating;
    Button buttonSubmit;
    String userCity; String userName;
    SharedPreferences sharedPreferences;
    HashMap <String, String> userFeedback = new HashMap<>();

    private DatabaseReference firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize XML views
        initialize();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = editTextUserName.getText().toString();
                boolean retValue = setMapData();

                boolean status = addToFirebase();

                if(status == true && retValue == true)
                {
                    Log.println(Log.INFO,"MyMessage","Written to Firebase");
                    //SUCCESS

                    Toast.makeText(getApplicationContext(),"Written to Firebase",Toast.LENGTH_SHORT).show();
                    exit();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Something's wrong, try again",Toast.LENGTH_SHORT).show();
                    //please try again
                    exit();
                }
            }
        });

    }
    public void initialize()
    {
        editTextUserName = findViewById(R.id.editTextUsername);
        editTextHotelReview = findViewById(R.id.editTextHotelReview);
        editTextTransportReview = findViewById(R.id.editTextTransportReview);
        editTextOverallRating  =findViewById(R.id.editTextOverallRating);

        buttonSubmit = findViewById(R.id.buttonSubmit);

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        userCity = sharedPreferences.getString("City","");
        Log.println(Log.INFO,"MyMessage","Got city "+userCity);
    }

    public boolean setMapData()
    {
        if(!userCity.isEmpty())
        {
            userFeedback.put("userCity",userCity);
        }
        else {
            Log.println(Log.INFO, "MyTag", "userCity is empty");
            return false;
        }
        //userFeedback.put("Username", editTextUserName.getText().toString());
        userFeedback.put("HotelReview", editTextHotelReview.getText().toString());
        userFeedback.put("TransportReview", editTextTransportReview.getText().toString());
        userFeedback.put("OverallRating", editTextOverallRating.getText().toString());
        return true;

    }

    public boolean addToFirebase()
    {
        /*
        * Refer https://firebase.google.com/docs/database/android/read-and-write
        * for more info. on CRUD operations on Firebase
         */
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase.child("trips").child(userCity).child(userName).setValue(userFeedback);
        return true;
    }

    public void exit()
    {
        userFeedback.clear();
        Intent intent = new Intent(this, CityPicker.class);
        startActivity(intent);
    }
}
