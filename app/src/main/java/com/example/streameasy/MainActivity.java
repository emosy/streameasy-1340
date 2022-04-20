package com.example.streameasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pedro.encoder.input.gl.SpriteGestureController;
import com.pedro.encoder.input.gl.render.filters.object.TextObjectFilterRender;
import com.pedro.encoder.utils.gl.TranslateTo;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;
import com.pedro.rtplibrary.view.OpenGlView;
import com.pedro.rtmp.utils.ConnectCheckerRtmp;

public class MainActivity extends AppCompatActivity implements ConnectCheckerRtmp, View.OnClickListener,
        SurfaceHolder.Callback, View.OnTouchListener  {

    private RtmpCamera1 rtmpCamera1;
    private ImageButton imgbtnStream, imgbtnSettings;
    private EditText etUrl;

    private final SpriteGestureController spriteGestureController = new SpriteGestureController();

    private final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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

        OpenGlView openGlView = findViewById(R.id.surfaceView);
        openGlView.setMinimumHeight(1080);
        openGlView.setMinimumWidth(1920);

        imgbtnStream = findViewById(R.id.imgbtn_stream);
        imgbtnSettings = findViewById(R.id.imgbtn_settings);
        etUrl = findViewById(R.id.editTextTextPersonName);
        etUrl.setHint("Server Address");

        rtmpCamera1 = new RtmpCamera1(openGlView, this);
        rtmpCamera1.setReTries(10);

        openGlView.getHolder().addCallback(this);
        openGlView.setOnTouchListener(this);

        imgbtnStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rtmpCamera1.isStreaming()) {
                    if (rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo(1920, 1080,  30, 6500 * 1024, 0)) {
                        imgbtnStream.setImageResource(R.drawable.ic_stream_blinking);
                        rtmpCamera1.startStream("rtmp://36bay2.tulix.tv/ryanios/channel4");
                        setTextToStream();

                    } else {
                        Toast.makeText(MainActivity.this, "Error preparing stream, This device cant do it",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    imgbtnStream.setImageResource(R.drawable.ic_stream_off);
                    rtmpCamera1.stopStream();
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
        textObjectFilterRender.setDefaultScale(1920,
                1080);
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
                    Toast.makeText(MainActivity.this, "Retry", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(MainActivity.this, "Connection failed. " + s, Toast.LENGTH_SHORT)
                            .show();
                    rtmpCamera1.stopStream();
                    imgbtnStream.setImageResource(R.drawable.ic_stream_off);
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
            }
        });
    }

    @Override
    public void onDisconnectRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNewBitrateRtmp(long l) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn_stream:
                Toast.makeText(this, "NOT WORKING", Toast.LENGTH_SHORT).show();
                if (!rtmpCamera1.isStreaming()) {
                    if (rtmpCamera1.isRecording()
                            || rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()) {
                        imgbtnStream.setImageResource(R.drawable.ic_stream_blinking);
                        rtmpCamera1.startStream(etUrl.getText().toString());
                    } else {
                        Toast.makeText(this, "Error preparing stream, This device cant do it",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    imgbtnStream.setImageResource(R.drawable.ic_stream_off);
                    rtmpCamera1.stopStream();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        rtmpCamera1.startPreview(1920, 1080);
        setTextToStream();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        if (rtmpCamera1.isStreaming()) {
            rtmpCamera1.stopStream();
            imgbtnStream.setImageResource(R.drawable.ic_stream_off);
        }
        rtmpCamera1.stopPreview();
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

}