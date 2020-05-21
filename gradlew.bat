package com.ht.text2speech;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

import java.io.IOException;
import java.util.List;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import fi.iki.elonen.NanoHTTPD;

public class MainActivity extends AppCompatActivity {

    static ControllerThread controllerThread;
    static Model model;

    final String TAG = "ahihi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controllerThread = new ControllerThread();
        controllerThread.start();

        model = new Model();

        try {
            new HttpServer(controllerThread);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class AddTrack2List implements Runnable {

        Bundle newTrack;

        AddTrack2List(Bundle trackInfo) {
            newTrack = trackInfo;
        }

        @Override
        public void run() {
            model.getTrackList("ahihi").add(newTrack);
        }
    }

    class Model {
        List<Bundle> mTrackList;

        public List<Bundle> getTrackList(String listName) {
            return mTrackList;
        }
    }

    class RequestYtbRunnable implements Runnable {
        String youtubeVideoId;
        final String strYoutubeBase = "http://youtube.com/watch?v=";

        RequestYtbRunnable(String ytbVideoId) {
            youtubeVideoId = strYoutubeBase + ytbVideoId;
            return;
        }

        @Override
        public void run() {
            // Request a string response from the provided URL.
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, youtubeVideoId,
//                    new Response.Listener<String>() {
//                        @Override
//                        public v