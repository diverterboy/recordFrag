package com.utotech.danzhehplus.fragments;

import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.FFmpegExecution;
import com.utotech.danzhehplus.R;
import com.utotech.danzhehplus.databinding.FragmentRecordBinding;
import com.utotech.danzhehplus.util.NewRecorderTest;

import java.io.File;
import java.io.IOException;

public class RecordFragment extends Fragment {

    private TextView startTV, stopTV, playTV, stopplayTV, statusTV;
    // creating a variable for media recorder object class.

    FragmentRecordBinding binding;
    // creating a variable for mediaplayer class
    private MediaPlayer mPlayer;

    // constant for storing audio permission
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecordBinding.inflate(inflater, container, false);
        init();

        NewRecorderTest test = new NewRecorderTest();

        startTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start recording method will
                // start the recording of audio.

                // startRecording();

                if (CheckPermissions()) {

                    // setbackgroundcolor method will change
                    // the background color of text view.
                    stopTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
                    startTV.setBackgroundColor(getResources().getColor(R.color.gray));
                    playTV.setBackgroundColor(getResources().getColor(R.color.gray));
                    stopplayTV.setBackgroundColor(getResources().getColor(R.color.gray));

                    test.startRecording(getPath("testRecording"));
                    statusTV.setText(R.string.recordStarted);
                } else {
                    // if audio recording permissions are
                    // not granted by user below method will
                    // ask for runtime permission for mic and storage.
                    RequestPermissions();

                }
            }
        });
        stopTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pause Recording method will
                // pause the recording of audio.

                //  pauseRecording();
                test.stopRecording();

            }
        });
        playTV.setOnClickListener(v -> {
            // play audio method will play
            // the audio which we have recorded

            //  playAudio();
            test.playRecording();
        });
        stopplayTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pause play method will
                // pause the play of audio

                //  pausePlaying();
                test.stopPlayback();
            }
        });
        binding.txthead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();

            }
        });


        return binding.getRoot();
    }

    private void init() {
        statusTV = binding.idTVstatus;
        startTV = binding.btnRecord;
        stopTV = binding.btnStop;
        playTV = binding.btnPlay;
        stopplayTV = binding.btnStopPlay;
        stopTV.setBackgroundColor(getResources().getColor(R.color.gray));
        playTV.setBackgroundColor(getResources().getColor(R.color.gray));
        stopplayTV.setBackgroundColor(getResources().getColor(R.color.gray));
        // below method is used to initialize
        // the media recorder class
    }

    private void startRecording() {
        // check permission method is used to check
        // that the user has granted permission
        // to record and store the audio.
        if (CheckPermissions()) {

            // setbackgroundcolor method will change
            // the background color of text view.
            stopTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
            startTV.setBackgroundColor(getResources().getColor(R.color.gray));
            playTV.setBackgroundColor(getResources().getColor(R.color.gray));
            stopplayTV.setBackgroundColor(getResources().getColor(R.color.gray));


        } else {

            RequestPermissions();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // this method is called when user will
        // grant the permission for audio recording.
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean CheckPermissions() {
        // this method is used to check permission
        int result = ContextCompat.checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(requireContext(), RECORD_AUDIO);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        // this method is used to request the
        // permission for audio recording and storage.
        ActivityCompat.requestPermissions(requireActivity(), new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE, MANAGE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }


    public void playAudio() {
        stopTV.setBackgroundColor(getResources().getColor(R.color.gray));
        startTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
        playTV.setBackgroundColor(getResources().getColor(R.color.gray));
        stopplayTV.setBackgroundColor(getResources().getColor(R.color.purple_200));

        // for playing our recorded audio
        // we are using media player class.
        mPlayer = new MediaPlayer();
        try {
            // below method is used to set the
            // data source which will be our file name
            mPlayer.setDataSource(getPath("testRecording"));

            // below method will prepare our media player
            mPlayer.prepare();

            // below method will start our media player.
            mPlayer.start();
            statusTV.setText(R.string.recordingStartedPlaying);
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }

    public void pauseRecording() {
        stopTV.setBackgroundColor(getResources().getColor(R.color.gray));
        startTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
        playTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
        stopplayTV.setBackgroundColor(getResources().getColor(R.color.purple_200));


        statusTV.setText(R.string.recordingStopped);
    }

    public void pausePlaying() {
        // this method will release the media player
        // class and pause the playing of our recorded audio.
        mPlayer.release();
        mPlayer = null;
        stopTV.setBackgroundColor(getResources().getColor(R.color.gray));
        startTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
        playTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
        stopplayTV.setBackgroundColor(getResources().getColor(R.color.gray));
        statusTV.setText(R.string.recordingPlayStopped);
    }

    private String getPath(String name) {
        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, name + ".mp3"); //todo make it 3gp

        return file.getPath();
    }

    private void test() {
        String[] c = {"-i", Environment.getExternalStorageDirectory().getPath()
                + "/Download/Ttt.mp4"
                , "-i", getPath("testRecording")
                /*+ "/Download/Download unavailable-480p.m4a"*/
                , "-c:v", "copy", "-c:a", "aac", "-map", "0:v:0", "-map", "1:a:0", "-shortest",
                Environment.getExternalStorageDirectory().getPath()
                        + "/Download/2MergeVideo.mp4"};
        MergeVideo(c);
    }

    private void MergeVideo(String[] co) {
        FFmpeg.executeAsync(co, new ExecuteCallback() {
            @Override
            public void apply(long executionId, int returnCode) {
                Log.d("hello", "return  " + returnCode);
                Log.d("hello", "executionID  " + executionId);
                Log.d("hello", "FFMPEG  " + new FFmpegExecution(executionId, co));

            }
        });


    }


}