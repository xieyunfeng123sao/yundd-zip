package com.vomont.yundudao.ui.factory.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vomont.yundudao.R;
import com.vomont.yundudao.ui.calendar.KCalendar;
import com.vomont.yundudao.ui.calendar.KCalendar.OnCalendarClickListener;
import com.vomont.yundudao.utils.PlayBackUtil;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.view.timebar.TimeBackBar;
import com.vomont.yundudao.view.timebar.TimeBackBar.PlayListener;
import com.wmclient.clientsdk.WMFileInfo;

@SuppressLint({ "InflateParams", "SimpleDateFormat", "HandlerLeak" })
public class PlaybackFragment extends Fragment {
	public static PlaybackFragment fragment;

	private TextView popupwindow_calendar_month;

	private KCalendar calendar;

	private String date = "";

	private int on_day;

	private PlayBackUtil playBackUtil;

	private List<WMFileInfo> mlFileInfos;

	private int deviceId, devChannelId;
	
	private RelativeLayout relativeLayout;
	
	private int type;

	long endTime = 0;

	long startTime = 0;

	private TimeBackBar myBar;


	private Dialog dialog;

	private Handler handler;

	public static PlaybackFragment getInstence() {
		if (null == fragment)
			fragment = new PlaybackFragment();
		return fragment;
	}

	@SuppressLint("NewApi")
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_playback, null);
		// view.getParent().requestDisallowInterceptTouchEvent(true);
		popupwindow_calendar_month = (TextView) view
				.findViewById(R.id.popupwindow_calendar_month);
		calendar = (KCalendar) view.findViewById(R.id.popupwindow_calendar);
		myBar = (TimeBackBar) view.findViewById(R.id.seerbar);
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
		date = sFormat.format(new Date());
		mlFileInfos = new ArrayList<WMFileInfo>();

		if (null != date) {
			int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
			int month = Integer.parseInt(date.substring(date.indexOf("-") + 1,
					date.lastIndexOf("-")));
			int day = Integer.parseInt(date.substring(
					date.lastIndexOf("-") + 1, date.length()));
			popupwindow_calendar_month.setText(years + "/" + month + "/" + day);
			calendar.showCalendar(years, month);
			calendar.setCalendarDayBgColor(date, R.drawable.new_dot_three);
			try {
				startTime = sFormat.parse(date + " 00:00:00").getTime();
				endTime = sFormat.parse(
						years + "-" + month + "-" + (day + 1)
								+ " 00:00:00").getTime();
			
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		// 监听所选中的日期
		calendar.setOnCalendarClickListener(new OnCalendarClickListener() {
			public void onCalendarClick(int row, int col, String dateFormat) {
				int year = Integer.parseInt(dateFormat.substring(0,
						dateFormat.indexOf("-")));
				int month = Integer.parseInt(dateFormat.substring(
						dateFormat.indexOf("-") + 1,
						dateFormat.lastIndexOf("-")));

				if (calendar.getCalendarMonth() - month == 1// 跨年跳转
						|| calendar.getCalendarMonth() - month == -11) {
					calendar.lastMonth();
				} else if (month - calendar.getCalendarMonth() == 1 // 跨年跳转
						|| month - calendar.getCalendarMonth() == -11) {
					calendar.nextMonth();

				} else {
					calendar.removeAllBgColor();
					calendar.setCalendarDayBgColor(dateFormat,
							R.drawable.new_dot_three);
					date = dateFormat;// 最后返回给全局 date
					on_day = Integer.parseInt(dateFormat.substring(
							dateFormat.lastIndexOf("-") + 1,
							dateFormat.length()));
					popupwindow_calendar_month.setText(year + "/" + month + "/"
							+ on_day);
					try {
						SimpleDateFormat sFormat = new SimpleDateFormat(
								"yyyy-MM-dd");
						startTime = sFormat.parse(date + " 00:00:00").getTime();
						endTime = sFormat.parse(
								year + "-" + month + "-" + (on_day + 1)
										+ " 00:00:00").getTime();
						playback();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// // 监听当前月份
		// calendar.setOnCalendarDateChangedListener(new
		// OnCalendarDateChangedListener()
		// {
		// public void onCalendarDateChanged(int year, int month)
		// {
		// }
		// });

		// 上月监听按钮
		RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) view
				.findViewById(R.id.popupwindow_calendar_last_month);
		popupwindow_calendar_last_month
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						calendar.lastMonth();
					}
				});

		// 下月监听按钮
		RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) view
				.findViewById(R.id.popupwindow_calendar_next_month);
		popupwindow_calendar_next_month
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						calendar.nextMonth();
					}
				});

		return view;
	}

	public void playback() {
		dialog = ProgressDialog.createLoadingDialog(getActivity(), "");
		dialog.show();
		new Thread(new Runnable() {
			public void run() {
				playBackUtil.searchFrontEndFileList(mlFileInfos, deviceId,
						devChannelId, startTime, endTime);
				Message message = new Message();

				message.what = 100;
				message.obj = mlFileInfos;
				handler.sendMessage(message);
			}
		}).start();
		myBar.setPlayListener(new PlayListener() {
			@Override
			public void onSucess() {
				dialog.dismiss();
			}
			@Override
			public void onStart() {
				dialog = ProgressDialog.createLoadingDialog(getActivity(), "加载中");
				dialog.show();
			}
			
			@Override
			public void onFail() {
				dialog.dismiss();
				Toast.makeText(getActivity(), "播放失败!", Toast.LENGTH_LONG).show();
			}
		});
		handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 100:
					mlFileInfos=(List<WMFileInfo>)msg.obj;
					if (mlFileInfos != null&&mlFileInfos.size()!=0) {
						 myBar.postDelayed(new Runnable() {
						 public void run() {
						 myBar.addTimeLine(mlFileInfos, endTime, startTime);
						 myBar.startPlay(playBackUtil, relativeLayout,type,deviceId,devChannelId);
						 }
						 }, 500);
					}
					dialog.dismiss();
					break;

				default:
					break;
				}
			}
		};

	}

	public void getPlay(RelativeLayout relativeLayout,int type,PlayBackUtil playBackUtil, int deviceId,
			int devChannelId) {
		this.playBackUtil = playBackUtil;
		this.deviceId = deviceId;
		this.devChannelId = devChannelId;
		this.relativeLayout=relativeLayout;
		this.type=type;
		 playback();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();

	}
}
