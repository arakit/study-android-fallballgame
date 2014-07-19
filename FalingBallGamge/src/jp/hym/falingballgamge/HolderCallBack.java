package jp.hym.falingballgamge;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.SurfaceHolder;

public class HolderCallBack implements SurfaceHolder.Callback, Runnable{

	private SurfaceHolder holder = null;
	private Thread thread = null;
	private boolean isAttached = true;

    //resources
    private Bitmap image_hall;
    private Bitmap image_ball;
    private Bitmap image_goal;
    private Bitmap image_floor;

	private float width, height;	//画面サイズ
	private float dx=0, dy=0;	//ボール速度
	
	//ゲーム調整用	
	private float coordAcceleration = 0.01f;	//加速度調整
	private float coordMaxSpeed = 20.0f;	//ボールスピードの上限調整
	private float coordResist = 0.1f;	//抵抗調整
	private float coordRebound = 0.8f;	//跳ね返りの大きさ調整
	private float coordCd = 0.5f;	//当たり判定

	
	//高校生がいじる用
	private int ballSize = 50;	//ボールサイズ
	private int ball_x, ball_y;	//ボール座標
	private int start_x = ballSize, start_y = ballSize;	//初期座標
	private float acceleration = 1.0f;	//加速度
	private float maxSpeed = 1.0f;	//ボールスピードの上限
	private List<PointF> holls = new ArrayList<PointF>();//穴の座標
	private int hollsSize = 100;	//穴の大きさ
	private int goal_x = 500, goal_y = 500;	//ゴール座標
	private int goalSize = 100;	//ゴールの大きさ
	private float resist = 1.0f; //抵抗
	private float rebound = 1.0f;	//跳ね返りの大きさ
	
	private FragmentActivity mActivity;

	Context context;
	InclinationSensor is;

	public HolderCallBack(FragmentActivity activity) {
		mActivity = activity;
		is = new InclinationSensor(activity, activity.getWindowManager().getDefaultDisplay().getOrientation());
        Resources res = activity.getResources();
        image_ball = ((BitmapDrawable) res.getDrawable(R.drawable.ic_ball_01)).getBitmap();
        image_hall = ((BitmapDrawable) res.getDrawable(R.drawable.ic_hall_01)).getBitmap();
        image_goal = ((BitmapDrawable) res.getDrawable(R.drawable.ic_goal_01)).getBitmap();
        image_floor = ((BitmapDrawable) res.getDrawable(R.drawable.bg_floor_01_tile_parts)).getBitmap();
	}

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	//Log.i("System.out" , "HoldCallBackのsurfaceChanged");
    	
    	this.width = width;
    	this.height = height;
    	
    	Log.i("System.out", "画面サイズ(縦:" + this.height + "横:" + this.width + ")");
    	
    	//ランダムで障害物配置
    	for(int i=0;i<5;i++){
    		//holls.add(new PointF((float)Math.random()*width, (float)Math.random()*height));
    	}

//↓プログラミング体験

    	start_x = ballSize;	//スタートx座標
    	start_y = ballSize;	//スタートy座標
    	ballSize = 50;	//ボールサイズ
    	acceleration = 1.0f;	//加速度
    	maxSpeed = 2.0f;	//ボールスピードの上限の絶対値

    	goal_x = 500;	//ゴールx座標
    	goal_y = 500;	//ゴールy座標
    	goalSize = 100;	//ゴールの大きさ

    	holls.add(new PointF(500, 200));	//holls.add(new PointF(穴x座標,穴y座標))	＊複数追加OK
    	hollsSize = 100;	//穴の大きさ

    	resist = 1.0f;	//抵抗
    	rebound = 1.0f;	//跳ね返りの大きさ
    	
//↑プログラミング体験

    	//無理な数値を調整
    	
    	maxSpeed = Math.abs(maxSpeed);	//速度
    	
    	//枠内に変更
    	start_x = Inside(start_x, ballSize, (int)this.width);	//スタートx座標
    	start_y = Inside(start_y, ballSize, (int)this.height);	//スタートy座標
    	ball_x = start_x;	//ボールの座標をあわせる
    	ball_y = start_y;	//ボールの座標をあわせる

    	goal_x = Inside(goal_x, goalSize, (int)this.width);	//ゴールx座標
    	goal_y = Inside(goal_y, goalSize, (int)this.height);	//ゴールy座標

