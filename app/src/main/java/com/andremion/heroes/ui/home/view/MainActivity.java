package com.andremion.heroes.ui.home.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.andremion.heroes.R;
import com.andremion.heroes.api.MarvelApi;
import com.andremion.heroes.api.data.CharacterVO;
import com.andremion.heroes.databinding.ActivityMainBinding;
import com.andremion.heroes.databinding.ItemListCharacterBinding;
import com.andremion.heroes.ui.adapter.ArrayAdapter;
import com.andremion.heroes.ui.character.view.CharacterActivity;
import com.andremion.heroes.ui.home.MainContract;
import com.andremion.heroes.ui.home.MainPresenter;
import com.andremion.heroes.ui.search.view.SearchActivity;
import com.andremion.heroes.ui.util.StringUtils;
import com.andremion.heroes.ui.util.ViewUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MainContract.View,
        CharacterAdapter.OnItemClickListener<CharacterVO, CharacterAdapter.ViewHolder, ItemListCharacterBinding>,
        CharacterAdapter.OnLoadListener {

    private static final String KEY_INITIAL_INFO_SHOWN = MainActivity.class.getSimpleName() + ".INITIAL_INFO_SHOWN";

    private ActivityMainBinding mBinding;
    private SharedPreferences mPreferences;
    private CharacterAdapter mCharacterAdapter;
    private MainPresenter mPresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBinding.toolbar);

        mBinding.recycler.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycler.setHasFixedSize(true);
        mBinding.recycler.setAdapter(mCharacterAdapter = new CharacterAdapter(R.layout.item_list_character, this, this));

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (savedInstanceState == null) {
            mPresenter = new MainPresenter(MarvelApi.getInstance());
        } else {
            mPresenter = (MainPresenter) getLastCustomNonConfigurationInstance();
        }
        mPresenter.attachView(this);
        mPresenter.initScreen();

        mBinding.setPresenter(mPresenter);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onItemClick(ArrayAdapter<CharacterVO, CharacterAdapter.ViewHolder> adapter, ItemListCharacterBinding binding, int position) {
        mPresenter.characterClick(binding.image, adapter.getItem(position));
    }

    @Override
    public void onLoadMore(int offset) {
        mPresenter.loadCharacters(offset);
    }

    @Override
    public boolean showInfoDialog() {

        if (!mPreferences.getBoolean(KEY_INITIAL_INFO_SHOWN, false)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(R.string.initial_info)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPreferences.edit().putBoolean(KEY_INITIAL_INFO_SHOWN, true).apply();
                            mPresenter.loadCharacters(0);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            builder.create().show();
            return true;
        }

        return false;
    }

    @Override
    public void showProgress() {
        mCharacterAdapter.setLoading(true);
        mCharacterAdapter.setHasMore(true);
    }

    @Override
    public void stopProgress(boolean hasMore) {
        mCharacterAdapter.setLoading(false);
        mCharacterAdapter.setHasMore(hasMore);
        mBinding.swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showAttribution(String attribution) {
        //noinspection ConstantConditions
        getSupportActionBar().setSubtitle(attribution);
    }

    @Override
    public void showResult(@NonNull List<CharacterVO> entries) {
        mCharacterAdapter.setItems(entries);

        if (mBinding.error.isShown()) {
            mBinding.error.setText(null);
            ViewUtils.fadeView(mBinding.recycler, true, true);
            ViewUtils.fadeView(mBinding.error, false, true);
        }
    }

    @Override
    public void showError(@NonNull Throwable e) {
        String msg = StringUtils.getApiErrorMessage(this, e);
        if (mCharacterAdapter.getItemCount() > 1) { // If user already has items shown
            Snackbar.make(mBinding.recycler, msg, Snackbar.LENGTH_LONG).show();
        } else {
            boolean animate = !TextUtils.equals(mBinding.error.getText(), msg);
            boolean showError = !TextUtils.isEmpty(msg);
            mBinding.error.setText(msg);
            ViewUtils.fadeView(mBinding.recycler, !showError, animate);
            ViewUtils.fadeView(mBinding.error, showError, animate);
        }
    }

    @Override
    public void openCharacter(@NonNull View heroView, @NonNull CharacterVO character) {
        CharacterActivity.start(this, heroView, character);
    }

    @Override
    public void openSearch() {
        startActivity(new Intent(this, SearchActivity.class));
    }

}