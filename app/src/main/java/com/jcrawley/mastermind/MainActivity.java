package com.jcrawley.mastermind;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jcrawley.mastermind.game.Clue;
import com.jcrawley.mastermind.game.Game;
import com.jcrawley.mastermind.game.PegColor;
import com.jcrawley.mastermind.service.GameService;
import com.jcrawley.mastermind.view.GameOverHelper;
import com.jcrawley.mastermind.view.GameView;
import com.jcrawley.mastermind.view.GridWiper;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements GameView {

    ViewGroup gameLayout;
    private Game game;
    private GridWiper gridWiper;
    private int numberOfRows;
    private int pegsPerRow;
    private final AtomicBoolean isServiceConnected = new AtomicBoolean(false);
    private boolean isInitialized;
    private GameOverHelper gameOverHelper;


    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            GameService.LocalBinder binder = (GameService.LocalBinder) service;
            GameService gameService = binder.getService();
            gameService.setActivity(MainActivity.this);
            isServiceConnected.set(true);
            game = gameService.getGame();
            numberOfRows = game.getNumberOfRows();
            pegsPerRow = game.getPegsPerRow();
            game.init();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isServiceConnected.set(false);
        }
    };


    private void initGridWiper(){
        if(gridWiper == null){
            gridWiper = new GridWiper(MainActivity.this, game);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        hideActionBar();
        setupLayout();
        gameLayout = findViewById(R.id.gameGridLayout);
        setupButtons();
        gameOverHelper = new GameOverHelper(this);
        setupGameService();
    }


    public Game getGame(){
        return game;
    }


    @Override
    public void notifyInitializationComplete(){
        isInitialized = true;
    }


    private void setupGameService() {
        Intent intent = new Intent(getApplicationContext(), GameService.class);
        getApplicationContext().startService(intent);
        getApplicationContext().bindService(intent, connection, 0);
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


    private void setupButtons() {
        setupColorButton(R.id.redButton, PegColor.RED);
        setupColorButton(R.id.blueButton, PegColor.BLUE);
        setupColorButton(R.id.greenButton, PegColor.GREEN);
        setupColorButton(R.id.yellowButton, PegColor.YELLOW);
        setupColorButton(R.id.orangeButton, PegColor.ORANGE);
        setupColorButton(R.id.purpleButton, PegColor.PURPLE);
        setupColorButton(R.id.brownButton, PegColor.BROWN);
        setupColorButton(R.id.pinkButton, PegColor.PINK);
    }


    private void setupColorButton(int buttonId, PegColor pegColor) {
        Button button = findViewById(buttonId);
        button.setOnClickListener((v -> {
            if(game != null && isInitialized){
                game.addPegColorAtCurrentIndex(pegColor);
            }
        }));
    }


    @Override
    public void showBadGameOver() {
        gameOverHelper.showBadGameOver();
    }


    @Override
    public void showGoodGameOver(int numberOfTries) {
        gameOverHelper.showGoodGameOver(numberOfTries);
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
    public void updateClues(int rowIndex, List<Clue> clues) {
        var rowLayout = getRow(rowIndex);
        var clueLayouts = getClueViewsIn(rowLayout);
        for (int i = 0; i < clueLayouts.size(); i++) {
            updateClueView(clueLayouts.get(i), clues.get(i));
        }
    }


    @Override
    public void updateRow(int rowIndex, List<PegColor> pegColors, List<Clue> clues) {
        updateClues(rowIndex, clues);
        updatePegColors(rowIndex, pegColors);
    }


    private void updatePegColors(int rowIndex, List<PegColor> pegColors) {
        if(game == null){
            return;
        }
        for(int i = 0; i < pegsPerRow; i++){
            var pegColor = pegColors.size() <= i ? PegColor.EMPTY : pegColors.get(i);
            setPegColor(rowIndex, i, pegColor);
        }
    }


    @Override
    public void resetAllCluesIn(int index) {
        ViewGroup row = getCluesRow(index);
        for (var clueView : getClueViewsIn(row)) {
            clueView.setBackgroundColor(getColor(R.color.clue_default_background));
        }
    }


    private List<View> getClueViewsIn(ViewGroup row) {
        return List.of(getClueLayout(row,R.id.clue1),
                getClueLayout(row, R.id.clue2),
                getClueLayout(row, R.id.clue3),
                getClueLayout(row, R.id.clue4));
    }


    private ViewGroup getClueLayout(ViewGroup row, int parentId){
        ViewGroup parent = row.findViewById(parentId);
        return parent.findViewById(R.id.clue);
    }


    @Override
    public void resetRowBackground(int index){
        int defaultBackground = getColor(R.color.pane_background);
        getRow(index).setBackgroundColor(defaultBackground);
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
    public void resetAllRows(){
        isInitialized = false;
        initGridWiper();
        gridWiper.resetAllRows();
    }


    @Override
    public void resetAllRowsInstantly(){
        isInitialized = false;
        initGridWiper();
        gridWiper.resetAllRowsInstantly();
    }



    private void setPegColor(View pegView, int colorId) {
        var drawable = pegView.getBackground();
        var amended = drawable.mutate();
        var color = getColor(colorId);
        amended.setColorFilter(color, PorterDuff.Mode.LIGHTEN);
        pegView.setBackground(amended);
    }


    @Override
    public void setPegColor(int row, int index, PegColor pegColor) {
        var pegView = getPegViewAt(row, index);
        setPegColor(pegView, pegColor.getColorId());
    }


    private View getPegViewAt(int row, int index) {
        var pegLayout = (ViewGroup) getPegRow(row).getChildAt(index);
        return pegLayout.getChildAt(0);
    }


    public void highlightAllRowsUpToAndIncluding(int rowNumber){
        var limit = Math.min(game.getNumberOfRows() - 1, rowNumber);
        for(int i = 0; i <= limit; i++){
            highlightRowBackground(i);
        }
    }


    @Override
    public void highlightRowBackground(int rowIndex) {
        int highlightedBackgroundColor = getColor(R.color.highlighted_row_background);
        getRow(rowIndex).setBackgroundColor(highlightedBackgroundColor);
    }


    private ViewGroup getRow(int index) {
        int rowIndex = numberOfRows - index - 1;
        return (ViewGroup) gameLayout.getChildAt(rowIndex);
    }


    private ViewGroup getPegRow(int index) {
        return (ViewGroup) getRow(index).getChildAt(0);
    }


    private ViewGroup getCluesRow(int index) {
        return (ViewGroup) getRow(index).getChildAt(1);
    }

}