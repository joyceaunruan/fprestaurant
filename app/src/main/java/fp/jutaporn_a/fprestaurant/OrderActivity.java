package fp.jutaporn_a.fprestaurant;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class OrderActivity extends AppCompatActivity {
    //Explicit
    private TextView showOfficerTextView;
    private Spinner deskSpinner;
    private ListView foodListView;

    private String officerString , deskString , foodString , amountString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        //Bind widget
        bindWidget();
        //Show view
        showView();
        //Create Desk Spinner
        createDeskSpinner();

        //create Food List view
        createFoodListview();
    }   //Main Method

    private void createFoodListview() {
        //read all SQLite
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                MODE_PRIVATE, null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + MyManager.food_table, null);
        cursor.moveToFirst();
        int intcount = cursor.getCount();

        String[] iconStrings = new String[intcount];
        String[] foodStrings = new String[intcount];
        String[] priceStrings = new String[intcount];

        for (int i = 0; i < intcount; i++) {
            iconStrings[i] = cursor.getString(cursor.getColumnIndex(MyManager.column_Source));
            foodStrings[i] = cursor.getString(cursor.getColumnIndex(MyManager.column_Food));
            priceStrings[i] = cursor.getString(cursor.getColumnIndex(MyManager.column_Price));

            cursor.moveToNext();

        } //for
        cursor.close();

        FoodAdapter foodAdapter = new FoodAdapter(OrderActivity.this, iconStrings,
                                                   foodStrings, priceStrings);
        foodListView.setAdapter(foodAdapter);
    }  //food list view

    private void createDeskSpinner() {
        final String[] deskStrings = {"โต๊ะ 1","โต๊ะ 2","โต๊ะ 3","โต๊ะ 4","โต๊ะ 5","โต๊ะ 6","โต๊ะ 7","โต๊ะ 8","โต๊ะ 9","โต๊ะ 10"};

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,deskStrings);
        deskSpinner.setAdapter(stringArrayAdapter);

        deskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                deskString = deskStrings[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                deskString = deskStrings[0];

            }
        });
    } // Desk Spinner

    private void showView() {
        //Receive Value From Intent
        officerString = getIntent().getStringExtra("Officer");
        showOfficerTextView.setText(officerString);
    }

    private void bindWidget() {
        showOfficerTextView = (TextView) findViewById(R.id.textView);
        deskSpinner = (Spinner) findViewById(R.id.spinner);
        foodListView = (ListView) findViewById(R.id.listView);
    }
}   // Main Class
