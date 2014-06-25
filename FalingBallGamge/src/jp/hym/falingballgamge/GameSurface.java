package jp.hym.falingballgamge;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView{
	
	private HolderCallBack cb;
	
	public GameSurface(Context context){
		super(context);
		SurfaceHolder holder = getHolder();
		cb = new HolderCallBack();
		holder.addCallback(cb);
	}
}