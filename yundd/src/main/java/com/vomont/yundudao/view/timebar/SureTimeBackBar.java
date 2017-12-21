package com.vomont.yundudao.view.timebar;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.vomont.yundudao.R;
import com.vomont.yundudao.utils.PlayBackUtil;
import com.wmclient.clientsdk.StreamPlayer;
import com.wmclient.clientsdk.WMFileInfo;
import android.annotation.SuppressLint;
import android.content.Context;
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
public class SureTimeBackBar extends RelativeLayout {
	// 刻度线
	private LinearLayout line;

	// 时间轴
	private RelativeLayout relayout;

	private TextView scroll_time;

	// 滑动的按钮
	private ImageView huakuai;

	private int _xDelta;

	private LinearLayout time_text;
	// 一天的毫秒值
	private long item_time = 24 * 60 * 60 * 1000;
	// 每像素的时间
	private long ps;

	private PlayBackUtil playBackUtil;

	private StreamPlayer streamPlayer;

	private RelativeLayout relativeLayout;

	private int type, deviceid, channlid;

	private List<WMFileInfo> mlsit;

	private List<ShowTimeInfo> shoWTimePos;

	private int time_layout_width;

	private List<LinearLayout> timeLine_mlit;
	private List<LinearLayout> showTimeLine_mlist;
	//计时器
	private Timer timer;
	//原来的位置
	private int old_left;
	
	private int where_i;
	//当前的位置
	private int now_location;
	
	private long startSurp,endSurp;

	//开头是否截取 末尾是否截取
	private boolean hasSurplusStart, hasSurplusEnd;
	public SureTimeBackBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
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
				time_layout_width = relayout.getMeasuredWidth();

