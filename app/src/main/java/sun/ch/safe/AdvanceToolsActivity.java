package sun.ch.safe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Administrator on 2016/12/7.
 */
public class AdvanceToolsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advancetools);
    }

    public void next(View view){
        //跳到归属地查询界面
        startActivity(new Intent(this,QueryAddress.class));
    }
}