package com.jcrawley.mastermind;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jcrawley.mastermind.game.Clue;
import com.jcrawley.mastermind.game.Game;
import com.jcrawley.mastermind.game.PegColor;
import com.jcrawley.mastermind.view.AnimationHelper;
import com.jcrawley.mastermind.view.GameView;

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
        hideActionBar();
        setupLayout();
        game = new Game(this);
        gameLayout = findViewById(R.id.gameGridLayout);
        setupButtons();
        setupGameOverScreen();
        game.resetGame();

    }

    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
    }



    private void setupLayout() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void setupGameOverScreen() {
        gameOverPanel = findViewById(R.id.gameOverPanelInclude);
        gameOverTitleText = findViewById(R.id.gameOverTitleText);
        gameOverMessageText = findViewById(R.id.gameOverMessageText);

        gameOverPanel.setOnClickListener(v -> {
            game.resetGame();
            AnimationHelper.hidePanel(gameOverPanel);
        });
    }


    private void setupButtons() {
        setupButton(R.id.redButton, PegColor.RED);
        setupButton(R.id.blueButton, PegColor.BLUE);
        setupButton(R.id.greenButton, PegColor.GREEN);
        setupButton(R.id.yellowButton, PegColor.YELLOW);
        setupButton(R.id.orangeButton, PegColor.ORANGE);
        setupButton(R.id.purpleButton, PegColor.PURPLE);
        setupButton(R.id.brownButton, PegColor.BROWN);
        setupButton(R.id.pinkButton, PegColor.PINK);
    }


    private void setupButton(int buttonId, PegColor pegColor) {
        ImageButton button = findViewById(buttonId);
        button.setOnClickListener((v -> game.addPegColorAtCurrentIndex(pegColor)));

    }


    @Override
    public void showBadGameOver() {
        gameOverMessageText.setText(R.string.game_over_message_fail);
        gameOverTitleText.setText(R.string.game_over_title);
        AnimationHelper.showPanel(gameOverPanel);
    }


    @Override
    public void showGoodGameOver(int numberOfTries) {
        gameOverTitleText.setText(R.string.game_over_title_success);
        showSuccessMessage(numberOfTries);
        AnimationHelper.showPanel(gameOverPanel);
    }


    private void showSuccessMessage(int numberOfTries) {
        if (numberOfTries == 1) {
            gameOverMessageText.setText(R.string.number_of_tries_one);
        } else {
            var msg = getString(R.string.number_of_tries, numberOfTries);
            gameOverMessageText.setText(msg);
        }
    }


    private void resetAllCluesIn(ViewGroup row) {
        for (var clueView : getClueViewsIn(row)) {
            clueView.setBackgroundColor(getColor(R.color.clue_default_background));
        }
    }


    public void setupSolutionPegs(List<PegColor> solution) {
        ViewGroup solutionPegsLayout = findViewById(R.id.solutionPegsLayout);

        for (int i = 0; i < solution.size(); i++) {
            if (i >= solutionPegsLayout.getChildCount()) {
                return;
            }
            var pegColor = solution.get(i);
            var pegLayout = (ViewGroup) solutionPegsLayout.getChildAt(i);
            setPegColor(pegLayout.getChildAt(0), pegColor.getColorId());
        }
    }


    @Override
    public void update(int rowIndex, List<Clue> clues) {
        var rowLayout = getRow(rowIndex);
        var clueLayouts = getClueViewsIn(rowLayout);
        for (int i = 0; i < clueLayouts.size(); i++) {
            updateClueView(clueLayouts.get(i), clues.get(i));
        }
    }


    private List<View> getClueViewsIn(ViewGroup row) {
        return List.of(row.findViewById(R.id.clue1),
                row.findViewById(R.id.clue2),
                row.findViewById(R.id.clue3),
                row.findViewById(R.id.clue4));
    }


    private void updateClueView(View clueLayout, Clue clue) {
        int clueColorId = switch (clue) {
            case WRONG -> R.color.clue_default_background;
            case COW -> R.color.clue_cow;
            case BULL -> R.color.clue_bull;
        };
        clueLayout.setBackgroundColor(getColor(clueColorId));
    }


    @Override
    public void resetAllRows() {
        int defaultBackgroundColor = getColor(R.color.pane_background);
        for (int i = 0; i < game.getNumberOfRows(); i++) {
            var row = getRow(i);
            row.setBackgroundColor(defaultBackgroundColor);
            resetAllPegsIn(getPegRow(i));
            resetAllCluesIn(getCluesRow(i));
        }
    }


    private void resetAllPegsIn(ViewGroup row) {
        ViewGroup pegRow = row.findViewById(R.id.pegRowLayout);
        for (int i = 0; i < pegRow.getChildCount(); i++) {
            var pegLayout = (ViewGroup) row.getChildAt(i);
            setPegColor(pegLayout.getChildAt(0), R.color.peg_default_background);
        }
    }


    private void setPegColor(View pegView, int colorId) {
        var drawable = pegView.getBackground();
        var amended = drawable.mutate();
        var color = getColor(colorId);
        amended.setColorFilter(color, PorterDuff.Mode.LIGHTEN);
        pegView.setBackground(amended);
    }


    @Override
    public void setPegColor(PegColor pegColor, int row, int index) {
        var pegView = getPegViewAt(row, index);
        setPegColor(pegView, pegColor.getColorId());
    }


    private View getPegViewAt(int row, int index) {
        var pegLayout = (ViewGroup) getPegRow(row).getChildAt(index);
        return pegLayout.getChildAt(0);
    }


    @Override
    public void highlightRowBackground(int rowIndex) {
        int highlightedBackgroundColor = getColor(R.color.highlighted_row_background);
        getRow(rowIndex).setBackgroundColor(highlightedBackgroundColor);
    }


    private ViewGroup getRow(int index) {
        int rowIndex = game.getNumberOfRows() - index - 1;
        return (ViewGroup) gameLayout.getChildAt(rowIndex);
    }


    private ViewGroup getPegRow(int index) {
        return (ViewGroup) getRow(index).getChildAt(0);
    }


    private ViewGroup getCluesRow(int index) {
        return (ViewGroup) getRow(index).getChildAt(1);
    }

}