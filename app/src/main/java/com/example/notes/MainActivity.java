package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

//    GoogleSignInOptions gso;
//    GoogleSignInClient gsc;
//    ImageView googleBtn;
////////////////////////////////////

//    private GoogleSignInClient googleSignInClient;
//    private String TAG="mainTag";
//    private int RESULT_CODE_SINGIN=999;


    private EditText mloginemail, mloginpassword;
    private RelativeLayout mlogin, mgotosignup;
    private TextView mgotoforgotpassword;

    private FirebaseAuth firebaseAuth;
    ProgressBar mprogressbarofmainactivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        mloginemail = findViewById(R.id.loginemail);
        mloginpassword = findViewById(R.id.loginpassword);
        mlogin = findViewById(R.id.login);
        mgotoforgotpassword = findViewById(R.id.gotoforgotpassword);
        mgotosignup = findViewById(R.id.gotosignup);
        mprogressbarofmainactivity = findViewById(R.id.progressbarofmainactivity);

        //google log in
//        GoogleSignInOptions gso = new
//                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        googleSignInClient = GoogleSignIn.getClient(this,gso);
//        googleBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signInM();
//            }
//        });


///////////////////////////////////////////////////////////////////////////////////////
//        googleBtn = findViewById(R.id.google_btn);
//
//        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        gsc= GoogleSignIn.getClient(this,gso);

//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
//        if (acct!=null){
//            finish();
//            startActivity(new Intent(MainActivity.this,notesActivity.class) );
//        }

//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
//        if (acct!=null){
//            finish();
//            startActivity(new Intent(MainActivity.this, notesActivity.class));
//        }
//
//        googleBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SignIn();
//            }
//        });
        ///////////////////////////////////////////////////////////////////////////////

        //firebase email+password
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            finish();
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        }

        mgotosignup.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SignUp.class)));

        mgotoforgotpassword.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ForgotPassword.class)));

        mlogin.setOnClickListener(view -> {
            String mail = mloginemail.getText().toString().trim();
            String password = mloginpassword.getText().toString().trim();

            if (mail.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Điền vào tất cả các miền", Toast.LENGTH_SHORT).show();

            } else {
                // login the user
                mprogressbarofmainactivity.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        checkMailverfication();
                    } else {
                        Toast.makeText(getApplicationContext(), "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                        mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }

    private void checkMailverfication() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.isEmailVerified()) {
            Toast.makeText(getApplicationContext(), "Đã đăng nhập!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        } else {
            mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Xác nhận email của bạn trước!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

////////////////////////////////////////////////
//    private void SignIn() {
//        Intent intent = gsc.getSignInIntent();
//        startActivityForResult(intent,100);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode==100){
//            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                task.getResult(ApiException.class);
//                finish();
//                startActivity(new Intent(MainActivity.this,notesActivity.class) );
//            } catch (ApiException e) {
//                Toast.makeText(this, "Xảy ra lỗi!", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    }
/////////////////////////////////////////////////////////////


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_CODE_SINGIN) {        //just to verify the code
//            //create a Task object and use GoogleSignInAccount from Intent and write a separate method to handle singIn Result.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
//    }
//
//    private void handleSignInResult(Task<GoogleSignInAccount> task) {
//
//        //we use try catch block because of Exception.
//        try {
//            GoogleSignInAccount account = task.getResult(ApiException.class);
//            Toast.makeText(MainActivity.this,"Signed In successfully",Toast.LENGTH_LONG).show();
//            //SignIn successful now show authentication
//            FirebaseGoogleAuth(account);
//
//        } catch (ApiException e) {
//            e.printStackTrace();
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            Toast.makeText(MainActivity.this,"SignIn Failed!!!",Toast.LENGTH_LONG).show();
//            FirebaseGoogleAuth(null);
//        }
//    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        //here we are checking the Authentication Credential and checking the task is successful or not and display the message
//        //based on that.
//        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_LONG).show();
//                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
////                    UpdateUI(firebaseUser);
//                } else {
//                    Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_LONG).show();
////                    UpdateUI(null);
//                }
//            }
//        });
//    }


    ////////////////////////////////////////////////////////////////////////
//    private void signInM()
//    {
//        Intent singInIntent = googleSignInClient.getSignInIntent();
//        startActivityForResult(singInIntent,RESULT_CODE_SINGIN);
//    }
    //////////////////////////////////////////////////////////////////////////


}