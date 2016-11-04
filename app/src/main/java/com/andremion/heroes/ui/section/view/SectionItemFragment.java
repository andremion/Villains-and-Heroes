package com.andremion.heroes.ui.section.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andremion.heroes.R;
import com.andremion.heroes.api.data.SectionVO;
import com.andremion.heroes.databinding.FragmentSectionItemBinding;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class SectionItemFragment extends Fragment implements RequestListener<String, GlideDrawable> {

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
        binding.setImageTransition(mImageTransitionName);
        binding.setSection(mItem);
        binding.setImageListener(this);
        return binding.getRoot();
    }

    @Override
    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
        startPostponedEnterTransition();
        return false;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
        startPostponedEnterTransition();
        return false;
    }

    private void startPostponedEnterTransition() {
        if (getActivity() != null) {
            getActivity().supportStartPostponedEnterTransition();
        }
    }

}
