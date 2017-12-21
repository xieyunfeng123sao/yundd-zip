
package com.vomont.yundudao.ui.setting;

import com.vomont.yundudao.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AppProblemActivity extends Activity
{
    private TextView top_name;
    private ImageView go_back;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appproblem);
        top_name=(TextView)findViewById(R.id.top_name);
        top_name.setText("常见问题");
        go_back=(ImageView)findViewById(R.id.go_back);
        go_back.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        
    }
}
