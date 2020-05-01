package com.stho.metrum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/*
  Fragment to display the beats horizontically
  You can start the editor, storage management, or just save
  TODO: display differently, if beat is combination of sounds.
 */
public class BeatsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.beats_fragment, container, false);
        final MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            final ViewModel viewModel = activity.viewModel;
            final LinearLayout layout = view.findViewById(R.id.container);
            updateUI(activity, viewModel, layout);
            activity.viewModel.setOnChangeListener(new ViewModel.OnChangeListener() {
                @Override
                public void onChange() {
                    updateUI(activity, viewModel, layout);
                }
            });
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.defineBeats();
                }
            });
            view.findViewById(R.id.buttonChoose).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.manageStorage();
                }
            });
            view.findViewById(R.id.buttonEdit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.defineBeats();
                }
            });
            view.findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.save();
                }
            });
        }
        return view;
    }

    private void updateUI(final MainActivity activity, final ViewModel viewModel, final LinearLayout layout) {
        layout.removeAllViews();
        int n = viewModel.getBeats().size();
        float weight = 1.0f / n;

        for (int beat : viewModel.getBeats()) {
            ImageView imageView = new ImageView(activity);

            layout.addView(imageView);

            imageView.setImageResource(ViewModel.getImageFor(beat));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.defineBeats();
                }
            });

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.weight = weight;
            imageView.setLayoutParams(layoutParams);
        }
    }
}





