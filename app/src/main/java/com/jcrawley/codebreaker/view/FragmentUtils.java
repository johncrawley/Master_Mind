package com.jcrawley.codebreaker.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jcrawley.codebreaker.R;
import com.jcrawley.codebreaker.view.rules.RulesFragment;

public class FragmentUtils {

    public static void loadRulesFragment(Fragment parentFragment){
        loadFragment(parentFragment, new RulesFragment(), "rules_fragment");
    }


    public static void loadFragment(Fragment parentFragment, Fragment fragment, String tag){
        loadFragment(parentFragment,fragment, tag, new Bundle());
    }


    public static void loadFragment(Fragment parentFragment, Fragment fragment, String tag, Bundle bundle){
        var fragmentManager = parentFragment.getParentFragmentManager();
        var fragmentTransaction = fragmentManager.beginTransaction();
        removePreviousFragmentTransaction(fragmentManager, tag, fragmentTransaction);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }



    private static void removePreviousFragmentTransaction(FragmentManager fragmentManager, String tag, FragmentTransaction fragmentTransaction){
        var prev = fragmentManager.findFragmentByTag(tag);
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);
    }


}
