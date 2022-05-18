package org.aldomanco.wimhofmethod.ui.wim_hof_breath_section;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import static android.content.Context.BIND_AUTO_CREATE;

public class WimHofBreathFragment extends Fragment implements View.OnClickListener {

    private WimHofBreathViewModel wimHofBreathViewModel;
    private FragmentWimHofBreathSectionBinding binding;

    private Intent startFirstRound;
    private Intent startSecondRound;
    private Intent startThirdRound;

    private TextView textView;
    private ConstraintLayout layout;
    private Button buttonStopRound;
    private Button buttonPauseRound;

    private boolean stopPressed;
    private boolean isPaused;

    boolean firstServiceBounded;
    boolean secondServiceBounded;
    boolean thirdServiceBounded;

    WimHofBreathService firstService;
    WimHofBreathService secondService;
    WimHofBreathService thirdService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        stopPressed = false;
        isPaused = false;

        firstServiceBounded = false;
        secondServiceBounded = false;
        thirdServiceBounded = false;

        wimHofBreathViewModel = new ViewModelProvider(this).get(WimHofBreathViewModel.class);

        binding = FragmentWimHofBreathSectionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textView = binding.textHome;
        layout = binding.layout;
        buttonStopRound = binding.buttonStopRound;
        buttonPauseRound = binding.buttonPauseRound;

        wimHofBreathViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        textView.setOnClickListener(this);
        layout.setOnClickListener(this);
        buttonStopRound.setOnClickListener(this);
        buttonPauseRound.setOnClickListener(this);

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

        } else if (thirdServiceBounded) {
            getActivity().unbindService(thirdConnection);
            thirdServiceBounded = false;
        }

        FragmentManager manager = ((Fragment) this).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((Fragment) this);
        trans.commit();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.layout:
            case R.id.text_home:

                MainActivity.numberOfRounds++;
                wimHofBreathViewModel.setTextView(MainActivity.numberOfRounds, textView);

                int remaining = MainActivity.numberOfRounds % 3;
                stopPressed = false;

                switch (remaining) {

                    case 1:

                        if (secondServiceBounded) {
                            getActivity().unbindService(secondConnection);
                            secondServiceBounded = false;
                        }
                        if (thirdServiceBounded) {
                            getActivity().unbindService(thirdConnection);
                            thirdServiceBounded = false;
                        }

                        startFirstRound = new Intent(getActivity(), WimHofBreathService.class);
                        startFirstRound.putExtra("remaining", 0);
                        getActivity().bindService(startFirstRound, firstConnection, BIND_AUTO_CREATE);

                        break;

                    case 2:

                        if (firstServiceBounded) {
                            getActivity().unbindService(firstConnection);
                            firstServiceBounded = false;
                        }
                        if (thirdServiceBounded) {
                            getActivity().unbindService(thirdConnection);
                            thirdServiceBounded = false;
                        }

                        startSecondRound = new Intent(getActivity(), WimHofBreathService.class);
                        startSecondRound.putExtra("remaining", 1);
                        getActivity().bindService(startSecondRound, secondConnection, BIND_AUTO_CREATE);

                        break;

                    case 0:

                        if (firstService != null && firstService.isPlaying()) {
                            getActivity().unbindService(firstConnection);
                            firstServiceBounded = false;
                        }
                        if (secondService != null && secondService.isPlaying()) {
                            getActivity().unbindService(secondConnection);
                            secondServiceBounded = false;
                        }

                        startThirdRound = new Intent(getActivity(), WimHofBreathService.class);
                        startThirdRound.putExtra("remaining", 2);
                        getActivity().bindService(startThirdRound, thirdConnection, BIND_AUTO_CREATE);

                        break;
                }

                Toast.makeText(getActivity(), "Starting Breathing Cycle",Toast.LENGTH_SHORT).show();

                break;

            case R.id.buttonStopRound:

                if (!stopPressed) {

                    if (firstServiceBounded) {
                        getActivity().unbindService(firstConnection);
                        firstServiceBounded = false;

                    } else if (secondServiceBounded) {
                        getActivity().unbindService(secondConnection);
                        secondServiceBounded = false;

                    } else if (thirdServiceBounded) {
                        getActivity().unbindService(thirdConnection);
                        thirdServiceBounded = false;
                    }

                    Toast.makeText(getActivity(), "Stopped Breathing Cycle",Toast.LENGTH_SHORT).show();

                    wimHofBreathViewModel.stopTextView(MainActivity.numberOfRounds, textView);
                    MainActivity.numberOfRounds--;

                    stopPressed = true;
                }

                break;

            case R.id.buttonPauseRound:

                if (firstServiceBounded) {

                    if (firstService.isPlaying()){
                        buttonPauseRound.setText("RESUME ROUND");
                        firstService.pauseMediaPlayer();
                        Toast.makeText(getActivity(), "Paused Breathing Cycle",Toast.LENGTH_SHORT).show();
                    }else {
                        buttonPauseRound.setText("PAUSE ROUND");
                        firstService.resumeMediaPlayer();
                        Toast.makeText(getActivity(), "Resumed Breathing Cycle",Toast.LENGTH_SHORT).show();
                    }

                } else if (secondServiceBounded) {

                    if (secondService.isPlaying()){
                        buttonPauseRound.setText("RESUME ROUND");
                        secondService.pauseMediaPlayer();
                        Toast.makeText(getActivity(), "Paused Breathing Cycle",Toast.LENGTH_SHORT).show();
                    }else {
                        buttonPauseRound.setText("PAUSE ROUND");
                        secondService.resumeMediaPlayer();
                        Toast.makeText(getActivity(), "Resumed Breathing Cycle",Toast.LENGTH_SHORT).show();
                    }

                } else if (thirdServiceBounded) {

                    if (thirdService.isPlaying()){
                        buttonPauseRound.setText("RESUME ROUND");
                        thirdService.pauseMediaPlayer();
                        Toast.makeText(getActivity(), "Paused Breathing Cycle",Toast.LENGTH_SHORT).show();
                    }else {
                        buttonPauseRound.setText("PAUSE ROUND");
                        thirdService.resumeMediaPlayer();
                        Toast.makeText(getActivity(), "Resumed Breathing Cycle",Toast.LENGTH_SHORT).show();
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
            WimHofBreathService.LocalBinder firstLocalBinder = (WimHofBreathService.LocalBinder) service;
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
            WimHofBreathService.LocalBinder secondLocalBinder = (WimHofBreathService.LocalBinder) service;
            secondService = secondLocalBinder.getServerInstance();
        }
    };

    ServiceConnection thirdConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            thirdServiceBounded = false;
            thirdService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            thirdServiceBounded = true;
            WimHofBreathService.LocalBinder thirdLocalBinder = (WimHofBreathService.LocalBinder) service;
            thirdService = thirdLocalBinder.getServerInstance();
        }
    };
}