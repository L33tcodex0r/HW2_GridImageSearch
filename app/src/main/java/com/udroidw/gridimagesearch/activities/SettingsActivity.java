package com.udroidw.gridimagesearch.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
        populateInputs();
    }

    public void populateInputs() {
        ArrayAdapter<CharSequence> imageSizeAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerImageSize, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> colorFilterAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerColorFilter, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> imageTypeAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerImageType, android.R.layout.simple_spinner_item);

        imageSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerImageSize.setAdapter(imageSizeAdapter);
        spinnerColorFilter.setAdapter(colorFilterAdapter);
        spinnerImageType.setAdapter(imageTypeAdapter);

        //Load previous settings
        SharedPreferences settings = getSharedPreferences("settings", 0);
        String color = settings.getString("color", "");
        String imageType = settings.getString("imageType", "");
        String imageSize = settings.getString("imageSize", "");
        String site = settings.getString("site", "");

        setSpinnerValue(spinnerImageSize, imageSize);
        setSpinnerValue(spinnerColorFilter, color);
        setSpinnerValue(spinnerImageType, imageType);
        etSiteFilter.setText(site);
    }

    //Sets the spinner value by iterating through the spinner items until one matches the string.
    public void setSpinnerValue(Spinner spinner, String value) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                index = i;
                break;
            }
        }
        spinner.setSelection(index);
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

    public void onSave(View v) {
        Intent result = new Intent();

        String imageSize = spinnerImageSize.getSelectedItem().toString();
        String color = spinnerColorFilter.getSelectedItem().toString();
        String imageType = spinnerImageType.getSelectedItem().toString();
        String site = etSiteFilter.getText().toString();

        //If the value is "any" we're not going to query for it, so we just set it to an empty string.
        if (imageSize.equals("any")) {
            imageSize = "";
        }
        if (color.equals("any")) {
            color = "";
        }
        if (imageType.equals("any")) {
            imageType = "";
        }

        //Save the settings and finish the activity.
        SharedPreferences settings = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("imageSize", imageSize);
        editor.putString("color", color);
        editor.putString("imageType", imageType);
        editor.putString("site", site.trim());
        editor.commit();
        finish();
    }
}
