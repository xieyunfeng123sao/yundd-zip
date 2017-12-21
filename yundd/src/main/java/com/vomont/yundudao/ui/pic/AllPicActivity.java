package com.vomont.yundudao.ui.pic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.PicTimeBean;
import com.vomont.yundudao.bean.PicTimeInfo;
import com.vomont.yundudao.ui.pic.adapter.LineListAdapter;
import com.vomont.yundudao.ui.pic.adapter.LineListAdapter.OnCenterPicListener;
import com.vomont.yundudao.utils.GlideCacheUtil;
import com.vomont.yundudao.utils.Playutil;
import com.vomont.yundudao.view.BaseActivity;
import com.vomont.yundudao.view.expandView.ViewBottom;
import com.vomont.yundudao.view.expandView.ViewBottom.OnItemClicListener;

public class AllPicActivity extends BaseActivity implements OnClickListener
{
    
    private ImageView go_back;
    
    private TextView top_name;
    
    private RelativeLayout select_pic;
    
    private List<FactoryInfo> mlist;
    
    private ListView pic_time_line;
    
    private LineListAdapter adapter;
    
    private Intent intent;
    
    private PopupWindow mPopupWindow;
    
    private ViewBottom viewBottom;
    
    private String moth, time, year;
    
    private Playutil playutil;
    
    private List<String> list;
    
    private List<PicTimeBean> pic_list;
    
    private List<PicTimeBean> show_list;
    
    private String from;
    
    private TextView show_pic_fatory;
    
    private ImageView show_fatory_view;
    
