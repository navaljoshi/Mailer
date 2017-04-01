package com.example.naval.mailer;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.crashlytics.android.Crashlytics;
import com.romainpiel.titanic.library.TitanicTextView;

import io.fabric.sdk.android.Fabric;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.GONE;

public class MainActivity extends Activity  {

    //variable Declarations
    File  newFile =  null;

    //Declaring EditText
    public EditText editTextEmail;
    public EditText editTextName;

    //Declaring Image View
    public ImageView SharedImg ;

    public  boolean sharingImage = false;
    public  static boolean fbFlagCancel = false;

    //buttons

    public Button shareButton ;
    public Button mailButton ;
    public Button fbButton ;
    public Button closeBtn;
    //public Button closeButton ;

    public LinearLayout gallery2 ;
    public LinearLayout gallery1 ;
    public LinearLayout social  ;
    public ImageView home  ;
    public ImageView thumbnail1  ;
    public ImageView thumbnail2  ;
    public ImageView thumbnail3  ;
    public ImageView more  ;
    public ImageView less  ;
    public ImageView homeButton  ;
    public Drawable homeDrwabletemp;
    public Drawable homeDrwable2;
    public Drawable homeDrwable3;
    public Drawable homeDrwable4;

    public TitanicTextView txtView ;

    // main path of the selected image

    public static  String imagePath ; // this will be used by Gmail & FB
    public String imagePath1 ;
    public String imagePath2 ;
    public String imagePath3 ;
    public String imagePath6 ;

    public int imageCount1;
    public int imageCount2;
    public int imageCount3;

    public final Dialog dialog1 =null ;
    private static Context context;


    File file= new File(android.os.Environment.getExternalStorageDirectory(),"lvmh");
    public int imageCount = file.listFiles().length;


    public static Context getAppContext() {
        return MainActivity.context;
    }

 // Sync server variable intializations
  TextView infoIp, infoPort;
 static final int SocketServerPORT = 8080;
    ServerSocket serverSocket;

    ServerSocketThread serverSocketThread;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Log.d("LVMH","Image count on start :"+imageCount);
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        Fabric.with(this, new Crashlytics());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        final PendingIntent intent = PendingIntent.getActivity(getApplication(), 0,
                new Intent(getIntent()), PendingIntent.FLAG_CANCEL_CURRENT);
        syncServer(); // calling syncServer to start listening
        final Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable ex) {
                AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, intent);
                System.exit(2);
                defaultHandler.uncaughtException(thread, ex);
            }
        });


        syncServer(); // calling syncServer to start listening


//        setDrawableImage(file.listFiles().length,false);


        // Intializing Components
        SharedImg = (ImageView)findViewById(R.id.imgShared);
        //SharedImg = null;
        shareButton = (Button) findViewById(R.id.btshr);
        mailButton = (Button) findViewById(R.id.btGmail);
        closeBtn = (Button) findViewById(R.id.btClose1);
        fbButton = (Button) findViewById(R.id.btFacebook);

        //gallery  = (LinearLayout) findViewById(R.id.linGal);
        gallery1 = (LinearLayout) findViewById(R.id.linGal1);
        gallery2 = (LinearLayout) findViewById(R.id.linShare);
        social  = (LinearLayout) findViewById(R.id.linSocial);
        home = (ImageView)findViewById(R.id.imgHome);

        thumbnail1 = (ImageView)findViewById(R.id.img4);
        thumbnail2 = (ImageView)findViewById(R.id.img5);
        thumbnail3 = (ImageView)findViewById(R.id.img6);
        more = (ImageView)findViewById(R.id.img7);
        less = (ImageView)findViewById(R.id.img8);
        //homeButton = (ImageView)findViewById(R.id.img9);




