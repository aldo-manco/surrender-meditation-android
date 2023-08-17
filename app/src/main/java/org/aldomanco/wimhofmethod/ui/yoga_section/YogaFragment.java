package org.aldomanco.wimhofmethod.ui.yoga_section;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.aldomanco.wimhofmethod.MainActivity;
import org.aldomanco.wimhofmethod.R;
import org.aldomanco.wimhofmethod.databinding.FragmentWimHofBreathSectionBinding;
import org.aldomanco.wimhofmethod.databinding.FragmentYogaSectionBinding;
import org.aldomanco.wimhofmethod.ui.wim_hof_breath_section.WimHofBreathService;
import org.aldomanco.wimhofmethod.ui.wim_hof_breath_section.WimHofBreathViewModel;

import static android.content.Context.BIND_AUTO_CREATE;

import java.util.Calendar;

public class YogaFragment extends Fragment implements View.OnClickListener {

    private YogaViewModel yogaViewModel;
    private FragmentYogaSectionBinding binding;

    private TextView textShower;
    private TextView textShowerWithoutCleaningPhase;

    private ImageView playShortSession;
    private ImageView playLongSession;

    private Intent startFirstRound;
    private Intent startSecondRound;

    private Button buttonStopRound;
    private Button buttonPauseRound;

    private boolean stopPressed;
    private boolean isPaused;

    boolean firstServiceBounded;
    boolean secondServiceBounded;

    YogaService firstService;
    YogaService secondService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        stopPressed = false;
        isPaused = false;

        firstServiceBounded = false;
        secondServiceBounded = false;

        binding = FragmentYogaSectionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textShower = root.findViewById(R.id.textRecoveryYogaShort);
        textShowerWithoutCleaningPhase = root.findViewById(R.id.textRecoveryYogaLong);

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        // Set the text according to the current hour and day of the week
        if (currentHour >= 4 && currentHour < 18) {
            textShower.setText("BODY CLEANSING SHOWER");
            textShowerWithoutCleaningPhase.setText("WIM HOF CLEANSING SHOWER");
        } else if (currentHour >= 18) {
            if (dayOfWeek == Calendar.SUNDAY) {
                textShower.setText("BODY SHAVING\nHAIR CLEANSING SHOWER\nHAIR CURLS DEFINITION\n\nCLICK HERE");
                textShowerWithoutCleaningPhase.setText("CONTRAST CLEANSING SHOWER");
            } else if (dayOfWeek == Calendar.TUESDAY || dayOfWeek == Calendar.THURSDAY) {
                textShower.setText("HAIR CLEANSING SHOWER\n\nCLICK HERE");
                textShowerWithoutCleaningPhase.setText("CONTRAST CLEANSING SHOWER");
            } else {
                textShower.setText("HAIR CLEANSING SHOWER");
                textShowerWithoutCleaningPhase.setText("CONTRAST CLEANSING SHOWER\n\nCLICK HERE");
            }
        } else if (currentHour < 4) {
            if (dayOfWeek == Calendar.MONDAY) {
                textShower.setText("BODY SHAVING\nHAIR CLEANSING SHOWER\nHAIR CURLS DEFINITION\n\nCLICK HERE");
                textShowerWithoutCleaningPhase.setText("CONTRAST CLEANSING SHOWER");
            } else if (dayOfWeek == Calendar.WEDNESDAY || dayOfWeek == Calendar.FRIDAY) {
                textShower.setText("HAIR CLEANSING SHOWER\n\nCLICK HERE");
                textShowerWithoutCleaningPhase.setText("CONTRAST CLEANSING SHOWER");
            } else {
                textShower.setText("HAIR CLEANSING SHOWER");
                textShowerWithoutCleaningPhase.setText("CONTRAST CLEANSING SHOWER\n\nCLICK HERE");
            }
        }

        yogaViewModel =
                new ViewModelProvider(this).get(YogaViewModel.class);

        final TextView textView = binding.textDashboard;
        buttonStopRound = binding.buttonStopYoga;
        buttonPauseRound = binding.buttonPauseYoga;
        playShortSession = binding.playShortSession;
        playLongSession = binding.playLongSession;

        yogaViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        buttonStopRound.setOnClickListener(this);
        buttonPauseRound.setOnClickListener(this);
        playShortSession.setOnClickListener(this);
        playLongSession.setOnClickListener(this);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (firstServiceBounded) {
            getActivity().unbindService(firstConnection);
            firstServiceBounded = false;

        } else if (secondServiceBounded) {
            getActivity().unbindService(secondConnection);
            secondServiceBounded = false;

        }

        FragmentManager manager = ((Fragment) this).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((Fragment) this);
        trans.commit();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.playShortSession:
            case R.id.textRecoveryYogaShort:

                stopPressed = false;

                if (secondServiceBounded) {
                    getActivity().unbindService(secondConnection);
                    secondServiceBounded = false;
                }

                startFirstRound = new Intent(getActivity(), YogaService.class);
                startFirstRound.putExtra("length", 0);
                getActivity().bindService(startFirstRound, firstConnection, BIND_AUTO_CREATE);
                Toast.makeText(getActivity(), "Starting Cleansing Shower",Toast.LENGTH_SHORT).show();

                break;

            case R.id.playLongSession:
            case R.id.textRecoveryYogaLong:

                stopPressed = false;

                if (firstServiceBounded) {
                    getActivity().unbindService(firstConnection);
                    firstServiceBounded = false;
                }

                startSecondRound = new Intent(getActivity(), YogaService.class);
                startSecondRound.putExtra("length", 1);
                getActivity().bindService(startSecondRound, secondConnection, BIND_AUTO_CREATE);
                Toast.makeText(getActivity(), "Starting Cleansing Shower without Cleaning Phase",Toast.LENGTH_SHORT).show();

                break;

            case R.id.buttonStopYoga:

                if (!stopPressed) {

                    if (firstServiceBounded) {
                        getActivity().unbindService(firstConnection);
                        firstServiceBounded = false;
                        Toast.makeText(getActivity(), "Stopped Cleansing Shower",Toast.LENGTH_SHORT).show();

                    } else if (secondServiceBounded) {
                        getActivity().unbindService(secondConnection);
                        secondServiceBounded = false;
                        Toast.makeText(getActivity(), "Stopped Cleansing Shower without Cleaning Phase",Toast.LENGTH_SHORT).show();
                    }

                    stopPressed = true;
                }

                break;

            case R.id.buttonPauseYoga:

                if (firstServiceBounded) {

                    if (firstService.isPlaying()){
                        buttonPauseRound.setText("RESUME ROUND");
                        firstService.pauseMediaPlayer();
                        Toast.makeText(getActivity(), "Paused Cleansing Shower",Toast.LENGTH_SHORT).show();
                    }else {
                        buttonPauseRound.setText(" PAUSE ROUND ");
                        firstService.resumeMediaPlayer();
                        Toast.makeText(getActivity(), "Resumed Cleansing Shower",Toast.LENGTH_SHORT).show();
                    }

                } else if (secondServiceBounded) {

                    if (secondService.isPlaying()){
                        buttonPauseRound.setText("RESUME ROUND");
                        secondService.pauseMediaPlayer();
                        Toast.makeText(getActivity(), "Paused Cleansing Shower without Cleaning Phase",Toast.LENGTH_SHORT).show();
                    }else {
                        buttonPauseRound.setText(" PAUSE ROUND ");
                        secondService.resumeMediaPlayer();
                        Toast.makeText(getActivity(), "Resumed Cleansing Shower without Cleaning Phase",Toast.LENGTH_SHORT).show();
                    }

                }

                break;
        }
    }

    ServiceConnection firstConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            firstServiceBounded = false;
            firstService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            firstServiceBounded = true;
            YogaService.LocalBinderYoga firstLocalBinder = (YogaService.LocalBinderYoga) service;
            firstService = firstLocalBinder.getServerInstance();
        }
    };

    ServiceConnection secondConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            secondServiceBounded = false;
            secondService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            secondServiceBounded = true;
            YogaService.LocalBinderYoga secondLocalBinder = (YogaService.LocalBinderYoga) service;
            secondService = secondLocalBinder.getServerInstance();
        }
    };
}