package com.example.naval.mailer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.FileObserver;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity  {

    //variable Declarations
    String  newFile =  null;

    //Declaring EditText
    public EditText editTextEmail;
    public EditText editTextName;

    //Declaring Image View
    public ImageView SharedImg ;

    //buttons

    public Button shareButton ;
    public Button mailButton ;
    public Button fbButton ;
    public LinearLayout gallery  ;
    public LinearLayout social  ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Intializing Components
        SharedImg = (ImageView)findViewById(R.id.imgShared);
        shareButton = (Button) findViewById(R.id.btshr);
        mailButton = (Button) findViewById(R.id.btGmail);
        fbButton = (Button) findViewById(R.id.btFacebook);
        gallery  = (LinearLayout) findViewById(R.id.linGal);
        social  = (LinearLayout) findViewById(R.id.linSocial);

        // Applying Event listners on Buttons

        shareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.d("LVMH", "share button clicked ");


                sharingScreen(); // call funtion to clear screen for FB&GMAIL sharing screen

            }
        });


        mailButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.d("LVMH", "MAIL button clicked ");

                sendEmail(); //funtion to send EMAIl

            }
        });


        fbButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.d("LVMH", "FB button clicked ");

                callFB(); // function to FB

            }
        });
    }


    // fucntion to move from Home - FB&GMAIL sharing screen
    void sharingScreen()
    {
        shareButton.setVisibility(GONE);//
        gallery.setVisibility(GONE); // gallery button disabled
        social.setVisibility(View.VISIBLE); //social buttons enabled
        fbButton.setVisibility(View.VISIBLE);
        mailButton.setVisibility(View.VISIBLE);
    }
    // fucntion to move from sharing screen FB&GMAIL to Shared successfull screen
    void successSharedScreen() {


        social.setVisibility(View.GONE); //social buttons enabled
        fbButton.setVisibility(View.GONE);
        mailButton.setVisibility(View.GONE);

        //enable shared iamge picture
        SharedImg.setVisibility(View.VISIBLE);
    }


    public void  callFB()
    {
        final Dialog dialog1 = new Dialog(this);

        Log.d("LVMH", " opening dialog ");
        //call FB activity
        Intent i=new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);

    }


    // function for file observer
    public String observer ()
    {


        FileObserver observer = new FileObserver("/storage/emulated/0/nikalodean/") {
            // set up a file observer to watch this directory on sd card

            @Override
            public void onEvent(int event, String file) {
                Log.d("LVMH", "File created [" + "/storage/emulated/0/nikalodean/" + file + "]");


                Toast.makeText(getBaseContext(), file + " was saved!", Toast.LENGTH_LONG).show();

                newFile = "/storage/emulated/0/nikalodean/"+file.toString();

            }
        };
        observer.startWatching(); //START OBSERVING

        return newFile;
    }



    public  void sendEmail() {
        final Dialog dialog1 = new Dialog(this);

        Log.d("LVMH", " opening MAIL dialog ");
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.mail_layout);
        dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
       // dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);

        dialog1.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        dialog1.dismiss();
                        Log.d("LVMH", " cancelled MAIL dialog ");

                    }
                }
        );

        //calling mail send button inside popup
        final Button mail = (Button)dialog1.findViewById(R.id.buttonSend);
        editTextEmail = (EditText) dialog1.findViewById(R.id.editTextMail);
        editTextName = (EditText) dialog1.findViewById(R.id.editTextName);


        mail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.d("LVMH", "mail button clicked ");

                //Changing image view
                //Getting content for email
                String email = editTextEmail.getText().toString().trim();
                String subject = "LVMH";
                String message = "hello";

                //Creating SendMail object
                SendMail sm = new SendMail(getWindow().getContext(), email, subject, message);

                //Executing send mail to send email
                sm.execute();

                dialog1.dismiss();// dismiss dialof and remove buttons


                successSharedScreen();




            }
        });
     dialog1.show();// opening mail dialog

    }
    };




