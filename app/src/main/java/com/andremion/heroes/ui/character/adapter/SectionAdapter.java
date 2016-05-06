package com.andremion.heroes.ui.character.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andremion.heroes.R;
import com.andremion.heroes.data.DataContract;
import com.andremion.heroes.ui.adapter.CursorAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

public class SectionAdapter extends CursorAdapter<SectionAdapter.ViewHolder> {

    private String mImageTransitionName;

    public SectionAdapter(OnAdapterInteractionListener listener) {
        super(listener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        mImageTransitionName = context.getString(R.string.transition_section_image);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_list_section, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Cursor data = getItem(position);
        String name = data.getString(data.getColumnIndex(DataContract.Section.COLUMN_NAME));
        String title = data.getString(data.getColumnIndex(DataContract.Comic.COLUMN_TITLE));
        String thumbnail = data.getString(data.getColumnIndex(DataContract.Comic.COLUMN_THUMBNAIL));

        holder.mNameView.setText(TextUtils.isEmpty(name) ? title : name);
        Glide.with(holder.mView.getContext())
                .load(thumbnail)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .error(R.mipmap.ic_launcher)
                .into(holder.mImageView);
        ViewCompat.setTransitionName(holder.mImageView, mImageTransitionName + position);
    }

    public class ViewHolder extends CursorAdapter.ViewHolder {

        public final TextView mNameView;
        public final ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            mNameView = (TextView) v.findViewById(R.id.name);
            mImageView = (ImageView) v.findViewById(R.id.image);
        }
    }
}

