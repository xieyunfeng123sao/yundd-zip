package com.vomont.yundudao.ui.factory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.view.BaseActivity;
import com.vomont.yundudao.view.listbar.CharacterParser;
import com.vomont.yundudao.view.listbar.ClearEditText;
import com.vomont.yundudao.view.listbar.PinyinSubComparator;
import com.vomont.yundudao.view.listbar.SideBar;
import com.vomont.yundudao.view.listbar.SideBar.OnTouchingLetterChangedListener;
import com.vomont.yundudao.view.listbar.SortAdapter;
import com.wmclient.clientsdk.WMClientSdk;
import com.wmclient.clientsdk.WMDeviceInfo;

public class FactoryActivity extends BaseActivity implements OnClickListener
{
    private ImageView go_back;
    
    @SuppressWarnings("unused")
    private TextView top_name, dialog;
    
    private SideBar sidrbar;
    
    private ListView sort_factory;
    
    private SortAdapter adapter;
    
    private ClearEditText filter_edit;
    
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    
    private List<SubFactory> SourceDateList;
    
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinSubComparator pinyinComparator;
    
    
    private FactoryInfo factoryInfo;
    
    private int factoryid;
    
    private List<FactoryInfo> mlist;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory);
        initViews();
        initData();
    }
    
    private void initViews()
    {
        go_back = (ImageView)findViewById(R.id.go_back);
        top_name = (TextView)findViewById(R.id.top_name);
        sidrbar = (SideBar)findViewById(R.id.sidrbar);
        sort_factory = (ListView)findViewById(R.id.sort_factory);
        dialog = (TextView)findViewById(R.id.dialog);
        filter_edit = (ClearEditText)findViewById(R.id.filter_edit);
        go_back.setOnClickListener(this);
        top_name.setText(R.string.radio_factory);
        
        filter_edit.setCursorVisible(false);
        filter_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_edit.setCursorVisible(true);
            }
         });
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        SourceDateList = new ArrayList<SubFactory>();
        pinyinComparator = new PinyinSubComparator();
        
        // 设置右侧触摸监听
        sidrbar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener()
        {
            @Override
            public void onTouchingLetterChanged(String s)
            {
                // 该字母首次出现的位置
                if (adapter != null && s != null)
                {
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1)
                    {
                        sort_factory.setSelection(position);
                    }
                }
                
            }
        });
        // sort_factory.setOnItemClickListener(new OnItemClickListener()
        // {
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view, int
        // position, long id)
        // {
        // // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
        // Toast.makeText(getApplication(),
        // ((SortModel)adapter.getItem(position)).getName(),
        // Toast.LENGTH_SHORT).show();
        // }
        // });
        // 根据a-z进行排序源数据
        
        filter_edit = (ClearEditText)findViewById(R.id.filter_edit);
        // 根据输入框输入值的改变来过滤搜索
        filter_edit.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
            
            @Override
            public void afterTextChanged(Editable s)
            {
                
            }
        });
        
    }
    
    @SuppressWarnings("unchecked")
    private void initData()
    {
        Intent intent = getIntent();
        factoryInfo = (FactoryInfo)intent.getSerializableExtra("factory");
        mlist = (List<FactoryInfo>)intent.getSerializableExtra("factoryBean");
        top_name.setText(factoryInfo.getFactoryname());
        // shareUtil = new ShareUtil(this);
        SourceDateList = new ArrayList<SubFactory>();
        factoryid = factoryInfo.getFactoryid();
        if (factoryInfo.getMlist() != null && factoryInfo.getMlist().size() != 0)
        {
            for (int i = 0; i < factoryInfo.getMlist().size(); i++)
            {
                SourceDateList.add(factoryInfo.getMlist().get(i));
            }
            SourceDateList = filledData(SourceDateList);
            adapter = new SortAdapter(this, SourceDateList, factoryid, mlist);
            sort_factory.setAdapter(adapter);
        }
        List<WMDeviceInfo> mlist=new ArrayList<WMDeviceInfo>();
        WMClientSdk.getInstance().getDeviceList(mlist, true);
        ACache aCache=ACache.get(this);
        aCache.put("deviceinfo", (Serializable)mlist);
    }
    
    /**
     * 为ListView填充数据
     * 
     * @param date
     * @return
     */
    @SuppressLint("DefaultLocale")
    private List<SubFactory> filledData(List<SubFactory> mlist)
    {
        List<SubFactory> mSortList = new ArrayList<SubFactory>();
        for (int i = 0; i < mlist.size(); i++)
        {
            SubFactory sortModel = new SubFactory();
            sortModel = mlist.get(i);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(mlist.get(i).getSubfactoryname());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]"))
            {
                sortModel.setSortLetters(sortString.toUpperCase());
            }
            else
            {
                sortModel.setSortLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;
        
    }
    
    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * 
     * @param filterStr
     */
    private void filterData(String filterStr)
    {
        List<SubFactory> filterDateList = new ArrayList<SubFactory>();
        
        if (TextUtils.isEmpty(filterStr))
        {
            filterDateList = SourceDateList;
        }
        else
        {
            filterDateList.clear();
            for (SubFactory sortModel : SourceDateList)
            {
                String name = sortModel.getSubfactoryname();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString()))
                {
                    filterDateList.add(sortModel);
                }
            }
        }
        
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        if (adapter != null)
            adapter.updateListView(filterDateList);
    }
    
    @Override
    public void onClick(View arg0)
    {
        switch (arg0.getId())
        {
            case R.id.go_back:
                finish();
                break;
            default:
                break;
        }
    }
    
    @SuppressWarnings("static-access")
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        // 切换为竖屏
        if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_PORTRAIT)
        {
            
        }
        // 切换为横屏
        else if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_LANDSCAPE)
        {
            
        }
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
