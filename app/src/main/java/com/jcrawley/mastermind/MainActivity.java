package com.jcrawley.mastermind;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private enum PegColor { RED, BLUE, GREEN, YELLOW, ORANGE, PURPLE, BROWN, PINK}

    private int currentPegNumber = 0;
    private int currentIndex = 0;
    private int currentRow = 0;
    private final int numberOfRows = 10;
    private List<PegColor> currentAnswer;
    private int numberOfTries;
    private Random random;
    PegColor[] possibleColors = PegColor.values();
    List<PegColor> solution;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setupLayout();
        setupRandomAnswer();
        setupButtons();
    }


    private void setupLayout(){
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
    }


    private void startGame(){

    }

    private void setupButtons(){
        setupButton(R.id.redButton, PegColor.RED);
        setupButton(R.id.blueButton, PegColor.BLUE);
        setupButton(R.id.greenButton, PegColor.GREEN);
        setupButton(R.id.yellowButton, PegColor.YELLOW);
        setupButton(R.id.orangeButton, PegColor.ORANGE);
        setupButton(R.id.purpleButton, PegColor.PURPLE);
        setupButton(R.id.brownButton, PegColor.BROWN);
        setupButton(R.id.pinkButton, PegColor.PINK);
    }



    private void setupButton(int buttonId, PegColor pegColor){
        ImageButton button = findViewById(buttonId);
        button.setOnClickListener((v -> addPegColorAtCurrentIndex(pegColor)));

    }

    private void checkCurrentAnswer(){
        for(int i = 0; i < solution.size(); i++){
            if(solution.get(i) != currentAnswer.get(i)){
                if(numberOfTries >= numberOfRows){
                    showBadGameOver();
                }
                return;
            }
        }
        showGoodGameOver();
    }


    private void showBadGameOver(){

    }


    private void showGoodGameOver(){

    }


    private void addPegColorAtCurrentIndex(PegColor pegColor){
        View view = getViewForCurrentIndex();
        view.setBackgroundColor(getBackgroundColorForPeg(pegColor));

    }


    public View getViewForCurrentIndex(){
        ViewGroup gameLayout = findViewById(R.id.gameGridLayout);
        ViewGroup row = (ViewGroup) gameLayout.getChildAt(numberOfRows - currentRow);
        ViewGroup pegLayout = (ViewGroup) row.getChildAt(currentIndex);
        return pegLayout;
    }


    private int getBackgroundColorForPeg(PegColor pegColor){
        return switch (pegColor){
            case RED -> R.color.game_red;
            case BLUE -> R.color.game_blue;
            case GREEN -> R.color.game_green;
            case YELLOW -> R.color.game_yellow;
            case ORANGE -> R.color.game_orange;
            case PURPLE -> R.color.game_purple;
            case BROWN -> R.color.game_brown;
            case PINK -> R.color.game_pink;
        };
    }


    private void setupRandomAnswer(){
        random = new Random(System.currentTimeMillis());
        solution = List.of(getRandomPegColor(),
                getRandomPegColor(),
                getRandomPegColor(),
                getRandomPegColor());
    }


    private PegColor getRandomPegColor(){
        int randomIndex = random.nextInt(possibleColors.length);
        return possibleColors[randomIndex];
    }
}