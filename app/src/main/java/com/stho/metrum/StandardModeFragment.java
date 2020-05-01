package com.stho.metrum;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stho.metrum.databinding.StandardModeFragmentBinding;

import java.util.Objects;

public class StandardModeFragment extends Fragment implements View.OnClickListener, IUpdateableFragment {

    private MainActivity mainActivity;
    private ViewModel viewModel;
    private StandardModeFragmentBinding binder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binder = DataBindingUtil.inflate(inflater, R.layout.standard_mode_fragment, container, false);
        View rootView = binder.getRoot();
        mainActivity = (MainActivity) getActivity();

        binder.mainViewPager.setAdapter(new MetrumPagerAdapter(Objects.requireNonNull(this.getActivity())));

        binder.btnPlayStandard.setOnClickListener(this);

        //        viewHolder.button = (ImageButton) rootView.findViewById(R.id.btn_state_change);
//        viewHolder.button.setOnClickListener(this);
//        viewHolder.button.setImageResource(android.R.drawable.ic_media_play);
//        viewHolder.tempo = rootView.findViewById(R.id.tempo);
//
//        viewHolder.frequency = rootView.findViewById(R.id.frequency);
//        viewHolder.frequency.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mainActivity.defineFrequency();
//            }
//        });
//        viewHolder.viewPager = rootView.findViewById(R.id.pager);

//        viewHolder.mainLayout = rootView.findViewById(R.id.main_layout);
//        viewHolder.track = rootView.findViewById(R.id.track);
//        viewHolder.dance = rootView.findViewById(R.id.dance);
        //viewHolder.lambOne = rootView.findViewById(R.id.lamp_one);
        //viewHolder.lambTwo = rootView.findViewById(R.id.lamp_two);
        //viewHolder.lambThree = rootView.findViewById(R.id.lamp_three);
        //viewHolder.lambFour = rootView.findViewById(R.id.lamp_four);

        assert mainActivity != null;
        mainActivity.updateData();

        return rootView;
    }

    @Override
    public void updateViewModel(ViewModel viewModel) {

        this.viewModel = viewModel;

        if (binder != null)
        {
            this.binder.setTempo(viewModel.getBeatsPerMinute());
            this.binder.setTempoText(Tempi.getNameByBeats(viewModel.getBeatsPerMinute()));

            if (viewModel.getRunState()) {
                binder.btnPlayStandard.setImageResource(android.R.drawable.ic_media_pause);
            } else if (!viewModel.getRunState()) {
                binder.btnPlayStandard.setImageResource(android.R.drawable.ic_media_play);
            }
        }
    }

    @Override
    public void onClick(View v) {
        //mainActivity.updateData();
        if (viewModel.getRunState()) {
            mainActivity.onNotifyPlayerStateChanging(false);
        }
        else if (!viewModel.getRunState()) {
            mainActivity.onNotifyPlayerStateChanging(true);
        }
    }
}
