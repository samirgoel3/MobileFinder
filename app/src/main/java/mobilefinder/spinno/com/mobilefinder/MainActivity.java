package mobilefinder.spinno.com.mobilefinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class MainActivity extends Activity {

    private int mBindFlag;
    public Messenger mServiceMessenger;
    Context  activityContext ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityContext  = MainActivity.this ;
        Intent service = new Intent(activityContext, VoiceCommandService.class);
        activityContext.startService(service);
        mBindFlag = Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH ? 0 : Context.BIND_ABOVE_CLIENT;
    }



    @Override
    protected void onStart()
    {
        super.onStart();

        bindService(new Intent(this, VoiceCommandService.class), mServiceConnection, mBindFlag);
    }


    @Override
    protected void onStop(){
        super.onStop();
        {
            unbindService(mServiceConnection);
            mServiceMessenger = null;
        }
    }


    private final ServiceConnection mServiceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
         //   if (DEBUG) {
        //        Log.d("____MAIN ACTIVITY", "onServiceConnected");}

            mServiceMessenger = new Messenger(service);
            Message msg = new Message();
            msg.what = VoiceCommandService.MSG_RECOGNIZER_START_LISTENING;

            try
            {

                mServiceMessenger.send(msg);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
           // if (DEBUG) {Log.d(TAG, "onServiceDisconnected");} //$NON-NLS-1$
            mServiceMessenger = null;
        }

    }; // mServiceConnection







}