package com.example.naval.mailer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
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
import java.io.FileNotFoundException;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import static android.view.View.GONE;
import static android.view.View.generateViewId;

public class MainActivity extends AppCompatActivity  {

    //variable Declarations
    File  newFile =  null;

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
    public ImageView home  ;
    public ImageView thumbnail1  ;
    public ImageView thumbnail2  ;
    public ImageView thumbnail3  ;
    public Drawable homeDrwable;
    public Drawable homeDrwabletemp;
    public Drawable homeDrwable2;
    public Drawable homeDrwable3;

    // main path of the selected image

    public static  String imagePath ; // this will be used by Gmail & FB
    public String imagePath1 ;
    public String imagePath2 ;
    public int imageCount;



    FileObserver observer;

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
        home = (ImageView)findViewById(R.id.imgHome);
        thumbnail1 = (ImageView)findViewById(R.id.img1);
        thumbnail2 = (ImageView)findViewById(R.id.img2);
        thumbnail3 = (ImageView)findViewById(R.id.img3);

        haveNetworkConnection();//check is net is there?


        observer(); // start observing the pics folder

        // Applying Event listners on Buttons


        //home.setImageBitmap(bmImg);
       // thumbnail1.setImageBitmap(bmImg);
       // thumbnail2.setImageBitmap(bmImg);
       // thumbnail3.setImageBitmap(bmImg);

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

        // ThumbNail click listners

        thumbnail1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Your code.

                homeDrwabletemp = Drawable.createFromPath(imagePath);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("LVMH", "Set Main Image and thumbnail 1 "+ imagePath );
                        home.setImageDrawable(homeDrwabletemp);
                        home.invalidate();

                    }
                });

            }
        });

        thumbnail2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Your code.
                //setDrawableImage("3nik.jpg");
                imagePath = imagePath1;

                homeDrwabletemp = Drawable.createFromPath(imagePath1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("LVMH", "Set Main Image and thumbnail 2 "+ imagePath );
                        home.setImageDrawable(homeDrwabletemp);
                        home.invalidate();

                    }
                });

            }
        });


        thumbnail3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Your code.
                //setDrawableImage("5nik.jpg");
                imagePath = imagePath2;
                homeDrwabletemp = Drawable.createFromPath(imagePath2);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("LVMH", "Set Main Image and thumbnail 3"+ imagePath );
                        home.setImageDrawable(homeDrwabletemp);
                        home.invalidate();

                    }
                });
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

    // fucntion to move from Home - FB&GMAIL sharing screen
    void homeScreen()
    {
        shareButton.setVisibility(View.VISIBLE);
        SharedImg.setVisibility(View.GONE);
        //gallery.setVisibility(GONE); // gallery button disabled
        //social.setVisibility(View.VISIBLE); //social buttons enabled
       // fbButton.setVisibility(View.VISIBLE);
       // mailButton.setVisibility(View.VISIBLE);

    }
    // fucntion to move from sharing screen FB&GMAIL to Shared successfull screen
    void successSharedScreen() {


        social.setVisibility(View.GONE); //social buttons enabled
        fbButton.setVisibility(View.GONE);
        mailButton.setVisibility(View.GONE);

        //enable shared image picture
        SharedImg.setVisibility(View.VISIBLE);
        findViewById(android.R.id.content).invalidate();

        try{
            Thread.sleep(5000);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        homeScreen();

    }


    public void  callFB()
    {
        final Dialog dialog1 = new Dialog(this);

        Log.d("LVMH", " opening dialog ");
        //call FB activity

        Intent i=new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);

    }

public void setDrawableImage()
{
    Log.d("LVMH", "called getDrawableImage" );

    File file = new File(android.os.Environment.getExternalStorageDirectory(), "Lvmh");
    imageCount = file.listFiles().length -1  ;

        String imagePath = Environment.getExternalStorageDirectory().toString() + "/lvmh/" +imageCount+"img.jpg";

        homeDrwable = Drawable.createFromPath(imagePath);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("LVMH", "Set Main Image and thumbnail " );
                home.setImageDrawable(homeDrwable);
                thumbnail1.setImageDrawable(homeDrwable);

                //home.setVisibility(View.VISIBLE);
                //home.invalidate();
                //thumbnail1.invalidate();

            }
        });

    int imageCount1 = imageCount - 1;
    imagePath1 = Environment.getExternalStorageDirectory().toString() + "/lvmh/" +imageCount1+"img.jpg";

    homeDrwable2 = Drawable.createFromPath(imagePath1);
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Log.d("LVMH", "Set thumbnail2 " );
            thumbnail2.setImageDrawable(homeDrwable2);
            //thumbnail2.invalidate();

        }
    });

    int imageCount2 = imageCount - 2;
     imagePath2 = Environment.getExternalStorageDirectory().toString() + "/lvmh/" +imageCount2+"img.jpg";
    homeDrwable3 = Drawable.createFromPath(imagePath2);
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Log.d("LVMH", "Set thumbnail3 " );
            thumbnail3.setImageDrawable(homeDrwable3);
            //thumbnail3.invalidate();

        }
    });
    }



    // function for file observer
    public void observer() {

        FileObserver fobsv = new FileObserver("/storage/emulated/0/lvmh/") {

            @Override
            public void onEvent(int event, String path) {

                // On every event we will have a new image in our application
                // we will only keep 3 , we will make copy of all images for backup and upload on google drive
                // if we are more or equal then 3 , Sync server will delete , this will happen in loop
                if(event == CREATE) {

                        Log.d("LVMH -  ", path);

                        setDrawableImage();

                    }


                }

                //findViewById(android.R.id.content).invalidate();


        };
        fobsv.startWatching();

    }

    private Bitmap ProcessingBitmap(){
        Bitmap bm1 ;
        Bitmap newBitmap = null;

        try {
            bm1 =  BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.shared);


            int w = bm1.getWidth();
            int h = bm1.getHeight();


            Bitmap.Config config = bm1.getConfig();
            if(config == null){
                config = Bitmap.Config.ARGB_8888;
            }

            newBitmap = Bitmap.createBitmap(w, h, config);
            Canvas newCanvas = new Canvas(newBitmap);

            newCanvas.drawBitmap(bm1, 0, 0, null);

            Paint paint = new Paint();
            paint.setAlpha(128);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return newBitmap;
    }

    void checkInternetSpeed()
    {
        //WifiInfo wifiInfo = WifiManager.getConnectionInfo();

        //int speedMbps = wifiInfo.getLinkSpeed();

       // Log.d("LVMH","Internet speed in MBPS = " +speedMbps);
    }


    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                    Log.d("LVMH","Mobile Internet present");
                }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected()) {
                    haveConnectedMobile = true;
                    Log.d("LVMH","Mobile Internet present");

                }
        }
        return haveConnectedWifi || haveConnectedMobile;
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
                String message = editTextName.getText().toString().trim();

                //Creating SendMail object
                SendMail sm = new SendMail(getWindow().getContext(), email, subject, message);

                //Executing send mail to send email
                sm.execute();

                dialog1.dismiss();// dismiss dialog and remove buttons


                successSharedScreen();


            }
        });
     dialog1.show();// opening mail dialog

    }
    };




