package com.jcrawley.codebreaker.view.rules;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jcrawley.codebreaker.R;

import java.util.List;
import java.util.stream.Collectors;

public class RulesFragment extends Fragment {

    ViewGroup rulesLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_rules, container, false);

        rulesLayout = view.findViewById(R.id.rulesLayout);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addHeader();
        showRulesAsBullets();
    }


    private void showRulesAsBullets() {
        var rules = getRules();
        for (int i = 0; i < rules.size(); i++) {
            addAndShowRule(rules.get(i), i);
        }
    }


    private void addHeader(){
        var header = new TextView(requireContext());
        header.setText(R.string.rules_heading);
        header.setTextSize(28f);
        header.setTypeface(null, Typeface.BOLD);
        header.setGravity(Gravity.CENTER);
        header.setPadding(0, 16, 0, 32);
        header.setTextColor(getResources().getColor(android.R.color.white, null));
        addToLayout(header);
    }


    private boolean addToLayout(View view){
        boolean wasAdded = true;
        if(rulesLayout != null){
            rulesLayout.addView(view);
            return wasAdded;
        }
        return !wasAdded;
    }


    private void addAndShowRule(String text, int index){
        rulesLayout.postDelayed(() -> {
            var context = getContext();
            if(context != null){
                createBulletItemAndAddToLayout(text);
            }

        }, (index + 1) * 250L);
    }


    private void createBulletItemAndAddToLayout(String text){
        var bulletItem = new LinearLayout(requireContext());
        bulletItem.setOrientation(LinearLayout.HORIZONTAL);
        var params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0,12,0,12);
        bulletItem.setLayoutParams(params);

        bulletItem.addView(createBullet());
        bulletItem.addView(createRuleTextView(text));

        bulletItem.setAlpha(0f);
        bulletItem.setTranslationY(40f);
        if(addToLayout(bulletItem)){
            startAnimationOn(bulletItem);
        }
    }


    private TextView createRuleTextView(String text){
        var ruleText = new TextView(requireContext());
        ruleText.setText(text);
        ruleText.setTextSize(18f);
        ruleText.setLineSpacing(6, 1f);
        ruleText.setPadding(16, 0, 0, 0);
        ruleText.setTextColor(getResources().getColor(android.R.color.white, null));
        var params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);
        ruleText.setLayoutParams(params);
        return ruleText;
    }


    private TextView createBullet(){
        var bullet = new TextView(requireContext());
        bullet.setText("•");
        bullet.setTextSize(22f);
        bullet.setGravity(Gravity.TOP);
        bullet.setTextColor(getResources().getColor(R.color.white, null));

        var params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0,15,0,0);
        bullet.setLayoutParams(params);
        return bullet;
    }


    private void startAnimationOn(View view){
            view.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(500)
                    .start();
    }


    private List<String> getRules() {
        var rulesResIds = List.of(R.string.rule_1, R.string.rule_2, R.string.rule_3, R.string.rule_4, R.string.rule_5,
                R.string.rule_6, R.string.rule_7, R.string.rule_8, R.string.rule_9, R.string.rule_10);

        return rulesResIds.stream().map(this::getString).collect(Collectors.toList());
    }

}