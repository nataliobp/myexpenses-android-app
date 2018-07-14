package com.myexpenses.activity.save_expense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myexpenses.R;
import com.myexpenses.activity.expense_details.ExpenseDetailsActivity;
import com.myexpenses.activity.expense_list.ExpenseListActivity;
import com.myexpenses.activity.expense_list_report.ExpenseListReportActivity;
import com.myexpenses.activity.log_in.LogInActivity;
import com.myexpenses.model.Category;
import com.myexpenses.model.Expense;
import com.myexpenses.service.builder.ExpenseBuilder;
import com.myexpenses.service.http.MyExpensesApiClient;
import com.myexpenses.service.http.Response;
import com.myexpenses.service.http.SomethingWentWrongResponse;
import com.myexpenses.service.transformer.CategoryTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.HttpURLConnection;

public class SaveExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText amountEdit;
    private EditText descriptionEdit;
    private String expenseListId;
    private String spenderId;
    private Spinner categoriesSpinner;
    private Expense expense;
    private String selectedCategoryId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        getDataFromSharedPreferences();
        expenseListId = getIntent().getStringExtra(ExpenseListActivity.EXPENSE_LIST_ID);
        amountEdit = (EditText) findViewById(R.id.amountEdit);
        descriptionEdit = (EditText) findViewById(R.id.descriptionEdit);
        setCategoriesSpinner();

        if (isAnEdition()) {
            expense = getExpense();
            amountEdit.setText(expense.amount);
            descriptionEdit.setText(expense.description);
        }
    }

    private boolean isAnEdition() {
        return getIntent().hasExtra(ExpenseListReportActivity.EXPENSE_PAYLOAD);
    }

    private void setCategoriesSpinner() {
        categoriesSpinner = (Spinner) findViewById(R.id.categoriesSpinner);
        categoriesSpinner.setAdapter(new CategoriesAdapter());
        categoriesSpinner.setOnItemSelectedListener(this);
        requestCategoriesSpinnerData();
    }

    private void requestCategoriesSpinnerData() {
        new GetCategoriesTask().execute(expenseListId);
    }

    private void getDataFromSharedPreferences() {
        SharedPreferences sharedpreferences = getSharedPreferences(LogInActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        spenderId = sharedpreferences.getString(LogInActivity.SPENDER_ID_KEY, "");
    }

    private Expense getExpense() {
        String expensePayload = getIntent().getStringExtra(ExpenseListReportActivity.EXPENSE_PAYLOAD);

        return new Gson().fromJson(expensePayload, Expense.class);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_expense, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveExpense) {
            Expense anExpense = buildExpenseFromAvailableData();

            if (isAnEdition()) {
                new AlterExpenseTask().execute(anExpense);
            } else {
                new AddExpenseTask().execute(anExpense);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Expense buildExpenseFromAvailableData() {
        return new ExpenseBuilder()
                    .withExpenseId(null == expense ? null : expense.expenseId)
                    .withExpenseListId(expenseListId)
                    .withSpenderId(spenderId)
                    .withCategoryId(selectedCategoryId)
                    .withAmount(amountEdit.getText().toString())
                    .withDescription(descriptionEdit.getText().toString())
                    .build();
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedCategoryId = ((Category) adapterView.getSelectedItem()).categoryId;
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void setCategoriesSpinnerDataFromJson(String json) throws JSONException {
        JSONArray payload = new JSONArray(new JSONTokener(json));

        Category[] categories = new Category[payload.length()];
        for (int i = 0; i < payload.length(); i++) {
            categories[i] = new CategoryTransformer().fromJson(payload.getJSONObject(i));
        }

        ((CategoriesAdapter) categoriesSpinner.getAdapter()).setData(categories);
        selectedCategoryId = categories.length == 0 ? null  : categories[0].categoryId;

        if (expense != null) {
            int index = ((CategoriesAdapter) categoriesSpinner.getAdapter()).getIndexOfCategory(expense.categoryId);
            categoriesSpinner.setSelection(index);
        }
    }

    private class AddExpenseTask extends AsyncTask<Expense, Void, Response> {

        protected Response doInBackground(Expense... expenses) {
            try {
                return new MyExpensesApiClient().addExpense(expenses[0]);
            } catch (IOException e) {
                Log.e(getClass().toString(), e.toString());

                return new SomethingWentWrongResponse(e.toString());
            }
        }

        protected void onPostExecute(Response response) {
            String message = response.code == HttpURLConnection.HTTP_CREATED
                ? "Expense successfully created"
                : "Error: " + response.message;

            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

            Intent expenseListReportActivityIntent = new Intent(getBaseContext(), ExpenseListReportActivity.class);
            expenseListReportActivityIntent.putExtra(ExpenseListActivity.EXPENSE_LIST_ID, expenseListId);
            startActivity(expenseListReportActivityIntent);
            finish();
        }
    }

    private class AlterExpenseTask extends AsyncTask<Expense, Void, Response> {

        protected Response doInBackground(Expense... expenses) {
            try {
                return new MyExpensesApiClient().alterExpense(expenses[0]);
            } catch (IOException e) {
                Log.e(getClass().toString(), e.toString());

                return new SomethingWentWrongResponse(e.toString());
            }
        }

        protected void onPostExecute(Response response) {
            String message = response.code == HttpURLConnection.HTTP_NO_CONTENT
                ? "Expense successfully modified"
                : "Error: " + response.message;

            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

            Intent expenseDetailsActivityIntent = new Intent(getBaseContext(), ExpenseDetailsActivity.class);
            expenseDetailsActivityIntent.putExtra(ExpenseListReportActivity.EXPENSE_ID, expense.expenseId);
            startActivity(expenseDetailsActivityIntent);
            finish();
        }
    }

    private class GetCategoriesTask extends AsyncTask<String, Void, Response> {

        public Response doInBackground(String... ids) {
            try {
                return new MyExpensesApiClient().getCategoriesOfExpenseListOfId(ids[0]);
            } catch (IOException e) {
                Log.e(getClass().toString(), e.toString());

                return new SomethingWentWrongResponse(e.toString());
            }
        }

        protected void onPostExecute(Response response) {
            if (response.code == HttpURLConnection.HTTP_OK) {
                try {
                    setCategoriesSpinnerDataFromJson(response.message);
                } catch (JSONException e) {
                    Log.e(getClass().toString(), e.toString());
                }
            } else {
                Toast.makeText(getBaseContext(), response.message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
