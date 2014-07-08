package jp.hym.falingballgamge;

import java.util.List;

//import com.example.graffitimessage.SensorValue;

//import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
//import android.util.EventLog.Event;
//import android.widget.TextView;

public class InclinationSensor{
	
	SensorManager mSensorManager;
	Sensor mMagSensor;
	Sensor mAccSensor;
	
	SensorValue mSensorValue;
	
	private float roll;	//回転角
	private float picth;	//傾斜角

	public InclinationSensor(Context context){
		mSensorValue = new SensorValue();
		mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
	}

	public void getInclination(){
		// センサの取得
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        // センサマネージャへリスナーを登録(implements SensorEventListenerにより、thisで登録する)
        for (Sensor sensor : sensors) {

            if( sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                mSensorManager.registerListener(mSendorLis, sensor, SensorManager.SENSOR_DELAY_UI);
                mMagSensor = sensor;
            }

            if( sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                mSensorManager.registerListener(mSendorLis, sensor, SensorManager.SENSOR_DELAY_UI);
                mAccSensor = sensor;
            }
        }
	}

	//センサー解放
    public void releaseSensor(){
    	//センサーマネージャのリスナ登録破棄
        if (mMagSensor!=null || mAccSensor!=null) {
            mSensorManager.unregisterListener(mSendorLis);
            mMagSensor = null;
            mAccSensor = null;
        }
    }

      //センサー
    private SensorEventListener mSendorLis = new SensorEventListener() {

    	public void onSensorChanged(SensorEvent event) {
    		mSensorValue.onSensorChanged(event);

    		roll = (float)mSensorValue.getRoll() ;
    		picth = (float)mSensorValue.getPitch() ;
    		//direction = (float)mSensorValue.getAzimuthDegree();
    	}

    	public void onAccuracyChanged(Sensor sensor, int accuracy){

   		}
    }; 
    
    float GetRoll(){
		return roll;
	}
	
	public float GetPicth(){
		return picth;
	}
}