package jp.crudefox.hall.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import jp.crudefox.hall.R;
import jp.crudefox.hall.game.GameLogic;
import jp.crudefox.hall.game.GameSurfaceView;
import jp.crudefox.hall.game.GameThread;

/**
 * Created by chikara on 2014/07/16.
 */
public class GameFragment extends Fragment{

    public static final GameFragment newInstance(){
        GameFragment f = new GameFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }


    private GameThread mGameThread;
    private GameLogic mGameLogic;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGameLogic = new GameLogic();

        mGameThread = new GameThread(mGameLogic, null);
        mGameThread.start();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, null);

        Log.d("GameFragment", "onCreateView");

        ViewGroup gameContainer = (ViewGroup) view.findViewById(R.id.fragment_game_container);
        GameSurfaceView gv = new GameSurfaceView(getActivity());
        gameContainer.addView(gv);
        mGameThread.setView(gv);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mGameThread.cancel();
        mGameThread = null;

    }




}
