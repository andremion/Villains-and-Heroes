package com.andremion.heroes.ui.character.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.andremion.heroes.BR;
import com.andremion.heroes.R;
import com.andremion.heroes.api.data.SectionVO;
import com.andremion.heroes.ui.adapter.ArrayAdapter;

public class SectionAdapter extends ArrayAdapter<SectionVO, SectionAdapter.ViewHolder> {

    private final String mImageTransitionName;

    public SectionAdapter(Context context, OnItemClickListener listener) {
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
        holder.mBinding.setVariable(BR.section, getItem(position));
        holder.mBinding.setVariable(BR.imageTransition, mImageTransitionName + position);
        holder.mBinding.executePendingBindings();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    public class ViewHolder extends ArrayAdapter.ViewHolder {
        public ViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

}
