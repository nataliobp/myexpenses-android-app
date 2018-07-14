package com.myexpenses.activity.create_expense_list;

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
import com.myexpenses.activity.expense_list.ExpenseListActivity;
import com.myexpenses.model.ExpenseList;
import com.myexpenses.service.http.MyExpensesApiClient;
import com.myexpenses.service.http.Response;
import com.myexpenses.service.http.SomethingWentWrongResponse;

import java.io.IOException;
import java.net.HttpURLConnection;

public class CreateExpenseListActivity extends AppCompatActivity {

    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense_list);
        name = (EditText) findViewById(R.id.inputName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_spender, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveSpender) {
            new CreateExpenseListTask().execute(
                new ExpenseList(name.getText().toString())
            );

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CreateExpenseListTask extends AsyncTask<ExpenseList, Void, Response> {

        protected Response doInBackground(ExpenseList... expenseLists) {
            try {
                return new MyExpensesApiClient().startExpenseList(expenseLists[0]);
            } catch (IOException e) {
                Log.e(getClass().toString(), e.toString());

                return new SomethingWentWrongResponse(e.toString());
            }
        }

        protected void onPostExecute(Response response) {
            String message = response.code == HttpURLConnection.HTTP_CREATED
                ? "Expense List successfully created"
                : "Error: " + response.message;

            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            Intent expenseListActivityIntent = new Intent(getBaseContext(), ExpenseListActivity.class);
            startActivity(expenseListActivityIntent);
            finish();
        }
    }
}