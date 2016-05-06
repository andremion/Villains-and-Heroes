package com.andremion.heroes.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.andremion.heroes.R;
import com.andremion.heroes.api.MarvelApi;
import com.andremion.heroes.api.MarvelException;
import com.andremion.heroes.data.DataContract;
import com.andremion.heroes.sync.service.SyncAdapter;
import com.andremion.heroes.sync.util.EntryManager;
import com.andremion.heroes.sync.util.SyncHelper;
import com.andremion.heroes.ui.adapter.CursorAdapter;
import com.andremion.heroes.ui.character.CharacterActivity;
import com.andremion.heroes.ui.home.adapter.CharacterAdapter;
import com.andremion.heroes.ui.search.SearchActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, CharacterAdapter.OnCharacterAdapterInteractionListener {

    private static final String OFFSET = "offset";
    private static final int FETCH_LIMIT = MarvelApi.MAX_FETCH_LIMIT;
    private final BroadcastReceiver mSyncReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(SyncAdapter.EXTRA_ERROR)) {
                Exception e = (Exception) intent.getExtras().get(SyncAdapter.EXTRA_ERROR);
                String msg = null;
                if (e instanceof IOException) {
                    msg = getString(R.string.connection_error);
                } else if (e instanceof MarvelException) {
                    msg = getString(R.string.server_error);
                }
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    private SharedPreferences mPrefs;
    private LocalBroadcastManager mBroadcastManager;
    private CharacterAdapter mCharacterAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        mBroadcastManager.registerReceiver(mSyncReceiver, new IntentFilter(SyncAdapter.ACTION_SYNC_STATUS));

        if (savedInstanceState == null && !SyncHelper.isInternetConnected(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(R.string.connection_info)
                    .setPositiveButton(android.R.string.ok, null);
            builder.create().show();
        }
        SyncHelper.initializeSync(this);
        SyncHelper.syncImmediately(this);

        mCharacterAdapter = new CharacterAdapter(this);

        RecyclerView charactersView = (RecyclerView) findViewById(R.id.characters);
        assert charactersView != null;
        charactersView.setLayoutManager(new LinearLayoutManager(this));
        charactersView.setAdapter(mCharacterAdapter);

        getSupportLoaderManager().initLoader(0, new Bundle(), this);
    }

    @Override
    protected void onDestroy() {
        mBroadcastManager.unregisterReceiver(mSyncReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_search == item.getItemId()) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int offset = args.getInt(OFFSET, 0);
        return new CursorLoader(this,
                DataContract.Character.buildUri(offset, FETCH_LIMIT),
                null,
                null,
                null,
                DataContract.Character.COLUMN_NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Add empty row to 'load more' feature
        if (data != null) {
            int total = mPrefs.getInt(EntryManager.Keys.getCharactersTotalKey(), 0);
            int count = data.getCount();
            if (count == 0 || count < total) {
                String[] columnNames = new String[data.getColumnCount()];
                for (int i = 0; i < columnNames.length; i++) {
                    columnNames[i] = data.getColumnName(i);
                }
                MatrixCursor matrixCursor = new MatrixCursor(columnNames);
                matrixCursor.newRow().add(0);
                data = new MergeCursor(new Cursor[]{data, matrixCursor});
            }
        }

        mCharacterAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCharacterAdapter.swapCursor(null);
    }

    @Override
    public void onLoadMoreItems(final int offset) {
        Bundle args = new Bundle();
        args.putInt(OFFSET, offset);
        getSupportLoaderManager().restartLoader(0, args, MainActivity.this);
    }

    @Override
    public void onItemClick(CursorAdapter adapter, View view, int position) {

        Cursor data = adapter.getItem(position);
        View imageView = view.findViewById(R.id.image);

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(MainActivity.this,
                        imageView, ViewCompat.getTransitionName(imageView));
        Intent intent = new Intent(this, CharacterActivity.class);
        intent.putExtra(CharacterActivity.EXTRA_ID, data.getLong(data.getColumnIndex(DataContract.Character._ID)));

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

}