package fp.jutaporn_a.fprestaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.media.MediaDescriptionCompatApi21;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    //Explicit
    private TextView showOfficerTextView;
    private Spinner deskSpinner;
    private ListView foodListView;

    private String officerString, deskString, foodString, amountString;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }   //Main Method

    private void createFoodListview() {
        //read all SQLite
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                MODE_PRIVATE, null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + MyManager.food_table, null);
        cursor.moveToFirst();
        int intcount = cursor.getCount();

        String[] iconStrings = new String[intcount];
        final String[] foodStrings = new String[intcount];
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

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                foodString = foodStrings[i];

                chooseAmount();

            }   //on Item Click
        });

    }  //food list view

    private void chooseAmount() {
        CharSequence[] charSequences = {"1 จาน","2 จาน","3 จาน","4 จาน","5 จาน",};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(foodString);
        builder.setSingleChoiceItems(charSequences, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                amountString = Integer.toString(i + 1);
                dialogInterface.dismiss();

                confirmOrder();


            }   // onclick
        });
        builder.show();
    }

    private void confirmOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.icon_myaccount);
        builder.setTitle("โปรดตรวจทาน");
        builder.setMessage("Officer =" + officerString + "\n" +
                "Desk =" + deskString + "\n" +
                "Food = " + foodString + "\n" +
                "Amount =" + amountString);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateToServer();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        builder.show();

    }   //confirm order

    private void updateToServer() {
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("isAdd","true"));
            nameValuePairs.add(new BasicNameValuePair("Officer", officerString));
            nameValuePairs.add(new BasicNameValuePair("Desk", deskString));
            nameValuePairs.add(new BasicNameValuePair("Food", foodString));
            nameValuePairs.add(new BasicNameValuePair("Amount", amountString));

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://swiftcodingthai.com/19Mar/php_add_order.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            httpClient.execute(httpPost);

            Toast.makeText(OrderActivity.this, "Order" + foodString + "เรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(OrderActivity.this, "ไม่สามารถเชื่อมต่อได้", Toast.LENGTH_SHORT).show();

        }

    }   // Update to server

    private void createDeskSpinner() {
        final String[] deskStrings = {"โต๊ะ 1", "โต๊ะ 2", "โต๊ะ 3", "โต๊ะ 4", "โต๊ะ 5", "โต๊ะ 6", "โต๊ะ 7", "โต๊ะ 8", "โต๊ะ 9", "โต๊ะ 10"};

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, deskStrings);
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

   /* @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Order Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://fp.jutaporn_a.fprestaurant/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }*/

   /* @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Order Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://fp.jutaporn_a.fprestaurant/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }*/
}   // Main Class
