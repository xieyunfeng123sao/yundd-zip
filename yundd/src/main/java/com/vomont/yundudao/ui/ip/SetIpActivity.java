package com.vomont.yundudao.ui.ip;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.vomont.yundudao.R;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.ui.ip.adapter.ItemIpAdapter;
import com.vomont.yundudao.utils.ACache;

public class SetIpActivity extends Activity implements OnClickListener
{
    private ImageView ip_go_back;
    
    private TextView ip_sure;
    
    private EditText input_ip;
    
    private ImageView clear_ip;
    
    private ListView history_ip;
    
    private String top = "http://";
    
    private String url;
    
    private ACache aCache;
    
    private List<String> mlist;
    
    private ItemIpAdapter adapter;
    
    private boolean isFinish = true;
    
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_setting);
        aCache = ACache.get(this);
        mlist = (List<String>)aCache.getAsObject("ips");
        initView();
        initData();
        initListener();
    }
    
    private void initListener()
    {
        ip_go_back.setOnClickListener(this);
        ip_sure.setOnClickListener(this);
        clear_ip.setOnClickListener(this);
    }
    
    private void initData()
    {
        url = Appcation.BASE_URL.replace(top, "");
        input_ip.setText(url);
        adapter = new ItemIpAdapter(this);
//        Collections.reverse(mlist);
        adapter.setData(mlist);
        history_ip.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(history_ip);
        history_ip.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                input_ip.setText(mlist.get(position));
                Appcation.BASE_URL = top + mlist.get(position);
                String[] strs = Appcation.BASE_URL.split(":");
                if (strs.length != 3)
                {
                    Appcation.BASE_URL = Appcation.BASE_URL + ":8051";
                }
            }
        });
    }
    
    private void initView()
    {
        ip_go_back = (ImageView)findViewById(R.id.ip_go_back);
        ip_sure = (TextView)findViewById(R.id.ip_sure);
        input_ip = (EditText)findViewById(R.id.input_ip);
        clear_ip = (ImageView)findViewById(R.id.clear_ip);
        history_ip = (ListView)findViewById(R.id.history_ip);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ip_go_back:
                finish();
                break;
            case R.id.ip_sure:
                isFinish = true;
                if (!input_ip.getText().toString().isEmpty())
                {
                    Appcation.BASE_URL = top + input_ip.getText().toString();
                    String[] str = Appcation.BASE_URL.split(":");
                    if (str.length != 3)
                    {
                        if (Appcation.BASE_URL.endsWith(":"))
                        {
                            Appcation.BASE_URL = Appcation.BASE_URL + "8051";
                        }
                        else
                        {
                            Appcation.BASE_URL = Appcation.BASE_URL + ":8051";
                        }
                    }
                    else
                    {
                        for (int i = 0; i < str.length; i++)
                        {
                            if (i == 1 && str[1].isEmpty())
                            {
                                Toast.makeText(SetIpActivity.this, "请输入IP地址", Toast.LENGTH_SHORT).show();
                                isFinish = false;
                                break;
                            }
                            if (i == 2 && str[2].isEmpty() || str[2].substring(0, 1).equals("0"))
                            {
                                Toast.makeText(SetIpActivity.this, "请输入正确的端口号", Toast.LENGTH_SHORT).show();
                                isFinish = false;
                                break;
                            }
                        }
                    }
                    if (isFinish)
                        finish();
                }
                else
                {
                    Toast.makeText(SetIpActivity.this, "请输入IP地址", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.clear_ip:
                input_ip.setText("");
                break;
            default:
                break;
        }
    }
    
    public void setListViewHeightBasedOnChildren(ListView listView)
    {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return;
        }
        
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++)
        {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
