package com.vomont.yundudao.ui.patrol;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.vomont.fileloadsdk.WMFileLoadSdk;
import com.vomont.yundudao.R;
import com.vomont.yundudao.ui.patrol.adapter.LoadingAdapter;
import com.vomont.yundudao.ui.patrol.adapter.LoadingAdapter.CheckListener;
import com.vomont.yundudao.upload.UpLoadReceiver;
import com.vomont.yundudao.upload.UpLoadReceiver.OnLoadListener;
import com.vomont.yundudao.upload.VideoBean;
import com.vomont.yundudao.upload.VideoHelpter;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.ios.AlertDialog;

@SuppressLint("HandlerLeak")
public class LoadActivity extends Activity implements OnClickListener {
    private ImageView load_go_back;

    private ImageView load_delete;

    private ListView video_load_list;

    private VideoHelpter videoHelpter;

    private List<VideoBean> mlist;

    // private VideoUpService videoUpService;

    private LoadingAdapter adapter;

    private boolean isDelete;

    private List<DVPostion> delete_position;

    private int removeI;

    private UpLoadReceiver mReceiver;

    private IntentFilter mIntentFilter;

    private ShareUtil shareUtil;

    private ZipService zipService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        shareUtil = new ShareUtil(this);
        Intent intent = new Intent(this, ZipService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        initView();
        initListener();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.vomont.load.info");
        mReceiver = new UpLoadReceiver();
        registerReceiver(mReceiver, mIntentFilter);
        mReceiver.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onSuccess(int fileId, String url) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pullView();
                    }
                });
            }

            @Override
            public void onProgress(int fileId, int progress) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pullView();
                    }
                });
            }

            @Override
            public void onFail(int type, int fileId, int errorId) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pullView();
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private void pullView() {
        mlist.clear();
        List<VideoBean> list = videoHelpter.selectNoPack();
        if (list != null) {
            List<VideoBean> beans = new ArrayList<VideoBean>();
            for (VideoBean videoBean : list) {
                if (shareUtil.getShare().getAccountid() == videoBean.getCreateId()) {
                    if ((videoBean.getIsSave() == 1) && (videoBean.getLoadstate() != 2)) {
                        beans.add(videoBean);
                    }
                }
            }
            mlist.addAll(beans);
        }
        adapter.notifyDataSetChanged();
    }

    private boolean contains(DVPostion dvPostion) {
        boolean result = false;
        if (delete_position != null && delete_position.size() != 0) {
            for (int i = 0; i < delete_position.size(); i++) {
                if (delete_position.get(i).position == dvPostion.position) {
                    result = true;
                    removeI = i;
                }
            }
        }
        return result;
    }

    private void initData() {
        videoHelpter = new VideoHelpter(this);
        delete_position = new ArrayList<DVPostion>();
        mlist = new ArrayList<VideoBean>();
        List<VideoBean> list = videoHelpter.selectNoPack();
        if (list != null) {
            List<VideoBean> beans = new ArrayList<VideoBean>();
            for (VideoBean videoBean : list) {
                if ((videoBean.getIsSave() == 1) && (videoBean.getLoadstate() != 2)) {
                    beans.add(videoBean);
                }
            }
            mlist.addAll(beans);
        }
        adapter = new LoadingAdapter(this, mlist);
        adapter.setCheckBoxListener(new CheckListener() {
            @Override
            public void deletePosition(int i) {
                DVPostion dvPostion = new DVPostion();
                dvPostion.position = i;
                if (contains(dvPostion)) {
                    delete_position.remove(removeI);
                }
            }
            @Override
            public void addPosition(int i) {
                DVPostion dvPostion = new DVPostion();
                dvPostion.position = i;
                if (!contains(dvPostion)) {
                    delete_position.add(dvPostion);
                }
            }
        });
        video_load_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        video_load_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (!isDelete) {
                    Log.e("insert","=========onItemClick========"+mlist.get(position).getLoadstate());
                    if (mlist.get(position).getLoadstate() != 1&&mlist.get(position).getLoadstate() != 3) {
                        Log.e("insert","=========setOnItemClickListener========"+mlist.get(position).getLoadstate());
                        mlist.get(position).setLoadstate(1);
                        if (mlist.get(position).getFileId() == 0) {
                            if (mlist.get(position).getVideoPath().equals(VideoManager.path)||mlist.get(position).getVideoPath().contains(VideoManager.compress)) {
                                //自己本地录制的视频 或者已经压缩的视频
                                int fileId = WMFileLoadSdk.getInstance().WM_VFile_UploadBigFile(mlist.get(position).getVideoPath() + "/" + mlist.get(position).getName() + ".mp4");
                                mlist.get(position).setFileId(fileId);
                                mlist.get(position).setLoadstate(1);
                                videoHelpter.updateBean(mlist.get(position).getName(), mlist.get(position));
                            } else {
                                //其他源视频 需要先压缩 在上传
                                zipService.zipVideo(mlist.get(position).getVideoPath(), mlist.get(position).getName(), mlist.get(position));
                            }
                        } else {
                            WMFileLoadSdk.getInstance().WM_VFile_PauseUpload(mlist.get(position).getFileId(), true);
                            WMFileLoadSdk.getInstance().WM_VFile_PauseUpload(mlist.get(position).getFileId(), false);
                            videoHelpter.updateLoadState(mlist.get(position).getName(), 1, 0);
                        }
                    } else if(mlist.get(position).getLoadstate() != 3) {
                        mlist.get(position).setLoadstate(0);
                        WMFileLoadSdk.getInstance().WM_VFile_PauseUpload(mlist.get(position).getFileId(), true);
                        videoHelpter.updateLoadState(mlist.get(position).getName(), 0, 0);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initListener() {
        load_go_back.setOnClickListener(this);
        load_delete.setOnClickListener(this);
    }

    private void initView() {
        load_go_back = (ImageView) findViewById(R.id.load_go_back);
        load_delete = (ImageView) findViewById(R.id.load_delete);
        video_load_list = (ListView) findViewById(R.id.video_load_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load_go_back:
                finish();
                break;
            case R.id.load_delete:
                if (!isDelete) {
                    load_delete.setImageResource(R.drawable.spotsupervision_delete);
                    if (mlist != null) {
                        for (VideoBean bean : mlist) {
                            bean.setDelete(1);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    if (mlist != null && mlist.size() != 0) {
                        for (int i = 0; i < mlist.size(); i++) {
                            WMFileLoadSdk.getInstance().WM_VFile_PauseUpload(mlist.get(i).getFileId(), true);
                        }
                    }
                    if (delete_position != null && delete_position.size() != 0) {
                        new AlertDialog(LoadActivity.this).builder().setMsg("确定删除视频").setNegativeButton("确定", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                List<VideoBean> deleteList = new ArrayList<VideoBean>();
                                for (int i = 0; i < delete_position.size(); i++) {
                                    deleteList.add(mlist.get(delete_position.get(i).position));
                                    File file = new File(mlist.get(delete_position.get(i).position).getVideoPath() + "/" + mlist.get(delete_position.get(i).position).getName() + ".mp4");
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                    WMFileLoadSdk.getInstance().WM_VFile_PauseUpload(mlist.get(delete_position.get(i).position).getFileId(), true);
                                    WMFileLoadSdk.getInstance().WM_VFile_StopUpload(mlist.get(delete_position.get(i).position).getFileId());
                                    videoHelpter.deleteVideo(mlist.get(delete_position.get(i).position).getName());
                                }
                                mlist.removeAll(deleteList);

                                if (mlist != null) {
                                    for (VideoBean bean : mlist) {
                                        bean.setDelete(0);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                adapter.notifyDataSetChanged();
                                delete_position.removeAll(delete_position);
                                load_delete.setImageResource(R.drawable.spotsupervision_picmanag);
                            }
                        }).setPositiveButton("取消", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isDelete = false;
                                if (mlist != null) {
                                    for (VideoBean bean : mlist) {
                                        bean.setDelete(0);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                load_delete.setImageResource(R.drawable.spotsupervision_picmanag);
                            }
                        }).show();
                    } else {
                        load_delete.setImageResource(R.drawable.spotsupervision_picmanag);
                        if (mlist != null) {
                            for (VideoBean bean : mlist) {
                                bean.setDelete(0);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                isDelete = !isDelete;
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    class DVPostion {
        int position;
    }

    private ServiceConnection conn = new ServiceConnection() {
        /** 获取服务对象时的操作 */
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 返回一个MsgService对象
            zipService = ((ZipService.ZipBinder) service).getService();
            zipService.setOnZipListener(new ZipService.OnZipListener() {
                @Override
                public void onCallBack(String oldName, VideoBean videoBean) {
                    if(mlist!=null&&mlist.size()!=0)
                    {
                        for(int i=0;i<mlist.size();i++ )
                        {
                            if(mlist.get(i).getName().equals(oldName))
                            {
                                mlist.set(i,videoBean);
                            }
                        }
                    }

                }

                @Override
                public void onSucess(String oldName, VideoBean videoBean) {
                    if(mlist!=null&&mlist.size()!=0)
                    {
                        for(int i=0;i<mlist.size();i++ )
                        {
                            if(mlist.get(i).getName().equals(oldName))
                            {
                                mlist.set(i,videoBean);
                            }
                        }
                    }
                }
            });
        }
        /** 无法获取到服务对象时的操作 */
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
