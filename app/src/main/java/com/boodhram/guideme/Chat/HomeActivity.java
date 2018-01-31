package com.boodhram.guideme.Chat;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.boodhram.guideme.MainActivity;
import com.boodhram.guideme.R;
import com.boodhram.guideme.Utils.AccountDTO;
import com.boodhram.guideme.Utils.SharedPreferenceHelper;

public class HomeActivity extends AppCompatActivity {

    Button login, signup;
    TextView txt1,txt2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AccountDTO accountDTO = SharedPreferenceHelper.getAccountFromShared(HomeActivity.this);
        if(accountDTO != null && accountDTO.getUsername() != null){
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        login = findViewById(R.id.button2);
        signup = findViewById(R.id.button3);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);

        Typeface type = Typeface.createFromAsset(getAssets(),"old_stamper.ttf");
        login.setTypeface(type);
        signup.setTypeface(type);

        Typeface type2 = Typeface.createFromAsset(getAssets(),"Capture_it.ttf");
        txt1.setTypeface(type2);
        txt2.setTypeface(type2);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(HomeActivity.this,Register.class);
                startActivity(signup);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(HomeActivity.this,Login.class);
                startActivity(login);
            }
        });
    }
}
