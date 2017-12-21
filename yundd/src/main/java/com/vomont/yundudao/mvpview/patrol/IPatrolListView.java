package com.vomont.yundudao.mvpview.patrol;

import java.util.List;

import com.vomont.yundudao.bean.PatrolDetailItem;
import com.vomont.yundudao.bean.PatrolListItem;

public interface IPatrolListView
{
    
    void getListData(List<PatrolListItem> mlist);
    
    void onFail();
    
    void getSucess(PatrolDetailItem item);
    
    void getFail();
    
}
