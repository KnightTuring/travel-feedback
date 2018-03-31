package travelfeedback.abdul.com.travelfeedback.feedbackModule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import travelfeedback.abdul.com.travelfeedback.R;
import travelfeedback.abdul.com.travelfeedback.userHandler.LoginActivity;

public class CityPicker extends AppCompatActivity {

    Button buttonProceed;
    Button buttonLogOut;
    Spinner spinnerCityPicker;
    String selectedSpinnerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_picker);

        buttonProceed = findViewById(R.id.buttonProceed);
        buttonLogOut = findViewById(R.id.buttonLogOut);
        spinnerCityPicker = findViewById(R.id.spinnerCity);


        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Delhi");
        spinnerArray.add("Ladakh");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerArray);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCityPicker.setAdapter(spinnerAdapter);

        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveToNewActivity();
            }
        });

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    logUserOut();
                }
                catch (Exception e)
                {
                    Log.d("myTag","An exception occurred while logging user out");
                }

            }
        });
    }


    public void moveToNewActivity()
    {
        selectedSpinnerItem = spinnerCityPicker.getSelectedItem().toString();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        if(!selectedSpinnerItem.isEmpty())
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("City",selectedSpinnerItem);
            editor.commit();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            //error please pick a city
        }

    }

    public void logUserOut()
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
        }
        catch (Exception e)
        {
            Log.d("MyTag","An exception while deleting InstanceID");
        }

        mAuth.signOut();

        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
