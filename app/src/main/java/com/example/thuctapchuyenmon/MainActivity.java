package com.example.thuctapchuyenmon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.RangeValueIterator;
import android.icu.util.ValueIterator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.database.connect;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import io.grpc.internal.LogExceptionRunnable;
import io.reactivex.annotations.NonNull;

public class MainActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN = 115;
    public static final int FB_SIGN_IN = 113;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String tag = "Facebook Login :";
    private EditText edtUser,edtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createRequest();
        addViews();
        //setupLoginFacebook();
    }

    public void saveUserInfo(FirebaseUser firebaseUser)
    {
        SharedPreferences preferences = this.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if(firebaseUser!=null)
        {
            String UserID = firebaseUser.getUid();
            String UserEmail = firebaseUser.getEmail();
            editor.putString("userid",UserID);
            editor.putString("useremail",UserEmail);
            editor.putString("usertype","facebook");
            editor.putInt("Islogin",1);
        }
        else
        {
            editor.putString("userid","");
            editor.putString("useremail","");
            editor.putString("usertype","facebook");
            editor.putInt("Islogin",0);
        }
        editor.commit();
    }
    private void addViews() {
        edtUser = findViewById(R.id.edtUser);
        edtPassword = findViewById(R.id.edtPass);

    }
    public void createRequest()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
    }
    public void clickGoogle(View view) {
            signIn();
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // FBCallbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("lỗi","firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("Google sign in failed", e.getMessage());
            }
        }
    }
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Lỗi", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null)
                            {
                                Intent intent = new Intent(MainActivity.this,ChucNangActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void clickSignIn(View view) {
        KiemTraDangNhap kiemTraDangNhap = new KiemTraDangNhap();
        kiemTraDangNhap.execute();
    }

    public void clickRegister(View view) {
        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    class KiemTraDangNhap extends AsyncTask<Void,Void,Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean b) {
            if(b)
            {
                Intent intent = new Intent(MainActivity.this,ChucNangActivity.class);
                startActivity(intent);
            }
            else 
            {
                Toast.makeText(MainActivity.this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(b);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Boolean b) {
            super.onCancelled(b);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                connect connect = new connect("kiemtraDangNhap");
                String param = "tk="+edtUser.getText()+"&mk="+edtPassword.getText();
                NodeList nodeList = connect.getDataParameter(param,"boolean");
                String kiemtra = nodeList.item(0).getTextContent();
                boolean kt = Boolean.parseBoolean(kiemtra);
                return kt;
            }
            catch (Exception e)
            {
                return false;
            }
        }
    }
}
