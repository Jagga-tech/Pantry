package com.pantrypal.ui.onboarding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pantrypal.ui.onboarding.fragments.OnboardingFragment1;
import com.pantrypal.ui.onboarding.fragments.OnboardingFragment2;
import com.pantrypal.ui.onboarding.fragments.OnboardingFragment3;

public class OnboardingPagerAdapter extends FragmentStateAdapter {
    private static final int FRAGMENT_COUNT = 3;

    public OnboardingPagerAdapter(FragmentActivity activity) {
        super(activity);
    }

    public OnboardingPagerAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new OnboardingFragment1();
            case 1:
                return new OnboardingFragment2();
            case 2:
                return new OnboardingFragment3();
            default:
                return new OnboardingFragment1();
        }
    }

    @Override
    public int getItemCount() {
        return FRAGMENT_COUNT;
    }
}