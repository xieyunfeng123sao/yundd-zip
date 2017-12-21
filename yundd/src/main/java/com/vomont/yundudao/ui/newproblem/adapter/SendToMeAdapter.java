package com.vomont.yundudao.ui.newproblem.adapter;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.NewProblemBean;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.ui.createproblem.LookDetailImageActivity;
import com.vomont.yundudao.ui.home.fragment.adapter.ActionAdapter;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.NoScrollGridView.NoScrollGridView;
import com.vomont.yundudao.view.NoScrollListView;

@SuppressLint("InflateParams")
public class SendToMeAdapter extends BaseAdapter
{
    private Context context;
    
    private List<NewProblemBean> mlist;
    
    private ShareUtil shareUtil;
    
    OnActionCallBack onActionCallBack;
    
    public SendToMeAdapter(Context context)
    {
        this.context = context;
    }
    
    public void setData(List<NewProblemBean> mlist)
    {
        this.mlist = mlist;
    }
    
    @Override
    public int getCount()
    {
        return null != mlist ? mlist.size() : 0;
    }
    
    @Override
    public Object getItem(int position)
    {
        return mlist.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @SuppressLint("SimpleDateFormat")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        Holder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_problem_sendtome, null);
            holder = new Holder();
            holder.problem_img_list = (NoScrollGridView)convertView.findViewById(R.id.problem_img_list);
            holder.newproblem_send_name = (TextView)convertView.findViewById(R.id.newproblem_send_name);
            holder.newproblem_now_statues = (TextView)convertView.findViewById(R.id.newproblem_now_statues);
            holder.newproblem_send_time = (TextView)convertView.findViewById(R.id.newproblem_send_time);
            holder.newproblem_desp = (TextView)convertView.findViewById(R.id.newproblem_desp);
            holder.newproblem_owner = (TextView)convertView.findViewById(R.id.newproblem_owner);
            holder.newproblem_donetime = (TextView)convertView.findViewById(R.id.newproblem_donetime);
            holder.newproblem_sub_info = (TextView)convertView.findViewById(R.id.newproblem_sub_info);
            holder.send_man_img = (ImageView)convertView.findViewById(R.id.send_man_img);
            holder.item_problem_action_list = (NoScrollListView)convertView.findViewById(R.id.item_problem_action_list);
            holder.problem_action_intent = (RelativeLayout)convertView.findViewById(R.id.problem_action_intent);
            holder.pinlun_img = (ImageView)convertView.findViewById(R.id.pinlun_img);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder)convertView.getTag();
        }
        // 创建人
        holder.newproblem_send_name.setText(mlist.get(position).getCreatorname());
        ProblemImgAdapter adapter = new ProblemImgAdapter(context);
        adapter.setData(mlist.get(position).getImages());
        holder.problem_img_list.setAdapter(adapter);
        // 创建时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String createTime = dateFormat.format(new Date(mlist.get(position).getCreatetime() * 1000));
        holder.newproblem_send_time.setText(createTime);
        // 评论
        String desp = mlist.get(position).getProblemdesp();
        if (desp != null)
            try
            {
                holder.newproblem_desp.setText(URLDecoder.decode(desp, "utf-8"));
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        // 责任人
        holder.newproblem_owner.setText(mlist.get(position).getOwnername());
        // 到期日期
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String doneTime = format.format(new Date(mlist.get(position).getDonetime() * 1000));
        holder.newproblem_donetime.setText(doneTime);
        // 所属设备和问题类型
        ACache aCache = ACache.get(context);
        FactoryBean factoryBean = (FactoryBean)aCache.getAsObject("factoryBean");
        String info = "";
        if (factoryBean != null)
        {
          
            
            int m=0;
            if (factoryBean.getSubfactorys() != null)
            {
                for (SubFactory subFactory : factoryBean.getSubfactorys())
                {
                    if (subFactory.getSubfactoryid() == mlist.get(position).getRelatedsubfactoryid())
                    {
                        info = info + subFactory.getSubfactoryname();
                        m=subFactory.getOwnerfactoryid();
                        break;
                    }
                }
            }
            
            if (factoryBean.getFactorys() != null)
            {
                for (FactoryInfo factoryInfo : factoryBean.getFactorys())
                {
                    if (factoryInfo.getFactoryid() == m)
                    {
                        info =factoryInfo.getFactoryname()+" / "+info;
                        break;
                    }
                }
            }
            
            if (factoryBean.getDevices() != null)
            {
                for (DeviceInfo deviceInfo : factoryBean.getDevices())
                {
                    if (deviceInfo.getDeviceid() == mlist.get(position).getRelateddeviceid())
                    {
                        info = info + " " + deviceInfo.getDevicename();
                        break;
                    }
                }
            }
        }
        info = info + "(" + mlist.get(position).getProblemtypename() + ")";
        holder.newproblem_sub_info.setText(info);
        // 问题状态
        switch (mlist.get(position).getProblemstatus())
        {
            case 0:
                holder.newproblem_now_statues.setText("");
                break;
            case 1:
                holder.newproblem_now_statues.setText("不合格");
                holder.newproblem_now_statues.setTextColor(context.getResources().getColor(R.color.actionsheet_red));
                break;
            case 2:
                holder.newproblem_now_statues.setText("已整改");
                holder.newproblem_now_statues.setTextColor(context.getResources().getColor(R.color.zhenggai));
                break;
            case 3:
                holder.newproblem_now_statues.setText("无需整改");
                holder.newproblem_now_statues.setTextColor(context.getResources().getColor(R.color.zhenggai));
                break;
            case 4:
                holder.newproblem_now_statues.setText("整改通过");
                holder.newproblem_now_statues.setTextColor(context.getResources().getColor(R.color.pinglun));
                break;
            case 5:
                holder.newproblem_now_statues.setText("整改未通过");
                holder.newproblem_now_statues.setTextColor(context.getResources().getColor(R.color.actionsheet_red));
                break;
            default:
                break;
        }
        
        // 评论
        ActionAdapter actionAdapter = new ActionAdapter(context);
        actionAdapter.setData(mlist.get(position).getActions());
        holder.item_problem_action_list.setAdapter(actionAdapter);
//        setListViewHeightBasedOnChildren(holder.item_problem_action_list);
        shareUtil = new ShareUtil(context);
        // 预览图片
        holder.problem_img_list.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int childPosition, long id)
            {
                Intent intent = new Intent(context, LookDetailImageActivity.class);
                intent.putExtra("position", childPosition);
                intent.putExtra("mlistPath", (Serializable)mlist.get(position).getImages());
                context.startActivity(intent);
            }
        });
        
        if (mlist.get(position).getProblemstatus() == 4)
        {
            holder.problem_action_intent.setEnabled(false);
            holder.pinlun_img.setImageResource(R.drawable.qcenter_cell_btnforbid);
        }
        else
        {
            holder.problem_action_intent.setEnabled(true);
            holder.pinlun_img.setImageResource(R.drawable.problem_call);
        }
        
        // 评论的状态
        holder.problem_action_intent.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int action_state = 0;
                if (shareUtil.getShare().getAccountid() == mlist.get(position).getCreatorid())
                {
                    // 创建人
                    if (mlist.get(position).getProblemstatus() == 2 || mlist.get(position).getProblemstatus() == 3)
                    {
                        // 复查 跳转到复查界面
                        action_state = 4;
                    }
                    else if (mlist.get(position).getProblemstatus() == 4)
                    {
                        // 整改通过 没有跳转的需求
                        action_state = 0;
                    }
                    else
                    {
                        // 整改状态 创建人只能评论
                        action_state = 2;
                    }
                }
                else if (shareUtil.getShare().getAccountid() == mlist.get(position).getOwnerid())
                {
                    // 处理人
                    if (mlist.get(position).getProblemstatus() == 1 || mlist.get(position).getProblemstatus() == 5)
                    {
                        // 整改 跳转到整改界面
                        action_state = 3;
                    }
                    else if (mlist.get(position).getProblemstatus() == 4)
                    {
                        // 整改通过 无需操作
                        action_state = 0;
                    }
                    else
                    {
                        // 带复查的阶段 可以评论
                        action_state = 2;
                    }
                }
                else
                {
                    // 抄送人
                    if (mlist.get(position).getProblemstatus() == 4)
                    {
                        // 整改完成 无需操作
                        action_state = 0;
                    }
                    else
                    {
                        // 抄送人只能评论
                        action_state = 2;
                    }
                }
                
                if (action_state != 0)
                {
                    if (onActionCallBack != null)
                        onActionCallBack.OnClick(position, action_state, mlist.get(position).getProblemid());
                }
            }
        });
        
        return convertView;
    }
    
    class Holder
    {
        NoScrollGridView problem_img_list;
        
        TextView newproblem_send_name;
        
        TextView newproblem_now_statues;
        
        TextView newproblem_send_time;
        
        TextView newproblem_desp;
        
        TextView newproblem_owner;
        
        TextView newproblem_donetime;
        
        TextView newproblem_sub_info;
        
        ImageView send_man_img;
        
        NoScrollListView item_problem_action_list;
        
        RelativeLayout problem_action_intent;
        
        ImageView pinlun_img;
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
    
    public void setOnActionListener(OnActionCallBack onActionCallBack)
    {
        this.onActionCallBack = onActionCallBack;
    }
    
    public interface OnActionCallBack
    {
        void OnClick(int position, int state, int problemid);
    }
}
