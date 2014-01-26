package com.codepath.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class ImageFilterActivity extends Activity {
	Spinner spSize;
	Spinner spColor;
	Spinner spType;
	EditText etSiteFilter;
	Button btnFilterSave;
	ImageSearchFilters filters;

	public static final String FILTER_KEY = "filter_set";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_search_filters);
		
		spSize = (Spinner) findViewById(R.id.spSize);
		spColor = (Spinner) findViewById(R.id.spColor);
		spType = (Spinner) findViewById(R.id.spType);
		etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
		btnFilterSave = (Button) findViewById(R.id.btnFilterSave);
		
		filters = (ImageSearchFilters) getIntent().getSerializableExtra(SearchActivity.FILTER_KEY);
		
		setupFields();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_search_filters, menu);
		return true;
	}
	
	private void setupFields() {
		setSpinnerToValue(spSize, filters.getSize());
		setSpinnerToValue(spColor, filters.getColor());
		setSpinnerToValue(spType, filters.getType());
		etSiteFilter.setText(filters.getSite());
	}
	
	private void setSpinnerToValue(Spinner spinner, String value) {
		int index = 0;
		SpinnerAdapter adapter = spinner.getAdapter();
		for(int i = 0; i < adapter.getCount(); i++) {
			if(adapter.getItem(i).equals(value)) {
				index = i;
			}
		}
		spinner.setSelection(index);
	}
	
	public void onSave(View v) {
		filters.setSize(spSize.getSelectedItem().toString());
		filters.setColor(spColor.getSelectedItem().toString());
		filters.setType(spType.getSelectedItem().toString());
		filters.setSite(etSiteFilter.getText().toString());
		
		Intent i = new Intent();
		i.putExtra(FILTER_KEY, filters);
		setResult(RESULT_OK, i);
		
		finish();
	}
}
