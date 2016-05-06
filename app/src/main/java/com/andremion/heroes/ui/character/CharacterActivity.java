package com.andremion.heroes.ui.character;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andremion.heroes.R;
import com.andremion.heroes.data.DataContract.Character;
import com.andremion.heroes.data.DataContract.Section;
import com.andremion.heroes.sync.util.SyncHelper;
import com.andremion.heroes.ui.adapter.CursorAdapter;
import com.andremion.heroes.ui.search.SearchActivity;
import com.andremion.heroes.ui.section.SectionActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

public class CharacterActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, SectionFragment.OnFragmentInteractionListener {

    public static final String EXTRA_ID = CharacterActivity.class.getPackage().getName() + ".extra." + Character._ID;

    private CollapsingToolbarLayout mToolbarLayout;
    private ImageView mImageView;
    private TextView mDescriptionView;
    private TextView mDetailView;
    private TextView mWikiView;
    private TextView mComicLinkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mImageView = (ImageView) findViewById(R.id.image);
        mDescriptionView = (TextView) findViewById(R.id.description);
        mDetailView = (TextView) findViewById(R.id.link_detail);
        mWikiView = (TextView) findViewById(R.id.link_wiki);
        mComicLinkView = (TextView) findViewById(R.id.link_comiclink);

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

        mToolbarLayout.setTitle(name);
        Glide.with(this)
                .load(image)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .error(R.mipmap.ic_launcher)
                .into(mImageView);

        if (!TextUtils.isEmpty(description)) {
            mDescriptionView.setText(description);
        } else {
            mDescriptionView.setText(R.string.unavailable_data);
        }

        if (!TextUtils.isEmpty(detail)) {
            mDetailView.setTag(detail);
            mDetailView.setVisibility(View.VISIBLE);
        } else {
            mDetailView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(wiki)) {
            mWikiView.setTag(wiki);
            mWikiView.setVisibility(View.VISIBLE);
        } else {
            mWikiView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(comiclink)) {
            mComicLinkView.setTag(comiclink);
            mComicLinkView.setVisibility(View.VISIBLE);
        } else {
            mComicLinkView.setVisibility(View.GONE);
        }

        supportStartPostponedEnterTransition();
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
