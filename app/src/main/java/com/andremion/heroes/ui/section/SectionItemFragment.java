package com.andremion.heroes.ui.section;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andremion.heroes.R;
import com.andremion.heroes.data.DataContract.Comic;
import com.andremion.heroes.data.DataContract.Section;
import com.andremion.heroes.data.DataContract.Series;
import com.andremion.heroes.databinding.FragmentSectionItemBinding;
import com.andremion.heroes.ui.binding.SectionWrapper;

import static com.andremion.heroes.data.DataContract.Story;

public class SectionItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_TYPE = "type";
    private static final String ARG_POSITION = "position";
    private static final String ARG_ID = "id";

    private String mType;
    private int mPosition;
    private long mId;

    private String mImageTransitionName;
    private FragmentSectionItemBinding mBinding;

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
        mImageTransitionName = getString(R.string.transition_section_image);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_section_item, container, false);
        mBinding.setImageTransition(mImageTransitionName + mPosition);
        getLoaderManager().initLoader(0, null, this);
        return mBinding.getRoot();
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
        } else if (Section.TYPE_STORIES.equals(mType)) {
            return new CursorLoader(getContext(),
                    Story.buildUri(mId),
                    null,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToNext()) {
            mBinding.setSection(SectionWrapper.wrap(data));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
