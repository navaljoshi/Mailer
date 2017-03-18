package com.example.naval.mailer;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;


import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import static android.view.View.GONE;
import static android.view.View.generateViewId;

public class MainActivity extends Activity  {

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
    public LinearLayout gallery ;
    public LinearLayout gallery1 ;
    public LinearLayout social  ;
    public ImageView home  ;
    public ImageView thumbnail1  ;
    public ImageView thumbnail2  ;
    public ImageView thumbnail3  ;
    public ImageView thumbnail4  ;
    public ImageView thumbnail5  ;
    public ImageView thumbnail6  ;
    public ImageView more  ;
    public ImageView less  ;
    public Drawable homeDrwable;
    public Drawable homeDrwabletemp;
    public Drawable homeDrwable2;
    public Drawable homeDrwable3;
    public Drawable homeDrwable4;
    public Drawable homeDrwable5;
    public Drawable homeDrwable6;
    public Drawable homeDrwable7;
    public TitanicTextView txtView ;

    // main path of the selected image

    public static  String imagePath ; // this will be used by Gmail & FB
    public String imagePath1 ;
    public String imagePath2 ;
    public String imagePath3 ;
    public String imagePath4 ;
    public String imagePath5 ;
    public String imagePath6 ;
    File file= new File(android.os.Environment.getExternalStorageDirectory(),"lvmh");
    public int imageCount  = file.listFiles().length+1;



 // Sync server variable intializations
  TextView infoIp, infoPort;
 static final int SocketServerPORT = 8880;
    ServerSocket serverSocket;

    ServerSocketThread serverSocketThread;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        syncServer(); // calling syncServer to start listening


        // Intializing Components
        SharedImg = (ImageView)findViewById(R.id.imgShared);
        shareButton = (Button) findViewById(R.id.btshr);
        mailButton = (Button) findViewById(R.id.btGmail);
        fbButton = (Button) findViewById(R.id.btFacebook);
        gallery  = (LinearLayout) findViewById(R.id.linGal);
        gallery1 = (LinearLayout) findViewById(R.id.linGal1);
        social  = (LinearLayout) findViewById(R.id.linSocial);
        home = (ImageView)findViewById(R.id.imgHome);
        thumbnail1 = (ImageView)findViewById(R.id.img1);
        thumbnail2 = (ImageView)findViewById(R.id.img2);
        thumbnail3 = (ImageView)findViewById(R.id.img3);
        thumbnail4 = (ImageView)findViewById(R.id.img4);
        thumbnail5 = (ImageView)findViewById(R.id.img5);
        thumbnail6 = (ImageView)findViewById(R.id.img6);
        more = (ImageView)findViewById(R.id.img7);
        less = (ImageView)findViewById(R.id.img8);

       // txtView = (TitanicTextView)findViewById(R.id.txt) ;


        //observer(); // start observing the pics folder

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

                //sendEmail(); //funtion to send EMAIl

                openGmail(MainActivity.this,"navaljosh@gmail.com","LVMH","hOW ARE YOU");

            }
        });


        fbButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.d("LVMH", "FB button clicked ");

                if( haveNetworkConnection())
                callFB(); // function to FB
                else
                {
                    //No internet connection here
                    Toast.makeText(getApplicationContext(), "No Internet Connection , Please Try Later",
                            Toast.LENGTH_LONG).show();

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
        thumbnail4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                imagePath = imagePath4;
                homeDrwabletemp = Drawable.createFromPath(imagePath4);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("LVMH", "Image Drawable 4 with Path: "+ imagePath );
                        home.setImageDrawable(homeDrwabletemp);
                        home.invalidate();

                    }
                });
            }
        });
        thumbnail5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                imagePath = imagePath5;
                homeDrwabletemp = Drawable.createFromPath(imagePath5);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("LVMH", "Image Drawable 5 with Path: "+ imagePath );
                        home.setImageDrawable(homeDrwabletemp);
                        home.invalidate();

                    }
                });
            }
        });
        thumbnail6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                imagePath = imagePath6;
                homeDrwabletemp = Drawable.createFromPath(imagePath6);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("LVMH", "Image Drawable 6 with Path: "+ imagePath );
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
                            setDrawableImage(imageCount );

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
                        home.setImageDrawable(homeDrwabletemp);
                        home.invalidate();


                            setDrawableImage(imageCount );




                    }
                });
            }
        });


    }


    void syncServer()
    {
        final AlertDialog alert;
        alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Alert Dialog With EditText"); //Set Alert dialog title here
        alert.setMessage("Enter Your Name Here"); //Message here

        // Set an EditText view to get user input
        alert.setTitle("LVMH Sync Server ");
        alert.setMessage(" Port:8880 & IP:"+getIpAddress());
        alert.setMessage(" press OK to start the Server ");




        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        serverSocketThread = new ServerSocketThread();
                        serverSocketThread.start();

                    }
        });

        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // here we can have static IP
            alert.cancel();
            }
        });

        alert.show();


    }
    // fucntion to move from Home - FB&GMAIL sharing screen
    void sharingScreen()
    {
        shareButton.setVisibility(GONE);//
        gallery.setVisibility(GONE); // gallery button disabled
        gallery1.setVisibility(GONE); // gallery button disabled
        social.setVisibility(View.VISIBLE); //social buttons enabled
        fbButton.setVisibility(View.VISIBLE);
        mailButton.setVisibility(View.VISIBLE);
    }

    // fucntion to move from Home - FB&GMAIL sharing screen
    void homeScreen()
    {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("LVMH", "Set Main Image and thumbnail 3"+ imagePath );
             //   txtView.setVisibility(View.VISIBLE);
                shareButton.setVisibility(View.VISIBLE);
                SharedImg.setVisibility(View.GONE);
                gallery.setVisibility(View.VISIBLE); // gallery button disabled
                gallery1.setVisibility(View.VISIBLE); // gallery button disabled

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
                social.setVisibility(View.GONE); //social buttons enabled
                fbButton.setVisibility(View.GONE);
                mailButton.setVisibility(View.GONE);
                shareButton.setVisibility(GONE);
                SharedImg.setVisibility(View.VISIBLE);

            }
        });

          new Timer().schedule(new TimerTask() {
              @Override
              public void run() {
                  // this code will be executed after 2 seconds
                  homeScreen();
              }
          }, 10000);



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



