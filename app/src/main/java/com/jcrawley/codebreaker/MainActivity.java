package com.jcrawley.codebreaker;

import static com.jcrawley.codebreaker.game.PegColor.*;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.jcrawley.codebreaker.game.Clue;
import com.jcrawley.codebreaker.game.Game;
import com.jcrawley.codebreaker.game.PegColor;
import com.jcrawley.codebreaker.view.panel.GameOverHelper;
import com.jcrawley.codebreaker.view.GameView;
import com.jcrawley.codebreaker.view.GridWiper;
import com.jcrawley.codebreaker.view.panel.InfoPanelHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GameView {

    private Game game;
    private GridWiper gridWiper;
    private boolean isInitialized;
    private GameOverHelper gameOverHelper;
    private InfoPanelHelper infoPanelHelper;
    private final Map<Integer, ViewGroup> gridRowMap = new HashMap<>();
    private ImageButton undoButton;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initViewModel();
        setupLayout();
        configureNavAndStatusBarAppearance();
        setupGridMap();
        gameOverHelper = new GameOverHelper(this);
        infoPanelHelper = new InfoPanelHelper(this);
        setupButtons();
        game = new Game(viewModel.gameModel, this);
        game.init();
    }


    private void initViewModel(){
        viewModel  = new ViewModelProvider(this).get(MainViewModel.class);

    }

    private void initGridWiper(){
        if(gridWiper == null){
            gridWiper = new GridWiper(MainActivity.this, game);
        }
    }


    private ImageButton setupButton(int resId, Runnable runnable){
        ImageButton button  = findViewById(resId);
        button.setOnClickListener(v -> runnable.run());
        return button;
    }


    public Game getGame(){
        return game;
    }


    @Override
    public void notifyInitializationComplete(){
        isInitialized = true;
    }


    @Override
    public void disableUndoButton(){
        undoButton.setEnabled(false);
    }


    @Override
    public void enableUndoButton(){
        undoButton.setEnabled(true);
        // not using this because it is too distracting
        //setUndoButtonDrawable(R.drawable.ic_undo);
    }


    private void setUndoButtonDrawable(int resId){
        var drawable = AppCompatResources.getDrawable(getApplicationContext(), resId);
        undoButton.setImageDrawable(drawable);
    }



    private void setupLayout() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void configureNavAndStatusBarAppearance(){
        var window = getWindow();
        var insetsController = WindowCompat.getInsetsController(window, window.getDecorView());
        insetsController.setAppearanceLightNavigationBars(false);
        insetsController.setAppearanceLightStatusBars(false);
    }


    private void setupButtons() {
        setupButton(R.id.infoButton, ()-> infoPanelHelper.showPanel() );
        undoButton = setupButton(R.id.undoButton, ()-> game.removePeg() );
        setupButton(R.id.newGameButton, this::startNewGame);

        setupInputButtonRow(R.id.panel1, List.of(RED, BLUE, GREEN, YELLOW) );
        setupInputButtonRow(R.id.panel2, List.of(BROWN, ORANGE, PINK, PURPLE) );
    }


    private void setupInputButtonRow(int panelId, List<PegColor> pegColors){
        ViewGroup panel = findViewById(panelId);
        for(int i = 0; i < pegColors.size(); i++){
            setupInputButton(panel, i, pegColors.get(i));
        }
    }


    private void setupInputButton(ViewGroup panel, int index, PegColor pegColor){
        var buttonLayout = (ViewGroup) panel.getChildAt(index);
        var button = buttonLayout.getChildAt(0);
        setContentDescription(button, pegColor);
        setColorTint(button, pegColor);
        setClickListenerOn(button, pegColor);
    }


    private void setContentDescription(View view, PegColor pegColor){
        var description = getString(pegColor.getContentDescriptionId());
        view.setContentDescription(description);
    }


    private void setClickListenerOn(View view, PegColor pegColor){
        view.setOnClickListener((v -> {
            if(game != null && isInitialized){
                game.addPeg(pegColor);
            }
        }));
    }


    private void setColorTint(View view, PegColor pegColor){
        int color = getColor(pegColor.getColorId());
        var colorStateList = ColorStateList.valueOf(color);
        view.setBackgroundTintList(colorStateList);
    }


    private void startNewGame(){
        if(game != null){
            game.startNewGame();
        }
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
        ViewGroup layout = findViewById(R.id.solutionPegsLayout);
        for (int i = 0; i < solution.size() && i < layout.getChildCount(); i++) {
            setupSolutionPeg(layout, solution, i);
        }
    }


    private void setupSolutionPeg(ViewGroup solutionPegsLayout, List<PegColor> solution, int index){
        var pegColor = solution.get(index);
        var pegLayout = (ViewGroup) solutionPegsLayout.getChildAt(index);
        setPegColor(pegLayout.getChildAt(0), pegColor.getColorId());
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
        for(int i = 0; i < viewModel.gameModel.getPegsPerRow(); i++){
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
        var color = getThemedColor(R.attr.row_color);
        getRow(index).setBackgroundColor(color);
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
    public void highlightRowBackground(int rowIndex){
        var color = getThemedColor(R.attr.highlighted_row_color);
        getRow(rowIndex).setBackgroundColor(color);
    }


    private int getThemedColor(int attrId){
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(attrId, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }


    private void setupGridMap(){
        gridRowMap.clear();
        setupGridSection(R.id.gameGridLayout, 9);
        setupGridSection(R.id.gameGridLayout2, 4);
    }


    private void setupGridSection(int parentLayoutId, int rowNumber){
        var rowIds = List.of(R.id.row_a, R.id.row_b, R.id.row_c, R.id.row_d, R.id.row_e);
        ViewGroup sectionLayout = findViewById(parentLayoutId);
        for(int i = 0; i < 5; i++, rowNumber--){
            addRowToMap(rowNumber, sectionLayout, rowIds.get(i));
        }
    }


    private void addRowToMap(int number, ViewGroup parent, int rowId){
        ViewGroup row = parent.findViewById(rowId);
        gridRowMap.put(number, row);
    }


    private ViewGroup getPegRow(int index) {
        return (ViewGroup) getRow(index).getChildAt(0);
    }


    private ViewGroup getCluesRow(int index) {
        return (ViewGroup) getRow(index).getChildAt(1);
    }

    private ViewGroup getRow(int index) {
        return gridRowMap.get(index);
    }
}