package com.example.model;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.thuctapchuyenmon.DoiMatKhauActivity;
import com.example.thuctapchuyenmon.R;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;


public class ProfileFragment extends Fragment {
    private TextView txtNameProfile;
    private GoogleProgressBar progressBarProfile;
    private ImageView imgProfile, imageProfileUpload;
    private LinearLayout layoutChangePassword;
    public String makh;
    public static final int REQUEST_CODE=117;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile, container, false);
        addViews(v);
        addEvents(v);
        return v;
    }
    private void addEvents(View v) {
        txtNameProfile.setText("Minh Tuấn");
        layoutChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DoiMatKhauActivity.class);
                intent.putExtra("makh",makh);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }
    private void addViews(View v) {
        txtNameProfile = v.findViewById(R.id.txtNameProfile);
        imgProfile = v.findViewById(R.id.imageProfile);
        layoutChangePassword = v.findViewById(R.id.layoutChangePassword);
        progressBarProfile = v.findViewById(R.id.progressBarProfile);
    }
}
