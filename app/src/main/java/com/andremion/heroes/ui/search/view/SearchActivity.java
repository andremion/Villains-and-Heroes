package com.andremion.heroes.ui.search.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;

import com.andremion.heroes.R;
import com.andremion.heroes.api.data.CharacterVO;
import com.andremion.heroes.databinding.ActivitySearchBinding;
import com.andremion.heroes.databinding.ItemListSearchBinding;
import com.andremion.heroes.ui.adapter.ArrayAdapter;
import com.andremion.heroes.ui.character.view.CharacterActivity;
import com.andremion.heroes.ui.home.view.CharacterAdapter;
import com.andremion.heroes.ui.search.SearchContract;
import com.andremion.heroes.ui.search.SearchPresenter;
import com.andremion.heroes.ui.util.StringUtils;

import java.util.List;

public class SearchActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener, SearchContract.View,
        CharacterAdapter.OnItemClickListener<CharacterVO, CharacterAdapter.ViewHolder, ItemListSearchBinding> {

    private ActivitySearchBinding mBinding;
    private CharacterAdapter mSearchAdapter;
    private SearchPresenter mPresenter;

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

        mBinding.recycler.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycler.setHasFixedSize(true);
        mBinding.recycler.setAdapter(mSearchAdapter = new CharacterAdapter(R.layout.item_list_search, this, null));

        if (savedInstanceState == null) {
            mPresenter = new SearchPresenter();
        } else {
            mPresenter = (SearchPresenter) getLastCustomNonConfigurationInstance();
        }
        mPresenter.attachView(this);
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        //noinspection ConstantConditions
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText)) {
            mPresenter.searchCharacters(newText);
        } else {
            mPresenter.loadCharacters();
        }
        return true;
    }

    @Override
    public boolean onClose() {
        String query = mBinding.search.getQuery().toString();
        mPresenter.searchCharacters(query);
        return true;
    }

    @Override
    public void onItemClick(ArrayAdapter<CharacterVO, CharacterAdapter.ViewHolder> adapter, ItemListSearchBinding binding, int position) {
        mPresenter.characterClick(binding.image, adapter.getItem(position));
    }

    @Override
    public void showProgress() {
        mSearchAdapter.setHasMore(true);
    }

    @Override
    public void stopProgress() {
        mSearchAdapter.setHasMore(false);
    }

    @Override
    public void showResult(List<CharacterVO> entries) {
        mSearchAdapter.setItems(entries);
    }

    @Override
    public void showError(Throwable e) {
        Snackbar.make(mBinding.toolbar, StringUtils.getApiErrorMessage(this, e), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void openCharacter(@NonNull View heroView, @NonNull CharacterVO character) {
        CharacterActivity.start(this, heroView, character);
    }

}
