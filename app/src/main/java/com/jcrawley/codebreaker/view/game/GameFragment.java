package com.jcrawley.codebreaker.view.game;

import static com.jcrawley.codebreaker.game.PegColor.BLUE;
import static com.jcrawley.codebreaker.game.PegColor.BROWN;
import static com.jcrawley.codebreaker.game.PegColor.GREEN;
import static com.jcrawley.codebreaker.game.PegColor.ORANGE;
import static com.jcrawley.codebreaker.game.PegColor.PINK;
import static com.jcrawley.codebreaker.game.PegColor.PURPLE;
import static com.jcrawley.codebreaker.game.PegColor.RED;
import static com.jcrawley.codebreaker.game.PegColor.YELLOW;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.content.res.AppCompatResources;

import com.jcrawley.codebreaker.MainActivity;
import com.jcrawley.codebreaker.MainViewModel;
import com.jcrawley.codebreaker.R;
import com.jcrawley.codebreaker.game.Clue;
import com.jcrawley.codebreaker.game.Game;
import com.jcrawley.codebreaker.game.PegColor;
import com.jcrawley.codebreaker.game.PegCoordinates;
import com.jcrawley.codebreaker.game.GameView;
import com.jcrawley.codebreaker.view.GridMap;
import com.jcrawley.codebreaker.view.GridWiper;
import com.jcrawley.codebreaker.view.panel.GameOverHelper;
import com.jcrawley.codebreaker.view.panel.InfoPanelHelper;

import java.util.List;

