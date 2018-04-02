package travelfeedback.abdul.com.travelfeedback;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CityPickerFinal extends AppCompatActivity {


    String[] resultsCities;
    String cities[];

    SQLiteDatabase citiesDb;
    Cursor databaseCities;
    EditText editTextSearch;
    ListView listView;
    ArrayList<String> displayResultList;
    ArrayAdapter<String> displayResult;
    TextView textView;
    ArrayAdapter<String> dataAdapter;
    List<String> categories;
    Handler handler;
    TextView textview;
    FloatingActionButton logOutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_picker_final);


        initialize();

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != "")
                    search();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                Log.e("resultCities[]", resultsCities[i]);
                editor.putString("City", resultsCities[i]);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);


            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CityPickerFinal.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        });

    }


    public void search() {
        int i = 0;
        textView.setText("In search not found!");
        //Toast.makeText(this,"In search.",Toast.LENGTH_SHORT).show();
        displayResultList = new ArrayList<>();
        String nameOfCity;

        nameOfCity = editTextSearch.getText().toString();
        if (nameOfCity == null)
            nameOfCity = "445sdsd";
        databaseCities = citiesDb.rawQuery("SELECT * FROM CitiesDb WHERE cityName LIKE '%" + nameOfCity + "%'", null);
        databaseCities.moveToFirst();


        while (databaseCities.isAfterLast() == false) {


            if (i > cities.length)
                break;


            resultsCities[i] = databaseCities.getString(0);
            displayResultList.add(databaseCities.getString(0));
            databaseCities.moveToNext();
            i++;

            displayFinalList();
        }
        i = 0;

        displayFinalList();
    }

    public void displayFinalList() {

        textView.setText("In search found!");
        displayResult = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, displayResultList);
        listView.setAdapter(displayResult);
    }

    private void initialize() {

        cities = new String[]{"Aurangabad", "Simla", "Delhi", "Pune", "Mumbai", "Leh", "Haridwar", "Kaniyakumari", "Port Blair", "Hyderabad"};
        citiesDb = openOrCreateDatabase("CitiesDb", MODE_PRIVATE, null);
        citiesDb.execSQL("CREATE TABLE IF NOT EXISTS CitiesDb(cityName VARCHAR);");
        citiesDb.delete("CitiesDb", null, null);


        citiesDb = openOrCreateDatabase("CitiesDb", MODE_PRIVATE, null);
        citiesDb.execSQL("CREATE TABLE IF NOT EXISTS CitiesDb(cityName VARCHAR);");

        for (int i = 0; i < cities.length; i++)
            citiesDb.execSQL("INSERT INTO CitiesDb values('" + cities[i] + "');");

        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textViewHelper);
        logOutButton = (FloatingActionButton)findViewById(R.id.signOut);
        resultsCities = new String[100];


    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(CityPickerFinal.this,CityPickerFinal.class);
        startActivity(intent);
    }

}

