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


import java.util.List;

public class MainActivity extends AppCompatActivity implements GameView {

    ViewGroup gameLayout;
    private Game game;
    private ViewGroup gameOverPanel;
    private TextView gameOverTitleText, gameOverMessageText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setupLayout();
        game = new Game(this);
        gameLayout = findViewById(R.id.gameGridLayout);
        setupButtons();
        setupGameOverScreen();
        game.resetGame();
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
            game.resetGame();
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
        button.setOnClickListener((v -> game.addPegColorAtCurrentIndex(pegColor)));

    }


    private List<View> getClueViewsIn(ViewGroup row){
        ViewGroup cluesTopLayout = (ViewGroup) row.getChildAt(game.getPegsPerRow());
        ViewGroup cluesLayout = (ViewGroup) cluesTopLayout.getChildAt(0);
        ViewGroup row1 = (ViewGroup) cluesLayout.getChildAt(0);
        ViewGroup row2 = (ViewGroup) cluesLayout.getChildAt(1);

        return List.of(row1.getChildAt(0),
                row1.getChildAt(1),
                row2.getChildAt(0),
                row2.getChildAt(1));
    }


    @Override
    public void showBadGameOver(){
        gameOverMessageText.setText(R.string.game_over_message_fail);
        gameOverTitleText.setText(R.string.game_over_title);
        showGameOverPanel();
    }


    @Override
    public void showGoodGameOver(int numberOfTries){
        gameOverTitleText.setText(R.string.game_over_title_success);
        showSuccessMessage(numberOfTries);
        showGameOverPanel();
    }


    private void showGameOverPanel(){
        gameOverPanel.setVisibility(VISIBLE);
    }


    private void showSuccessMessage(int numberOfTries){
        if(numberOfTries == 1){
            gameOverMessageText.setText(R.string.number_of_tries_one);
        }
        else{
            var msg = getString(R.string.number_of_tries, numberOfTries);
            gameOverMessageText.setText(msg);
        }
    }


    private void resetAllPegsIn(ViewGroup row){
        for(int i = 0; i < row.getChildCount(); i++){
            var peg = row.getChildAt(i);
            peg.setBackgroundColor(getColor(R.color.peg_default_background));
        }
    }

    private void resetAllCluesIn(ViewGroup row){
        for(var clueView : getClueViewsIn(row)){
            clueView.setBackgroundColor(getColor(R.color.clue_default_background));
        }
    }


    private ViewGroup getRow(int index){
        int rowIndex = game.getNumberOfRows() - index -1;
        return (ViewGroup) gameLayout.getChildAt(rowIndex);
    }


    private void log(String msg){
        System.out.println("^^^ MainActivity: " + msg);
    }


    public void setupSolutionPegs(List<PegColor> solution){
        ViewGroup solutionPegsLayout = findViewById(R.id.solutionPegsLayout);
        for(int i = 0 ; i < solution.size(); i++){
            if(i >= solutionPegsLayout.getChildCount()){
                return;
            }
            var pegColor = solution.get(i);
            solutionPegsLayout.getChildAt(i)
                    .setBackgroundColor(getColorOf(pegColor));
        }
    }


    @Override
    public void update(int row, List<Clue> clues){

        var rowLayout = getRow(row);
        var clueLayouts = getClueViewsIn(rowLayout);
        for(int i = 0; i < clueLayouts.size(); i++){
            updateClueView(clueLayouts.get(i), clues.get(i));
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


    @Override
    public void resetAllRows(){
        int defaultBackgroundColor = getColor(R.color.pane_background);
        for(int i = 0; i < game.getNumberOfRows(); i++){
            var row = getRow(i);
            row.setBackgroundColor(defaultBackgroundColor);
            resetAllPegsIn(row);
            resetAllCluesIn(row);
        }
    }


    @Override
    public  void setPegColor(PegColor pegColor, int row, int index){
        var pegView = getViewAt(row, index);
        pegView.setBackgroundColor(getColorOf(pegColor));
    }


    private View getViewAt(int row, int index){
        return getRow(row).getChildAt(index);
    }


    @Override
    public void highlightRowBackground(int rowIndex){
        int highlightedBackgroundColor = getColor(R.color.highlighted_row_background);
        getRow(rowIndex).setBackgroundColor(highlightedBackgroundColor);
    }


    private int getColorOf(PegColor pegColor){
        return getColor(pegColor.getColorId());
    }

}