				LayoutParams text_layout = new LayoutParams(
						LayoutParams.MATCH_PARENT, time_text_height);
				text_layout.setMargins(0, line_height + text_height + re_height
						+ 5, 0, 0);
				time_text.setLayoutParams(text_layout);
				addLineView();
				addTextView(hua_weidth);
			}
		}, 300);
	}

	public void startPlay(PlayBackUtil playBackUtil,
			RelativeLayout relativeLayout, int type, int deviceid, int channalid) {
		this.playBackUtil = playBackUtil;
		this.relativeLayout = relativeLayout;
		this.type = type;
		this.deviceid = deviceid;
		this.channlid = channalid;
	}

	/**
	 * 绘制时间片段
	 * 
	 * @param mlit
	 *            播放文件的集合
	 * @param endTime
	 *            搜索的结束时间
	 * @param startTime
	 *            搜索的开始时间
	 */
	public void getShowTime(List<WMFileInfo> mlit, long endTime, long startTime) {
		this.mlsit = mlit;
		shoWTimePos = new ArrayList<SureTimeBackBar.ShowTimeInfo>();
		timeLine_mlit = new ArrayList<LinearLayout>();
		// 初始化 布局
		if (relayout.getChildCount() != 0) {
			relayout.removeAllViews();
		}
		for (int i = 0; i < mlit.size(); i++) {
			long starttime = 0;

			long endtime = 0;
			// 判断第一个片段是否有前一天的片段，如果有进行截取
			if (i == 0 && mlsit.get(i).getStartTime() < startTime) {
				starttime = 0;
				endtime = mlit.get(i).getEndTime() - startTime;
				startSurp = (starttime - mlsit.get(0).getStartTime())
						* 10000
						/ (mlsit.get(0).getEndTime() - mlsit.get(0)
								.getStartTime());
			}
			// 判断最后一个片段的是否含有下一日的片段，如果有进行截取
			else if ((i == mlit.size() - 1)
					&& mlsit.get(i).getEndTime() > endTime) {
				endtime = endTime - startTime;
				starttime = mlsit.get(i).getStartTime();
				endSurp = (mlsit.get(mlsit.size() - 1).getEndTime() - endtime)
						* 10000
						/ (mlsit.get(0).getEndTime() - mlsit.get(0)
								.getStartTime());
			}
			// 其他的正常显示就好了
			else {
				starttime = mlsit.get(i).getStartTime() - startTime;
				endtime = mlit.get(i).getEndTime() - startTime;
			}
			int start = (int) (starttime * time_layout_width / item_time);
			int end = (int) (endtime * time_layout_width / item_time);
			LinearLayout itemLayout = new LinearLayout(getContext());
			LayoutParams layoutParams = new LayoutParams(end - start,
					LayoutParams.MATCH_PARENT);
			layoutParams.setMargins(start + 20, 0, 0, 0);
			itemLayout.setLayoutParams(layoutParams);
			itemLayout.setBackgroundResource(R.drawable.bg_time);
			relayout.addView(itemLayout);
			timeLine_mlit.add(itemLayout);

			ShowTimeInfo showTimeInfo = new ShowTimeInfo();
			showTimeInfo.setStartPos(start);
			showTimeInfo.setEndPos(end);
			shoWTimePos.add(showTimeInfo);

		}
		addColorLine();
		initScroll();
	}
	
	/**
	 * 初始化滑块的位置
	 * 
	 */
	  private void 	initScroll()
	  {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) huakuai
					.getLayoutParams();
				layoutParams.leftMargin = 0;
			huakuai.setLayoutParams(layoutParams);
	  }

	/**
	 * 添加已播放回放片段
	 */
	private void addColorLine() {
		showTimeLine_mlist = new ArrayList<LinearLayout>();
		for (int i = 0; i < shoWTimePos.size(); i++) {
			LinearLayout lyaout = new LinearLayout(getContext());
			LayoutParams layoutParams = new LayoutParams(0,
					LayoutParams.MATCH_PARENT);
			layoutParams.setMargins(shoWTimePos.get(i).getStartPos() + 20, 0,
					0, 0);
			lyaout.setLayoutParams(layoutParams);
			lyaout.setBackgroundResource(R.drawable.bg);
			relayout.addView(lyaout);
			showTimeLine_mlist.add(lyaout);
		}
	}

	
	/**
	 * 时间轴回弹 如果最新的滑动位置没有播放片段 而上一次的滑动位置有播放位置 就回到之前的播放位置
	 */
	private void springBack() {
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) huakuai
				.getLayoutParams();
		for (int i = 0; i < shoWTimePos.size(); i++) {
			if ((layoutParams.leftMargin - 20) < shoWTimePos.get(0).getStartPos()) {
				if ((old_left >= shoWTimePos.get(i).getStartPos())
						&& (old_left <= shoWTimePos.get(i).getEndPos())) {
					layoutParams.leftMargin = old_left;
					huakuai.setLayoutParams(layoutParams);
					 playIng(layoutParams.leftMargin);
				}
			} else if ((layoutParams.leftMargin - 20) > shoWTimePos.get(
					shoWTimePos.size() - 1).getEndPos()) {
				if ((old_left >= shoWTimePos.get(i).getStartPos())
						&& (old_left <= shoWTimePos.get(i).getEndPos())) {
					layoutParams.leftMargin = old_left;
					huakuai.setLayoutParams(layoutParams);
					 playIng(layoutParams.leftMargin);
				}
			} else if (i < shoWTimePos.size()) {
				if (((layoutParams.leftMargin - 20) > shoWTimePos.get(i)
						.getEndPos())
						&& ((layoutParams.leftMargin - 20) < shoWTimePos.get(
								i + 1).getStartPos())) {
					if ((old_left >= shoWTimePos.get(i).getStartPos())
							&& (old_left <= shoWTimePos.get(i).getEndPos())) {
						layoutParams.leftMargin = old_left;
						huakuai.setLayoutParams(layoutParams);
						 playIng(layoutParams.leftMargin);
					}
					if ((old_left >= shoWTimePos.get(i + 1).getStartPos())
							&& (old_left <= shoWTimePos.get(i + 1).getEndPos())) {
						layoutParams.leftMargin = old_left;
						huakuai.setLayoutParams(layoutParams);
						 playIng(layoutParams.leftMargin);
					}
				}
			}
		}
	}
	class ShowTimeInfo {
		private int startPos;

		private int endPos;

		public int getStartPos() {
			return startPos;
		}

		public void setStartPos(int startPos) {
			this.startPos = startPos;
		}

		public int getEndPos() {
			return endPos;
		}

		public void setEndPos(int endPos) {
			this.endPos = endPos;
		}

	}
	
	/**
	 * 控制已播放的时间片段的绘制
	 * 
	 * @param left
	 *            滑块的左边距
	 * 
	 */
	private void playIng(int left) {
		for (int i = 0; i < shoWTimePos.size(); i++) {
			if (left >= shoWTimePos.get(i).getStartPos()
					&& (left <= shoWTimePos.get(i).getEndPos())) {
				LayoutParams item = (LayoutParams) showTimeLine_mlist.get(i)
						.getLayoutParams();
				item.leftMargin = shoWTimePos.get(i).getStartPos() + 20;
				if ((left - shoWTimePos.get(i).getStartPos() - 20) <= 0) {
					item.width = 0;
				} else if ((left - shoWTimePos.get(i).getStartPos() - 20) >= (shoWTimePos
						.get(i).getEndPos() - shoWTimePos.get(i).getStartPos())) {
					item.width = shoWTimePos.get(i).getEndPos()
							- shoWTimePos.get(i).getStartPos();
				} else {
					item.width = left - shoWTimePos.get(i).getStartPos();
				}
				showTimeLine_mlist.get(i).setLayoutParams(item);
				if ((left - shoWTimePos.get(i).getStartPos() - 20) == (shoWTimePos
						.get(i).getEndPos() - shoWTimePos.get(i).getStartPos())) {
					clearTime();
				}
			} else {
				if (left >= shoWTimePos.get(i).getEndPos()) {
					LayoutParams item = (LayoutParams) showTimeLine_mlist.get(i)
							.getLayoutParams();
					item.leftMargin = shoWTimePos.get(i).getStartPos() + 20;
					item.width = shoWTimePos.get(i).getEndPos()
							- shoWTimePos.get(i).getStartPos();
					showTimeLine_mlist.get(i).setLayoutParams(item);
				} else {
					LayoutParams item = (LayoutParams) showTimeLine_mlist.get(i)
							.getLayoutParams();
					item.leftMargin = shoWTimePos.get(i).getStartPos() + 20;
					item.width = 0;
					showTimeLine_mlist.get(i).setLayoutParams(item);
				}
				clearTime();
			}
		}
	}

	/**
	 * 添加时间刻度
	 * 
	 * @param hua_weidth
	 */
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

	private void huakuaiTouch() {
		huakuai.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int X = (int) event.getRawX();
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) huakuai
							.getLayoutParams();
					_xDelta = X - lParams.leftMargin;
					old_left = lParams.leftMargin;
					getParent().requestDisallowInterceptTouchEvent(true);
					break;
				case MotionEvent.ACTION_UP:
					scroll_time.setVisibility(View.INVISIBLE);
					getParent().requestDisallowInterceptTouchEvent(false);
					hasShowTime(X);
					now_location = X - _xDelta;
					springBack();
				
					play();
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					break;
				case MotionEvent.ACTION_POINTER_UP:
					break;
				case MotionEvent.ACTION_MOVE:
					scroll_time.setVisibility(View.VISIBLE);
					scrollMove(X);
					break;
				}
				return true;
			}
		});
	}
	private void play() {
//		if (hasPlay) {
			LayoutParams item = (LayoutParams) timeLine_mlit.get(where_i)
					.getLayoutParams();
			int old_nPos = (now_location - shoWTimePos.get(where_i).getStartPos() - 20)
					* 10000 / item.width;

			int nPos;
			if (hasSurplusStart && where_i == 0) {
				nPos = (int) (startSurp + old_nPos * (10000 - startSurp)
						/ 10000);
			} else if (hasSurplusEnd && where_i == (mlsit.size() - 1)) {
				nPos = (int) ((10000 - endSurp) * old_nPos / 10000);
			} else {
				nPos = old_nPos;
			}
			Log.d("insert", " item.width===" + item.width + "=where_i=="
					+ where_i + "==nPos==" + nPos);
//			relativeLayout.removeAllViews();
			streamPlayer=playBackUtil.getStreamPlayer(type, relativeLayout, deviceid, channlid);
			
			playBackUtil.startFrontEndFilePlay(nPos, mlsit.get(where_i),
					streamPlayer);
//		}
	}
	
	
	private void clearTime() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
	}

	/**
	 * 已播放片段的显示
	 * @param X
	 */
	private void hasShowTime(int X) {
		// 计算单位内的时间
		for (int i = 0; i < shoWTimePos.size(); i++) {
			// 滑动后的位置 大于播放的时间片段的开始 // 滑动后的位置 小于播放的时间片段的结束
			if ((X - _xDelta) >= shoWTimePos.get(i).getStartPos()
					&& (X - _xDelta) <= shoWTimePos.get(i).getEndPos()) {
				 where_i = i;
				// hasPlay = true;
				LayoutParams item = (LayoutParams) showTimeLine_mlist.get(i)
						.getLayoutParams();
				item.leftMargin = shoWTimePos.get(i).getStartPos() + 20;
				// 通过滑动的后的位置判断 已播放时间段的长度
				if ((X - _xDelta - shoWTimePos.get(i).getStartPos() - 20) <= 0) {
					item.width = 0;
				} else if ((X - _xDelta - shoWTimePos.get(i).getStartPos() - 20) >= (shoWTimePos
						.get(i).getEndPos() - shoWTimePos.get(i).getStartPos())) {
					item.width = shoWTimePos.get(i).getEndPos()
							- shoWTimePos.get(i).getStartPos();
				} else {
					item.width = X - _xDelta - shoWTimePos.get(i).getStartPos();
				}
				showTimeLine_mlist.get(i).setLayoutParams(item);
			}
		}
	}

	/**
	 * 滑块移动逻辑
	 * 
	 * @param X
	 */
	private void scrollMove(int X) {
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
			scroll_time.setText(secToTime((X - _xDelta - 20) * ps / 1000));
		}
		huakuai.setLayoutParams(layoutParams);
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
