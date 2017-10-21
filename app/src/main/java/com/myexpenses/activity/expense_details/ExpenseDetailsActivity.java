package com.myexpenses.activity.expense_details;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myexpenses.R;
import com.myexpenses.activity.expense_list.ExpenseListActivity;
import com.myexpenses.activity.save_expense.SaveExpenseActivity;
import com.myexpenses.activity.expense_list_report.ExpenseListReportActivity;
import com.myexpenses.model.Expense;
import com.myexpenses.service.http.MyExpensesApiClient;
import com.myexpenses.service.http.Response;
import com.myexpenses.service.http.SomethingWentWrongResponse;
import com.myexpenses.service.transformer.ExpenseTransformer;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExpenseDetailsActivity extends AppCompatActivity {

    private Expense expense;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);
        requestExpenseData();
    }

    private void requestExpenseData() {
        String anExpenseId = getIntent().getStringExtra(ExpenseListReportActivity.EXPENSE_ID);
        new GetExpenseTask().execute(anExpenseId);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_expense, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (isARemoval(item)) {
            new RemoveExpenseTask().execute(expense.expenseId);
        } else if (isAnEdition(item)) {
            Intent editExpenseActivityIntent = new Intent(getBaseContext(), SaveExpenseActivity.class);
            editExpenseActivityIntent.putExtra(ExpenseListReportActivity.EXPENSE_PAYLOAD, new Gson().toJson(expense));
            editExpenseActivityIntent.putExtra(ExpenseListActivity.EXPENSE_LIST_ID, expense.expenseListId);
            startActivity(editExpenseActivityIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isARemoval(MenuItem item) {
        return item.getItemId() == R.id.removeExpense;
    }

    private boolean isAnEdition(MenuItem item) {
        return item.getItemId() == R.id.editExpense;
    }

    private void setViewsDataFromExpense() throws ParseException {
        setView(R.id.expense_details_amount, formattedAmount());
        setView(R.id.expense_details_category_name, expense.categoryName);
        setView(R.id.expense_details_creation, formattedDate());
        setView(R.id.expense_details_description, expense.description);
    }

    private String formattedDate() throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s", Locale.ENGLISH);
        Date date = format.parse(expense.createdAt);
        SimpleDateFormat formattedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        return String.format("Created on %s by %s", formattedDate.format(date), expense.spenderName);
    }

    private String formattedAmount() {
        return String.format(Locale.forLanguageTag("es_ES"), "%.2f€", Double.valueOf(expense.amount));
    }

    private void setView(int aViewId, String aText) {
        TextView aView = (TextView) findViewById(aViewId);
        aView.setText(aText);
    }

    private class GetExpenseTask extends AsyncTask<String, Void, Response> {

        protected Response doInBackground(String... ids) {
            try {
                return new MyExpensesApiClient().getAnExpense(ids[0]);
            } catch (IOException e) {
                Log.e(getClass().toString(), e.toString());

                return new SomethingWentWrongResponse();
            }
        }

        protected void onPostExecute(Response response) {
            if (response.code == HttpURLConnection.HTTP_OK) {
                try {
                    expense = new ExpenseTransformer().fromJson(response.message);
                    setViewsDataFromExpense();
                } catch (JSONException | ParseException e) {
                    Log.e(getClass().toString(), e.toString());
                }
            } else {
                Toast.makeText(getBaseContext(), "Error: " + response.message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class RemoveExpenseTask extends AsyncTask<String, Void, Response> {

        protected Response doInBackground(String... ids) {
            try {
                return new MyExpensesApiClient().removeAnExpense(ids[0]);
            } catch (IOException e) {
                Log.e(getClass().toString(), e.toString());

                return new SomethingWentWrongResponse();
            }
        }

        protected void onPostExecute(Response aResponse) {
            String message = aResponse.code == HttpURLConnection.HTTP_NO_CONTENT
                ? "Expense successfully deleted"
                : "Something went wrong";

            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            Intent expenseListReportIntent = new Intent(getBaseContext(), ExpenseListReportActivity.class);
            expenseListReportIntent.putExtra(ExpenseListActivity.EXPENSE_LIST_ID, expense.expenseListId);
            startActivity(expenseListReportIntent);
            finish();
        }
    }

}
