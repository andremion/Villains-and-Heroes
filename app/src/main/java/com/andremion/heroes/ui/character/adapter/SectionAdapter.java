package com.andremion.heroes.ui.character.adapter;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.andremion.heroes.BR;
import com.andremion.heroes.R;
import com.andremion.heroes.ui.adapter.CursorAdapter;
import com.andremion.heroes.ui.binding.SectionWrapper;

public class SectionAdapter extends CursorAdapter<SectionAdapter.ViewHolder> {

    private final String mImageTransitionName;

    public SectionAdapter(Context context, OnAdapterInteractionListener listener) {
        super(listener);
        mImageTransitionName = context.getString(R.string.transition_section_image);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_list_section, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cursor data = getItem(position);
        holder.mBinding.setVariable(BR.section, SectionWrapper.wrap(data));
        holder.mBinding.setVariable(BR.imageTransition, mImageTransitionName + position);
        holder.mBinding.executePendingBindings();
    }

    public class ViewHolder extends CursorAdapter.ViewHolder {
        public ViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

}