/*=================================== Button Event Listeners =================================================*/

        View view = getWindow().getDecorView();
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        // The user just touched the screen

                        break;
                    case MotionEvent.ACTION_UP:
                        // The touch just ended

                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                // this code will be executed after 100 seconds
                                homeScreen();
                            }
                        }, 100000);

                        break;
                }

                return false;
            }
        });


        shareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.d("LVMH", "share button clicked ");
                sharingImage = true;
                sharingScreen(); // call funtion to clear screen for FB&GMAIL sharing screen

            }
        });




        mailButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.d("LVMH", "MAIL button clicked ");

                //sendEmail(); //funtion to send EMAIl

                new InternetUtil().getSpeed(new AsyncResponse() {
                    @Override
                    public double getSpeed(double speed) {
                        double sp = speed;
                        Log.d("lvmh","speed:"+String.valueOf(sp));
                        return 0;
                    }
                });


                if( haveNetworkConnection())
                    sendEmail();
                else
                    openGmail(MainActivity.this,"mhiarmumbai@gmail.com","You images from Glenmorangie Augmented Reality Experience!","\n It was great having you at the Glenmorangie Augmented Reality experience zone.\n" +
                            "Please find you images attached .\n" +
                            "\n" +
                            "Best\n" +
                            "Team Glenmornagie");


            }
        });

        fbButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub



                if( haveNetworkConnection()) {
                    sharingImage = true; // true while sharing
                    callFB(); // function to FB
                }
                else
                {
                    //No internet connection here
                    Toast.makeText(getApplicationContext(), "No Internet Connection , Please Try Later or try offline Mail",
                            Toast.LENGTH_LONG).show();

                    homeScreen();

                }
            }
        });

        // ThumbNail click listners

        thumbnail1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                imagePath = imagePath1;
                homeDrwabletemp = Drawable.createFromPath(imagePath1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("LVMH", "Image Drawable 1 with Path: "+ imagePath );
                        home.setImageDrawable(homeDrwabletemp);
                        home.invalidate();

                    }
                });

            }
        });

        thumbnail2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                imagePath = imagePath2;

                homeDrwabletemp = Drawable.createFromPath(imagePath2);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("LVMH", "Image Drawable 2 with Path: "+ imagePath );
                        home.setImageDrawable(homeDrwabletemp);
                        home.invalidate();

                    }
                });

            }
        });


        thumbnail3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                imagePath = imagePath3;
                homeDrwabletemp = Drawable.createFromPath(imagePath3);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("LVMH", "Image Drawable 3 with Path: "+ imagePath );
                        home.setImageDrawable(homeDrwabletemp);
                        home.invalidate();

                    }
                });
            }
        });



        more.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Your code.

                homeDrwabletemp = Drawable.createFromPath(imagePath6);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("LVMH", "Clicked More images , Path: "+ imagePath );
                            setDrawableImageAdd(imageCount3,true);
                            imageCount3 = imageCount3 +2;
                    }
                });
            }
        });


        less.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Your code.

                imagePath = imagePath6;
                homeDrwabletemp = Drawable.createFromPath(imagePath6);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("LVMH", "Inside Less images button "+ imagePath );

                        setDrawableImage(imageCount1,true);
                        imageCount1 = imageCount1 - 2;

                    }
                });
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Your code.


                homeDrwabletemp = Drawable.createFromPath(imagePath6);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("LVMH", "clicked close button  "+ imagePath );

                 homeScreen();

                    }
                });
            }
        });
        setDrawableImage(file.listFiles().length -1,true);

    }



/*=================================== Button Event Listeners =================================================*/


    /*=================================== Functions  =================================================*/

    void syncServer()


    {

        serverSocketThread = new ServerSocketThread();
        serverSocketThread.start();

        /*
        Log.d("LVMH", "Inside syncServer");
        final AlertDialog alert;
        alert = new AlertDialog.Builder(this).create();


        // Set an EditText view to get user input
        alert.setTitle("LVMH Sync Server ");
        alert.setMessage(" Port:8080 & IP:");
        //alert.setMessage(" press OK to start the Server ");





        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
        });

        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // here we can have static IP
            alert.cancel();
            }
        });

        alert.show();*/

  /*      new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds
                homeScreen();
            }
        }, 5000);

        alert.cancel();*/


    }

    public void onShakeImage() {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                RelativeLayout image;
                image = (RelativeLayout) findViewById(R.id.activity_main);
                final Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                image.startAnimation(animShake);

                image.setAnimation(animShake);

                // now play music too
               // audioPlayer();

            }
        });

    }

    public void audioPlayer(){
        //set up MediaPlayer
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // fucntion to move from Home - FB&GMAIL sharing screen
    void sharingScreen()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("LVMH", "called sharingScreen");
                SharedImg.setVisibility(View.GONE);
                more.setVisibility(GONE);
                less.setVisibility(View.GONE);
                shareButton.setVisibility(GONE);//
                closeBtn.setVisibility(View.VISIBLE);
//        closeButton.setVisibility(View.VISIBLE);

                gallery1.setVisibility(GONE); // gallery button disabled
                social.setVisibility(View.VISIBLE); //social buttons enabled
                fbButton.setVisibility(View.VISIBLE);
                mailButton.setVisibility(View.VISIBLE);
            }
        });

    }

    // fucntion to move from Home - FB&GMAIL sharing screen
    void homeScreen()
    {
        sharingImage = false ;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("LVMH", "homeScreen"+ imagePath );

                social.setVisibility(View.GONE); //social buttons enabled
                fbButton.setVisibility(View.GONE);
                mailButton.setVisibility(View.GONE);
                closeBtn.setVisibility(GONE);
             //   txtView.setVisibility(View.VISIBLE);
                shareButton.setVisibility(View.VISIBLE);
                SharedImg.setVisibility(View.GONE);
                //gallery.setVisibility(View.VISIBLE); // gallery button disabled
                gallery1.setVisibility(View.VISIBLE); // gallery button disabled

                more.setVisibility(View.VISIBLE);
                less.setVisibility(View.VISIBLE);

            }
        });


    }
    // fucntion to move from sharing screen FB&GMAIL to Shared successfull screen
      void successSharedScreen() {

        //enable shared image picture

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("LVMH", "successSharedScreen"+ imagePath );
                closeBtn.setVisibility(GONE);
                social.setVisibility(View.GONE); //social buttons enabled
                fbButton.setVisibility(View.GONE);
                mailButton.setVisibility(View.GONE);
                shareButton.setVisibility(GONE);
                Drawable myDrawable = getResources().getDrawable(R.drawable.shared);
                SharedImg.setImageDrawable(myDrawable);
                SharedImg.setVisibility(View.VISIBLE);

            }
        });

          sharingImage = false;


          new Timer().schedule(new TimerTask() {
              @Override
              public void run() {
                  // this code will be executed after 2 seconds
                  homeScreen();
              }
          }, 5000);



    }


    public void  callFB()
    {
        final Dialog dialog1 = new Dialog(this);

        Log.d("LVMH", " opening dialog ");
        //call FB activity

        Intent i=new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(i, 1);
        //startActivity(i);

    }


