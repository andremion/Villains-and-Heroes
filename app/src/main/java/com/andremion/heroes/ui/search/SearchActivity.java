package com.andremion.heroes.ui.search;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;

import com.andremion.heroes.R;
import com.andremion.heroes.api.MarvelApi;
import com.andremion.heroes.data.DataContract.Character;
import com.andremion.heroes.databinding.ActivitySearchBinding;
import com.andremion.heroes.ui.adapter.CursorAdapter;
import com.andremion.heroes.ui.character.CharacterActivity;
import com.andremion.heroes.ui.search.adapter.SearchAdapter;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        LoaderManager.LoaderCallbacks<Cursor>, CursorAdapter.OnAdapterInteractionListener, SearchView.OnCloseListener {

    private static final String ARG_QUERY = "query";

    private ActivitySearchBinding mBinding;
    private SearchAdapter mSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        setSupportActionBar(mBinding.toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBinding.search.setIconified(false);
        mBinding.search.setOnQueryTextListener(this);
        mBinding.search.setOnCloseListener(this);
        onClose();

        mSearchAdapter = new SearchAdapter(this);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycler.setHasFixedSize(true);
        mBinding.recycler.setAdapter(mSearchAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, newText);
        getSupportLoaderManager().restartLoader(0, args, this);
        return true;
    }

    @Override
    public boolean onClose() {
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, mBinding.search.getQuery().toString());
        getSupportLoaderManager().restartLoader(0, args, this);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri.Builder uriBuilder = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(getString(R.string.data_provider_authority));

        uriBuilder.appendPath(Character.URI_PATH);

        uriBuilder.appendPath(SearchManager.SUGGEST_URI_PATH_QUERY);

        String query = args.getString(ARG_QUERY);
        if (!TextUtils.isEmpty(query)) {
            uriBuilder.appendPath(query);
        }

        uriBuilder.appendQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT, String.valueOf(MarvelApi.MAX_FETCH_LIMIT));

        Uri uri = uriBuilder.build();

        return new CursorLoader(this,
                uri,
                null,
                null,
                null,
                Character.COLUMN_NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSearchAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSearchAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(CursorAdapter adapter, View view, int position) {

        Cursor data = adapter.getItem(position);
        View imageView = view.findViewById(R.id.image);

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(SearchActivity.this,
                        imageView, ViewCompat.getTransitionName(imageView));
        Intent intent = new Intent(this, CharacterActivity.class);
        intent.putExtra(CharacterActivity.EXTRA_ID, data.getLong(data.getColumnIndex(BaseColumns._ID)));

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
}
