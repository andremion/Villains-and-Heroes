package com.andremion.heroes.ui.section;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andremion.heroes.R;
import com.andremion.heroes.data.DataContract.Comic;
import com.andremion.heroes.data.DataContract.Section;
import com.andremion.heroes.data.DataContract.Series;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

public class SectionItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_TYPE = "type";
    private static final String ARG_POSITION = "position";
    private static final String ARG_ID = "id";
    private String mType;
    private int mPosition;
    private long mId;
    private ImageView mImageView;
    private TextView mTitleView;

    public SectionItemFragment() {
    }

    public static SectionItemFragment newInstance(String type, int position, long id) {
        SectionItemFragment fragment = new SectionItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        args.putInt(ARG_POSITION, position);
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE);
            mPosition = getArguments().getInt(ARG_POSITION);
            mId = getArguments().getLong(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_section_item, container, false);
        mImageView = (ImageView) rootView.findViewById(R.id.image);
        ViewCompat.setTransitionName(mImageView, getString(R.string.transition_section_image) + mPosition);
        mTitleView = (TextView) rootView.findViewById(R.id.title);
        getLoaderManager().initLoader(0, null, this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (Section.TYPE_COMIC.equals(mType)) {
            return new CursorLoader(getContext(),
                    Comic.buildUri(mId),
                    null,
                    null,
                    null,
                    null);
        } else if (Section.TYPE_SERIES.equals(mType)) {
            return new CursorLoader(getContext(),
                    Series.buildUri(mId),
                    null,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String type = getArguments().getString(ARG_TYPE);
        if (Section.TYPE_COMIC.equals(type)) {
            if (data != null && data.moveToNext()) {
                Glide.with(this)
                        .load(data.getString(data.getColumnIndex(Comic.COLUMN_IMAGE)))
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .error(R.mipmap.ic_launcher)
                        .into(mImageView);
                mTitleView.setText(data.getString(data.getColumnIndex(Section.COLUMN_NAME)));
            }
        } else if (Section.TYPE_SERIES.equals(type)) {
            if (data != null && data.moveToNext()) {
                Glide.with(this)
                        .load(data.getString(data.getColumnIndex(Series.COLUMN_IMAGE)))
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .error(R.mipmap.ic_launcher)
                        .into(mImageView);
                mTitleView.setText(data.getString(data.getColumnIndex(Section.COLUMN_NAME)));
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
