package com.andremion.heroes.ui.home.adapter;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.andremion.heroes.BR;
import com.andremion.heroes.R;
import com.andremion.heroes.data.DataContract.Character;
import com.andremion.heroes.databinding.ItemListCharacterBinding;
import com.andremion.heroes.ui.binding.CharacterWrapper;
import com.andremion.heroes.ui.adapter.CursorAdapter;

public class CharacterAdapter extends CursorAdapter<CharacterAdapter.ViewHolder> {

    private static final int VIEW_TYPE_DEFAULT = 0;
    private static final int VIEW_TYPE_MORE = 1;

    public CharacterAdapter(OnCharacterAdapterInteractionListener listener) {
        super(listener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.item_list_character;
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
            CursorAdapter.OnAdapterInteractionListener listener = getItemClickListener();
            if (listener instanceof OnCharacterAdapterInteractionListener) {
                ((OnCharacterAdapterInteractionListener) listener).onLoadMoreItems(getItemCount() - 1);
            }
            return;
        }

        Cursor data = getItem(position);
        holder.mBinding.setVariable(BR.character, CharacterWrapper.wrap(data));
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemViewType(int position) {
        Cursor data = getItem(position);
        if (data.getLong(data.getColumnIndex(Character._ID)) == 0) {
            return VIEW_TYPE_MORE;
        }
        return VIEW_TYPE_DEFAULT;
    }

    public interface OnCharacterAdapterInteractionListener
            extends CursorAdapter.OnAdapterInteractionListener<ItemListCharacterBinding> {
        void onLoadMoreItems(int offset);
    }

    public class ViewHolder extends CursorAdapter.ViewHolder {
        public ViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

}
