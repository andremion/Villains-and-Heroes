package com.andremion.heroes.ui.home.adapter;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andremion.heroes.R;
import com.andremion.heroes.data.DataContract.Character;
import com.andremion.heroes.ui.adapter.CursorAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (holder.getItemViewType() == VIEW_TYPE_MORE) {
            CursorAdapter.OnAdapterInteractionListener listener = getItemClickListener();
            if (listener instanceof OnCharacterAdapterInteractionListener) {
                ((OnCharacterAdapterInteractionListener) listener).onLoadMoreItems(getItemCount() - 1);
            }
            return;
        }

        Cursor data = getItem(position);
        String name = data.getString(data.getColumnIndex(Character.COLUMN_NAME));
        String image = data.getString(data.getColumnIndex(Character.COLUMN_IMAGE));

        holder.mNameView.setText(name);
        Glide.with(holder.mView.getContext())
                .load(image)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .error(R.mipmap.ic_launcher)
                .into(holder.mImageView);
    }

    @Override
    public int getItemViewType(int position) {
        Cursor data = getItem(position);
        if (data.getLong(data.getColumnIndex(Character._ID)) == 0) {
            return VIEW_TYPE_MORE;
        }
        return VIEW_TYPE_DEFAULT;
    }

    public interface OnCharacterAdapterInteractionListener extends CursorAdapter.OnAdapterInteractionListener {
        void onLoadMoreItems(int offset);
    }

    public class ViewHolder extends CursorAdapter.ViewHolder {

        public final TextView mNameView;
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mNameView = (TextView) view.findViewById(R.id.name);
            mImageView = (ImageView) view.findViewById(R.id.image);
        }
    }
}
