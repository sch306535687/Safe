package sun.ch.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import sun.ch.dao.AddressDao;
import sun.ch.utils.ShowWindowManager;

/**
 * 接收打出去的电话
 */
public class OutgoingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String resultData = getResultData();//获取外拨电话
        String address = AddressDao.getAddress(resultData);//获取归属地
        //弹出浮窗
        ShowWindowManager.showWindow(context,address);
    }
}
