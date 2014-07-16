package jp.crudefox.hall.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import jp.hym.falingballgamge.HolderCallBack;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {


    private Paint mPaint;

    private int mWidth;
    private int mHeight;



	public GameSurfaceView(Context context){
		super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        Log.d("GameSurfaceView", "init");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

    public void drawGame(GameLogic logic){
        SurfaceHolder holder = getHolder();
        if(holder == null) return ;
        Canvas cv = holder.lockCanvas();
        if(cv == null) return ;
        onDrawGame(cv, logic);
        holder.unlockCanvasAndPost(cv);
    }


    protected void onDrawGame(Canvas canvas, GameLogic logic){

        Log.d("GameSurfaceView", "onDrawGame");

        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(mWidth/2, mHeight/2, 100, mPaint);

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }



}