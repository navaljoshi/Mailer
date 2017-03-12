package com.example.naval.mailer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity  {

    //Declaring EditText
    private EditText editTextEmail;
    private EditText editTextSubject;
    private EditText editTextMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declarations

        //buttons


        final Button shareButton = (Button) findViewById(R.id.btshr);
        final Button mailButton = (Button) findViewById(R.id.btGmail);
        final Button fbButton = (Button) findViewById(R.id.btFacebook);


        shareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.d("LVMH", "share button clicked ");


                shareButton.setVisibility(GONE);

            }
        });


        mailButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.d("LVMH", "MAIL button clicked ");

                sendEmail();


            }
        });


        fbButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.d("LVMH", "FB button clicked ");

                callFB();

            }
        });
    }



    public void  callFB()
    {
        final Dialog dialog1 = new Dialog(this);

        Log.d("LVMH", " opening dialog ");
        //call FB activity
        Intent i=new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);

    }


    public  void sendEmail() {
        final Dialog dialog1 = new Dialog(this);

        Log.d("LVMH", " opening dialog ");
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.mail_layout);
        dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
       // dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setCancelable(false);

        dialog1.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //When you touch outside of dialog bounds,
                        //the dialog gets canceled and this method executes..
                        //home();
                    }
                }
        );

        //calling mail send button inside popup
        final Button mail = (Button)dialog1.findViewById(R.id.buttonSend);
        editTextEmail = (EditText) dialog1.findViewById(R.id.editTextEmail);
        editTextSubject = (EditText) dialog1.findViewById(R.id.editTextSubject);
        editTextMessage = (EditText) dialog1.findViewById(R.id.editTextMessage);

        mail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.d("LVMH", "share button clicked ");

                // changing image view
                //Getting content for email
                String email = editTextEmail.getText().toString().trim();
                String subject = editTextSubject.getText().toString().trim();
                String message = editTextMessage.getText().toString().trim();

                //Creating SendMail object
                SendMail sm = new SendMail(getWindow().getContext(), email, subject, message);

                //Executing sendmail to send email
                sm.execute();


            }
        });
     dialog1.show();// opening mail dialog

    }
    };




