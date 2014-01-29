package com.codepath.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	ImageSearchFilters filters;

	public static final String FILTER_KEY = "filter";
	private final int FILTER_REQUEST_CODE = 20;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		filters = new ImageSearchFilters();
		
		setupViews();
		setupListeners();
		setupScrollListeners();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	public void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
		btnSearch = (Button) findViewById(R.id.btnSearch);
	}
	
	public void setupListeners() {
		gvResults.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter,
					View parent, int position, long arg3) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivity(i);
			}
		});
	}
	
	public void setupScrollListeners() {
		gvResults.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				customLoadMoreDataFromApi(totalItemsCount);
			}
		});
	}
	
	public void onImageSearch(View v) {
		imageResults.clear();
		customLoadMoreDataFromApi(0);
	}
	
	private String getAPIRequest(String query, int size) {
		String api = "https://ajax.googleapis.com/ajax/services/"
				+ "search/images?rsz=8&v=1.0&q=" + Uri.encode(query);
		if(filters.getSize() != null) {
			api+="&imgsz="+filters.getSize();
		}
		if(filters.getColor() != null) {
			api+="&imgcolor="+filters.getColor();	
		}
		if(filters.getType() != null) {
			api+="&imgtype="+filters.getType();
		}
		if(filters.getSite() != null) {
			api+="&as_sitesearch="+filters.getSite();
		}
		api+="&start="+size;
		
		Log.d("API", "API Requests: " + api);
		return api;
	}
	
	public void customLoadMoreDataFromApi(int page) {
		String query = etQuery.getText().toString();
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(getAPIRequest(query, page),
				new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				JSONArray imageJsonResults = null;
				try {
					imageJsonResults = response.getJSONObject(
							"responseData").getJSONArray("results");
									
					imageAdapter.addAll(ImageResult
							.fromJSONArray(imageJsonResults));
					Log.d("DEBUG", imageResults.toString());
					Log.d("Response", response.getJSONObject(
							"responseData").getJSONObject("cursor").toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void onSettingsAction(MenuItem mi) {
		Intent i = new Intent(getApplicationContext(),
				ImageFilterActivity.class);
		i.putExtra(FILTER_KEY, filters);
		startActivityForResult(i, FILTER_REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == FILTER_REQUEST_CODE) {
			filters = (ImageSearchFilters) data.getSerializableExtra(
					ImageFilterActivity.FILTER_KEY);
		}
	}
}
