package ir.mbaas.pushnotification.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.mbaas.push.helper.PrefUtil;
import ir.mbaas.pushnotification.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {


    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View retView = inflater.inflate(R.layout.fragment_report, container, false);
        TextView tv = (TextView) retView.findViewById(R.id.tv_push);

        tv.setText(PrefUtil.getString(getActivity(), PrefUtil.LAST_PUSH_RECEIVED, "nothing!!!"));

        return retView;
    }

}
