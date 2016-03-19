package fp.jutaporn_a.fprestaurant;

import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Request SQLite
        myManager = new MyManager(this);

        //Test Add Value
        //testAddValue();

        //Delete All SQLite
        deleteAllSQLite();

        //Syn JSON to SQLite
        synJSONtoSQLite();



    }   //Main method

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
