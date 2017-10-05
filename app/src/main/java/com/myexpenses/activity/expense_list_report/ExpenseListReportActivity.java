package com.myexpenses.activity.expense_list_report;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.myexpenses.activity.create_category.CreateCategoryActivity;
import com.myexpenses.activity.expense_details.ExpenseDetailsActivity;
import com.myexpenses.R;
import com.myexpenses.activity.save_expense.SaveExpenseActivity;
import com.myexpenses.activity.expense_list.ExpenseListActivity;
import com.myexpenses.model.Expense;
import com.myexpenses.model.ExpenseListReport;
import com.myexpenses.service.http.MyExpensesApiClient;
import com.myexpenses.service.http.Response;
import com.myexpenses.service.http.SomethingWentWrongResponse;
import com.myexpenses.service.transformer.ExpenseListReportTransformer;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ExpenseListReportActivity extends AppCompatActivity implements ExpensesAdapter.ExpenseListReportClickListener {

    public static final String EXPENSE_PAYLOAD = "EXPENSE_PAYLOAD";
    public static final String EXPENSE_ID = "EXPENSE_ID";
    private ListView summary;
    private RecyclerView view;
    private String expenseListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_list_report_main);
        expenseListId = getIntent().getStringExtra(ExpenseListActivity.EXPENSE_LIST_ID);
        requestExpenseListReportData();
        setExpensesView();
        setSummaryView();
        setAddExpenseButton();
    }

    private void setAddExpenseButton() {
        FloatingActionButton addExpenseButton = (FloatingActionButton) findViewById(R.id.addExpenseButton);
        addExpenseButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            Intent addExpenseActivityIntent = new Intent(getBaseContext(), SaveExpenseActivity.class);
            addExpenseActivityIntent.putExtra(ExpenseListActivity.EXPENSE_LIST_ID, expenseListId);
            startActivity(addExpenseActivityIntent);
            }
        });
    }

    private void setSummaryView() {
        summary = (ListView) findViewById(R.id.summaries_list);
        summary.setAdapter(new SummaryAdapter());
    }

    private void setExpensesView() {
        view = (RecyclerView) findViewById(R.id.expense_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        view.setLayoutManager(layoutManager);
        view.setAdapter(new ExpensesAdapter(this));
        view.addItemDecoration(
            new DividerItemDecoration(
                view.getContext(),
                layoutManager.getOrientation()
            )
        );
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_expense_list_report, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.createCategory) {
            Intent expenseDetailsActivityIntent = new Intent(this, CreateCategoryActivity.class);
            expenseDetailsActivityIntent.putExtra(ExpenseListActivity.EXPENSE_LIST_ID, expenseListId);
            startActivity(expenseDetailsActivityIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestExpenseListReportData() {
        new GetExpenseListReportTask().execute(expenseListId);
    }

    private void setReportAdaptersDataFromJson(Response response) throws JSONException {
        JSONObject payload = new JSONObject(new JSONTokener(response.message));
        ExpenseListReport report = new ExpenseListReportTransformer().fromJson(payload);
        ((ExpensesAdapter) view.getAdapter()).setData(report.expenses);
        ((SummaryAdapter) summary.getAdapter()).setData(report.summary);
    }

    public void onItemClick(Expense anExpense) {
        Intent expenseDetailsActivityIntent = new Intent(this, ExpenseDetailsActivity.class);
        expenseDetailsActivityIntent.putExtra(EXPENSE_ID, anExpense.expenseId);
        startActivity(expenseDetailsActivityIntent);
    }

    private class GetExpenseListReportTask extends AsyncTask<String, Void, Response> {

        public Response doInBackground(String... ids) {
            try {
                return new MyExpensesApiClient().getAnExpenseListReport(ids[0]);
            } catch (IOException e) {
                Log.e(getClass().toString(), e.toString());

                return new SomethingWentWrongResponse();
            }
        }

        protected void onPostExecute(Response response) {
            if (response.code == HttpURLConnection.HTTP_OK) {
                try {
                    setReportAdaptersDataFromJson(response);
                } catch (JSONException e) {
                    Log.e(getClass().toString(), e.toString());
                }
            } else {
                Toast.makeText(getBaseContext(), response.message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
