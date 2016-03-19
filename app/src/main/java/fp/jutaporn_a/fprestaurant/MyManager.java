package fp.jutaporn_a.fprestaurant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jutaporn-a on 19/3/2559.
 */
public class MyManager {

    // Explicit
    private MyOpenHelper myOpenHelper;
    private SQLiteDatabase writeSqLiteDatabase,readSqLiteDatabase;

    public MyManager(Context context) {

        //Create & connect SQLite
        myOpenHelper = new MyOpenHelper(context);
        writeSqLiteDatabase = myOpenHelper.getWritableDatabase();
        readSqLiteDatabase = myOpenHelper.getReadableDatabase();


    }   //Constructor


}  // Main Class
