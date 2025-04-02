package com.jcrawley.mastermind.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


import com.jcrawley.mastermind.MainActivity;
import com.jcrawley.mastermind.game.Game;


public class GameService extends Service {
    IBinder binder = new LocalBinder();
    private final Game game;
    private MainActivity mainActivity;



    public GameService() {
        super();
        game = new Game();
    }



    @Override
    public void onCreate() {
      //  game.init();
    }


    public Game getGame(){
        return game;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY; // service is not restarted when terminated
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }


    @Override
    public void onRebind(Intent intent) {
    }


    @Override
    public void onDestroy() {
        mainActivity = null;
    }


    public boolean isActivityUnbound(){
        return mainActivity == null;
    }


    public void setActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        game.setView(mainActivity);
    }


    public class LocalBinder extends Binder {
        public GameService getService() {
            return GameService.this;
        }
    }

}
