package com.smeunier.plasmatic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class PlasmaSettings extends Activity {
	public static final String PREFS_NAME = "PlasmaticPrefs";
	private String plasmaTypeVal;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

		EditText roughnessText = (EditText) findViewById(R.id.roughnessText);
		roughnessText.setText(settings.getString("roughness", "2"));

				
		Spinner plasmaTypeSpinner = (Spinner) findViewById(R.id.plasmaTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.plasmaType_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plasmaTypeSpinner.setAdapter(adapter);
		plasmaTypeSpinner.setOnItemSelectedListener(listener);

		plasmaTypeVal = settings.getString("plasmaType", "Plasma");
		plasmaTypeSpinner.setSelection(adapter.getPosition(plasmaTypeVal));

		Button save = (Button) findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
				//Put in saving code here
            	Editor editor = getSharedPreferences(PREFS_NAME, 0).edit();
            	
				EditText roughnessText = (EditText) findViewById(R.id.roughnessText);

				editor.putString("roughness", roughnessText.getText().toString());
				editor.putString("plasmaType", plasmaTypeVal);
				editor.commit();
				
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

        });

		Button cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

        });

    }
    
    AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener () {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        	plasmaTypeVal = parent.getSelectedItem().toString();
        }

		public void onNothingSelected(AdapterView<?> parent) {
			
		}
    };
}