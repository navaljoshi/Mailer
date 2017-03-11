package com.example.naval.mailer;

import android.app.Dialog;
import android.content.DialogInterface;
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

    //Send button
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declarations

        //buttons
        final Button shareButton = (Button) findViewById(R.id.button1);
        final Button mailButton = (Button) findViewById(R.id.button2);


        //Image View
        //final ImageView img = (ImageView) findViewById(R.id.imageView);
       // final ImageView img1 = (ImageView) findViewById(R.id.imageView1);


        //Initializing the views

       // img1.setVisibility(View.GONE); // intially share image view is GONE


        shareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.d("LVMH", "share button clicked ");

                // changing image view

                // Remove home image View
              //  img.setVisibility(GONE);
                shareButton.setVisibility(GONE);

                // Enable share image view
              //  img1.setVisibility(View.VISIBLE);

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
    }

    private void sendEmail() {
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




