package travelfeedback.abdul.com.travelfeedback;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class CityPicker extends AppCompatActivity {

    Button buttonProceed;
    Spinner spinnerCityPicker;
    String selectedSpinnerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_picker);

        buttonProceed = findViewById(R.id.buttonProceed);
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
}
