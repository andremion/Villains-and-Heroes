package com.andremion.heroes.ui.character;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andremion.heroes.R;
import com.andremion.heroes.data.DataContract;
import com.andremion.heroes.databinding.FragmentSectionBinding;
import com.andremion.heroes.databinding.ItemListSectionBinding;
import com.andremion.heroes.ui.adapter.CursorAdapter;
import com.andremion.heroes.ui.character.adapter.SectionAdapter;

public class SectionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_TYPE = "type";
    private static final String ARG_CHARACTER = "character";
    private static final String ARG_LABEL = "label";

    private String mType;
    private long mCharacter;
    private String mLabel;

    private FragmentSectionBinding mBinding;
    private OnFragmentInteractionListener mListener;
    private SectionAdapter mSectionAdapter;

    public SectionFragment() {
        // Required empty public constructor
    }

    public static SectionFragment newInstance(String type, long character, String label) {
        SectionFragment fragment = new SectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        args.putLong(ARG_CHARACTER, character);
        args.putString(ARG_LABEL, label);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE);
            mCharacter = getArguments().getLong(ARG_CHARACTER);
            mLabel = getArguments().getString(ARG_LABEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_section, container, false);
        mBinding.setLabel(mLabel);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mBinding.recycler.setNestedScrollingEnabled(false);
        mBinding.recycler.setHasFixedSize(true);
        mBinding.recycler.setAdapter(mSectionAdapter =
                new SectionAdapter(getContext(),
                        new OnAdapterInteractionListenerWrapper(mType, mListener)));
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.getClass().getSimpleName()
                    + " must implement " + OnFragmentInteractionListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                DataContract.Section.CONTENT_URI,
                null,
                DataContract.Section.COLUMN_TYPE + "=? AND " + DataContract.Section.COLUMN_CHARACTER + "=?",
                new String[]{mType, String.valueOf(mCharacter)},
                DataContract.Section.COLUMN_NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSectionAdapter.swapCursor(data);
        mBinding.setEmpty(data == null || data.getCount() == 0);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSectionAdapter.swapCursor(null);
    }

    public interface OnFragmentInteractionListener {
        void onItemClick(CursorAdapter adapter, ItemListSectionBinding binding, int position, String type);
    }

    public static class OnAdapterInteractionListenerWrapper
            implements CursorAdapter.OnAdapterInteractionListener<ItemListSectionBinding> {

        private final String mType;
        private final OnFragmentInteractionListener mListener;

        public OnAdapterInteractionListenerWrapper(String type, OnFragmentInteractionListener listener) {
            mType = type;
            mListener = listener;
        }

        @Override
        public void onItemClick(CursorAdapter adapter, ItemListSectionBinding binding, int position) {
            mListener.onItemClick(adapter, binding, position, mType);
        }
    }

}