    private ImageView empty_all_pic;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_pic);
        initView();
        initData();
        initImage();
        initListener();
    }
    
    private void initImage()
    {
        playutil = new Playutil(this);
        list = playutil.getImageName();
        show_list = new ArrayList<PicTimeBean>();
        pic_list = new ArrayList<PicTimeBean>();
        List<String> hasmoth = new ArrayList<String>();
        if (list != null)
        {
            for (int i = 0; i < list.size(); i++)
            {
                PicTimeBean picTimeBean = new PicTimeBean();
                year = list.get(i).substring(0, 4);
                moth = list.get(i).substring(4, 8);
                time = list.get(i).substring(8, 12);
                picTimeBean.setYear(year);
                picTimeBean.setTime(time);
                picTimeBean.setDate(moth);
                if (!hasmoth.contains(moth))
                {
                    pic_list.add(picTimeBean);
                    hasmoth.add(moth);
                }
            }
            for (int i = 0; i < pic_list.size(); i++)
            {
                List<PicTimeInfo> mlsit = new ArrayList<PicTimeInfo>();
                for (int j = 0; j < list.size(); j++)
                {
                    moth = list.get(j).substring(4, 8);
                    year = list.get(j).substring(0, 4);
                    if (pic_list.get(i).getDate().equals(moth))
                    {
                        String newname = list.get(j).substring(0, list.get(j).length() - 4);
                        String string = new String(newname);
                        String[] info = string.split("-");
                        if (info.length == 4)
                        {
                            PicTimeInfo picTimeInfo = new PicTimeInfo();
                            picTimeInfo.setDeviceid(Integer.parseInt(info[3]));
                            picTimeInfo.setSubfactoryid(Integer.parseInt(info[2]));
                            picTimeInfo.setFactoryid(Integer.parseInt(info[1]));
                            picTimeInfo.setPath(playutil.getPath());
                            picTimeInfo.setTime(info[0]);
                            picTimeInfo.setName(list.get(j));
                            mlsit.add(picTimeInfo);
                        }
                        
                    }
                }
                pic_list.get(i).setPaths(mlsit);
            }
        }
        if (pic_list != null && pic_list.size() != 0)
        {
            for (int i = 0; i < pic_list.size(); i++)
            {
                if ((pic_list.get(i).getPaths() == null) || (pic_list.get(i).getPaths().size() == 0))
                {
                    pic_list.remove(i);
                }
            }
        }
        adapterControl(pic_list);
        if (pic_list == null || pic_list.size() == 0)
        {
            empty_all_pic.setVisibility(View.VISIBLE);
        }
        else
        {
            empty_all_pic.setVisibility(View.GONE);
        }
        
    }
    
    public PicTimeInfo getPicInfo(int i, PicTimeBean picTimeBean)
    {
        PicTimeInfo picTimeInfo = new PicTimeInfo();
        picTimeInfo.setDealId(picTimeBean.getPaths().get(i).getDealId());
        picTimeInfo.setDeviceid(picTimeBean.getPaths().get(i).getDeviceid());
        picTimeInfo.setFactoryid(picTimeBean.getPaths().get(i).getFactoryid());
        picTimeInfo.setName(picTimeBean.getPaths().get(i).getName());
        picTimeInfo.setPath(picTimeBean.getPaths().get(i).getPath());
        picTimeInfo.setSubfactoryid(picTimeBean.getPaths().get(i).getFactoryid());
        picTimeInfo.setTime(picTimeBean.getPaths().get(i).getTime());
        return picTimeInfo;
    }
    
    @Override
    public void onWindowAttributesChanged(android.view.WindowManager.LayoutParams params)
    {
        super.onWindowAttributesChanged(params);
    }
    
    private void initView()
    {
        show_pic_fatory = (TextView)findViewById(R.id.show_pic_fatory);
        pic_time_line = (ListView)findViewById(R.id.pic_time_line);
        go_back = (ImageView)findViewById(R.id.go_back);
        top_name = (TextView)findViewById(R.id.top_name);
        select_pic = (RelativeLayout)findViewById(R.id.select_pic);
        show_fatory_view = (ImageView)findViewById(R.id.show_fatory_view);
        empty_all_pic = (ImageView)findViewById(R.id.empty_all_pic);
    }
    
    @SuppressWarnings("unchecked")
    private void initData()
    {
        top_name.setText("图片中心");
        intent = getIntent();
        from = intent.getStringExtra("from");
        
        mlist = (List<FactoryInfo>)intent.getSerializableExtra("factoryBean");
        viewBottom = new ViewBottom(this, mlist);
        
        mPopupWindow = new PopupWindow(viewBottom, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionsheet_top_normal));// 设置背景
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setOnDismissListener(new OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                show_fatory_view.setPivotX(show_fatory_view.getWidth() / 2);
                show_fatory_view.setPivotY(show_fatory_view.getHeight() / 2);// 支点在图片中心
                show_fatory_view.setRotation(0);
            }
        });
        viewBottom.setOnitemClickListener(new OnItemClicListener()
        {
            @Override
            public void getItem(int onePostion, int twoPostion, int threePostion)
            {
                show_list.clear();
                if (onePostion == 0)
                {
                    // 所有厂区的图片
                    show_list.addAll(pic_list);
                    show_pic_fatory.setText(mlist.get(onePostion).getFactoryname());
                }
                else
                {
                    // 指定厂区
                    if (twoPostion == 0)
                    {
                        show_pic_fatory.setText(mlist.get(onePostion).getFactoryname());
                        // 指定厂 的所有的图片
                        if (pic_list != null && pic_list.size() != 0)
                        {
                            for (int i = 0; i < pic_list.size(); i++)
                            {
                                PicTimeBean bean = new PicTimeBean();
                                bean.setYear(pic_list.get(i).getYear());
                                bean.setDate(pic_list.get(i).getDate());
                                if (pic_list.get(i).getPaths() != null && pic_list.get(i).getPaths().size() != 0)
                                {
                                    List<PicTimeInfo> pic_info = new ArrayList<PicTimeInfo>();
                                    for (int j = 0; j < pic_list.get(i).getPaths().size(); j++)
                                    {
                                        if (mlist.get(onePostion).getFactoryid() == pic_list.get(i).getPaths().get(j).getFactoryid())
                                        {
                                            pic_info.add(pic_list.get(i).getPaths().get(j));
                                        }
                                    }
                                    bean.setPaths(pic_info);
                                }
                                show_list.add(bean);
                            }
                        }
                    }
                    else
                    {
                        show_pic_fatory.setText(mlist.get(onePostion).getFactoryname() + "/" + mlist.get(onePostion).getMlist().get(twoPostion - 1).getSubfactoryname());
                        if (threePostion == 0)
                        {
                            // 指定厂 指定厂区的所有设备的图片
                            if (pic_list != null && pic_list.size() != 0)
                            {
                                for (int i = 0; i < pic_list.size(); i++)
                                {
                                    PicTimeBean bean = new PicTimeBean();
                                    bean.setYear(pic_list.get(i).getYear());
                                    bean.setDate(pic_list.get(i).getDate());
                                    if (pic_list.get(i).getPaths() != null && pic_list.get(i).getPaths().size() != 0)
                                    {
                                        List<PicTimeInfo> pic_info = new ArrayList<PicTimeInfo>();
                                        for (int j = 0; j < pic_list.get(i).getPaths().size(); j++)
                                        {
                                            if (mlist.get(onePostion).getFactoryid() == pic_list.get(i).getPaths().get(j).getFactoryid())
                                            {
                                                if (mlist.get(onePostion).getMlist() != null && mlist.get(onePostion).getMlist().size() != 0)
                                                {
                                                    if (mlist.get(onePostion).getMlist().get(twoPostion - 1).getSubfactoryid() == pic_list.get(i).getPaths().get(j).getSubfactoryid())
                                                    {
                                                        pic_info.add(pic_list.get(i).getPaths().get(j));
                                                    }
                                                }
                                            }
                                        }
                                        bean.setPaths(pic_info);
                                    }
                                    show_list.add(bean);
                                }
                                
                            }
                        }
                        else
                        {
                            show_pic_fatory.setText(mlist.get(onePostion).getFactoryname() + "/" + mlist.get(onePostion).getMlist().get(twoPostion - 1).getSubfactoryname() + "/"
                                + mlist.get(onePostion).getMlist().get(twoPostion - 1).getMlist_device().get(threePostion - 1).getDevicename());
                            // 指定厂 指定厂区 指定设备的所有设备的图片
                            if (pic_list != null && pic_list.size() != 0)
                            {
                                for (int i = 0; i < pic_list.size(); i++)
                                {
                                    PicTimeBean bean = new PicTimeBean();
                                    bean.setYear(pic_list.get(i).getYear());
                                    bean.setDate(pic_list.get(i).getDate());
                                    if (pic_list.get(i).getPaths() != null && pic_list.get(i).getPaths().size() != 0)
                                    {
                                        List<PicTimeInfo> pic_info = new ArrayList<PicTimeInfo>();
                                        for (int j = 0; j < pic_list.get(i).getPaths().size(); j++)
                                        {
                                            if (mlist.get(onePostion).getFactoryid() == pic_list.get(i).getPaths().get(j).getFactoryid())
                                            {
                                                if (mlist.get(onePostion).getMlist() != null && mlist.get(onePostion).getMlist().size() != 0)
                                                {
                                                    if (mlist.get(onePostion).getMlist().get(twoPostion - 1).getSubfactoryid() == pic_list.get(i).getPaths().get(j).getSubfactoryid())
                                                    {
                                                        if (mlist.get(onePostion).getMlist().get(twoPostion - 1).getMlist_device() != null
                                                            && mlist.get(onePostion).getMlist().get(twoPostion - 1).getMlist_device().size() != 0)
                                                        {
                                                            if (mlist.get(onePostion).getMlist().get(twoPostion - 1).getMlist_device().get(threePostion - 1).getDeviceid() == pic_list.get(i)
                                                                .getPaths()
                                                                .get(j)
                                                                .getDeviceid())
                                                            {
                                                                pic_info.add(pic_list.get(i).getPaths().get(j));
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        bean.setPaths(pic_info);
                                    }
                                    show_list.add(bean);
                                }
                            }
                        }
                    }
                }
                
                if (show_list != null && show_list.size() != 0)
                {
                    for (int i = 0; i < show_list.size(); i++)
                    {
                        if ((show_list.get(i).getPaths() == null) || (show_list.get(i).getPaths().size() == 0))
                        {
                            show_list.remove(i);
                            i--;
                        }
                    }
                }
                adapterControl(show_list);
                // adapter = new LineListAdapter(AllPicActivity.this, show_list, from, intent);
                // pic_time_line.setAdapter(adapter);
                // adapter.notifyDataSetChanged();
                
            }
            
            @Override
            public void lastItem()
            {
                mPopupWindow.dismiss();
                show_fatory_view.setPivotX(show_fatory_view.getWidth() / 2);
                show_fatory_view.setPivotY(show_fatory_view.getHeight() / 2);// 支点在图片中心
                show_fatory_view.setRotation(0);
            }
        });
    }
    
    public void adapterControl(List<PicTimeBean> list)
    {
        // 过滤删选 重新排列 不用GridView的原因是 如果使用GridView 由于嵌套在listView中 所以需要重新定义GridView高度
        // 导致GridView中的元素 是一下子全部加载的 而不是通过Holder复用 导致占用内存太高 在后面编辑图片时 会出现内存溢出
        // 所以改成listview通过复用的关系 可以更好的管理内存
        final List<PicTimeBean> show_pic = new ArrayList<PicTimeBean>();
        for (PicTimeBean picTimeBean : list)
        {
            int h = 0;
            if (picTimeBean.getPaths().size() > 3)
            {
                int size = picTimeBean.getPaths().size() % 3 == 0 ? picTimeBean.getPaths().size() / 3 : (picTimeBean.getPaths().size() / 3 + 1);
                for (int i = 0; i < size; i++)
                {
                    PicTimeBean picTimeBean2 = new PicTimeBean();
                    picTimeBean2.setDate(picTimeBean.getDate());
                    picTimeBean2.setTime(picTimeBean.getTime());
                    picTimeBean2.setYear(picTimeBean.getYear());
                    if (h == 0)
                    {
                        picTimeBean2.setFirst(true);
                    }
                    else
                    {
                        picTimeBean2.setFirst(false);
                    }
                    h++;
                    List<PicTimeInfo> mlist_info = new ArrayList<PicTimeInfo>();
                    if (i * 3 < picTimeBean.getPaths().size())
                    {
                        PicTimeInfo picTimeInfo = getPicInfo(i * 3, picTimeBean);
                        mlist_info.add(picTimeInfo);
                    }
                    if ((i * 3 + 1) < picTimeBean.getPaths().size())
                    {
                        PicTimeInfo picTimeInfo = getPicInfo(i * 3 + 1, picTimeBean);
                        mlist_info.add(picTimeInfo);
                    }
                    if ((i * 3 + 2) < picTimeBean.getPaths().size())
                    {
                        PicTimeInfo picTimeInfo = getPicInfo(i * 3 + 2, picTimeBean);
                        mlist_info.add(picTimeInfo);
                    }
                    picTimeBean2.setPaths(mlist_info);
                    show_pic.add(picTimeBean2);
                }
                
            }
            else
            {
                picTimeBean.setFirst(true);
                show_pic.add(picTimeBean);
            }
        }
        adapter = new LineListAdapter(this, show_pic, from, intent);
        pic_time_line.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setImageClickListener(new OnCenterPicListener()
        {
            @Override
            public void onClick(int position, int childPosition, String name)
            {
                for (PicTimeBean picTimeBean : pic_list)
                {
                    
                    for (int i = 0; i < picTimeBean.getPaths().size(); i++)
                    {
                        if (name.equals(picTimeBean.getPaths().get(i).getName()))
                        {
                            Intent intent = new Intent(AllPicActivity.this, PicCenterActivity.class);
                            intent.putExtra("picinfo", (Serializable)picTimeBean);
                            intent.putExtra("postion", i);
                            startActivity(intent);
                            break;
                        }
                    }
                }
            }
        });
    }
    
    private void initListener()
    {
        go_back.setOnClickListener(this);
        select_pic.setOnClickListener(this);
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        GlideCacheUtil.getInstance().clearImageAllCache(this);
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        GlideCacheUtil.getInstance().clearImageAllCache(this);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.go_back:
                finish();
                break;
            case R.id.select_pic:
                mPopupWindow.showAsDropDown(v);
                show_fatory_view.setPivotX(show_fatory_view.getWidth() / 2);
                show_fatory_view.setPivotY(show_fatory_view.getHeight() / 2);// 支点在图片中心
                show_fatory_view.setRotation(180);
                break;
            default:
                break;
        }
    }
    
    public void animotion()
    {
        
    }
}
