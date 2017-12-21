package com.vomont.yundudao.ui.createproblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DetalBean;
import com.vomont.yundudao.bean.DetalInfo;
import com.vomont.yundudao.mvpview.detal.IDetalView;
import com.vomont.yundudao.presenter.detal.DetalPresenter;
import com.vomont.yundudao.ui.createproblem.adapter.DetalAapter;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.listbar.CharacterParser;
import com.vomont.yundudao.view.listbar.ClearEditText;
import com.vomont.yundudao.view.listbar.PinyinDetalComparator;
import com.vomont.yundudao.view.listbar.SideBar;
import com.vomont.yundudao.view.listbar.SideBar.OnTouchingLetterChangedListener;

public class SelectDealActivity extends Activity implements OnClickListener, IDetalView
{
    
    private ClearEditText detalman_search;
    
    private TextView copyman_sure;
    
    private ListView detalman_list;
    
    private SideBar detalman_sidrbar;
    
    private DetalAapter adapter;
    
    private List<DetalInfo> mlist;
    
    private List<DetalInfo> filterDateList;
    
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinDetalComparator pinyinComparator;
    
    private DetalPresenter detalPresenter;
    
    private ShareUtil shareUtil;
    
    private ImageView copyman_back;
    
    private int selectedPosition = -1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dealman);
        initView();
        initData();
        initListener();
    }
    
    private void initView()
    {
        detalman_search = (ClearEditText)findViewById(R.id.detalman_search);
        detalman_list = (ListView)findViewById(R.id.detalman_list);
        detalman_sidrbar = (SideBar)findViewById(R.id.detalman_sidrbar);
        copyman_sure = (TextView)findViewById(R.id.copyman_sure);
        copyman_back = (ImageView)findViewById(R.id.copyman_back);
    }
    
    private void initData()
    {
        // copyman_sure.setVisibility(View.GONE);
        copyman_sure.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                if (selectedPosition != -1)
                {
                    Intent intent = getIntent();
                    if(filterDateList!=null&&filterDateList.size()!=0)
                    {
                        intent.putExtra("dealman", filterDateList.get(selectedPosition));
                    }
                    else
                    {
                        intent.putExtra("dealman", mlist.get(selectedPosition));
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                {
                    Toast.makeText(SelectDealActivity.this, "请选择处理人", Toast.LENGTH_LONG).show();
                }
            }
        });
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinDetalComparator();
        detalPresenter = new DetalPresenter(this);
        shareUtil = new ShareUtil(this);
        detalPresenter.getDetal(shareUtil.getShare().getUser_id() + "");
        mlist = new ArrayList<DetalInfo>();
        copyman_back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
    
    private void initListener()
    {
        detalman_sidrbar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener()
        {
            
            @Override
            public void onTouchingLetterChanged(String s)
            {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1)
                {
                    detalman_list.setSelection(position);
                }
            }
        });
        detalman_list.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                selectedPosition = position;
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
            }
        });
        
        // 根据输入框输入值的改变来过滤搜索
        detalman_search.addTextChangedListener(new TextWatcher()
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
    
    /**
     * 为ListView填充数据
     * 
     * @param date
     * @return
     */
    @SuppressLint("DefaultLocale")
    private List<DetalInfo> filledData(List<DetalInfo> mlist)
    {
        List<DetalInfo> mSortList = new ArrayList<DetalInfo>();
        for (int i = 0; i < mlist.size(); i++)
        {
            DetalInfo sortModel = new DetalInfo();
            sortModel = mlist.get(i);
            // 汉字转换成拼音
            String pinyin = "";
            if (TextUtils.isEmpty(mlist.get(i).getName()))
            {
                pinyin = mlist.get(i).getTel();
            }
            else
            {
                pinyin = characterParser.getSelling(mlist.get(i).getName());
            }
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
        Collections.sort(mSortList, pinyinComparator);
        return mSortList;
    }
    
    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * 
     * @param filterStr
     */
    private void filterData(String filterStr)
    {
       filterDateList = new ArrayList<DetalInfo>();
        
        if (TextUtils.isEmpty(filterStr))
        {
            filterDateList = mlist;
        }
        else
        {
            filterDateList.clear();
            for (DetalInfo sortModel : mlist)
            {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString()))
                {
                    filterDateList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }
    
    @Override
    public void onClick(View v)
    {
        
    }
    
    @Override
    public void getDetalman(DetalBean detalBean)
    {
        if (detalBean != null && detalBean.getAccounts() != null)
        {
            mlist.addAll(detalBean.getAccounts());
            for (int i = 0; i < mlist.size(); i++)
            {
                if (shareUtil.getShare().getAccountid() == mlist.get(i).getId())
                {
                    mlist.remove(i);
                }
            }
            mlist = filledData(mlist);
            adapter = new DetalAapter(this, mlist, 0);
            detalman_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
    
    @Override
    public void getFailed()
    {
        
    }
}
