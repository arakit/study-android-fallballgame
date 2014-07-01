package jp.hym.falingballgamge;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.graphics.PorterDuff;

public class HolderCallBack implements SurfaceHolder.Callback, Runnable{
	
	private SurfaceHolder holder = null;
	private Thread thread = null;
	private boolean isAttached = true;
	
	private float dx = 10, dy = 10;	//ボールスピード
	private float width, height;	//画面サイズ
	
	private int ballSize = 50;	//ボールサイズ
	private int ball_x, ball_y;	//ボール座標
	private int start_x = ball_x, start_y = ball_y;	//初期座標
	
	private List<PointF> holls = new ArrayList<PointF>();//穴の座標
	private int hollsSize = 50;	//穴の大きさ
	
	private int goal_x, goal_y;	//ゴール座標
	private int goalSize;	//ゴールの大きさ
	
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	Log.i("System.out" , "HoldCallBackのsurfaceChanged");
    	this.width = width;
    	this.height = height;
    	
    	//ランダムで障害物配置
    	for(int i=0;i<5;i++){
    		holls.add(new PointF((float)Math.random()*width, (float)Math.random()*height));
    	}
    	
    	//プログラミング体験
    	
    	/*スタート座標変更
    	 * start_x = 100;
    	 * start_y = 200;
    	 */
    	
    	//ゴール座標変更
    	goal_x = 500;
    	goal_y = 500;
    	
    	goalSize = 60;
    	 
    	/*穴の座標変更
    	 * holls.add(new PointF(100, 200);
    	 * holls.add(new PointF(100, 220); 
    	 */
    	
    	/*穴のサイズ変更
    	 * hollsSize = 50;
    	 */
    		
    	//スタート位置
    	ball_x = ballSize*2;
    	ball_y = ballSize*2;
    	
    	start_x = ball_x*2;
    	start_y = ball_y*2;
    	
    	//枠外の座標を枠内にする
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
    		
    		//枠外判定
    		if(ball_x <= ballSize || this.width - ballSize <= ball_x){
    			dx = -dx;
    		}
    		if(ball_y <= ballSize || this.height - ballSize <= ball_y){
    			dy = -dy;
    		}
    		
    		ball_x += dx;
    		ball_y += dy;
    		
    		//描画処理を開始
			Canvas canvas = holder.lockCanvas();
			canvas.drawColor(255, PorterDuff.Mode.CLEAR);
			Paint paint = new Paint();
			paint.setColor(Color.RED);	//色
			canvas.drawCircle(ball_x, ball_y, ballSize, paint);	//ボール描画
			
			for(int i=0;i<5;i++){
				paint.setColor(Color.WHITE);	//色
				canvas.drawCircle(holls.get(i).x, holls.get(i).y, ballSize, paint);	//穴描画
				//当たり判定
				/*
				if(Math.abs(holls.get(i).x - ball_x) < 30 && Math.abs(holls.get(i).y - ball_y) < 30){
					ball_x = start_x;
					ball_y = start_y;
				}
				*/
				
			}
			
			paint.setColor(Color.BLUE);	//色
			canvas.drawCircle(goal_x, goal_y, goalSize, paint);
			
			//ゴール判定
			if(Math.abs(goal_x - ball_x) < 30 && Math.abs(goal_y - ball_y) < 30)
			{
				Log.i("System.out", "ゴーーーーーーール");
			}
			
			Log.i("System.out", "座標(" + ball_x + "," + ball_y + ")\n");
			
			//描画処理を終了
			holder.unlockCanvasAndPost(canvas);
        }
    }
    
    //枠内調整
    int adjustment(int point, int size, int max){
    	if(point < size){ return size; }
    	if(max - size/2 < point){ return max - size/2; }
    	return point;
    }
}
