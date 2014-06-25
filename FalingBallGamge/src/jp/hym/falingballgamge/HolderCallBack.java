package jp.hym.falingballgamge;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.graphics.PorterDuff;

public class HolderCallBack implements SurfaceHolder.Callback, Runnable{
	
	private SurfaceHolder holder = null;
	private Thread thread = null;
	private boolean isAttached = true;
	
	private float dx = 10, dy = 10;
	private float width, height;
	private int circle_x, circle_y; 
	
	/*
	public HolderCallBack()	{
		
	}
	*/
	
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	Log.i("System.out" , "HoldCallBackのsurfaceChanged");
    	this.width = width;
    	this.height = height;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	Log.i("System.out" , "HoldCallBackのsurfaceCreated");
        this.holder = holder;
        thread = new Thread(this);
        thread.start(); //スレッドを開始
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	Log.i("System.out" , "HoldCallBackのsurfaceDestroyed");
           isAttached = false;
           thread = null; //スレッドを終了
    }

    @Override
    public void run() {
    	Log.i("System.out" , "HoldCallBackのrun");
    	
    	while( isAttached ){
    		if(circle_x < 0 || this.width < circle_x){
    			dx = -dx;
    		}
    		if(circle_y < 0 || this.height < circle_y){
    			dy = -dy;
    		}
    		
    		circle_x += dx;
    		circle_y += dy;
    		
    		//描画処理を開始
			Canvas canvas = holder.lockCanvas();
			canvas.drawColor(0, PorterDuff.Mode.CLEAR);
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			canvas.drawCircle(circle_x, circle_y, 50, paint);

			//描画処理を終了
			holder.unlockCanvasAndPost(canvas);
        }
    }   
}
