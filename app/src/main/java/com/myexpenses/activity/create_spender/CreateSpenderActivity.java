package com.myexpenses.activity.create_spender;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.myexpenses.R;
import com.myexpenses.activity.log_in.LogInActivity;
import com.myexpenses.model.Spender;
import com.myexpenses.service.http.MyExpensesApiClient;
import com.myexpenses.service.http.Response;
import com.myexpenses.service.http.SomethingWentWrongResponse;

import java.io.IOException;
import java.net.HttpURLConnection;

public class CreateSpenderActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spender);
        name = (EditText) findViewById(R.id.inputName);
        email = (EditText) findViewById(R.id.inputEmail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_spender, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveSpender) {
            new RegisterSpenderTask().execute(
                new Spender(
                    name.getText().toString(),
                    email.getText().toString()
                )
            );

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class RegisterSpenderTask extends AsyncTask<Spender, Void, Response>{

        @Override
        protected Response doInBackground(Spender... spenders) {
            try {
                return new MyExpensesApiClient().registerSpender(spenders[0]);
            } catch (IOException e) {
                Log.e(getClass().toString(), e.toString());

                return new SomethingWentWrongResponse(e.toString());
            }
        }

        protected void onPostExecute(Response response) {
            String message = response.code == HttpURLConnection.HTTP_CREATED
                ? "Spender successfully created"
                : "Error: " + response.message;

            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

            Intent logInActivityIntent = new Intent(getBaseContext(), LogInActivity.class);
            startActivity(logInActivityIntent);
            finish();
        }

    }
}
