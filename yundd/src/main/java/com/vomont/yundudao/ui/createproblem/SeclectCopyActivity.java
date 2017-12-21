package com.vomont.yundudao.ui.createproblem;

import java.io.Serializable;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DetalBean;
import com.vomont.yundudao.bean.DetalInfo;
import com.vomont.yundudao.mvpview.detal.IDetalView;
import com.vomont.yundudao.presenter.detal.DetalPresenter;
import com.vomont.yundudao.ui.createproblem.adapter.CopyAapter;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.listbar.CharacterParser;
import com.vomont.yundudao.view.listbar.ClearEditText;
import com.vomont.yundudao.view.listbar.PinyinDetalComparator;
import com.vomont.yundudao.view.listbar.SideBar;
import com.vomont.yundudao.view.listbar.SideBar.OnTouchingLetterChangedListener;

public class SeclectCopyActivity extends Activity implements OnClickListener, IDetalView
{
    private ClearEditText detalman_search;
    
    private ListView detalman_list;
    
    private TextView copyman_name;
    
    private ImageView copyman_back;
    
    private TextView copyman_sure;
    
    private SideBar detalman_sidrbar;
    
    private CopyAapter adapter;
    
    private List<DetalInfo> mlist;
    
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
    
    private List<Integer> list_position;
    
    private List<DetalInfo> filterDateList;
    
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
        copyman_name = (TextView)findViewById(R.id.copyman_name);
        copyman_back = (ImageView)findViewById(R.id.copyman_back);
        copyman_sure = (TextView)findViewById(R.id.copyman_sure);
    }
    
    private void initData()
    {
        copyman_name.setText("选择抄送人");
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinDetalComparator();
        detalPresenter = new DetalPresenter(this);
        shareUtil = new ShareUtil(this);
        detalPresenter.getDetal(shareUtil.getShare().getUser_id() + "");
        mlist = new ArrayList<DetalInfo>();
        list_position = new ArrayList<Integer>();
    }
    
    private void initListener()
    {
        copyman_back.setOnClickListener(this);
        copyman_sure.setOnClickListener(this);
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
        
        // 根据输入框输入值的改变来过滤搜索
        detalman_search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                list_position.clear();
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
        switch (v.getId())
        {
            case R.id.copyman_back:
                finish();
                break;
            case R.id.copyman_sure:
                List<DetalInfo> back_mlist = new ArrayList<DetalInfo>();
                if(filterDateList!=null&&filterDateList.size()!=0)
                {
                    sendBackList(back_mlist, filterDateList);
                }
                else
                {
                    sendBackList(back_mlist, mlist);
                }
                break;
            default:
                break;
        }
    }
    
    private void sendBackList(List<DetalInfo> back_mlist, List<DetalInfo> mList)
    {
        Intent intent = getIntent();
        if (mList != null && mList.size() != 0 && list_position != null && list_position.size() != 0)
        {
            for (int i = 0; i < mList.size(); i++)
            {
                for (int j = 0; j < list_position.size(); j++)
                {
                    if (i == list_position.get(j))
                    {
                        back_mlist.add(mList.get(i));
                    }
                }
            }
            intent.putExtra("copyman", (Serializable)back_mlist);
            setResult(RESULT_OK, intent);
            finish();
        }
        else
        {
            Toast.makeText(SeclectCopyActivity.this, "请选择抄送人!", Toast.LENGTH_SHORT).show();
        }
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
            adapter = new CopyAapter(this, mlist, 1);
            adapter.setChextBoxIsChange(new com.vomont.yundudao.ui.createproblem.adapter.CopyAapter.OnItemCheckChangeLinstener()
            {
                @Override
                public void noChecked(int position)
                {
                    if (list_position.contains(position))
                    {
                        for (int i = 0; i < list_position.size(); i++)
                        {
                            if (list_position.get(i) == position)
                            {
                                list_position.remove(i);
                            }
                        }
                    }
                }
                
                @Override
                public void hasChcked(int position)
                {
                    list_position.add(position);
                }
            });
            detalman_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
    
    @Override
    public void getFailed()
    {
        
    }
    
}
