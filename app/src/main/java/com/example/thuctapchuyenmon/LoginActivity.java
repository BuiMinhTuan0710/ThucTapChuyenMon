package com.example.thuctapchuyenmon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chabbal.slidingdotsplash.SlidingSplashView;
import com.example.database.connect;
import com.example.model.DialogLoading;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import es.dmoral.toasty.Toasty;
import io.reactivex.annotations.NonNull;

public class LoginActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN = 115;
    private Button btnLogin;
    private TextInputEditText edtPhoneLogin,edtPasswordLogin;
    private GoogleProgressBar googleProgressBar;
    public static String makh="";
    ImageView imgTopAnimation;
    LinearLayout body;

    Animation animationTop,animationBot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addViews();
        createRequest();
        addEvents();
    }

    private void addViews() {
        btnLogin = findViewById(R.id.btnLogin);
        edtPhoneLogin = findViewById(R.id.edtphoneLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        googleProgressBar = findViewById(R.id.google_progress);
        imgTopAnimation = findViewById(R.id.imgTopAnimation);
        body = findViewById(R.id.bodyLogin);
        animationBot = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        animationTop = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        imgTopAnimation.setAnimation(animationTop);
        body.setAnimation(animationBot);
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValid())
                {
                    KiemTraDangNhap kiemTraDangNhap = new KiemTraDangNhap();
                    kiemTraDangNhap.execute();
                }

            }
        });
    }
    private boolean checkValid(){
        String email = edtPhoneLogin.getText().toString().trim();
        String password = edtPasswordLogin.getText().toString().trim();
        if(email.length() == 0){
            edtPhoneLogin.setError("Phone number Invalid");
            edtPhoneLogin.requestFocus();
            return false;
        }else if (password.length() == 0) {
            edtPasswordLogin.setError("Password Invalid");
            edtPasswordLogin.requestFocus();
            return false;
        }
        return  true;
    }

    public void clickSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    public void forgotPass(View view) {
        Intent intent = new Intent(LoginActivity.this,ForgotPassMainActivity.class);
        startActivity(intent);
    }

    public void createRequest()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

    public void clickSignInGoogle(View view) {
        signIn();
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Lỗi", "signInWithCredential:success");
                            user = mAuth.getCurrentUser();
                            if(user!=null)
                            {
                                KiemTraKhachHang kiemTraKhachHang = new KiemTraKhachHang();
                                kiemTraKhachHang.execute(user);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    class InsertKhachHang extends AsyncTask<FirebaseUser,Void,Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean)
            {
                KiemTraKhachHang kiemTraKhachHang = new KiemTraKhachHang();
                kiemTraKhachHang.execute(user);
            }
            super.onPostExecute(aBoolean);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
        }

        @Override
        protected Boolean doInBackground(FirebaseUser... account) {
            String params = "tenkh="+account[0].getDisplayName()+"&sdt=''"+"&diachi=''"+"&email="+account[0].getEmail();
            try {
                connect connect = new connect("insertKhachHang");
                NodeList nodeList = connect.getDataParameter(params, "boolean");
                String kt = nodeList.item(0).getTextContent();
                return Boolean.parseBoolean(kt);
            } catch (Exception e) {
                return false;
            }
        }
    }

    class KiemTraKhachHang extends AsyncTask<FirebaseUser,Void,String> {
        @Override
        protected void onPostExecute(String s) {
            if(s=="")
            {
                InsertKhachHang insertKhachHang = new InsertKhachHang();
                insertKhachHang.execute(user);
            }
            else
            {
                DialogLoading.LoadingGoogle(false,googleProgressBar);
                Intent intent = new Intent(LoginActivity.this,ChucNangActivity.class);
                Log.e("makh",s );
                intent.putExtra("makh",s);
                startActivity(intent);
                finish();
            }
            super.onPostExecute(s);
        }

        @Override
        protected void onPreExecute() {
            DialogLoading.LoadingGoogle(true,googleProgressBar);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String aBoolean) {
            super.onCancelled(aBoolean);
        }

        @Override
        protected String doInBackground(FirebaseUser... firebaseUsers) {
            try {
                connect connect = new connect("thongtinKHbyEmail");
                String param = "email=" + firebaseUsers[0].getEmail();
                NodeList nodeList = connect.getDataParameter(param, "KhachHang");
                Element element = (Element) nodeList.item(0);
                return element.getElementsByTagName("MaKH").item(0).getTextContent();
            } catch (Exception e) {
                return "";
            }
        }
    }
    class KiemTraDangNhap extends AsyncTask<Void,Void,String>
    {
        @Override
        protected void onPreExecute() {
            DialogLoading.LoadingGoogle(true,googleProgressBar);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String b) {
            if(!b.equals(""))
            {
                DialogLoading.LoadingGoogle(false,googleProgressBar);
                Toasty.success(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT, true).show();
                Intent intent = new Intent(LoginActivity.this,ChucNangActivity.class);
                Log.e("makh",b );
                intent.putExtra("makh",b);
                startActivity(intent);
                finish();
            }
            else
            {
                DialogLoading.LoadingGoogle(false,googleProgressBar);
                Toasty.error(LoginActivity.this, "Login fail!", Toast.LENGTH_SHORT, true).show();
            }
            super.onPostExecute(b);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String b) {
            super.onCancelled(b);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                connect connect = new connect("kiemtraDangNhap");
                String param = "tk="+edtPhoneLogin.getText()+"&mk="+edtPasswordLogin.getText();
                NodeList nodeList = connect.getDataParameter(param,"boolean");
                String kiemtra = nodeList.item(0).getTextContent();
                boolean kt = Boolean.parseBoolean(kiemtra);
                if(kt)
                {
                    connect connect1 = new connect("thongtinkh");
                    String param1 = "sdt="+edtPhoneLogin.getText();
                    NodeList nodeList1 = connect1.getDataParameter(param1,"KhachHang");
                    Element element = (Element) nodeList1.item(0);
                    String makh = element.getElementsByTagName("MaKH").item(0).getTextContent();
                    return makh;
                }
                else
                    return "";
            }
            catch (Exception e)
            {
                return "";
            }
        }
    }
}