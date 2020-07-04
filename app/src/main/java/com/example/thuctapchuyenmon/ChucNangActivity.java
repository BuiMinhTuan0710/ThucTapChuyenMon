package com.example.thuctapchuyenmon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChucNangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuc_nang);
    }

    public void clickDoAn(View view) {
        Intent intent = new Intent(ChucNangActivity.this,MainBanHangActivity.class);
        startActivity(intent);
    }
    public void clickLogOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(ChucNangActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void clickKhachHang(View view) {
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        Intent intent = new Intent(ChucNangActivity.this,ThongTinActivity.class);
        intent.putExtra("hoten",signInAccount.getDisplayName());
        intent.putExtra("email",signInAccount.getEmail());
        intent.putExtra("hinhanh",signInAccount.getPhotoUrl().toString());
        startActivity(intent);
    }
}
