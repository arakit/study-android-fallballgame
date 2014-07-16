package jp.crudefox.hall.game;

import android.util.Log;

import java.util.Date;

/**
 * Created by chikara on 2014/07/16.
 */
public class GameThread extends Thread {

    enum State{
        pause,
        started,
        destroyed,
    }

    boolean mIsRunning = false;


    private GameSurfaceView mView;
    private GameLogic mGameLogic;


    public GameThread(GameLogic logic, GameSurfaceView view){
        super();
        mView = view;
        mGameLogic = logic;
    }

    public void setView(GameSurfaceView view){
        mView = view;
    }




    @Override
    public void run() {
        super.run();
        mIsRunning = true;

        mGameLogic.init();

        while(mIsRunning){

            if(mGameLogic!=null){
                mGameLogic.progress();

                if(mView!=null) {
                    mView.drawGame(mGameLogic);
                }
            }

            try {
                Thread.sleep(50);
            }catch (InterruptedException ex){
                if(!mIsRunning) continue;
            }

        }


    }






    public boolean isRunning(){
        return mIsRunning;
    }


    public void cancel(){
        if(!mIsRunning) return ;

        mIsRunning = false;
        this.interrupt();

    }


}
