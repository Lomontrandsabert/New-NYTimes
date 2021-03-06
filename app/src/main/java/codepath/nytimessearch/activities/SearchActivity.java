package codepath.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codepath.nytimessearch.Article;
import codepath.nytimessearch.ArticleArrayAdapter;
import codepath.nytimessearch.R;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;

    String BeginDate;
    String Sort;
    String NewsDeskValues;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Settings");
        setupViews();

        if(getIntent().getStringExtra("NewsDeskValues")!=null){
            NewsDeskValues= getIntent().getStringExtra("NewsDeskValues");
        }
    }

    public void setupViews(){
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        // hook up listener for grid click
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create an intent to display the article
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                // get the article to display
                Article article = articles.get(position);
                // pass in that article into intent
                i.putExtra("article", article);
                // launch the activity
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
                    startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();
        //Toast.makeText(this, "Searching for" +query, Toast.LENGTH_LONG).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", "2f3ce602906b412086d48c15dfe30d1d");
        params.put("page", 0);
        params.put("q", query);

        if(getIntent().getStringExtra("DeskValues")!= null ){
            String DeskValues=getIntent().getStringExtra("DeskValues");
            Toast.makeText(this,"DeskValues is : " +DeskValues, Toast.LENGTH_LONG).show();
            params.put("fq", DeskValues);
        }

        if(getIntent().getStringExtra("BeginDate")!= null ){
            BeginDate=getIntent().getStringExtra("BeginDate");
            Toast.makeText(this,"Begin date search: " +BeginDate, Toast.LENGTH_LONG).show();
            params.put("begin_date", BeginDate);
        }

        if(getIntent().getStringExtra("Sort") != null){
            Sort=getIntent().getStringExtra("Sort");
            Toast.makeText(this,"Sort search: " +Sort, Toast.LENGTH_LONG).show();
            params.put("sort", Sort);
        }

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
                    Log.d("DEBUG", articles.toString());

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
