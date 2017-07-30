package codepath.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import codepath.nytimessearch.R;

import static codepath.nytimessearch.R.id.cbArts;

public class SettingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public Spinner spinner;
    public EditText etDate;
    public Button btnSave;
    public CheckBox Arts;
    public CheckBox Fashion_n_Style;
    public CheckBox Sports;
    String DeskValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        spinner = (Spinner) findViewById(R.id.spinnerOldestNewest);
        etDate = (EditText) findViewById(R.id.etDate);
        btnSave = (Button) findViewById(R.id.btnSave);
        Arts = (CheckBox) findViewById(cbArts);
        Fashion_n_Style = (CheckBox) findViewById(R.id.cbFashion_n_Style);
        Sports =  (CheckBox) findViewById(R.id.cbSports);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeskValues="news_desk:(";
                boolean hasDeskValues = false;
                // first parameter is the context, second is the class of the activity to launch
                Intent i = new Intent(SettingActivity.this, SearchActivity.class );
                // put "extras" into the bundle for access in the second activity
                i.putExtra("BeginDate", etDate.getText().toString());
                i.putExtra("Sort", spinner.getSelectedItem().toString());
                i.putExtra("DeskValues", DeskValues);

                if ((Sports.isChecked())){
                    //Toast.makeText(getApplicationContext(), Sports.getText().toString(), Toast.LENGTH_SHORT).show();
                    DeskValues+= " \""+Sports.getText()+"\"";
                    hasDeskValues = true;
                }
                if(Arts.isChecked()){
                    DeskValues+= " \""+Arts.getText()+"\"";
                    hasDeskValues = true;
                    //Toast.makeText(getApplicationContext(), Arts.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                if(Fashion_n_Style.isChecked()){
                    DeskValues+= " \""+Fashion_n_Style.getText()+"\"";
                    hasDeskValues = true;
                    //Toast.makeText(getApplicationContext(), Fashion_n_Style.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                DeskValues += ")";
                if(hasDeskValues){
                    Toast.makeText(getApplicationContext(),DeskValues, Toast.LENGTH_SHORT).show();
                    i.putExtra("NewsDeskValues", DeskValues);
                }
                // brings up the second activity
                startActivity(i);
            }
        });

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.sort_array, R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    // handle the date selected
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMdd");
        etDate.setText(simpleDateFormat.format(c.getTime()));
    }
}