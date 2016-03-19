package fp.jutaporn_a.fprestaurant;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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


    }   //Main method
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
