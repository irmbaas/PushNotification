package ir.mbaas.pushnotification.fragment;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.mbaas.pushnotification.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFragment extends Fragment {


    public IntroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvVersion = (TextView) view.findViewById(R.id.tv_version);
        try {
            String version =
                    String.format(getActivity().getResources().getString(R.string.version),
                            getActivity().getPackageManager()
                                    .getPackageInfo(getActivity().getPackageName(), 0).versionName);
            tvVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
