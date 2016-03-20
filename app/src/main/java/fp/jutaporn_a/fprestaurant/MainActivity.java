package fp.jutaporn_a.fprestaurant;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {


    //Explicit
    private MyManager myManager;
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind widget
        bindWidget();


        //Request SQLite
        myManager = new MyManager(this);

        //Test Add Value
        //testAddValue();

        //Delete All SQLite
        deleteAllSQLite();

        //Syn JSON to SQLite
        synJSONtoSQLite();



    }   //Main method

    public void clickLogin(View view) {
        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();
        //check space
        if (userString.equals("") || passwordString.equals("")) {
            //Have space
            myAlert("มีช่องว่าง");
            //No space

        } else {
            //No space
            checkUser();
        }

    }   //clickLogin

    private void checkUser() {

        try {
            String[] myResultStrings = myManager.searchUser(userString);

            //check password
            if (passwordString.equals(myResultStrings[2])) {
                //password True
                Intent intent = new Intent(MainActivity.this,OrderActivity.class);
                intent.putExtra("Officer", myResultStrings[3]);
                startActivity(intent);
                finish();
            } else {
                //Password False
                myAlert("Password ผิด");

            }

            myAlert("ยินดีต้อนรับ" + myResultStrings[3]);

        } catch (Exception e) {
            myAlert("ไม่มี " + userString + "ในฐานข้อมูลของเรา");
        }

    } //checkUser

    private void myAlert(String strMessage) {
        Toast.makeText(MainActivity.this,strMessage, Toast.LENGTH_SHORT).show();

    }

    private void bindWidget() {
        userEditText = (EditText) findViewById(R.id.editText);
        passwordEditText = (EditText) findViewById(R.id.editText2);
    }

    private void synJSONtoSQLite() {

        //Connected HTTP://
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        int intTable =0;
        while (intTable <=1) {
            //1  create inputStream
            InputStream inputStream =null;
            String[] urlStrings = {"http://swiftcodingthai.com/19Mar/php_get_user_joyce.php","http://swiftcodingthai.com/19Mar/php_get_food_joyce.php"};

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urlStrings[intTable]);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();

            } catch (Exception e) {
                Log.d("Rest","InputStream ==>" +e.toString());
            }

            //2 create JSON String
            String strJSON =null;
            try {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String strLine = null;

                while ((strLine = bufferedReader.readLine()) !=null) {
                    stringBuilder.append(strLine);
                }

                inputStream.close();
                strJSON = stringBuilder.toString();

            } catch (Exception e) {
                Log.d("Rest", "JSON String ==>" + e.toString());
            }

            //3 Update to SQLite
            try {

                JSONArray jsonArray = new JSONArray(strJSON);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    switch (intTable) {
                        case 0:
                            String strUser = jsonObject.getString(MyManager.column_User);
                            String strPassword = jsonObject.getString(MyManager.column_Password);
                            String strName = jsonObject.getString(MyManager.column_Name);

                            myManager.addValue(1, strUser, strPassword, strName);

                            break;
                        case 1:
                            String strFood = jsonObject.getString(MyManager.column_Food);
                            String strPrice = jsonObject.getString(MyManager.column_Price);
                            String strSource = jsonObject.getString(MyManager.column_Source);

                            myManager.addValue(2, strFood, strPrice, strSource);
                            break;

                    }
                }

            } catch (Exception e) {
                Log.d("Rest","Update RQLite ==>" +e.toString());

            }

           intTable += 1;
        }   //while

    }   //synJSON

    private void deleteAllSQLite(){
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                MODE_PRIVATE,null);
        sqLiteDatabase.delete(MyManager.user_table,null,null);
        sqLiteDatabase.delete(MyManager.food_table,null,null);

    }

    private void testAddValue() {
        myManager.addValue(1,"user","pass","name");
        myManager.addValue(2,"food","price","source");
    }
}   //Main class
