package com.vomont.yundudao.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.SubFactory;

public class FactoryBeanUtil
{
    
    public static List<FactoryInfo> addFactoryInfo(Context context)
    {
        ACache aCache = ACache.get(context);
        FactoryBean factoryBean = (FactoryBean)aCache.getAsObject("factoryBean");
        if (factoryBean != null)
        {
            List<FactoryInfo> mlist = new ArrayList<FactoryInfo>();
            if (factoryBean.getFactorys() != null)
            {
                mlist.addAll(factoryBean.getFactorys());
                if (factoryBean.getSubfactorys() != null)
                {
                    for (int i = 0; i < mlist.size(); i++)
                    {
                        List<SubFactory> sub_list = new ArrayList<SubFactory>();
                        if (factoryBean.getSubfactorys() != null && factoryBean.getSubfactorys().size() != 0)
                        {
                            for (int j = 0; j < factoryBean.getSubfactorys().size(); j++)
                            {
                                if (mlist.get(i).getFactoryid() == factoryBean.getSubfactorys().get(j).getOwnerfactoryid())
                                {
                                    sub_list.add(factoryBean.getSubfactorys().get(j));
                                }
                            }
                            mlist.get(i).setMlist(sub_list);
                        }
                    }
                }
                if (factoryBean.getDevices() != null)
                {
                    for (int i = 0; i < mlist.size(); i++)
                    {
                        if (mlist.get(i).getMlist() != null && mlist.get(i).getMlist().size() != 0)
                        {
                            for (int j = 0; j < mlist.get(i).getMlist().size(); j++)
                            {
                                List<DeviceInfo> dev_list = new ArrayList<DeviceInfo>();
                                for (int k = 0; k < factoryBean.getDevices().size(); k++)
                                {
                                    if (mlist.get(i).getMlist().get(j).getSubfactoryid() == factoryBean.getDevices().get(k).getSubfactoryid())
                                    {
                                        dev_list.add(factoryBean.getDevices().get(k));
                                    }
                                }
                                mlist.get(i).getMlist().get(j).setMlist_device(dev_list);
                            }
                        }
                    }
                }
            }
            return mlist;
        }
        return new ArrayList<FactoryInfo>();
    }
}
