package com.andremion.heroes.ui.character;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.andremion.heroes.R;
import com.andremion.heroes.data.DataContract.Character;
import com.andremion.heroes.data.DataContract.Section;
import com.andremion.heroes.databinding.ActivityCharacterBinding;
import com.andremion.heroes.databinding.ItemListSectionBinding;
import com.andremion.heroes.sync.service.SyncAdapter;
import com.andremion.heroes.sync.util.SyncHelper;
import com.andremion.heroes.ui.adapter.CursorAdapter;
import com.andremion.heroes.ui.binding.CharacterWrapper;
import com.andremion.heroes.ui.search.SearchActivity;
import com.andremion.heroes.ui.section.SectionActivity;

public class CharacterActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, SectionFragment.OnFragmentInteractionListener {

    public static final String EXTRA_ID = CharacterActivity.class.getPackage().getName() + ".extra." + Character._ID;

    private SharedPreferences mPrefs;

    private ActivityCharacterBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_character);
        setSupportActionBar(mBinding.toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        long characterId = getIntent().getExtras().getLong(EXTRA_ID);

        if (savedInstanceState == null) {
            String comicLabel = getString(R.string.label_comics);
            Fragment comicFragment = SectionFragment.newInstance(Section.TYPE_COMIC, characterId, comicLabel);
            String seriesLabel = getString(R.string.label_series);
            Fragment seriesFragment = SectionFragment.newInstance(Section.TYPE_SERIES, characterId, seriesLabel);
            String storiesLabel = getString(R.string.label_stories);
            Fragment storiesFragment = SectionFragment.newInstance(Section.TYPE_STORIES, characterId, storiesLabel);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sections, comicFragment)
                    .add(R.id.sections, seriesFragment)
                    .add(R.id.sections, storiesFragment)
                    .commit();
        }

        Bundle args = new Bundle();
        args.putLong(Character._ID, characterId);
        getSupportLoaderManager().initLoader(0, args, this);

        // Sync to load sections data
        Bundle extras = new Bundle();
        extras.putLong(Section.COLUMN_CHARACTER, characterId);
        SyncHelper.syncImmediately(this, extras);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_character, menu);
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
        return new CursorLoader(this,
                Character.buildUri(args.getLong(Character._ID)),
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data == null || !data.moveToFirst()) {
            return;
        }

        mBinding.setCharacter(CharacterWrapper.wrap(data));
        mBinding.setAttribution(mPrefs.getString(SyncAdapter.KEY_ATTRIBUTION, null));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public void onLinkClick(View v) {
        String url = (String) v.getTag();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onItemClick(CursorAdapter adapter, ItemListSectionBinding binding, int position, String type) {

        Cursor data = adapter.getItem(position);

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this,
                        binding.image, ViewCompat.getTransitionName(binding.image));
        Intent intent = new Intent(this, SectionActivity.class);
        intent.putExtra(SectionActivity.EXTRA_TYPE, type);
        intent.putExtra(SectionActivity.EXTRA_CHARACTER, data.getLong(data.getColumnIndex(Section.COLUMN_CHARACTER)));
        intent.putExtra(SectionActivity.EXTRA_POSITION, position);

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

}
