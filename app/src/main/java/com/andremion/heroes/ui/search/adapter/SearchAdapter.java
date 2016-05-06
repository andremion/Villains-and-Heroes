package com.andremion.heroes.ui.search.adapter;

import android.app.SearchManager;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andremion.heroes.R;
import com.andremion.heroes.ui.adapter.CursorAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

public class SearchAdapter extends CursorAdapter<SearchAdapter.ViewHolder> {

    public SearchAdapter(OnAdapterInteractionListener listener) {
        super(listener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Cursor data = getItem(position);
        String text = data.getString(data.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
        String image = data.getString(data.getColumnIndex(SearchManager.SUGGEST_COLUMN_ICON_1));

        holder.mTextView.setText(text);
        Glide.with(holder.mView.getContext())
                .load(image)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .error(R.mipmap.ic_launcher)
                .into(holder.mImageView);
    }

    public class ViewHolder extends CursorAdapter.ViewHolder {

        public final TextView mTextView;
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.name);
            mImageView = (ImageView) view.findViewById(R.id.image);
        }
    }
}
