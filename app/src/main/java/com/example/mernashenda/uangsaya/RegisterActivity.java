package com.example.mernashenda.uangsaya;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

public class RegisterActivity extends AppCompatActivity {
    private Context context;
    private ProgressDialog pDialog;
    private customfonts.MyEditText nm_lengkap;
    private customfonts.MyEditText no_hp;
    private customfonts.MyEditText email;
    private customfonts.MyEditText NIK;
    private AppCompatButton buttonregister;
    String valid_email;
    NiftyDialogBuilder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.induk_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        setSupportActionBar(toolbar);
        context = RegisterActivity.this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NIK = (customfonts.MyEditText)findViewById(R.id.txtnik);
        nm_lengkap = (customfonts.MyEditText)findViewById(R.id.txtfullname);
        no_hp = (customfonts.MyEditText)findViewById(R.id.txtnohp);
        email = (customfonts.MyEditText)findViewById(R.id.txtemail);
        buttonregister = (AppCompatButton) findViewById(R.id.register);

        NIK.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (NIK.getText().toString().trim().length() < 16) {
                        NIK.setError("Invalid NIK");
                    } else {
                        // your code here
                        NIK.setError(null);
                    }
                } else {
                    if (NIK.getText().toString().trim().length() < 16) {
                        NIK.setError("Invalid NIK");
                    } else {
                        // your code here
                        NIK.setError(null);
                    }
                }

            }
        });

        no_hp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (no_hp.getText().toString().trim().length() < 12) {
                        no_hp.setError("Invalid Number Phone");
                    } else {
                        // your code here
                        no_hp.setError(null);
                    }
                } else {
                    if (no_hp.getText().toString().trim().length() < 12) {
                        no_hp.setError("Invalid Number Phone");
                    } else {
                        // your code here
                        no_hp.setError(null);
                    }
                }

            }
        });

        InputFilter[] Textfilters = new InputFilter[1];
        Textfilters[0] = new InputFilter(){
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {

                    char[] acceptedChars = new char[]{' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z','.'};

                    for (int index = start; index < end; index++) {
                        if (!new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) {
                            return "";
                        }
                    }
                }
                return null;
            }

        };
        nm_lengkap.setFilters(Textfilters);

        email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                // TODO Auto-generated method stub
                Is_Valid_Email(email); // pass your EditText Obj here.
            }

            public void Is_Valid_Email(customfonts.MyEditText edt) {
                if (edt.getText().toString() == null) {
                    edt.setError("Invalid Email Address");
                    valid_email = null;
                } else if (isEmailValid(edt.getText().toString()) == false) {
                    edt.setError("Invalid Email Address");
                    valid_email = null;
                } else {
                    valid_email = edt.getText().toString();
                }
            }

            boolean isEmailValid(CharSequence email) {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches();
            } // end of TextWatcher (email)
        });


        buttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saving
                if (NIK.getText().toString().trim().length() == 0 ) {
                    dialogBuilder
                            .withTitle("Peringatan")                                  //.withTitle(null)  no title
                            .withTitleColor("#FFFFFF")                                  //def
                            .withDividerColor("#11000000")                              //def
                            .withMessage("NIK Harus Diisi")                     //.withMessage(null)  no Msg
                            .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                            .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)                               //def
                            .withIcon(getResources().getDrawable(R.mipmap.ic_logous))
                            .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                            .withDuration(700)                                          //def
                            .withEffect(Effectstype.Slidetop)                                         //def Effectstype.Slidetop
                            .withButton1Text("Ok")
                            .setButton1Click(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder
                                            .hide();
                                    NIK.requestFocus();
                                }
                            })
                            .show();
                }
                else if (TextUtils.isEmpty(nm_lengkap.getText())) {
                    dialogBuilder
                            .withTitle("Peringatan")                                  //.withTitle(null)  no title
                            .withTitleColor("#FFFFFF")                                  //def
                            .withDividerColor("#11000000")                              //def
                            .withMessage("Nama Lengkap Harus Diisi")                     //.withMessage(null)  no Msg
                            .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                            .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)                               //def
                            .withIcon(getResources().getDrawable(R.mipmap.ic_logous))
                            .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                            .withDuration(700)                                          //def
                            .withEffect(Effectstype.Slidetop)                                         //def Effectstype.Slidetop
                            .withButton1Text("Ok")
                            .setButton1Click(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder
                                            .hide();

                                    nm_lengkap.requestFocus();
                                }
                            })
                            .show();
                }
                else if (TextUtils.isEmpty(email.getText())) {
                    dialogBuilder
                            .withTitle("Peringatan")                                  //.withTitle(null)  no title
                            .withTitleColor("#FFFFFF")                                  //def
                            .withDividerColor("#11000000")                              //def
                            .withMessage("No Handphone Harus Diisi")                     //.withMessage(null)  no Msg
                            .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                            .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)                               //def
                            .withIcon(getResources().getDrawable(R.mipmap.ic_logous))
                            .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                            .withDuration(700)                                          //def
                            .withEffect(Effectstype.Slidetop)                                         //def Effectstype.Slidetop
                            .withButton1Text("Ok")
                            .setButton1Click(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder
                                            .hide();
                                    email.requestFocus();
                                }
                            })
                            .show();
                }
                else if (TextUtils.isEmpty(no_hp.getText())) {
                    dialogBuilder
                            .withTitle("Peringatan")                                  //.withTitle(null)  no title
                            .withTitleColor("#FFFFFF")                                  //def
                            .withDividerColor("#11000000")                              //def
                            .withMessage("Email Harus Diisi")                     //.withMessage(null)  no Msg
                            .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                            .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)                               //def
                            .withIcon(getResources().getDrawable(R.mipmap.ic_logous))
                            .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                            .withDuration(700)                                          //def
                            .withEffect(Effectstype.Slidetop)                                         //def Effectstype.Slidetop
                            .withButton1Text("Ok")
                            .setButton1Click(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder
                                            .hide();
                                    no_hp.requestFocus();
                                }
                            })
                            .show();
                }
                else {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = RegisterActivity.this.getSharedPreferences(AppVar.SHARED_PREF_REGISTER, Context.MODE_PRIVATE);

                    //Creating editor to store values to shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    //Adding values to editor
                    editor.putString(AppVar.NIK_SHARED_PREF, NIK.getText().toString());
                    editor.putString(AppVar.NMlengkap_SHARED_PREF, nm_lengkap.getText().toString());
                    editor.putString(AppVar.EMAIL_SHARED_PREF, email.getText().toString());
                    editor.putString(AppVar.NOHP_SHARED_PREF, no_hp.getText().toString());
                    editor.commit();

                    startActivity(new Intent(RegisterActivity.this,KodeUnikActivity.class));
                }



            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Toast.makeText(tentangkamiActivity.this,
        //        "Your Message", Toast.LENGTH_LONG).show();
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