public void setDrawableImage(int count )
{
    Log.d("LVMH", "called getDrawableImage" );


    new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
            // this code will be executed after 5 seconds

        }
    }, 5000);



    Log.d("LVMH", "Path to start with is :" + imageCount);

    String imagePath = Environment.getExternalStorageDirectory().toString() + "/lvmh/" +imageCount +"img.jpg";


    int imageCount1 = imageCount - 1;
    imagePath1 = Environment.getExternalStorageDirectory().toString() + "/lvmh/" +imageCount1+"img.jpg";

    homeDrwable2 = Drawable.createFromPath(imagePath1);
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Log.d("LVMH", "Set thumbnail2 " );
            thumbnail1.setImageDrawable(homeDrwable2);
            home.setImageDrawable(homeDrwable2);
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
            thumbnail2.setImageDrawable(homeDrwable3);
            //thumbnail3.invalidate();

        }
    });

    int imageCount3 = imageCount - 3;
    imagePath3 = Environment.getExternalStorageDirectory().toString() + "/lvmh/" +imageCount3+"img.jpg";
    homeDrwable4 = Drawable.createFromPath(imagePath3);
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Log.d("LVMH", "Set thumbnail4 " );
            thumbnail3.setImageDrawable(homeDrwable4);
            //thumbnail3.invalidate();

        }
    });

    int imageCount4 = imageCount - 4 ;
    imagePath4 = Environment.getExternalStorageDirectory().toString() + "/lvmh/" +imageCount4+"img.jpg";
    homeDrwable5 = Drawable.createFromPath(imagePath4);
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Log.d("LVMH", "Set thumbnail5 " );
            thumbnail4.setImageDrawable(homeDrwable5);
            //thumbnail3.invalidate();

        }
    });

    int imageCount5 = imageCount - 5;
    imagePath5 = Environment.getExternalStorageDirectory().toString() + "/lvmh/" +imageCount5+"img.jpg";
    homeDrwable6 = Drawable.createFromPath(imagePath5);
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Log.d("LVMH", "Set thumbnail6 " );
            thumbnail5.setImageDrawable(homeDrwable6);
            //thumbnail3.invalidate();

        }
    });

    int imageCount6 = imageCount - 6;
    imagePath6 = Environment.getExternalStorageDirectory().toString() + "/lvmh/" +imageCount6+"img.jpg";
    homeDrwable7 = Drawable.createFromPath(imagePath6);
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Log.d("LVMH", "Set thumbnail7 " );
            thumbnail6.setImageDrawable(homeDrwable7);
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


                        setDrawableImage(file.listFiles().length);

                    }
                }
        };
        fobsv.startWatching();

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
                String subject = editTextName.getText().toString().trim();
                String message = "";

                //Creating SendMail object
                SendMail sm = new SendMail(getWindow().getContext(), email, subject, message);

                //Executing send mail to send email
                sm.execute();

                dialog1.dismiss();// dismiss dialog and remove buttons


                // timer here to wait

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // this code will be executed after 2 seconds
                        successSharedScreen();
                    }
                }, 15000);



            }
        });
     dialog1.show();// opening mail dialog

    }

    public  void openGmail(Activity activity,String email, String subject, String content) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/plain");
        emailIntent.setType("application/image");

        Uri uri = Uri.parse("file://" + imagePath);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
       // emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imagePath));
        Log.d("LVMH","GMAIl Path selected :"+imagePath);
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

        Log.d("LVMH","GMAIl succes here");
        successSharedScreen();
    }//onActivityResult



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
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    public class ServerSocketThread extends Thread {

        @Override
        public void run() {
            Socket socket = null;

            try {
                serverSocket = new ServerSocket(SocketServerPORT);


                while (true) {
                    Log.d("naval", "waiting for connection");
                    socket = serverSocket.accept();
                    Log.d("naval", "Got connection");
                    FileTxThread fileTxThread = new FileTxThread(socket);
                    Log.d("naval", "created file thread");
                    fileTxThread.start();
                    Log.d("naval", "started file thread");
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



                // Log.d("naval", "Tab file location :" + path);


                try {
                    if(imageCount>=28)// for LVMH we will save only 28 images and then we will replace them
                        imageCount=1;
                    fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/lvmh/"+imageCount+"img.jpg");

                }catch (IOException e)
                {
                    e.printStackTrace();
                }
                Log.d("naval", "setting path for file");
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                Log.d("naval-count pic","val"+imageCount);
                imageCount++;// count increase for file name


                System.out.println("Receiving...");
                Log.d("naval", "Receiving");

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

                if(imageCount>7)
                setDrawableImage(imageCount);// calle to populate UI

                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                inputStream.close();
                //clientSocket.close();
                //serverSocket.close();

                System.out.println("Sever recieved the file");
                Log.d("naval","server received file");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


        }
    }

    }




