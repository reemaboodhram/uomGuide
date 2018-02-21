package com.boodhram.guideme.Chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.boodhram.guideme.MainActivity;
import com.boodhram.guideme.R;
import com.boodhram.guideme.Utils.AccountDTO;
import com.boodhram.guideme.Utils.SharedPreferenceHelper;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {
    EditText username, password;
    Button registerButton;
    String user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        registerButton = (Button)findViewById(R.id.registerButton);


        Typeface type = Typeface.createFromAsset(getAssets(),"old_stamper.ttf");
        registerButton.setTypeface(type);
        Firebase.setAndroidContext(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();
                if(user.equals("")){
                    username.setError("can't be blank");
                }
                else if(pass.equals("")){
                        password.setError("can't be blank");
                }
                    else if(!user.matches("[A-Za-z0-9]+")){
                            username.setError("only alphabet or number allowed");
                        }
                        else if(user.length()<5){
                                username.setError("at least 5 characters long");
                            }
                            else if(pass.length()<5){
                                password.setError("at least 5 characters long");
                            }
                            else {
                                final ProgressDialog pd = new ProgressDialog(Register.this);
                                pd.setMessage("Loading...");
                                pd.show();
                                String url = "https://guideme-7a3a9.firebaseio.com/users.json";
                                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                                    @Override
                                    public void onResponse(String s) {
                                        Firebase reference = new Firebase("https://guideme-7a3a9.firebaseio.com/users");
                                        if(s.equals("null")) {
                                            reference.child(user).child("password").setValue(pass);
                                            Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                                            AccountDTO accountDTO = new AccountDTO();
                                            accountDTO.setUsername(user);
                                            accountDTO.setPassword(pass);
                                            SharedPreferenceHelper.putAccountInSharedPrefence(Register.this,accountDTO);
                                            Intent intent = new Intent(Register.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            try {
                                                JSONObject obj = new JSONObject(s);
                                                if (!obj.has(user)) {
                                                    reference.child(user).child("password").setValue(pass);
                                                    Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                                                    AccountDTO accountDTO = new AccountDTO();
                                                    accountDTO.setUsername(user);
                                                    accountDTO.setPassword(pass);
                                                    SharedPreferenceHelper.putAccountInSharedPrefence(Register.this,accountDTO);
                                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
                                                }

                                            } catch (JSONException e) {
                                                    e.printStackTrace();
                                            }
                                        }

                                        pd.dismiss();
                                    }

                                },new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        System.out.println("" + volleyError );
                                        pd.dismiss();
                                    }
                                });

                                RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                                rQueue.add(request);
                            }
            }
        });
    }
}