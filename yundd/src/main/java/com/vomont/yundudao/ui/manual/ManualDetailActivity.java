package com.vomont.yundudao.ui.manual;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.ManualBean;
import com.vomont.yundudao.db.Db_Manual;
import com.vomont.yundudao.ui.manual.adapter.SelectedDevAdapter;
import com.vomont.yundudao.ui.manual.adapter.SelectedDevAdapter.OnDeleteListener;

public class ManualDetailActivity extends Activity implements OnClickListener
{
    
    private EditText input_manual_name;
    
    private TextView manual_actual_time;
    
    private SeekBar change_manual_time;
    
    private CheckBox check_manual;
    
    private TextView add_manual;
    
    private ImageView manual_go_back;
    
    private int text_left;
    
    private Db_Manual db_Manual;
    
    private ImageView add_manual_dev;
    
    private ListView manual_list;
    
    private final int ACTION_SELDEV = 0xffff01;
    
    private List<DeviceInfo> mlist;
    
    private List<FactoryInfo> mlist_factory;
    
    private SelectedDevAdapter adapter;
    
    private int pos = 60;
    
    private boolean isFirst = true;
    
    private ManualBean manualBean;
    
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_detail);
        initView();
        initListener();
        db_Manual = new Db_Manual(this);
        mlist = new ArrayList<DeviceInfo>();
        mlist_factory = (List<FactoryInfo>)getIntent().getSerializableExtra("factoryBean");
        manualBean = (ManualBean)getIntent().getSerializableExtra("manualdetail");
    }
    
    private void initData()
    {
        
        adapter = new SelectedDevAdapter(this, mlist, mlist_factory);
        manual_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(manual_list);
        adapter.setDeleteClickListener(new OnDeleteListener()
        {
            @Override
            public void onClick(int position)
            {
                mlist.remove(position);
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(manual_list);
            }
        });
        
        text_left = change_manual_time.getWidth() - manual_actual_time.getWidth();
        change_manual_time.setMax(150 - 15);
        change_manual_time.setProgress(45);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(text_left * 45 / (150 - 15), 0, 0, 0);
        manual_actual_time.setLayoutParams(params);
        manual_actual_time.setText("60s");
        change_manual_time.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                
            }
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.setMargins(text_left * progress / (150 - 15), 0, 0, 0);
                manual_actual_time.setLayoutParams(params);
                manual_actual_time.setVisibility(View.VISIBLE);
                manual_actual_time.setText(15 + progress + "s");
                pos = progress + 15;
            }
        });
        initManual();
    }
    
    private void initManual()
    {
        if (manualBean != null)
        {
            change_manual_time.setProgress(manualBean.getLength() - 15);
            manual_actual_time.setText(manualBean.getLength() + "s");
            if (manualBean.getMode() == 1)
            {
                check_manual.setChecked(true);
            }
            input_manual_name.setText(manualBean.getName());
            mlist.addAll(manualBean.getDevList());
            adapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(manual_list);
        }
    }
    
    private void savaManual()
    {
        ManualBean bean = new ManualBean();
        if (TextUtils.isEmpty(input_manual_name.getText().toString()))
        {
            Toast.makeText(this, "方案名不能为空！", Toast.LENGTH_LONG).show();
            return;
        }
        if (mlist == null || mlist.size() == 0)
        {
            Toast.makeText(this, "监控点不能为空！", Toast.LENGTH_LONG).show();
            return;
        }
        
        String idList = "";
        for (int i = 0; i < mlist.size(); i++)
        {
            idList = idList + mlist.get(i).getDeviceid() + ",";
        }
        bean.setName(input_manual_name.getText().toString());
        bean.setDevList(mlist);
        bean.setLength(pos);
        bean.setDeviceIdList(idList);
        bean.setMode(check_manual.isChecked() ? 1 : 0);
        db_Manual.addManual(bean);
        finish();
    }
    
    private void upDataManual()
    {
        ManualBean bean = new ManualBean();
        if (TextUtils.isEmpty(input_manual_name.getText().toString()))
        {
            Toast.makeText(this, "方案名不能为空！", Toast.LENGTH_LONG).show();
            return;
        }
        if (mlist == null || mlist.size() == 0)
        {
            Toast.makeText(this, "监控点不能为空！", Toast.LENGTH_LONG).show();
            return;
        }
        
        String idList = "";
        for (int i = 0; i < mlist.size(); i++)
        {
            idList = idList + mlist.get(i).getDeviceid() + ",";
        }
        bean.setId(manualBean.getId());
        bean.setName(input_manual_name.getText().toString());
        bean.setDevList(mlist);
        bean.setLength(pos);
        bean.setDeviceIdList(idList);
        bean.setMode(check_manual.isChecked() ? 1 : 0);
        db_Manual.updataManual(bean);
        finish();
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (isFirst)
        {
            initData();
            isFirst = false;
        }
    }
    
    private void initListener()
    {
        add_manual.setOnClickListener(this);
        manual_go_back.setOnClickListener(this);
        add_manual_dev.setOnClickListener(this);
    }
    
    private void initView()
    {
        manual_list = (ListView)findViewById(R.id.manual_list);
        input_manual_name = (EditText)findViewById(R.id.input_manual_name);
        manual_actual_time = (TextView)findViewById(R.id.manual_actual_time);
        change_manual_time = (SeekBar)findViewById(R.id.change_manual_time);
        check_manual = (CheckBox)findViewById(R.id.check_manual);
        add_manual = (TextView)findViewById(R.id.add_manual);
        manual_go_back = (ImageView)findViewById(R.id.manual_go_back);
        add_manual_dev = (ImageView)findViewById(R.id.add_manual_dev);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.add_manual:
                if (manualBean == null)
                {
                    savaManual();
                }
                else
                {
                    upDataManual();
                }
                break;
            case R.id.manual_go_back:
                finish();
                break;
            case R.id.add_manual_dev:
                Intent intent = new Intent(ManualDetailActivity.this, ManualSelDevActivity.class);
                startActivityForResult(intent, ACTION_SELDEV);
                break;
            default:
                break;
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case ACTION_SELDEV:
                if (data != null)
                {
                    mlist.addAll((List<DeviceInfo>)data.getSerializableExtra("dev"));
                    adapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(manual_list);
                }
                break;
            
            default:
                break;
        }
    }
    
    /**
     * 动态设置ListView的高度
     * 
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView)
    {
        if (listView == null)
            return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
