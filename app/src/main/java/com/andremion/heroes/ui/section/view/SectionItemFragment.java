package com.andremion.heroes.ui.section.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andremion.heroes.R;
import com.andremion.heroes.api.data.SectionVO;
import com.andremion.heroes.databinding.FragmentSectionItemBinding;
import com.andremion.heroes.ui.binding.ImageLoadingListener;

public class SectionItemFragment extends Fragment {

    private static final String ARG_ITEM = "item";
    private static final String ARG_POSITION = "position";

    private SectionVO mItem;
    private String mImageTransitionName;

    public SectionItemFragment() {
    }

    public static SectionItemFragment newInstance(SectionVO item, int position) {
        SectionItemFragment fragment = new SectionItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, item);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = (SectionVO) getArguments().get(ARG_ITEM);
            mImageTransitionName = getString(R.string.transition_section_image) +
                    getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentSectionItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_section_item, container, false);
        binding.setSection(mItem);
        binding.setImageTransition(mImageTransitionName);
        binding.setImageListener(new ImageLoadingListener() {
            @Override
            public void onSuccess() {
                startPostponedEnterTransition();
            }

            @Override
            public void onFailed(@NonNull Exception e) {
                startPostponedEnterTransition();
            }
        });
        return binding.getRoot();
    }

    private void startPostponedEnterTransition() {
        if (getActivity() != null) {
            getActivity().supportStartPostponedEnterTransition();
        }
    }

}
