package jp.crudefox.hall.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import jp.crudefox.hall.R;

/**
 * Created by chikara on 2014/07/16.
 */
public class MainActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            GameFragment f = GameFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, f)
                    //.addToBackStack("AAA")
                    .commit()
            ;
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}