/*
 * Copyright (c) 2017. André Mion
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

package com.andremion.heroes.ui.home.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.andremion.heroes.BR;
import com.andremion.heroes.R;
import com.andremion.heroes.api.data.CharacterVO;
import com.andremion.heroes.ui.adapter.ArrayAdapter;

public class CharacterAdapter extends ArrayAdapter<CharacterVO, CharacterAdapter.ViewHolder> {

    private static final int VIEW_TYPE_DEFAULT = 0;
    private static final int VIEW_TYPE_MORE = 1;

    private final int mItemLayoutRes;
    private final OnLoadListener mLoadListener;
    private boolean mHasMore;
    private boolean mLoading;

    public CharacterAdapter(@LayoutRes int itemLayoutRes, @Nullable OnItemClickListener itemClickListener, @Nullable OnLoadListener loadListener) {
        super(itemClickListener);
        mItemLayoutRes = itemLayoutRes;
        mLoadListener = loadListener;
    }

    public void setHasMore(boolean hasMore) {
        if (hasMore != mHasMore) {
            mHasMore = hasMore;
            notifyDataSetChanged();
        }
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
    }

    @Override
    public int getItemCount() {
        int count = super.getItemCount();
        if (mHasMore) {
            count++;
        }
        return count;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = mItemLayoutRes;
        if (viewType == VIEW_TYPE_MORE) {
            layout = R.layout.item_list_more;
        }
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder.getItemViewType() == VIEW_TYPE_MORE) {
            if (mLoadListener != null && !mLoading) {
                mLoadListener.onLoadMore(getItemCount() - 1);
                mLoading = true;
            }
            return;
        }

        holder.mBinding.setVariable(BR.character, getItem(position));
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && mHasMore) {
            return VIEW_TYPE_MORE;
        }
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public long getItemId(int position) {
        if (getItemViewType(position) == VIEW_TYPE_MORE) {
            return RecyclerView.NO_ID;
        }
        return getItem(position).getId();
    }

    public interface OnLoadListener {
        void onLoadMore(int offset);
    }

    public class ViewHolder extends ArrayAdapter.ViewHolder {
        public ViewHolder(@NonNull ViewDataBinding binding) {
            super(binding);
        }
    }

}
