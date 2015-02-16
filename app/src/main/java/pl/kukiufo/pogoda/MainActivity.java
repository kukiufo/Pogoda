package pl.kukiufo.pogoda;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by kukiufo on 16.02.15.
 */
public class MainActivity extends ActionBarActivity {

    private PogodaComponent pc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pc = (PogodaComponent) findViewById(R.id.am_pogoda_component);

        PogodaRefresh();

        ((ImageButton) findViewById(R.id.am_im_refresh)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PogodaRefresh();
            }
        });
    }

    private void PogodaRefresh() {
        new PogodaTask(this, pc).execute();
    }
}
