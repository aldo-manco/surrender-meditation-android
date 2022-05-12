package org.aldomanco.wimhofmethod.ui.prana_breath_section;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.aldomanco.wimhofmethod.R;
import org.aldomanco.wimhofmethod.databinding.FragmentPranaBreathSectionBinding;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.AUDIO_SERVICE;

public class PranaBreathFragment extends Fragment implements View.OnClickListener {

    private PranaBreathViewModel pranaBreathViewModel;
    private FragmentPranaBreathSectionBinding binding;

    private SeekBar setTimer;
    private Button startOrStop;
    private ImageView background;

    private Button button5minutes;
    private Button button10minutes;
    private Button button15minutes;
    private Button button20minutes;
    private Button button30minutes;
    private Button button40minutes;

    private Button sectionTimeLeft;
    private Button sectionSeekBar;

    private TextView timeLeft;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pranaBreathViewModel =
                new ViewModelProvider(this).get(PranaBreathViewModel.class);

        binding = FragmentPranaBreathSectionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        pranaBreathViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        setTimer = binding.setTimer;
        startOrStop = binding.start;
        background = binding.imageView;

        button5minutes = binding.button5minutes;
        button10minutes = binding.button10minutes;
        button15minutes = binding.button15minutes;
        button20minutes = binding.button20minutes;
        button30minutes = binding.button30minutes;
        button40minutes = binding.button40minutes;

        sectionTimeLeft = binding.sectionTimeLeft;
        sectionSeekBar = binding.sectionSeekBar;

        timeLeft = binding.countdown;

        startOrStop.setOnClickListener(this);
        button5minutes.setOnClickListener(this);
        button10minutes.setOnClickListener(this);
        button15minutes.setOnClickListener(this);
        button20minutes.setOnClickListener(this);
        button30minutes.setOnClickListener(this);
        button40minutes.setOnClickListener(this);

        startOrStop.bringToFront();

        Date currentTime = Calendar.getInstance().getTime();

        if (currentTime.getHours() >= 21 || currentTime.getHours() <= 5) {
            background.setImageResource(R.drawable.night);
            sectionTimeLeft.setBackgroundColor(getResources().getColor(R.color.black));
            sectionSeekBar.setBackgroundColor(getResources().getColor(R.color.black));
            timeLeft.setTextColor(getResources().getColor(R.color.white));
        } else if (currentTime.getHours() >= 8 && currentTime.getHours() <= 18) {
            background.setImageResource(R.drawable.morning);
            sectionTimeLeft.setBackgroundColor(getResources().getColor(R.color.white));
            sectionSeekBar.setBackgroundColor(getResources().getColor(R.color.white));
            timeLeft.setTextColor(getResources().getColor(R.color.white));
        } else {
            background.setImageResource(R.drawable.sunset);
            sectionTimeLeft.setBackgroundColor(getResources().getColor(R.color.orange));
            sectionSeekBar.setBackgroundColor(getResources().getColor(R.color.orange));
            timeLeft.setTextColor(getResources().getColor(R.color.white));
        }

        setTimer.setMax(1152);
        setTimer.setProgress(1152);

        setTimer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int minutes = progress / 60;
                int seconds = progress % 60;

                if (minutes < 10 && seconds < 10) {
                    timeLeft.setText("0" + minutes + ":0" + seconds);
                } else if (minutes < 10) {
                    timeLeft.setText("0" + minutes + ":" + seconds);
                } else if (seconds < 10) {
                    timeLeft.setText(minutes + ":0" + seconds);
                } else {
                    timeLeft.setText(minutes + ":" + seconds);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int resto = setTimer.getProgress() % 18;

                if (resto == 0) {
                    setTimer.setProgress(setTimer.getProgress());
                } else {
                    setTimer.setProgress(setTimer.getProgress() + ((18 * resto / resto) - resto));
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    boolean active = false;

    protected void playAlarm() {

        this.active = false;

        final MediaPlayer alarm = MediaPlayer.create(getActivity(), R.raw.temple_bell);
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(AUDIO_SERVICE);
        int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, 0);
        alarm.start();
    }

    public void start(View view) {

        final Handler handler = new Handler();
        final Runnable runnable;
        final Button startOrStop = (Button) view;

        if (!active) {
            this.active = true;
            runnable = new Runnable() {
                MediaPlayer tick = MediaPlayer.create(getActivity(), R.raw.breath);

                @Override
                public void run() {

                    if (active && setTimer.getProgress() != 0) {

                        startOrStop.setText("STOP");
                        setTimer.setEnabled(false);
                        tick.setLooping(true);
                        tick.start();
                        setTimer.setProgress(setTimer.getProgress() - 1);
                        handler.postDelayed(this, 1000);

                    } else if (setTimer.getProgress() == 0) {

                        startOrStop.setText("START");
                        setTimer.setEnabled(true);
                        tick.setLooping(false);
                        tick.stop();
                        playAlarm();
                        setTimer.setProgress(setTimer.getMax());

                    } else if (!active) {

                        startOrStop.setText("START");
                        setTimer.setEnabled(true);
                        tick.setLooping(false);
                        tick.stop();
                        playAlarm();

                        int resto = setTimer.getProgress() % 18;

                        if (resto==0){
                            setTimer.setProgress(setTimer.getProgress());
                        }else{
                            setTimer.setProgress(setTimer.getProgress() + ((18 * resto / resto) - resto));
                        }
                    }
                }
            };

            handler.post(runnable);

        } else if (active) {

            setTimer.setEnabled(true);
            startOrStop.setText("START");
            this.active = false;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.start:
                start(view);
                break;

            case R.id.button5minutes:
                setTimer.setMax(288);
                setTimer.setProgress(288);
                break;

            case R.id.button10minutes:
                setTimer.setMax(576);
                setTimer.setProgress(576);
                break;

            case R.id.button15minutes:
                setTimer.setMax(864);
                setTimer.setProgress(864);
                break;

            case R.id.button20minutes:
                setTimer.setMax(1152);
                setTimer.setProgress(1152);
                break;

            case R.id.button30minutes:
                setTimer.setMax(1728);
                setTimer.setProgress(1728);
                break;

            case R.id.button40minutes:
                setTimer.setMax(2304);
                setTimer.setProgress(2304);
                break;
        }
    }
}