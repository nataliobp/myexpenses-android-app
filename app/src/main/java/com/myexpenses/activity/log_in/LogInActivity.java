package com.myexpenses.activity.log_in;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.myexpenses.activity.expense_list.ExpenseListActivity;
import com.myexpenses.R;
import com.myexpenses.activity.create_spender.CreateSpenderActivity;
import com.myexpenses.model.Spender;
import com.myexpenses.service.http.MyExpensesApiClient;
import com.myexpenses.service.http.Response;
import com.myexpenses.service.http.SomethingWentWrongResponse;
import com.myexpenses.service.transformer.SpenderTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.HttpURLConnection;

public class LogInActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spendersSpinner;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String SPENDER_ID_KEY = "spenderIdKey";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setToolbar();
        setAddSpenderButton();
        setSpendersData();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        setLogInButton();
    }

    private void setLogInButton() {
        Button logInButton = (Button) findViewById(R.id.logInButton);
        logInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent expenseListIntent = new Intent(view.getContext(), ExpenseListActivity.class);
                startActivity(expenseListIntent);
            }
        });
    }

    private void setSpendersData() {
        spendersSpinner = (Spinner) findViewById(R.id.spendersSpinner);
        spendersSpinner.setAdapter(new SpendersAdapter());
        spendersSpinner.setOnItemSelectedListener(this);
        requestSpendersData();
    }

    private void requestSpendersData() {
        new GetSpendersTask().execute();
    }

    private void setAddSpenderButton() {
        FloatingActionButton addSpenderButton = (FloatingActionButton) findViewById(R.id.addSpenderButton);
        addSpenderButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent registerSpenderActivityIntent = new Intent(getBaseContext(), CreateSpenderActivity.class);
                startActivity(registerSpenderActivityIntent);
                finish();
            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.logInToolbar);
        setSupportActionBar(toolbar);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Spender aSpender = (Spender) adapterView.getAdapter().getItem(i);
        editor.putString(SPENDER_ID_KEY, aSpender.spenderId);
        editor.apply();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void setSpendersSpinnerDataFromJson(String json) throws JSONException {
        JSONArray payload = new JSONArray(new JSONTokener(json));

        Spender[] spenders = new Spender[payload.length()];
        for (int i = 0; i < payload.length(); i++) {
            spenders[i] = new SpenderTransformer().fromJson(payload.getJSONObject(i));
        }

        ((SpendersAdapter) spendersSpinner.getAdapter()).setData(spenders);
    }

    private class GetSpendersTask extends AsyncTask<String, Void, Response> {

        protected Response doInBackground(String... parameters) {
            try {
                return new MyExpensesApiClient().getSpenders();
            } catch (IOException e) {
                Log.e(getClass().toString(), e.toString());

                return new SomethingWentWrongResponse();
            }
        }

        protected void onPostExecute(Response response) {
            if (HttpURLConnection.HTTP_OK == response.code) {
                try {
                    setSpendersSpinnerDataFromJson(response.message);
                } catch (JSONException e) {
                    Log.e(getClass().toString(), e.toString());
                }
            } else {
                Toast.makeText(getBaseContext(), response.message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
