package com.udroidw.gridimagesearch.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.udroidw.gridimagesearch.R;

public class SettingsActivity extends ActionBarActivity {
    private Spinner spinnerImageSize;
    private Spinner spinnerColorFilter;
    private Spinner spinnerImageType;
    private EditText etSiteFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getViews();
        populateSpinners();
    }

    public void populateSpinners() {
        ArrayAdapter<CharSequence> imageSizeAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerImageSize, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> colorFilterAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerColorFilter, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> imageTypeAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerImageType, android.R.layout.simple_spinner_item);

        imageSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerImageSize.setAdapter(imageSizeAdapter);
        spinnerColorFilter.setAdapter(colorFilterAdapter);
        spinnerImageType.setAdapter(imageTypeAdapter);
    }

    public void getViews() {
        spinnerImageSize = (Spinner) findViewById(R.id.spinnerImageSize);
        spinnerColorFilter = (Spinner) findViewById(R.id.spinnerColorFilter);
        spinnerImageType = (Spinner) findViewById(R.id.spinnerImageType);
        etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
