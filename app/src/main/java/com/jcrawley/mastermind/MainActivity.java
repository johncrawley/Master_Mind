package com.jcrawley.mastermind;

import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final int pegsPerRow = 4;
    private int currentIndex, currentRow, pegsPlaced;
    private final int numberOfRows = 10;
    private final int MAX_PEGS = pegsPerRow * numberOfRows;
    private final List<PegColor> currentAnswer = new ArrayList<>();
    private int numberOfTries;
    private Random random;
    PegColor[] possibleColors = PegColor.values();
    List<PegColor> solution = new ArrayList<>(pegsPerRow);
    ViewGroup gameLayout;
    private ViewGroup gameOverPanel;
    private TextView gameOverTitleText, gameOverMessageText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setupLayout();
        gameLayout = findViewById(R.id.gameGridLayout);
        setupButtons();
        setupGameOverScreen();
        resetGame();
    }


    private void setupLayout(){
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
    }


    private void setupGameOverScreen(){
        gameOverPanel = findViewById(R.id.gameOverPanelInclude);
        gameOverTitleText = findViewById(R.id.gameOverTitleText);
        gameOverMessageText = findViewById(R.id.gameOverMessageText);

        gameOverPanel.setOnClickListener(v -> {
            resetGame();
            gameOverPanel.setVisibility(View.GONE);
        });
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
        var clues = GameUtils.generateClues(currentAnswer, solution);
        updateViewWith(clues);
        if(isAnswerCorrect(clues)){
            showGoodGameOver();
            return;
        }
        if(pegsPlaced >= MAX_PEGS){
            showBadGameOver();
        }
        log("pegs placed: " + pegsPlaced + " max pegs: " + MAX_PEGS);
    }


    private void updateViewWith(List<Clue> clues){
        ViewGroup cluesTopLayout = (ViewGroup) getRow(currentRow).getChildAt(pegsPerRow);
        ViewGroup cluesLayout = (ViewGroup) cluesTopLayout.getChildAt(0);
        ViewGroup row1 = (ViewGroup) cluesLayout.getChildAt(0);
        ViewGroup row2 = (ViewGroup) cluesLayout.getChildAt(1);

        List<View> cluesLayouts = List.of(row1.getChildAt(0),
                row1.getChildAt(1),
                row2.getChildAt(0),
                row2.getChildAt(1));

        for(int i = 0; i < cluesLayouts.size(); i++){
            updateClueView(cluesLayouts.get(i), clues.get(i));
        }
    }


    private void updateClueView(View clueLayout, Clue clue){
        int clueColorId = switch (clue){
            case WRONG -> R.color.clue_default_background;
            case COW -> R.color.clue_cow;
            case BULL -> R.color.clue_bull;
        };

        clueLayout.setBackgroundColor(getColor(clueColorId));
    }


    private boolean isAnswerCorrect(List<Clue> clues){
        return clues.stream()
                .allMatch(c -> c == Clue.BULL);
    }


    private void showBadGameOver(){
        gameOverMessageText.setText(R.string.game_over_message_fail);
        gameOverTitleText.setText(R.string.game_over_title);
        showGameOverPanel();
    }


    private void showGoodGameOver(){
        gameOverTitleText.setText(R.string.game_over_title_success);
        showSuccessMessage();
        showGameOverPanel();
    }


    private void showGameOverPanel(){
        gameOverPanel.setVisibility(VISIBLE);
    }


    private void showSuccessMessage(){
        if(numberOfTries == 1){
            gameOverMessageText.setText(R.string.number_of_tries_one);
        }
        else{
            var msg = getString(R.string.number_of_tries, numberOfTries);
            gameOverMessageText.setText(msg);
        }
    }


    private void addPegColorAtCurrentIndex(PegColor pegColor){
        if(pegsPlaced >= MAX_PEGS){
            return;
        }

        var pegView = getViewForCurrentIndex();
        pegView.setBackgroundColor(getBackgroundColorForPeg(pegColor));
        currentAnswer.add(pegColor);
        if(++currentIndex >= pegsPerRow){
            checkCurrentAnswer();
            moveToNextRow();
        }
        pegsPlaced++;
    }


    private void moveToNextRow(){
        currentIndex = 0;
        numberOfTries++;
        currentRow++;
        currentAnswer.clear();
        highlightCurrentRowBackground();
    }


    private void resetGame(){
        resetAllRowBackgrounds();
        numberOfTries = 0;
        currentRow = 0;
        currentIndex = 0;
        pegsPlaced = 0;
        currentAnswer.clear();
        setupRandomAnswer();
        highlightCurrentRowBackground();
    }


    private void resetAllRowBackgrounds(){
        int defaultBackgroundColor = getColor(R.color.pane_background);
        for(int i = 0; i < numberOfRows; i++){
            getRow(i).setBackgroundColor(defaultBackgroundColor);
        }
    }


    private void highlightCurrentRowBackground(){
        int highlightedBackgroundColor = getColor(R.color.highlighted_row_background);
        if(currentRow < numberOfRows){
            getRow(currentRow).setBackgroundColor(highlightedBackgroundColor);
        }
    }


    public View getViewForCurrentIndex(){
        return getRow(currentRow).getChildAt(currentIndex);
    }


    private ViewGroup getRow(int index){
        int rowIndex = numberOfRows - index -1;
        return (ViewGroup) gameLayout.getChildAt(rowIndex);
    }


    private void log(String msg){
        System.out.println("^^^ MainActivity: " + msg);
    }


    private int getBackgroundColorForPeg(PegColor pegColor){
        log("getBackgroundColorForPeg() color: " + pegColor.name());
        int colorId = switch (pegColor){
            case RED -> R.color.game_red;
            case BLUE -> R.color.game_blue;
            case GREEN -> R.color.game_green;
            case YELLOW -> R.color.game_yellow;
            case ORANGE -> R.color.game_orange;
            case PURPLE -> R.color.game_purple;
            case BROWN -> R.color.game_brown;
            case PINK -> R.color.game_pink;
        };
        return getColor(colorId);
    }


    private void setupRandomAnswer(){
        random = new Random(System.currentTimeMillis());
        solution.clear();
        for(int i = 0; i < pegsPerRow; i++ ){
            solution.add(getRandomPegColor());
        }
        setupSolutionPegs(solution);
    }


    private void setupSolutionPegs(List<PegColor> solution){
        ViewGroup solutionPegsLayout = findViewById(R.id.solutionPegsLayout);
        for(int i = 0 ; i < solution.size(); i++){
            if(i >= solutionPegsLayout.getChildCount()){
                return;
            }
            solutionPegsLayout.getChildAt(i)
                    .setBackgroundColor(getBackgroundColorForPeg(solution.get(i)));
        }
    }


    private PegColor getRandomPegColor(){
        int randomIndex = random.nextInt(possibleColors.length);
        return possibleColors[randomIndex];
    }

}