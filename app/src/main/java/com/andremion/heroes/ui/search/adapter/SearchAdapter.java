package com.andremion.heroes.ui.search.adapter;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.andremion.heroes.BR;
import com.andremion.heroes.R;
import com.andremion.heroes.ui.binding.SearchWrapper;
import com.andremion.heroes.ui.adapter.CursorAdapter;

public class SearchAdapter extends CursorAdapter<SearchAdapter.ViewHolder> {

    public SearchAdapter(OnAdapterInteractionListener listener) {
        super(listener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_list_search, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Cursor data = getItem(position);
        holder.mBinding.setVariable(BR.search, SearchWrapper.wrap(data));
        holder.mBinding.executePendingBindings();
    }

    public class ViewHolder extends CursorAdapter.ViewHolder {

        public ViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

}
