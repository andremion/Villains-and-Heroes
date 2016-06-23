package com.andremion.heroes.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.andremion.heroes.R;
import com.andremion.heroes.api.MarvelApi;
import com.andremion.heroes.api.MarvelException;
import com.andremion.heroes.data.DataContract;
import com.andremion.heroes.databinding.ActivityMainBinding;
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

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String OFFSET = "offset";
    private static final String KEY_INITIAL_INFO = MainActivity.class.getSimpleName() + ".INITIAL_INFO";
    private static final int FETCH_LIMIT = MarvelApi.MAX_FETCH_LIMIT;

    private final BroadcastReceiver mSyncReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(SyncAdapter.EXTRA_ERROR)) {
                Exception error = (Exception) intent.getExtras().get(SyncAdapter.EXTRA_ERROR);
                String msg = null;
                if (error instanceof IOException) {
                    msg = getString(R.string.connection_error);
                } else if (error instanceof MarvelException) {
                    msg = getString(R.string.server_error);
                }

                boolean showError = !TextUtils.isEmpty(msg) && mCharacterAdapter.getItemCount() <= 1;

                // Cancel any on-going animation
                ViewCompat.animate(mBinding.error).cancel();
                ViewCompat.animate(mBinding.recycler).cancel();

                if (showError) {
                    Log.d(LOG_TAG, msg);

                    mBinding.error.setText(msg);
                    mBinding.error.setVisibility(View.VISIBLE);

                    ViewCompat.setAlpha(mBinding.error, 0f);
                    ViewCompat.animate(mBinding.error).alpha(1f).start();
                    ViewCompat.animate(mBinding.recycler)
                            .alpha(0f)
                            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(View view) {
                                    view.setVisibility(View.INVISIBLE);
                                }
                            }).start();
                } else {
                    mBinding.recycler.setVisibility(View.VISIBLE);

                    ViewCompat.animate(mBinding.error)
                            .alpha(0f)
                            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(View view) {
                                    view.setVisibility(View.INVISIBLE);
                                }
                            }).start();
                    ViewCompat.setAlpha(mBinding.recycler, 0f);
                    ViewCompat.animate(mBinding.recycler).alpha(1f).start();
                }
            }
        }
    };

    private SharedPreferences mPrefs;
    private LocalBroadcastManager mBroadcastManager;
    private CharacterAdapter mCharacterAdapter;
    private ActivityMainBinding mBinding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBinding.toolbar);

        mCharacterAdapter = new CharacterAdapter(this);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycler.setAdapter(mCharacterAdapter);

        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        mBroadcastManager.registerReceiver(mSyncReceiver, new IntentFilter(SyncAdapter.ACTION_SYNC_STATUS));

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (savedInstanceState == null && !mPrefs.getBoolean(KEY_INITIAL_INFO, false)) {
            mPrefs.edit().putBoolean(KEY_INITIAL_INFO, true).apply();
            showInfoDialog();
        }

        SyncHelper.initializeSync(this);
        SyncHelper.syncImmediately(this);
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

        // Set the attribution text
        //noinspection ConstantConditions
        getSupportActionBar().setSubtitle(mPrefs.getString(SyncAdapter.KEY_ATTRIBUTION, null));

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

    private void showInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(R.string.initial_info)
                .setPositiveButton(android.R.string.ok, null);
        builder.create().show();
    }

}