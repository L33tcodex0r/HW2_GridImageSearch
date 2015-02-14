package com.udroidw.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.udroidw.gridimagesearch.R;
import com.udroidw.gridimagesearch.adapters.ImageResultsAdapter;
import com.udroidw.gridimagesearch.models.ImageResult;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity {
    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private String color = "";
    private String imageType = "";
    private String imageSize = "";
    private String site = "";
    static final int SETTINGS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Google Image Search");
        setSupportActionBar(toolbar);
        setupViews();

        //Creates the data source
        imageResults = new ArrayList<>();
        //Attaches the data source to an adapter
        aImageResults = new ImageResultsAdapter(this, imageResults);
        //Link the adapter to the adapterview (gridview)
        gvResults.setAdapter(aImageResults);

    }

    public void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Launch the image display activity
                //Create an intent
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                //Get the image result to display
                ImageResult result = imageResults.get(position);
                //Pass image result into the intent
                i.putExtra("result", result);
                //Launch the new activity
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    //Fired whenever the button is pressed (android:onclick property)
    public void onImageSearch(View v) {
        String query = etQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        //as_sitesearch
        //imgcolor
        //imgsz
        //imgtype

        // https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android&rsz=8
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&imgsz=" + imageSize + "&imgcolor=" + color + "&imgsz=" + imageSize + "&as_sitesearch=" + site + "&rsz=8";
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear(); //clear the existing images from the array (in cases where it's a new search)
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson)); //You actually trigger the notify when you do this.
                } catch (JSONException e) {
                    Log.e("DEBUG", e.toString());
                }
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, SETTINGS_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_REQUEST) {
            if (resultCode == RESULT_OK) {
                color = data.getStringExtra("color");
                imageType = data.getStringExtra("imageType");
                imageType = data.getStringExtra("imageType");
                imageSize = data.getStringExtra("imageSize");
                site = data.getStringExtra("site");
            }
        }
    }
}
