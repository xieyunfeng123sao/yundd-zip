package com.vomont.yundudao.view.timebar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.vomont.yundudao.R;
import com.vomont.yundudao.utils.PlayBackUtil;
import com.wmclient.clientsdk.StreamPlayer;
import com.wmclient.clientsdk.WMFileInfo;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ClickableViewAccessibility")
public class TimeBackBar extends RelativeLayout {
	// 刻度线
	private LinearLayout line;

	// 时间轴
	private RelativeLayout relayout;

	private TextView scroll_time;

	// 滑动的按钮
	private ImageView huakuai;

	private int _xDelta;

	// 时间轴的宽度
	private int re_x;

	// 视频播放时间段的时间轴的集合
	private List<Timelay> mlist_layout;

	// 已播放的时间段的时间轴的集合
	private List<LinearLayout> mlist_time_line;

	// 位播放的时间段的时间轴的集合
	private List<LinearLayout> no_mlist_time_line;

	// 定时器
	private Timer timer;

	private LinearLayout time_text;

	private Handler handler;

	// 一天的毫秒值
	private long item_time = 24 * 60 * 60 * 1000;

	// 每像素的时间
	private long ps;

	private int old_left = -1;

	// 计时
	private int time_n = 0;

	private int where_i;

	private int now_location;

	private List<WMFileInfo> mlist;

	private PlayBackUtil playBackUtil;

	private StreamPlayer streamPlayer;

	private boolean hasPlay;

	private boolean hasSurplusStart, hasSurplusEnd;

	private long startSurp, endSurp;

	private RelativeLayout relativeLayout;

	private int type, deviceid, channlid;

	private int isPlaySucess;

	private PlayListener playListener;

