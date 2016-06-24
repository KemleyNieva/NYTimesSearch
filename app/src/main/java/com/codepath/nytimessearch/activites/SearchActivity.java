package com.codepath.nytimessearch.activites;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.nytimessearch.Article;
import com.codepath.nytimessearch.ArticleAdapter;
import com.codepath.nytimessearch.ArticleArrayAdapter;
import com.codepath.nytimessearch.EndlessRecyclerViewScrollListener;
import com.codepath.nytimessearch.ItemClickSupport;
import com.codepath.nytimessearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {


    @BindView(R.id.rvArticles) RecyclerView rvArticles;
    ArrayList<Article> articles;
    ArticleAdapter adapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();

    }
    public void setupViews() {

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        articles = new ArrayList<>();
        adapter = new ArticleAdapter(this, articles);

        rvArticles.setAdapter(adapter);
        rvArticles.setLayoutManager(staggeredGridLayoutManager);

        onArticleSearch();


        rvArticles.clearOnScrollListeners();
        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadpage(page);
                adapter.notifyDataSetChanged();
            }
        });

        ItemClickSupport.addTo(rvArticles).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //create on intent to display the article
                Intent i = new Intent(getApplicationContext(),ArticleActivity.class);
                //get article to display
                Article article =articles.get(position);
                //pass
                i.putExtra("article", Parcels.wrap(article));
                //launch
                startActivity(i);
            }
        });
    }

    private void loadpage(int page) {
        if(page == 0){
            adapter.clearAll();
            rvArticles.clearOnScrollListeners();
            rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    loadpage(page);
                    adapter.notifyDataSetChanged();
                }
            });
            adapter.notifyDataSetChanged();
        }
        //String query = etQuery.getText().toString();
        //Toast.makeText(this,"Searching for" + query, Toast.LENGTH_LONG).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url ="http://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key","227c750bb7714fc39ef1559ef1bd8329");
        params.put("page",page);
        //params.put("q",query);

        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("a_response_in_loadpage", response.toString());
                JSONArray articleJsonResults = null;
                try{
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyItemInserted(0);
                    //Log.d("SearchActivity",articles.toString());

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("SearchActivity", "onFailure loadpage");
            }
        });

    }

    public void customLoadMoreDataFromApi(int offset) {
        // Send an API request to retrieve appropriate data using the offset value as a parameter.
        // Deserialize API response and then construct new objects to append to the adapter
        // Add the new objects to the data source for the adapter
        articles.addAll(articles);
        // For efficiency purposes, notify the adapter of only the elements that got changed
        // curSize will equal to the index of the first element inserted because the list is 0-indexed
        int curSize = adapter.getItemCount();
        adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                //Toast.makeText(this,"Searching for" + query, Toast.LENGTH_LONG).show();
                AsyncHttpClient client = new AsyncHttpClient();
                String url ="http://api.nytimes.com/svc/search/v2/articlesearch.json";

                RequestParams params = new RequestParams();
                params.put("api-key","227c750bb7714fc39ef1559ef1bd8329");
                params.put("page",0);
                params.put("q",query);

                client.get(url, params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //Log.d("a_response_in_onCreate  ", response.toString());
                        JSONArray articleJsonResults = null;
                        try{
                            articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                            articles.addAll(Article.fromJSONArray(articleJsonResults));
                            adapter.notifyItemInserted(0);
                            //Log.d("SearchActivity",articles.toString());

                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("SearchActivity", "onFailure onCreateOptions");
                    }
                });
               //adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch() {
        //String query = etQuery.getText().toString();

        //Toast.makeText(this,"Searching for" + query, Toast.LENGTH_LONG).show();
        AsyncHttpClient client = new AsyncHttpClient();
        //String url ="http://api.nytimes.com/svc/search/v2/articlesearch.json";
        String url = "https://api.nytimes.com/svc/topstories/v2/home.json";

        RequestParams params = new RequestParams();
        params.put("api-key","227c750bb7714fc39ef1559ef1bd8329");
        params.put("page",0);
        //params.put("q",query);

        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG   ", response.toString());
                JSONArray articleJsonResults = null;
                try{
                    articleJsonResults = response.getJSONArray("results");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    //Log.d("SearchActivity",articles.toString());

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("SearchActivity", "onFailure in ArticleSearch");
            }
        });
        //adapter.notifyDataSetChanged();
    }
}
