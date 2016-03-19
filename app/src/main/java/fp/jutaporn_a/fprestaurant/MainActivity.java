package fp.jutaporn_a.fprestaurant;

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
        testAddValue();
    }   //Main method

    private void testAddValue() {
        myManager.addValue(1,"user","pass","name");
        myManager.addValue(2,"food","price","source");
    }
}   //Main class
