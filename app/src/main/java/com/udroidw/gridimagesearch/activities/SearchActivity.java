package com.udroidw.gridimagesearch.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.udroidw.gridimagesearch.helpers.EndlessScrollListener;
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

    private String color;
    private String imageType;
    private String imageSize;
    private String site;
    private String query;

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
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("DEBUG", "Load more running");
                addImages(totalItemsCount);
            }
        });
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
        query = etQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();

        SharedPreferences settings = getSharedPreferences("settings", 0);
        color = settings.getString("color", "");
        imageType = settings.getString("imageType", "");
        imageSize = settings.getString("imageSize", "");
        site = settings.getString("site", "");

        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&imgsz=" + imageSize + "&imgcolor=" + color + "&imgtype=" + imageType + "&as_sitesearch=" + site + "&rsz=8";
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray imageResultsJson;
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

    public void addImages(int totalItemsCount) {
        int start = totalItemsCount - 1;
        AsyncHttpClient client = new AsyncHttpClient();

        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&imgsz=" + imageSize + "&imgcolor=" + color + "&imgtype=" + imageType + "&as_sitesearch=" + site + "&start=" + start + "&rsz=8";
        Log.d("DEBUG", searchUrl);
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageResultsJson;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
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
}
