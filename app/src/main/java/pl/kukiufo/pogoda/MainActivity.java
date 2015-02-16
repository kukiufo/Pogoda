package pl.kukiufo.pogoda;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            PogodaRefresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void PogodaRefresh() {
        new PogodaTask(this, pc).execute();
    }
}
