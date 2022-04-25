package com.example.streameasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pedro.encoder.input.gl.SpriteGestureController;
import com.pedro.encoder.input.gl.render.filters.object.TextObjectFilterRender;
import com.pedro.encoder.utils.gl.TranslateTo;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;
import com.pedro.rtplibrary.view.OpenGlView;
import com.pedro.rtmp.utils.ConnectCheckerRtmp;


public class MainActivity extends AppCompatActivity implements ConnectCheckerRtmp, /*View.OnClickListener, */SurfaceHolder.Callback, View.OnTouchListener  {

    private RtmpCamera1 rtmpCamera1;
    private ImageButton imgbtnStream, imgbtnSettings;
    private TextView txtInfo;
    SharedPreferences sharedPreferences;

    private final SpriteGestureController spriteGestureController = new SpriteGestureController();

    private final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }

        updatePrefs();


        OpenGlView openGlView = findViewById(R.id.surfaceView);
        openGlView.setMinimumHeight(sharedPreferences.getInt("height", 720)); // TODO
        openGlView.setMinimumWidth(sharedPreferences.getInt("width", 1280)); // TODO
//        openGlView.setMinimumHeight(1080);
//        openGlView.setMinimumWidth(1920);

        imgbtnStream = findViewById(R.id.imgbtn_stream);
        imgbtnSettings = findViewById(R.id.imgbtn_settings);
        txtInfo = findViewById(R.id.txt_info);

        if (!sharedPreferences.getString("destination", "Not Set").equals("Not Set") && !sharedPreferences.getString("destination", "Not Set").equals("")) {
            txtInfo.setText("Destination: " + sharedPreferences.getString("destination", "Not Set"));
            txtInfo.setTextColor(Color.GREEN);
        } else {
            txtInfo.setText("Destination Server Not Configured");
            txtInfo.setTextColor(Color.RED);
        }

        rtmpCamera1 = new RtmpCamera1(openGlView, this);
        rtmpCamera1.setReTries(10);


        openGlView.getHolder().addCallback(this);
        openGlView.setOnTouchListener(this);

        imgbtnStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePrefs();
                if (!rtmpCamera1.isStreaming()) {
                    if (rtmpCamera1.prepareAudio(Integer.parseInt((sharedPreferences.getString("bitrate_audio", "128"))) * 1024,
                            Integer.parseInt((sharedPreferences.getString("sample_rate", "48"))) * 1024, true, false, false)
                            && rtmpCamera1.prepareVideo(sharedPreferences.getInt("width", 1280),
                            sharedPreferences.getInt("height", 720), Integer.parseInt(sharedPreferences.getString("fps", "60")),
                            Integer.parseInt(sharedPreferences.getString("bitrate", "6000") ) * 1024, 0)) { // TODO
                        if (sharedPreferences.getString("destination", "Not Set").equals("Not Set") || sharedPreferences.getString("destination", "Not Set").equals("")) {
                            Toast.makeText(MainActivity.this, "Stream Server Not Configured", Toast.LENGTH_SHORT).show();
                        } else {
                            rtmpCamera1.startStream(sharedPreferences.getString("destination", "Not Set"));
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Error preparing stream, this device cant do it", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    imgbtnStream.setImageResource(R.drawable.ic_stream_off);
                    rtmpCamera1.stopStream();
                    MainActivity.this.recreate();
                }
            }
        });

        imgbtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setTextToStream() {
        TextObjectFilterRender textObjectFilterRender = new TextObjectFilterRender();
        rtmpCamera1.getGlInterface().setFilter(textObjectFilterRender);
        textObjectFilterRender.setText("Hello world", 80, Color.RED);
        textObjectFilterRender.setDefaultScale(sharedPreferences.getInt("width", 1280), sharedPreferences.getInt("height", 720)); // TODO
//        textObjectFilterRender.setDefaultScale(1920, 1080);
        textObjectFilterRender.setPosition(TranslateTo.CENTER);
        spriteGestureController.setBaseObjectFilterRender(textObjectFilterRender); //Optional
    }

    @Override
    public void onAuthErrorRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Auth error", Toast.LENGTH_SHORT).show();
                rtmpCamera1.stopStream();
                imgbtnStream.setImageResource(R.drawable.ic_stream_off);
            }
        });
    }

    @Override
    public void onAuthSuccessRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Auth success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionFailedRtmp(String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rtmpCamera1.reTry(5000, s, null)) {
                    Toast.makeText(MainActivity.this, "Retry", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Connection failed. " + s, Toast.LENGTH_SHORT).show();
                    rtmpCamera1.stopStream();
                    MainActivity.this.recreate();

                }
            }
        });
    }

    @Override
    public void onConnectionStartedRtmp(String s) {

    }

    @Override
    public void onConnectionSuccessRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Connection success", Toast.LENGTH_SHORT).show();
                imgbtnStream.setImageResource(R.drawable.ic_stream_blinking);
                ((AnimationDrawable) imgbtnStream.getDrawable()).start();
            }
        });

    }

    @Override
    public void onDisconnectRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
                imgbtnStream.setImageResource(R.drawable.ic_stream_off);
                MainActivity.this.recreate();
            }
        });

    }

    @Override
    public void onNewBitrateRtmp(long l) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (spriteGestureController.spriteTouched(v, event)) {
            v.performClick();
            spriteGestureController.moveSprite(v, event);
            spriteGestureController.scaleSprite(event);
            return true;
        }
        return false;
    }

    public void updatePrefs() { // TODO this may not be needed at all
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
//        rtmpCamera1.startPreview(1920, 1080); // TODO
        rtmpCamera1.startPreview(sharedPreferences.getInt("width", 1280), sharedPreferences.getInt("height", 720));
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        if (rtmpCamera1.isStreaming()) {
            rtmpCamera1.stopStream();
            MainActivity.this.recreate();
            imgbtnStream.setImageResource(R.drawable.ic_stream_off);
        }
        rtmpCamera1.stopPreview();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MainActivity.this.recreate();

    }
}