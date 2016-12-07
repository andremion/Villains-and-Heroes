/*
 * Copyright (c) 2016. André Mion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andremion.heroes.ui.adapter;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class ArrayAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private final OnItemClickListener mItemClickListener;
    private final List<T> mItems;

    public ArrayAdapter(@Nullable OnItemClickListener itemClickListener) {
        super();
        mItemClickListener = itemClickListener;
        mItems = new ArrayList<>();
        setHasStableIds(true);
    }

    @Override
    public abstract long getItemId(int position);

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public T getItem(int position) {
        if (mItems == null) {
            return null;
        }
        return mItems.get(position);
    }

    public void setItems(List<T> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public List<T> getItems() {
        return new ArrayList<>(mItems);
    }

    public interface OnItemClickListener<T, VH extends RecyclerView.ViewHolder, VB extends ViewDataBinding> {
        void onItemClick(ArrayAdapter<T, VH> adapter, VB binding, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ViewDataBinding mBinding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && mItemClickListener != null) {
                //noinspection unchecked
                mItemClickListener.onItemClick(ArrayAdapter.this, mBinding, position);
            }
        }
    }

}
