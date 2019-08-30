package desahogate.proyectofinal.desahogate.Onboarding;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import desahogate.proyectofinal.desahogate.R;

/**
 * Created by jose on 01/02/2018.
 */

public class OnboardingFragment1 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle s) {

        return inflater.inflate(
                R.layout.onboarding_screen1,
                container,
                false
        );

    }
}