    	for(int i=0;i<holls.size();i++){
    		holls.get(i).x = Inside((int)holls.get(i).x, hollsSize, (int)this.width);	//穴x座標
    		holls.get(i).x = Inside((int)holls.get(i).x, hollsSize, (int)this.height);	//穴y座標
    	}
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	//Log.i("System.out" , "HoldCallBackのsurfaceCreated");
        this.holder = holder;
        thread = new Thread(this);
        thread.start(); //スレッドを開始
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	//Log.i("System.out" , "HoldCallBackのsurfaceDestroyed");
           isAttached = false;
           thread = null; //スレッドを終了
    }

    @Override
    public void run() {

    	while( isAttached ){

    		is.getInclination();	//傾きセンサー取得
    		String strlog = "roll = " + is.GetRoll() + "\tpicth = " + is.GetPicth();
        	//Log.i("System.out", strlog);
        	
        	dx += is.GetRoll() * coordAcceleration * acceleration;
        	dy += -is.GetPicth() *coordAcceleration * acceleration;
        	
        	//速度の上限
        	if(dx > coordMaxSpeed * maxSpeed){dx = coordMaxSpeed * maxSpeed;}
        	if(dx < -(coordMaxSpeed * maxSpeed)){dx = -(coordMaxSpeed * maxSpeed);}
        	if(dy > coordMaxSpeed * maxSpeed){dy = coordMaxSpeed * maxSpeed;}
        	if(dy < -(coordMaxSpeed * maxSpeed)){dy = -(coordMaxSpeed * maxSpeed);}

        	//抵抗
        	if(dx > coordResist * resist){
        		dx -= coordResist * resist;
        	}else
        	if(dx < -(coordResist * resist)){
        		dx += coordResist * resist;
        	}else{
        		dx = 0;
        	}
        	if(dy > coordResist * resist){
        		dy -= coordResist * resist;
        	}else
        	if(dy < -(coordResist * resist)){
        		dy += coordResist * resist;
        	}else{
        		dy = 0;
        	}
        	
    		//枠外判定
    		if(ball_x < ballSize || this.width - ballSize < ball_x){
    			dx = (int) - dx * coordRebound * rebound;
    		}
    		if(ball_y < ballSize || this.height - ballSize < ball_y){
    			dy = (int) - dy * coordRebound * rebound;
    		}

    		//枠外に行ってしまった場合
    		if(ball_x < ballSize){ ball_x = ballSize; }
    		if(this.width - ballSize < ball_x){ ball_x = (int)this.width - ballSize; }
    		if(ball_y < ballSize){ ball_y = ballSize; }
    		if(this.height - ballSize < ball_y){ ball_y = (int)this.height - ballSize; }

    		//Log.i("System.out", "座標(" +ball_x + "," + ball_y + ")");
    		
    		ball_x = ball_x + (int)dx;
    		ball_y = ball_y + (int)dy;

    		draw();	//描画
    		
    		//ゴール判定
    		if(Math.pow((goalSize - (ballSize * coordCd)), 2) >= Math.pow((goal_x - ball_x), 2) + Math.pow((goal_y - ball_y), 2)){
				
				new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent(mActivity, GoalActivity.class);
						mActivity.startActivity(intent);
					}
				}, 100);
				//ループ止める
				isAttached = false;
				
				//傾きセンサー開放
				is.releaseSensor();
			}
			
    		//当たり判定
			for(int i=0;i<holls.size();i++)
				if(Math.pow((hollsSize - (ballSize * coordCd)), 2) >= Math.pow((holls.get(i).x - ball_x), 2) + Math.pow((holls.get(i).y - ball_y), 2)){
					ball_x = start_x;
					ball_y = start_y;
					dx = 0;
					dy = 0;
				}
			}
    }

    //描画
    private void draw(){
    	//描画処理を開始
		Canvas canvas = holder.lockCanvas();
        if(canvas == null) return ;
		//canvas.drawColor(255, PorterDuff.Mode.CLEAR);
		//canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
        drawBackgroundRepeat(canvas, image_floor, paint);

    	//穴描画
    	for(int i = 0; i < holls.size(); i++){
			paint.setColor(Color.BLACK);	//色
			//canvas.drawCircle(holls.get(i).x, holls.get(i).y, hollsSize, paint);
            drawBall(canvas, image_hall, paint, holls.get(i).x, holls.get(i).y, hollsSize);
    	}

    	//ゴール描画
		paint.setColor(Color.RED);	//色
        drawBall(canvas, image_goal, paint, goal_x, goal_y, goalSize);
		//canvas.drawCircle(goal_x, goal_y, goalSize, paint);

    	//ボール描画
		paint.setColor(Color.BLUE);	//色
        drawBall(canvas, image_ball, paint, ball_x, ball_y, ballSize);
		//canvas.drawCircle(ball_x, ball_y, ballSize, paint);

		holder.unlockCanvasAndPost(canvas);	//描画処理を終了
    }

    private void drawBall(Canvas cv, Bitmap bmp, Paint p, float cx, float cy, int ball_size){
        float scale = ball_size / ((float)bmp.getWidth()/2);

        Rect s = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        Rect d = new Rect(
                (int)(cx-bmp.getWidth()*scale/2), (int)(cy-bmp.getHeight()*scale/2),
                (int)(cx+bmp.getWidth()*scale/2), (int)(cy+bmp.getHeight()*scale/2)
        );

        cv.drawBitmap(bmp, s, d, p);
    }

    private void drawBackgroundRepeat(Canvas cv, Bitmap bmp, Paint p){

        for(int y=0; y<height; y+=bmp.getHeight()) {
            for (int x = 0; x < width; x += bmp.getWidth()) {
                cv.drawBitmap(bmp, x, y, p);
            }
        }

    }

    //枠内にする
    private int Inside(int point, int size, int max){

    	if(point < size){return size;}
    	if(max - size < point){return max - size;}
    	return point;
    }
}
