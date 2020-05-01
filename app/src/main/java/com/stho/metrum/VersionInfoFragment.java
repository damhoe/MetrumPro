package com.stho.metrum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VersionInfoFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.version_info_fragment, container, false);
        TextView versionInfo = (TextView) view.findViewById(R.id.versionInfo);
        versionInfo.setText(String.format("%s: %s", versionInfo.getText(), BuildConfig.VERSION_NAME));
        return view;
    }
}


