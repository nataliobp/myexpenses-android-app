package com.myexpenses.activity.expense_list;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.myexpenses.activity.expense_list_report.ExpenseListReportActivity;
import com.myexpenses.R;
import com.myexpenses.activity.create_expense_list.CreateExpenseListActivity;
import com.myexpenses.model.ExpenseList;
import com.myexpenses.service.http.MyExpensesApiClient;
import com.myexpenses.service.http.Response;
import com.myexpenses.service.http.SomethingWentWrongResponse;
import com.myexpenses.service.transformer.ExpenseListTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ExpenseListActivity extends AppCompatActivity implements ExpenseListAdapter.ExpenseListClickListener{

    private ListView expenseListList;
    public static final String EXPENSE_LIST_ID = "EXPENSE_LIST_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);
        setAddExpenseListButton();
        expenseListList = (ListView) findViewById(R.id.expense_list_list);
        expenseListList.setAdapter(new ExpenseListAdapter(this));
        new GetExpenseListsTask().execute();
    }

    @Override
    public void onItemClick(ExpenseList anExpenseList) {
        Intent expenseListReportActivityIntent = new Intent(getBaseContext(), ExpenseListReportActivity.class);
        expenseListReportActivityIntent.putExtra(EXPENSE_LIST_ID, anExpenseList.expenseListId);
        startActivity(expenseListReportActivityIntent);
    }

    private void setAddExpenseListButton() {
        FloatingActionButton addExpenseListButton = (FloatingActionButton) findViewById(R.id.addExpenseListButton);
        addExpenseListButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent createExpenseListActivityIntent = new Intent(getBaseContext(), CreateExpenseListActivity.class);
                startActivity(createExpenseListActivityIntent);
                finish();
            }
        });
    }

    private class GetExpenseListsTask extends AsyncTask<Void, Void, Response> {

        public Response doInBackground(Void ...voids) {
            try {
                return new MyExpensesApiClient().getExpenseLists();
            } catch (IOException e) {
                Log.e(getClass().toString(), e.toString());

                return new SomethingWentWrongResponse(e.toString());
            }
        }

        protected void onPostExecute(Response response) {
            if (response.code == HttpURLConnection.HTTP_OK) {
                try {
                    JSONArray payload = new JSONArray(new JSONTokener(response.message));

                    ExpenseList[] expenseLists = new ExpenseList[payload.length()];
                    for (int i = 0; i < payload.length(); i++) {
                        expenseLists[i] = new ExpenseListTransformer().fromJson(payload.getJSONObject(i));
                    }

                    ((ExpenseListAdapter) expenseListList.getAdapter()).setData(expenseLists);
                } catch (JSONException e) {
                    Log.e(getClass().toString(), e.toString());
                }
            } else {
                Toast.makeText(getBaseContext(), response.message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
