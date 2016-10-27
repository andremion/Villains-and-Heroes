package com.andremion.heroes.ui.character.view;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

    public static void start(Activity activity, View characterView, CharacterVO characterVO) {

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity,
                        characterView, ViewCompat.getTransitionName(characterView));
        Intent intent = new Intent(activity, CharacterActivity.class);
        intent.putExtra(EXTRA_CHARACTER, characterVO);

        ActivityCompat.startActivity(activity, intent, options.toBundle());
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

        setupSectionView(mBinding.recyclerComics);
        setupSectionView(mBinding.recyclerSeries);
        setupSectionView(mBinding.recyclerStories);
        setupSectionView(mBinding.recyclerEvents);

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

    private void setupSectionView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        SectionAdapter adapter = new SectionAdapter(this, this);
        recyclerView.setAdapter(adapter);
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
    public void onItemClick(ArrayAdapter<SectionVO, SectionAdapter.ViewHolder> adapter, ItemListSectionBinding binding, int position) {
        mPresenter.sectionClick(binding.image, adapter.getItems(), position);
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
    public void openSection(@NonNull View heroView, String attribution, @NonNull List<SectionVO> entries, int position) {
        SectionActivity.start(this, heroView, attribution, entries, position);
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
