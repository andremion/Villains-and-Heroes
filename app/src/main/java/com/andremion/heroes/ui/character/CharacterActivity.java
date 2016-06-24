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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.andremion.heroes.BR;
import com.andremion.heroes.R;
import com.andremion.heroes.data.DataContract.Character;
import com.andremion.heroes.data.DataContract.Section;
import com.andremion.heroes.data.binding.CharacterWrapper;
import com.andremion.heroes.databinding.ActivityCharacterBinding;
import com.andremion.heroes.sync.service.SyncAdapter;
import com.andremion.heroes.sync.util.SyncHelper;
import com.andremion.heroes.ui.adapter.CursorAdapter;
import com.andremion.heroes.ui.search.SearchActivity;
import com.andremion.heroes.ui.section.SectionActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

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
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sections, seriesFragment)
                    .add(R.id.sections, comicFragment)
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

        String name = data.getString(data.getColumnIndex(Character.COLUMN_NAME));
        String image = data.getString(data.getColumnIndex(Character.COLUMN_IMAGE));
        String description = data.getString(data.getColumnIndex(Character.COLUMN_DESCRIPTION));
        String detail = data.getString(data.getColumnIndex(Character.COLUMN_DETAIL));
        String wiki = data.getString(data.getColumnIndex(Character.COLUMN_WIKI));
        String comiclink = data.getString(data.getColumnIndex(Character.COLUMN_COMIC_LINK));

        mBinding.setVariable(BR.character, CharacterWrapper.wrap(data));
        mBinding.toolbarLayout.setTitle(name);
        Glide.with(this)
                .load(image)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .error(R.mipmap.ic_launcher)
                .into(mBinding.image);

        if (!TextUtils.isEmpty(description)) {
            mBinding.description.setText(description);
        } else {
            mBinding.description.setText(R.string.unavailable_data);
        }

        if (!TextUtils.isEmpty(detail)) {
            mBinding.linkDetail.setTag(detail);
            mBinding.linkDetail.setVisibility(View.VISIBLE);
        } else {
            mBinding.linkDetail.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(wiki)) {
            mBinding.linkWiki.setTag(wiki);
            mBinding.linkWiki.setVisibility(View.VISIBLE);
        } else {
            mBinding.linkWiki.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(comiclink)) {
            mBinding.linkComiclink.setTag(comiclink);
            mBinding.linkComiclink.setVisibility(View.VISIBLE);
        } else {
            mBinding.linkComiclink.setVisibility(View.GONE);
        }

        // Set the attribution text
        //noinspection ConstantConditions
        mBinding.attribution.setText(mPrefs.getString(SyncAdapter.KEY_ATTRIBUTION, null));

        mBinding.executePendingBindings();
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
    public void onItemClick(CursorAdapter adapter, View view, int position, String type) {

        Cursor data = adapter.getItem(position);
        View imageView = view.findViewById(R.id.image);

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this,
                        imageView, ViewCompat.getTransitionName(imageView
                        ));
        Intent intent = new Intent(this, SectionActivity.class);
        intent.putExtra(SectionActivity.EXTRA_TYPE, type);
        intent.putExtra(SectionActivity.EXTRA_CHARACTER, data.getLong(data.getColumnIndex(Section.COLUMN_CHARACTER)));
        intent.putExtra(SectionActivity.EXTRA_POSITION, position);

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

}