public void resizeImage(int imageCount)

{
    Log.d("LVMH", " resizeImage path :"+BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + "/lvmh/" +imageCount +"img.jpg"));
    //File dir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    Bitmap b= BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + "/lvmh/" +imageCount +"img.jpg");
    Bitmap out = Bitmap.createScaledBitmap(b, 320, 480, false);

    File file = new File(Environment.getExternalStorageDirectory().toString() + "/lvmh/" +imageCount +"img.jpg");
    FileOutputStream fOut;
    try {
        fOut = new FileOutputStream(file);
        out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        fOut.flush();
        fOut.close();
        b.recycle();
        out.recycle();
    } catch (Exception e) {}
}

public void setDrawableImage(int count , boolean flag)
{

   try {
       Log.d("LVMH", "called getDrawableImage");

       //lets shake & play music
       if (flag) {
           // do not shake
       } else {
           onShakeImage();
       }
       // wait for 5 seconds . so we can decde image
       new Timer().schedule(new TimerTask() {
           @Override
           public void run() {
           }
       }, 5000);

       Log.d("LVMH", "Count to start with in setDrawableImage Local:" + count);
       Log.d("LVMH", "Count to start with in setDrawableImage :" + imageCount);
       imageCount1 = count;

       if (imageCount1 == -1 || imageCount1 == -2 || imageCount1 == -3 || imageCount1 == -4) {
           imageCount1 = file.listFiles().length - 1;
       }


       imagePath1 = Environment.getExternalStorageDirectory().toString() + "/lvmh/" + imageCount1 + "img.jpg";
       imagePath = imagePath1;
       homeDrwable2 = Drawable.createFromPath(imagePath1);
       Log.d("LVMH", "Set thumbnail2 " + imageCount1 + "img.jpg");
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               // Log.d("LVMH", "Set thumbnail2 " );
               thumbnail1.setImageDrawable(homeDrwable2);
               home.setImageDrawable(homeDrwable2);
               more.setVisibility(View.VISIBLE);
               less.setVisibility(View.VISIBLE);
               //thumbnail2.invalidate();

           }
       });
       imageCount2 = imageCount1 - 1;


       if (imageCount2 == -1 || imageCount2 == -2 || imageCount2 == -3 || imageCount2 == -4) {
           imageCount2 = file.listFiles().length - 1;
       }


       imagePath2 = Environment.getExternalStorageDirectory().toString() + "/lvmh/" + imageCount2 + "img.jpg";
       Log.d("LVMH", "Set thumbnail3 " + imageCount2 + "img.jpg");
       homeDrwable3 = Drawable.createFromPath(imagePath2);
       runOnUiThread(new Runnable() {
           @Override
           public void run() {

               thumbnail2.setImageDrawable(homeDrwable3);
               //thumbnail3.invalidate();

           }
       });
       imageCount3 = imageCount1 - 2;

       if (imageCount3 == -1 || imageCount3 == -2 || imageCount3 == -3 || imageCount3 == -4) {
           imageCount3 = file.listFiles().length - 2;
       }


       imagePath3 = Environment.getExternalStorageDirectory().toString() + "/lvmh/" + imageCount3 + "img.jpg";
       homeDrwable4 = Drawable.createFromPath(imagePath3);
       Log.d("LVMH", "Set thumbnail4 " + imageCount3 + "img.jpg");
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               // Log.d("LVMH", "Set thumbnail4 " );
               thumbnail3.setImageDrawable(homeDrwable4);
               //thumbnail3.invalidate();

           }
       });
   }
   catch (Exception e)
   {
       e.printStackTrace();
   }

}


    public void setDrawableImageAdd(int count , boolean flag)
    {
        Log.d("LVMH", "called getDrawableImage" );

        if(imageCount == file.listFiles().length)
        {
          count  = 0;
        }
        //lets shake & play music
        if(flag)
        {
            // do not shake
        }else {
            onShakeImage();
        }

        // wait for 5 seconds . so we can decde image
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
            }
        }, 2000);

        Log.d("LVMH", "Count to start with in setDrawableImageAdd Local:" + count);
        Log.d("LVMH", "Count to start with in setDrawableImageAdd :" + imageCount);
        imageCount1 = count ;

        if(imageCount1 == 27 || imageCount1 == 28 || imageCount1 == 29 ||imageCount1 == 30)
        {
            imageCount1 = 0;
        }
        if(imageCount3 == -1 || imageCount3 == -2 || imageCount3 == -3 ||imageCount3 == -4)
        {
            imageCount3 = file.listFiles().length - 2;
        }


        imagePath1 = Environment.getExternalStorageDirectory().toString() + "/lvmh/" +imageCount1+"img.jpg";
        imagePath = imagePath1;
        homeDrwable2 = Drawable.createFromPath(imagePath1);
        Log.d("LVMH", "Set thumbnail2 " + imageCount1+"img.jpg");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Log.d("LVMH", "Set thumbnail2 " );
                thumbnail1.setImageDrawable(homeDrwable2);
                home.setImageDrawable(homeDrwable2);
                more.setVisibility(View.VISIBLE);
                less.setVisibility(View.VISIBLE);
                //thumbnail2.invalidate();

            }
        });
        imageCount2 = imageCount1 + 1 ;


        if(imageCount2 == 27 || imageCount2 == 28 || imageCount2 == 29 ||imageCount2 == 30)
        {
            imageCount1 = 0;
        }



        imagePath2 = Environment.getExternalStorageDirectory().toString() + "/lvmh/" +imageCount2+"img.jpg";
        Log.d("LVMH", "Set thumbnail3 " + imageCount2+"img.jpg");
        homeDrwable3 = Drawable.createFromPath(imagePath2);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                thumbnail2.setImageDrawable(homeDrwable3);
                //thumbnail3.invalidate();

            }
        });
        imageCount3 = imageCount1 + 2 ;

        if(imageCount3 == 27 || imageCount3 == 28 || imageCount3 == 29 ||imageCount3 == 30)
        {
            imageCount3 = 0;
        }



        imagePath3 = Environment.getExternalStorageDirectory().toString() + "/lvmh/" +imageCount3+"img.jpg";
        homeDrwable4 = Drawable.createFromPath(imagePath3);
        Log.d("LVMH", "Set thumbnail4 " + imageCount3+"img.jpg");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Log.d("LVMH", "Set thumbnail4 " );
                thumbnail3.setImageDrawable(homeDrwable4);
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
                if((event == CREATE) ||  (event == MODIFY) ){

                        Log.d("LVMH -  ", path);
                    File file = new File(android.os.Environment.getExternalStorageDirectory(), "Lvmh");

try {
    setDrawableImage(file.listFiles().length, false);
}catch (OutOfMemoryError e)
{
    e.printStackTrace();
}
                    }
                }
        };
        fobsv.startWatching();

    }


    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        int spd ;




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

        final Button closeButton = (Button) dialog1.findViewById(R.id.btClose);

        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.d("LVMH", "close button clicked ");

                //closeButton.setVisibility(GONE);
                dialog1.dismiss();
                sharingScreen();

            }
        });


        mail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.d("LVMH", "mail button clicked ");

                //Changing image view
                //Getting content for email
                String email = editTextEmail.getText().toString().trim();
                String subject = editTextName.getText().toString().trim();
                String message = "";

                //Creating SendMail object
                final SendMail sm = new SendMail(getWindow().getContext(), email, subject, message);


                //Executing send mail to send email
                sm.execute();

                dialog1.dismiss();// dismiss dialog and remove buttons




                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (sm.mailSent != true) {
                            handler.postDelayed(this, 1000);
                        }
                            else{
                                successSharedScreen();
                            }
                        }

                }, 1000);



            }
        });
     dialog1.show();// opening mail dialog

    }

    public  void openGmail(Activity activity,String email, String subject, String content) {


        String gmailImagePath = imagePath;
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/plain");
        emailIntent.setType("application/image");

        Uri uri = Uri.parse("file://" + gmailImagePath);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
       // emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imagePath));
        Log.d("LVMH","GMAIl Path selected :"+gmailImagePath);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        final PackageManager pm = activity.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for(final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

        //activity.startActivity(emailIntent);
        startActivityForResult(emailIntent, 1);






    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Log.d("LVMH","GMAIl succes here");

        if(fbFlagCancel)
        {
// no sucess screen shared , intiated in FacebookFragment.java
        }
        else {
            successSharedScreen();
        }

    }//onActivityResult



    @Override
    public void onBackPressed() {


        return;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += inetAddress.getHostAddress() ;
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            Log.d("LVMH","issue with IP ");
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
      //  Log.d("LVMH","IP selected "+ip);
        return ip;
    }
    /*=================================== Button Event Listeners END =================================================*/


    /*=================================== Classes  =================================================*/
    public class ServerSocketThread extends Thread {

        @Override
        public void run() {
            Socket socket = null;


            try {
                Log.d("LVMH", "IP:"+getIpAddress());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(), "Listening on : Port:8880 IP:"+getIpAddress(),
                                Toast.LENGTH_LONG).show();
                    }
                });


                //InetAddress locIP = InetAddress.getByName("192.168.1.88");
                 InetAddress locIP = InetAddress.getByName(getIpAddress());
                serverSocket = new ServerSocket(8880, 10, locIP);


                while (true) {
                    Log.d("naval", "waiting for connection" );
                    socket = serverSocket.accept();
                    Log.d("LVMH", "Got connection");
                    FileTxThread fileTxThread = new FileTxThread(socket);

                    fileTxThread.start();
                    Log.d("LVMH", "started file thread");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public class FileTxThread extends Thread {
        private  ServerSocket serverSocket;
        private  Socket clientSocket;
        private InputStream inputStream;
        private FileOutputStream fileOutputStream;
        private BufferedOutputStream bufferedOutputStream;
        private  int filesize = 10000000; // filesize temporary hardcoded
        private  int bytesRead;
        private  int current = 0;
        Socket socket;

        FileTxThread(Socket socket){
            this.socket= socket;
            Log.d("naval", "constructor file thread initialized");
        }

        @Override
        public void run() {

            Log.d("naval", "Run function  file thread");
            byte[] mybytearray = new byte[filesize];    //create byte array to buffer the file
            try {
                inputStream = socket.getInputStream();
                Log.d("naval", "setting input stream");

                // check for SD card mounted
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                {
                    Log.d("naval", "Mounted");
                }

                try {
                    if(imageCount>=27)// for LVMH we will save only 27 images and then we will replace them
                        imageCount=0;
                    fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/lvmh/"+imageCount+"img.jpg");

                }catch (IOException e)
                {
                    e.printStackTrace();
                }

                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                Log.d("LVMH","Rceiving setting path"+imageCount);



                //System.out.println("Receiving...");
                Log.d("LVMH", "Receiving");

                //following lines read the input slide file byte by byte
                bytesRead = inputStream.read(mybytearray, 0, mybytearray.length);
                current = bytesRead;

                do {
                    bytesRead = inputStream.read(mybytearray, current, (mybytearray.length - current));
                    if (bytesRead >= 0) {
                        current += bytesRead;
                    }
                } while (bytesRead > -1);


                bufferedOutputStream.write(mybytearray, 0, current);

                //resizeImage(imageCount); // resize the image
                File file1= new File(android.os.Environment.getExternalStorageDirectory(),"lvmh");



                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                inputStream.close();

                // if we are sharing , we will wait .. images will get saved.. will not load .
                if(sharingImage ==  false) {
                    try {
                        if (file1.listFiles().length > 3)
                            setDrawableImage(imageCount, false);// calleD to populate UI
                    }catch (OutOfMemoryError e)
                    {
                        e.printStackTrace();
                    }
                }

                imageCount++;// count increase for file name
                //clientSocket.close();
                //serverSocket.close();

               // System.out.println("Sever received the file");
                Log.d("naval","server received file");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


        }
    }

    }




