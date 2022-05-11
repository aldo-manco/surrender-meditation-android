package org.aldomanco.wimhofmethod.ui.home;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButtonToggleGroup;

import org.aldomanco.wimhofmethod.R;
import org.aldomanco.wimhofmethod.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private int numberOfRounds;

    private Intent startFirstRound;
    private Intent startSecondRound;
    private Intent startThirdRound;

    private TextView textView;
    private ConstraintLayout layout;
    private Button buttonStopRound;

    private boolean stopPressed;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        numberOfRounds=0;
        stopPressed = false;

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textView = binding.textHome;
        layout = binding.layout;
        buttonStopRound = binding.buttonStopRound;

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        textView.setOnClickListener(this);
        layout.setOnClickListener(this);
        buttonStopRound.setOnClickListener(this);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.layout:
            case R.id.text_home:

                numberOfRounds++;
                homeViewModel.setTextView(numberOfRounds, textView);

                int remaining = numberOfRounds%3;
                stopPressed=false;

                switch (remaining){

                    case 1:

                        startFirstRound = new Intent(getActivity(), RoundPlayerService.class);
                        startFirstRound.putExtra("remaining", 0);

                        if (startSecondRound!=null){
                            getActivity().stopService(startSecondRound);
                        }
                        if (startThirdRound!=null){
                            getActivity().stopService(startThirdRound);
                        }

                        getActivity().startService(startFirstRound);
                        break;

                    case 2:

                        startSecondRound = new Intent(getActivity(), RoundPlayerService.class);
                        startSecondRound.putExtra("remaining", 1);

                        if (startFirstRound!=null){
                            getActivity().stopService(startFirstRound);
                        }
                        if (startThirdRound!=null){
                            getActivity().stopService(startThirdRound);
                        }

                        getActivity().startService(startSecondRound);
                        break;

                    case 0:

                        startThirdRound = new Intent(getActivity(), RoundPlayerService.class);
                        startThirdRound.putExtra("remaining", 2);

                        if (startSecondRound!=null){
                            getActivity().stopService(startSecondRound);
                        }
                        if (startFirstRound!=null){
                            getActivity().stopService(startFirstRound);
                        }

                        getActivity().startService(startThirdRound);
                        break;
                }

                break;

            case R.id.buttonStopRound:

                if (!stopPressed){

                    if (startThirdRound!=null){
                        getActivity().stopService(startThirdRound);
                    }
                    if (startFirstRound!=null){
                        getActivity().stopService(startFirstRound);
                    }
                    if (startSecondRound!=null){
                        getActivity().stopService(startSecondRound);
                    }

                    homeViewModel.stopTextView(numberOfRounds, textView);
                    numberOfRounds--;

                    stopPressed=true;
                }

                break;
        }
    }
}