	public TimeBackBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.mybar, this);
		line = (LinearLayout) findViewById(R.id.line);
		relayout = (RelativeLayout) findViewById(R.id.relayout);
		huakuai = (ImageView) findViewById(R.id.huakuai);
		time_text = (LinearLayout) findViewById(R.id.time_text);
		scroll_time = (TextView) findViewById(R.id.scroll_time);

		huakuaiTouch();
		line.postDelayed(new Runnable() {
			@SuppressWarnings("unused")
			@Override
			public void run() {
				// 人为修改布局 使之显示合理
				int text_height = scroll_time.getMeasuredHeight();
				int line_height = line.getMeasuredHeight();
				int line_wedth = line.getMeasuredWidth();
				int re_height = relayout.getMeasuredHeight();
				int re_wedth = relayout.getMeasuredWidth();
				int hua_height = huakuai.getMeasuredHeight();
				int hua_weidth = huakuai.getMeasuredWidth();
				int time_text_height = time_text.getMeasuredHeight();
				int time_text_weidth = time_text.getMeasuredWidth();

				LayoutParams layoutParams = new LayoutParams(hua_weidth,
						hua_height);
				layoutParams.setMargins(0, line_height + re_height / 2
						- hua_height / 2 + text_height, 0, 0);
				huakuai.setLayoutParams(layoutParams);

				LayoutParams lineParams = new LayoutParams(
						LayoutParams.MATCH_PARENT, line_height);
				lineParams.setMargins(hua_weidth / 2 + 20, text_height,
						hua_weidth / 2 + 20, 0);
				line.setLayoutParams(lineParams);

				LayoutParams huaParams = new LayoutParams(
						LayoutParams.MATCH_PARENT, re_height);
				huaParams.setMargins(hua_weidth / 2, line_height + text_height,
						hua_weidth / 2, 0);
				relayout.setLayoutParams(huaParams);
				re_x = relayout.getMeasuredWidth();

				LayoutParams text_layout = new LayoutParams(
						LayoutParams.MATCH_PARENT, time_text_height);
				text_layout.setMargins(0, line_height + text_height + re_height
						+ 5, 0, 0);
				time_text.setLayoutParams(text_layout);
				addLineView();
				addTextView(hua_weidth);
			}
		}, 100);
	}

	private void addTextView(int hua_weidth) {

		for (int i = 0; i < 7; i++) {
			TextView textView = new TextView(getContext());
			textView.setTextSize(12);
			textView.setGravity(Gravity.CENTER);
			android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(
					(hua_weidth / 2 + 20) * 2,
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT);

			if (i == 0) {
				layoutParams.setMargins(0, 0, 0, 0);
			} else {
				layoutParams
						.setMargins(
								(time_text.getMeasuredWidth() - (hua_weidth / 2 + 20) * 2 * 7) / 6,
								0, 0, 0);
			}

			textView.setLayoutParams(layoutParams);
			if (i * 4 < 10) {
				textView.setText("0" + i * 4 + ":00");
			} else {
				textView.setText(i * 4 + ":00");
			}

			time_text.addView(textView);
		}

	}

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("unused")
	private void getHoursTime() {
		long nowPlayTime = (mlist.get(where_i).getEndTime() - mlist
				.get(where_i).getStartTime())
				/ playBackUtil.getFrontEndFilePlayPos()
				+ mlist.get(where_i).getStartTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date(nowPlayTime);
		String time = simpleDateFormat.format(date);
		time_text.removeAllViews();
		for (int i = 0; i < 7; i++) {
			TextView textView = new TextView(getContext());
			textView.setTextSize(12);
			textView.setGravity(Gravity.CENTER);
			android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(
					(huakuai.getMeasuredWidth() / 2 + 20) * 2,
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT);

			if (i == 0) {
				layoutParams.setMargins(0, 0, 0, 0);
			} else {
				layoutParams.setMargins(
						(time_text.getMeasuredWidth() - (huakuai
								.getMeasuredWidth() / 2 + 20) * 2 * 7) / 6, 0,
						0, 0);
			}

			textView.setLayoutParams(layoutParams);
			if (i * 4 < 10) {
				textView.setText("0" + i * 4 + ":00");
			} else {
				textView.setText(i * 4 + ":00");
			}
			time_text.addView(textView);
		}

	}

	/**
	 * 添加刻度线
	 */
	private void addLineView() {
		for (int i = 0; i < 7; i++) {
			LinearLayout layout = new LinearLayout(getContext());
			layout.setBackgroundResource(R.color.main_color);
			android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(
					3, android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
			// 设置每个时间刻度线之间的距离
			if (i == 0) {
				layoutParams.setMargins(0, 0, 0, 0);
			} else {
				layoutParams.setMargins(
						(line.getMeasuredWidth() - huakuai.getMeasuredWidth()
								- 40 - 21) / 6, 0, 0, 0);
			}
			layout.setLayoutParams(layoutParams);
			line.addView(layout);
		}
	}

	public void startPlay(PlayBackUtil playBackUtil,
			RelativeLayout relativeLayout, int type, int deviceid, int channalid) {
		this.playBackUtil = playBackUtil;
		this.relativeLayout = relativeLayout;
		this.type = type;
		this.deviceid = deviceid;
		this.channlid = channalid;
	}

	private void play() {
		if (hasPlay) {
			LayoutParams item = (LayoutParams) no_mlist_time_line.get(where_i)
					.getLayoutParams();
			int old_nPos = (now_location - mlist_layout.get(where_i).getStart() - 20)
					* 10000 / item.width;

			final int nPos;
			if (hasSurplusStart && where_i == 0) {
				nPos = (int) (startSurp + old_nPos * (10000 - startSurp)
						/ 10000);
			} else if (hasSurplusEnd && where_i == (mlist.size() - 1)) {
				nPos = (int) ((10000 - endSurp) * old_nPos / 10000);
			} else {
				nPos = old_nPos;
			}
			playListener.onStart();
			// relativeLayout.removeAllViews();
			streamPlayer = playBackUtil.getStreamPlayer(type, relativeLayout,
					deviceid, channlid);
			  Log.e("insert","3===="+ new Date().toString());
			playBackUtil.startFrontEndFilePlay(nPos,
					mlist.get(where_i), streamPlayer);
			  Log.e("insert","3===="+ new Date().toString());
			if (isPlaySucess <= 0) {
				playListener.onFail();
			} else {
				playListener.onSucess();
				controlLine();
			}
		}
	}

	public interface PlayListener {

		void onStart();

		void onSucess();

		void onFail();
	}

	public void setPlayListener(PlayListener playListener) {
		this.playListener = playListener;
	}

	private void huakuaiTouch() {
		huakuai.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int X = (int) event.getRawX();
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					hasPlay = false;
					RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) huakuai
							.getLayoutParams();
					_xDelta = X - lParams.leftMargin;
					old_left = lParams.leftMargin;
					getParent().requestDisallowInterceptTouchEvent(true);
					break;
				case MotionEvent.ACTION_UP:
					scroll_time.setVisibility(View.INVISIBLE);
					now_location = X - _xDelta;
					clearTime();
					springBack();
					play();
					getParent().requestDisallowInterceptTouchEvent(false);
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					break;
				case MotionEvent.ACTION_POINTER_UP:
					break;
				case MotionEvent.ACTION_MOVE:
					scroll_time.setVisibility(View.VISIBLE);
					ps = item_time / (relayout.getMeasuredWidth() - 40);
					getParent().requestDisallowInterceptTouchEvent(true);
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) huakuai
							.getLayoutParams();
					if ((X - _xDelta) < 0) {// 如果滑动小于0
						layoutParams.leftMargin = 0;
					} else if ((X - _xDelta) > relayout.getMeasuredWidth()) {// 滑动大于时间轴的长度
						layoutParams.leftMargin = relayout.getMeasuredWidth();
					} else {
						layoutParams.leftMargin = X - _xDelta;
						scroll_time.setText(secToTime((X - _xDelta - 20) * ps
								/ 1000));
					}
					huakuai.setLayoutParams(layoutParams);
					if(mlist_layout!=null)
					{
						// 计算单位内的时间
						for (int i = 0; i < mlist_layout.size(); i++) {
							// 滑动后的位置 大于播放的时间片段的开始 // 滑动后的位置 小于播放的时间片段的结束
							if ((X - _xDelta) >= mlist_layout.get(i).getStart()
									&& (X - _xDelta) <= mlist_layout.get(i)
											.getEnd()) {
								where_i = i;
								hasPlay = true;
								LayoutParams item = (LayoutParams) mlist_time_line
										.get(i).getLayoutParams();
								item.leftMargin = mlist_layout.get(i).getStart() + 20;
								// 通过滑动的后的位置判断 已播放时间段的长度
								if ((X - _xDelta - mlist_layout.get(i).getStart() - 20) <= 0) {
									item.width = 0;
								} else if ((X - _xDelta
										- mlist_layout.get(i).getStart() - 20) >= (mlist_layout
										.get(i).getEnd() - mlist_layout.get(i)
										.getStart())) {
									item.width = mlist_layout.get(i).getEnd()
											- mlist_layout.get(i).getStart();
								} else {
									item.width = X - _xDelta
											- mlist_layout.get(i).getStart();
								}
								mlist_time_line.get(i).setLayoutParams(item);
							}
						}
					}
					
					break;
				}
				return true;
			}
		});
	}

	// public void playBackInfo(PlayBackUtil playBackUtil,
	// List<WMFileInfo> mlWmFileInfos, StreamPlayer streamPlayer) {
	// LayoutParams item_line = (LayoutParams) mlist_time_line.get(where_i)
	// .getLayoutParams();
	// // where_i item_line.width now_location
	// int nPos = (now_location - mlist_layout.get(where_i).getStart() - 20)
	// / item_line.width * 10000;
	// Log.d("insert", "where_i===" + where_i + "==item_line.width=="
	// + item_line.width + "==nPos===" + nPos);
	// playBackUtil.startFrontEndFilePlay(nPos, mlWmFileInfos.get(where_i),
	// streamPlayer);
	// }

	/**
	 * 时间轴回弹 如果最新的滑动位置没有播放片段 而上一次的滑动位置有播放位置 就回到之前的播放位置
	 */
	private void springBack() {
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) huakuai
				.getLayoutParams();
		if(mlist_layout!=null)
		{
			for (int i = 0; i < mlist_layout.size(); i++) {
				if ((layoutParams.leftMargin - 20) < mlist_layout.get(0).getStart()) {
					if ((old_left >= mlist_layout.get(i).getStart())
							&& (old_left <= mlist_layout.get(i).getEnd())) {
						layoutParams.leftMargin = old_left;
						huakuai.setLayoutParams(layoutParams);
						playIng(layoutParams.leftMargin);
					}
				} else if ((layoutParams.leftMargin - 20) > mlist_layout.get(
						mlist_layout.size() - 1).getEnd()) {
					if ((old_left >= mlist_layout.get(i).getStart())
							&& (old_left <= mlist_layout.get(i).getEnd())) {
						layoutParams.leftMargin = old_left;
						huakuai.setLayoutParams(layoutParams);
						playIng(layoutParams.leftMargin);
					}
				} else if (i < mlist_layout.size()) {
					if (((layoutParams.leftMargin - 20) > mlist_layout.get(i)
							.getEnd())
							&& ((layoutParams.leftMargin - 20) < mlist_layout.get(
									i + 1).getStart())) {
						if ((old_left >= mlist_layout.get(i).getStart())
								&& (old_left <= mlist_layout.get(i).getEnd())) {
							layoutParams.leftMargin = old_left;
							huakuai.setLayoutParams(layoutParams);
							playIng(layoutParams.leftMargin);
						}
						if ((old_left >= mlist_layout.get(i + 1).getStart())
								&& (old_left <= mlist_layout.get(i + 1).getEnd())) {
							layoutParams.leftMargin = old_left;
							huakuai.setLayoutParams(layoutParams);
							playIng(layoutParams.leftMargin);
						}
					}
				}
			}
		}
	
	}

	/**
	 * 控制滑动按钮的位置
	 */
	@SuppressLint("HandlerLeak")
	public void controlLine() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
		}, 1000, 1000);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					int pos = playBackUtil.getFrontEndFilePlayPos();
					if (pos == 10000) {
						if ((mlist.size() < (where_i + 1))
								&& ((mlist.get(where_i).getEndTime() + 1000) == mlist
										.get(where_i + 1).getStartTime())) {
							playListener.onStart();
							// relativeLayout.removeAllViews();
							  Log.e("insert","4===="+ new Date().toString());
							streamPlayer = playBackUtil.getStreamPlayer(type,
									relativeLayout, deviceid, channlid);
							  Log.e("insert","4===="+ new Date().toString());
							playBackUtil.startFrontEndFilePlay(
									1, mlist.get(where_i + 1), streamPlayer);
							if (isPlaySucess <= 0) {
								playListener.onFail();
							} else {
								playListener.onSucess();
								controlLine();
							}
						} else {
							clearTime();
						}
					}
					time_n++;
					if (time_n % (ps / 1000) == 0) {
						RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) huakuai
								.getLayoutParams();
						lParams.leftMargin = lParams.leftMargin + 1;
						huakuai.setLayoutParams(lParams);
						playIng(lParams.leftMargin);
					}
					break;
				default:
					break;
				}
			}
		};
	}

	/**
	 * 控制已播放的时间片段的绘制
	 * 
	 * @param left
	 *            滑块的左边距
	 * 
	 */
	private void playIng(int left) {
		for (int i = 0; i < mlist_layout.size(); i++) {
			if (left >= mlist_layout.get(i).getStart()
					&& (left <= mlist_layout.get(i).getEnd())) {
				LayoutParams item = (LayoutParams) mlist_time_line.get(i)
						.getLayoutParams();
				item.leftMargin = mlist_layout.get(i).getStart() + 20;
				if ((left - mlist_layout.get(i).getStart() - 20) <= 0) {
					item.width = 0;
				} else if ((left - mlist_layout.get(i).getStart() - 20) >= (mlist_layout
						.get(i).getEnd() - mlist_layout.get(i).getStart())) {
					item.width = mlist_layout.get(i).getEnd()
							- mlist_layout.get(i).getStart();
				} else {
					item.width = left - mlist_layout.get(i).getStart();
				}
				mlist_time_line.get(i).setLayoutParams(item);
				if ((left - mlist_layout.get(i).getStart() - 20) == (mlist_layout
						.get(i).getEnd() - mlist_layout.get(i).getStart())) {
					clearTime();
				}
			} else {
				if (left >= mlist_layout.get(i).getEnd()) {
					LayoutParams item = (LayoutParams) mlist_time_line.get(i)
							.getLayoutParams();
					item.leftMargin = mlist_layout.get(i).getStart() + 20;
					item.width = mlist_layout.get(i).getEnd()
							- mlist_layout.get(i).getStart();
					mlist_time_line.get(i).setLayoutParams(item);
				} else {
					LayoutParams item = (LayoutParams) mlist_time_line.get(i)
							.getLayoutParams();
					item.leftMargin = mlist_layout.get(i).getStart() + 20;
					item.width = 0;
					mlist_time_line.get(i).setLayoutParams(item);
				}
				clearTime();
			}
		}
	}

	private void clearTime() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
	}

	/**
	 * 添加有回放的时间片段
	 * 
	 * @param mlist
	 *            时间片段
	 * @param date
	 *            日期
	 */
	@SuppressLint("SimpleDateFormat")
	public void addTimeLine(List<WMFileInfo> mlist, long endtime, long starttime) {
		this.mlist = mlist;
		mlist_time_line = new ArrayList<LinearLayout>();
		no_mlist_time_line = new ArrayList<LinearLayout>();
		mlist_layout = new ArrayList<Timelay>();
		where_i = 0;
		hasPlay = false;
		for (int i = 0; i < mlist.size(); i++) {
			SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss");
			String startTime = sDateFormat.format(new Date(mlist.get(i)
					.getStartTime()));
			String endTime = sDateFormat.format(new Date(mlist.get(i)
					.getEndTime()));
			String[] str_start = startTime.split(":");
			String[] str_end = endTime.split(":");
			long start_time;
			long end_time;
			if (i == 0
					&& Integer.parseInt(str_start[0]) > Integer
							.parseInt(str_end[0])) {
				start_time = 0;
				hasSurplusStart = true;
				startSurp = (starttime - mlist.get(0).getStartTime())
						* 10000
						/ (mlist.get(0).getEndTime() - mlist.get(0)
								.getStartTime());
			} else {
				start_time = Integer.parseInt(str_start[0]) * 60 * 60 * 1000
						+ Integer.parseInt(str_start[1]) * 60 * 1000
						+ Integer.parseInt(str_start[2]) * 1000;
			}
			if (i == 0
					&& Integer.parseInt(str_start[0]) <= Integer
							.parseInt(str_end[0])) {
				hasSurplusStart = false;
			}

			Log.d("insert", startTime + "====" + endTime);
			if (i == (mlist.size() - 1)
					&& mlist.get(mlist.size() - 1).getEndTime() > endtime) {
				end_time = 24 * 60 * 60 * 1000;
				hasSurplusEnd = true;
				endSurp = (mlist.get(mlist.size() - 1).getEndTime() - endtime)
						* 10000
						/ (mlist.get(0).getEndTime() - mlist.get(0)
								.getStartTime());

			} else {
				end_time = Integer.parseInt(str_end[0]) * 60 * 60 * 1000
						+ Integer.parseInt(str_end[1]) * 60 * 1000
						+ Integer.parseInt(str_end[2]) * 1000;
			}

			if (i == (mlist.size() - 1)
					&& mlist.get(mlist.size() - 1).getEndTime() <= endtime) {
				hasSurplusEnd = false;
			}
			int start = (int) (start_time * re_x / item_time);
			int end = (int) (end_time * re_x / item_time);
			Timelay timelay = new Timelay();
			timelay.setStart(start);
			timelay.setEnd(end);
			mlist_layout.add(timelay);

			LinearLayout itemLayout = new LinearLayout(getContext());
			LayoutParams layoutParams = new LayoutParams(end - start,
					LayoutParams.MATCH_PARENT);
			layoutParams.setMargins(start + 20, 0, 0, 0);
			itemLayout.setLayoutParams(layoutParams);
			itemLayout.setBackgroundResource(R.drawable.bg_time);
			relayout.addView(itemLayout);
			no_mlist_time_line.add(itemLayout);
		}
		Log.d("play", "mlist==" + mlist.size());
		addColorLine();
		scrollMove();
	}

	/**
	 * 滑块初始化
	 * 
	 * @param X
	 */
	private void scrollMove() {
		ps = item_time / (relayout.getMeasuredWidth() - 40);
		getParent().requestDisallowInterceptTouchEvent(true);
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) huakuai
				.getLayoutParams();
		layoutParams.leftMargin = 0;
		huakuai.setLayoutParams(layoutParams);
	}

	/**
	 * 添加已播放回放片段
	 */
	private void addColorLine() {
		for (int i = 0; i < mlist_layout.size(); i++) {
			LinearLayout lyaout = new LinearLayout(getContext());
			LayoutParams layoutParams = new LayoutParams(0,
					LayoutParams.MATCH_PARENT);
			layoutParams.setMargins(mlist_layout.get(i).getStart() + 20, 0, 0,
					0);
			lyaout.setLayoutParams(layoutParams);
			lyaout.setBackgroundResource(R.drawable.bg);
			relayout.addView(lyaout);
			mlist_time_line.add(lyaout);
		}
	}

	/**
	 * long型的值转换成String 类型的 xx：xx：xx的时间
	 */
	// a integer to xx:xx:xx
	private String secToTime(long time) {
		String timeStr = null;
		long hour = 0;
		long minute = 0;
		long second = 0;
		if (time <= 0)
			return "00:00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 23)
					return "24:00:00";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
						+ unitFormat(second);
			}
		}
		return timeStr;
	}

	private String unitFormat(long i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Long.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

}