public class GameFragment extends Fragment implements GameView {
    private MainViewModel viewModel;
    private Game game;
    private GridWiper gridWiper;
    private GameOverHelper gameOverHelper;
    private InfoPanelHelper infoPanelHelper;
    private ImageButton undoButton;
    private ViewGroup solutionPegsLayout;
    private final GridMap gridMap = new GridMap();
    private InputButtonHelper inputButtonHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_game, container, false);
        initViewModel();
        gridMap.init(view);
        gameOverHelper = new GameOverHelper(getContext(), view, this, this);
        infoPanelHelper = new InfoPanelHelper(getContext(), this, view);
        solutionPegsLayout = view.findViewById(R.id.solutionPegsLayout);
        setupButtons(view);
        game = new Game(viewModel.gameModel, this);
        game.init();
        return view;
    }


    @Override
    public Game getGame(){
        return game;
    }


    @Override
    public void showBadGameOver() {
        gameOverHelper.showBadGameOver();
    }


    @Override
    public void showGoodGameOver(int numberOfTries) {
        gameOverHelper.showGoodGameOver(numberOfTries);
    }


    public void initViewModel(){
        var activity = (MainActivity)getActivity();
        if(activity != null){
            viewModel = activity.getViewModel();
        }
    }


    public void setupSolutionPegs(List<PegColor> solution) {
        for (int i = 0; i < solution.size() && i < solutionPegsLayout.getChildCount(); i++) {
            setupSolutionPeg(solutionPegsLayout, solution, i);
        }
    }


    private void setupSolutionPeg(ViewGroup solutionPegsLayout, List<PegColor> solution, int index){
        var pegColor = solution.get(index);
        var pegLayout = (ViewGroup) solutionPegsLayout.getChildAt(index);
        setPegColor(pegLayout.getChildAt(0), pegColor.getColorId());
    }


    @Override
    public void updateClues(int rowIndex, List<Clue> clues) {
        var rowLayout = gridMap.getRow(rowIndex);
        var clueLayouts = getClueViewsIn(rowLayout);
        for (int i = 0; i < clueLayouts.size(); i++) {
            updateClueView(clueLayouts.get(i), clues.get(i));
        }
    }


    @Override
    public void notifyInitializationComplete(){
        inputButtonHelper.setInitialized(true);
    }


    @Override
    public void disableUndoButton(){
        undoButton.setEnabled(false);
    }


    @Override
    public void enableUndoButton(){
        undoButton.setEnabled(true); // decided to not change button drawable because it's distracting
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
        var row = gridMap.getCluesRow(index);
        var color = getClueDefaultBackgroundColor();
        for (var clueView : getClueViewsIn(row)) {
            clueView.setBackgroundColor(color);
        }
    }


    private int getClueDefaultBackgroundColor(){
        var context = getContext();
        if(context == null){
            return Color.BLACK;
        }
        return context.getColor(R.color.clue_default_background);
    }


    private void setupButtons(View parent) {
        setupButton(parent, R.id.infoButton, ()-> infoPanelHelper.showPanel() );
        setupButton(parent, R.id.newGameButton, this::startNewGame);

        undoButton = setupButton(parent, R.id.undoButton, ()-> game.removePeg() );

        inputButtonHelper = new InputButtonHelper(this);
        inputButtonHelper.setupInputButtonRow(getContext(), parent, R.id.panel1, List.of(RED, BLUE, GREEN, YELLOW) );
        inputButtonHelper.setupInputButtonRow(getContext(), parent, R.id.panel2, List.of(BROWN, ORANGE, PINK, PURPLE) );
    }


    private void startNewGame(){
        if(game != null){
            game.startNewGame();
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
        gridMap.getRow(index).setBackgroundColor(color);
    }


    private void updateClueView(View clueLayout, Clue clue) {
        int clueColorId = switch (clue) {
            case WRONG -> R.color.clue_default_background;
            case COW -> R.color.clue_cow;
            case BULL -> R.color.clue_bull;
        };
        var context = getContext();
        if(context != null){
            clueLayout.setBackgroundColor(context.getColor(clueColorId));
        }
    }


    @Override
    public void resetAllRows(){
        inputButtonHelper.setInitialized(false);
        initGridWiper();
        gridWiper.resetAllRows();
    }


    @Override
    public void resetAllRowsInstantly(){
        inputButtonHelper.setInitialized(false);
        initGridWiper();
        gridWiper.resetAllRowsInstantly();
        showPegAsCurrent(new PegCoordinates(0,0));
    }


    private void initGridWiper(){
        if(gridWiper == null){
            gridWiper = new GridWiper(this, game);
        }
    }


    private ImageButton setupButton(View parent, int resId, Runnable runnable){
        ImageButton button  = parent.findViewById(resId);
        button.setOnClickListener(v -> runnable.run());
        return button;
    }


    @Override
    public void setPegColor(int row, int index, PegColor pegColor) {
        var pegView = getPegViewAt(row, index);
        setPegColor(pegView, pegColor.getColorId());
    }


    private void setPegColor(View pegView, int colorId) {
        var context = getContext();
        if(context == null){
            return;
        }
        var drawable = AppCompatResources.getDrawable(getContext(), R.drawable.peg);
        if(drawable != null){
            var amended = drawable.mutate();
            var color = context.getColor(colorId);
            amended.setColorFilter(color, PorterDuff.Mode.LIGHTEN);
            pegView.setBackground(amended);
        }
    }


    @Override
    public void showPegAsCurrent(PegCoordinates pegCoordinates) {
        if(pegCoordinates.isNull()){
            return;
        }
        var row = pegCoordinates.row();
        if(row >= gridMap.size()){
            return;
        }
        var pegView = getPegViewAt(row, pegCoordinates.column());
        if(pegView != null){
            pegView.setBackgroundResource(R.drawable.peg_current);
        }
    }


    private View getPegViewAt(int row, int index) {
        var rowViewGroup = gridMap.getPegRow(row);
        if(rowViewGroup == null){
            return null;
        }
        var pegLayout = (ViewGroup)rowViewGroup.getChildAt(index);
        if(pegLayout == null){
            return null;
        }
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
        gridMap.getRow(rowIndex).setBackgroundColor(color);
    }


    private int getThemedColor(int attrId){
        var typedValue = new TypedValue();
        var activity = getActivity();
        if(activity == null){
            return Color.BLACK;
        }
        activity.getTheme().resolveAttribute(attrId, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

}