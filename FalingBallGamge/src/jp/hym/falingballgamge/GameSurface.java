package jp.hym.falingballgamge;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView{
	
	private HolderCallBack cb;
	
	public GameSurface(FragmentActivity context){
		super(context);
		SurfaceHolder holder = getHolder();
		cb = new HolderCallBack(context);
		holder.addCallback(cb);
	}
}