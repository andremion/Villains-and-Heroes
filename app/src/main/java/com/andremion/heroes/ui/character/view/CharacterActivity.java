package com.andremion.heroes.ui.character.view;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.andremion.heroes.R;
import com.andremion.heroes.api.data.CharacterVO;
import com.andremion.heroes.api.data.SectionVO;
import com.andremion.heroes.databinding.ActivityCharacterBinding;
import com.andremion.heroes.databinding.ItemListSectionBinding;
import com.andremion.heroes.ui.adapter.ArrayAdapter;
import com.andremion.heroes.ui.character.CharacterContract;
import com.andremion.heroes.ui.character.CharacterPresenter;
import com.andremion.heroes.ui.search.view.SearchActivity;
import com.andremion.heroes.ui.section.view.SectionActivity;

import java.util.List;

public class CharacterActivity extends AppCompatActivity implements CharacterContract.View,
        ArrayAdapter.OnItemClickListener<SectionVO, SectionAdapter.ViewHolder, ItemListSectionBinding> {

    public static final String EXTRA_CHARACTER = CharacterActivity.class.getPackage().getName() + ".extra.CHARACTER";

    public static void start(@NonNull Activity activity, @NonNull View characterView, @NonNull CharacterVO character) {

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity,
                        characterView, ViewCompat.getTransitionName(characterView));
        Intent intent = new Intent(activity, CharacterActivity.class);
        intent.putExtra(EXTRA_CHARACTER, character);

        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public static PendingIntent getPendingIntent(@NonNull Context context, @NonNull CharacterVO character, int id) {

        Intent intent = new Intent(context, CharacterActivity.class);
        intent.setAction(Integer.toString(id)); // Used to update all PendingIntent extras data for each widget
        intent.putExtra(EXTRA_CHARACTER, character);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent); // Return to MainActivity

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private ActivityCharacterBinding mBinding;
    private CharacterPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_character);
        setSupportActionBar(mBinding.toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupSectionView(mBinding.recyclerComics, SectionVO.TYPE_COMIC);
        setupSectionView(mBinding.recyclerSeries, SectionVO.TYPE_SERIES);
        setupSectionView(mBinding.recyclerStories, SectionVO.TYPE_STORY);
        setupSectionView(mBinding.recyclerEvents, SectionVO.TYPE_EVENT);

        CharacterVO character = (CharacterVO) getIntent().getExtras().get(EXTRA_CHARACTER);
        assert character != null;

        if (savedInstanceState == null) {
            mPresenter = new CharacterPresenter(character);
        } else {
            mPresenter = (CharacterPresenter) getLastCustomNonConfigurationInstance();
        }
        mPresenter.attachView(this);

        mBinding.setPresenter(mPresenter);
    }

    private void setupSectionView(RecyclerView recyclerView, @SectionVO.Type int type) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        SectionAdapter adapter = new SectionAdapter(this, type, this);
        recyclerView.setAdapter(adapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        //noinspection ConstantConditions
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mPresenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_character, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_search == item.getItemId()) {
            mPresenter.searchClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {

        int type = SectionActivity.getType(resultCode, data);
        int position = SectionActivity.getPosition(resultCode, data);

        final RecyclerView recyclerView;
        switch (type) {
            case SectionVO.TYPE_COMIC:
                recyclerView = mBinding.recyclerComics;
                break;
            case SectionVO.TYPE_SERIES:
                recyclerView = mBinding.recyclerSeries;
                break;
            case SectionVO.TYPE_STORY:
                recyclerView = mBinding.recyclerStories;
                break;
            case SectionVO.TYPE_EVENT:
                recyclerView = mBinding.recyclerEvents;
                break;
            default:
                recyclerView = null;
        }

        if (position != SectionActivity.EXTRA_NOT_FOUND && recyclerView != null
                && recyclerView.getAdapter().getItemCount() > 0
                && recyclerView.findViewHolderForAdapterPosition(position) == null) {

            recyclerView.scrollToPosition(position);

            supportPostponeEnterTransition();

            recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                    supportStartPostponedEnterTransition();
                    return true;
                }
            });
        }

    }

    @Override
    public void onItemClick(ArrayAdapter<SectionVO, SectionAdapter.ViewHolder> adapter, ItemListSectionBinding binding, int position) {
        SectionAdapter sectionAdapter = (SectionAdapter) adapter;
        mPresenter.sectionClick(sectionAdapter.getType(), binding.image, adapter.getItems(), position);
    }

    @Override
    public void showAttribution(String attribution) {
        mBinding.setAttribution(attribution);
    }

    @Override
    public void showCharacter(@NonNull CharacterVO character) {
        mBinding.setCharacter(character);
    }

    @Override
    public void showComics(@NonNull List<SectionVO> entries) {
        SectionAdapter adapter = (SectionAdapter) mBinding.recyclerComics.getAdapter();
        adapter.setItems(entries);
    }

    @Override
    public void showSeries(@NonNull List<SectionVO> entries) {
        SectionAdapter adapter = (SectionAdapter) mBinding.recyclerSeries.getAdapter();
        adapter.setItems(entries);
    }

    @Override
    public void showStories(@NonNull List<SectionVO> entries) {
        SectionAdapter adapter = (SectionAdapter) mBinding.recyclerStories.getAdapter();
        adapter.setItems(entries);
    }

    @Override
    public void showEvents(@NonNull List<SectionVO> entries) {
        SectionAdapter adapter = (SectionAdapter) mBinding.recyclerEvents.getAdapter();
        adapter.setItems(entries);
    }

    @Override
    public void showError(@NonNull Throwable e) {
        Snackbar.make(mBinding.toolbarLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void openSection(int type, @NonNull View heroView, String attribution, @NonNull List<SectionVO> entries, int position) {
        SectionActivity.start(this, type, heroView, attribution, entries, position);
    }

    @Override
    public void openLink(@NonNull String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void openSearch() {
        startActivity(new Intent(this, SearchActivity.class));
    }
}
