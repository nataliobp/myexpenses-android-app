package com.myexpenses.activity.create_category;

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
import com.myexpenses.activity.expense_list_report.ExpenseListReportActivity;
import com.myexpenses.model.Category;
import com.myexpenses.service.http.MyExpensesApiClient;
import com.myexpenses.service.http.Response;
import com.myexpenses.service.http.SomethingWentWrongResponse;

import java.io.IOException;
import java.net.HttpURLConnection;

public class CreateCategoryActivity extends AppCompatActivity {

    private EditText name;
    private String expenseListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        name = (EditText) findViewById(R.id.inputName);
        expenseListId = getIntent().getStringExtra(ExpenseListActivity.EXPENSE_LIST_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_category, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveCategory) {
            new CreateCategoryTask().execute(
                new Category(
                    name.getText().toString(),
                    expenseListId
                )
            );

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CreateCategoryTask extends AsyncTask<Category, Void, Response> {

        protected Response doInBackground(Category... categories) {
            try {
                return new MyExpensesApiClient().createCategory(categories[0]);
            } catch (IOException e) {
                Log.e(getClass().toString(), e.toString());

                return new SomethingWentWrongResponse(e.toString());
            }
        }

        protected void onPostExecute(Response response) {
            String message = response.code == HttpURLConnection.HTTP_CREATED
                ? "Category successfully created"
                : "Error: " + response.message;

            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            Intent expenseListReportActivityIntent = new Intent(getBaseContext(), ExpenseListReportActivity.class);
            expenseListReportActivityIntent.putExtra(ExpenseListActivity.EXPENSE_LIST_ID, expenseListId);
            startActivity(expenseListReportActivityIntent);
            finish();
        }
    }
}