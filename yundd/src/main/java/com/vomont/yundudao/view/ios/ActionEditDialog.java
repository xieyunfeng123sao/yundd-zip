package com.vomont.yundudao.view.ios;

import java.util.ArrayList;
import java.util.List;

import com.vomont.yundudao.R;
import com.vomont.yundudao.utils.DensityUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ActionEditDialog
{
    private Context context;
    
    private Dialog dialog;
    
    private TextView txt_title;
    
    private TextView txt_cancel;
    
    private LinearLayout lLayout_content;
    
    private ScrollView sLayout_content;
    
    private boolean showTitle = false;
    
    private List<SheetItem> sheetItemList;
    
    private Display display;
    
    public ActionEditDialog(Context context)
    {
        this.context = context;
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }
    
    @SuppressWarnings("deprecation")
    public ActionEditDialog builder()
    {
        View view = LayoutInflater.from(context).inflate(R.layout.view_actionsheet, null);
        
        view.setMinimumWidth(display.getWidth());
        
        sLayout_content = (ScrollView)view.findViewById(R.id.sLayout_content);
        lLayout_content = (LinearLayout)view.findViewById(R.id.lLayout_content);
        txt_title = (TextView)view.findViewById(R.id.txt_title);
        txt_cancel = (TextView)view.findViewById(R.id.txt_cancel);
        txt_cancel.setTextSize(16);
        txt_cancel.setTextColor(context.getResources().getColor(R.color.top_text_color));
        txt_cancel.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        
        return this;
    }
    
    public ActionEditDialog setTitle(String title)
    {
        showTitle = true;
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setText(title);
        return this;
    }
    
    public ActionEditDialog setCancelable(boolean cancel)
    {
        dialog.setCancelable(cancel);
        return this;
    }
    
    public ActionEditDialog setCanceledOnTouchOutside(boolean cancel)
    {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    
    /**
     * 
     * @param strItem
     * 
     * @param color
     * 
     * @param listener
     * @return
     */
    public ActionEditDialog addSheetItem(String strItem, SheetItemColor color, int resid, OnSheetItemClickListener listener)
    {
        if (sheetItemList == null)
        {
            sheetItemList = new ArrayList<SheetItem>();
        }
        sheetItemList.add(new SheetItem(strItem, color, resid, listener));
        return this;
    }
    
    @SuppressWarnings("deprecation")
    private void setSheetItems()
    {
        if (sheetItemList == null || sheetItemList.size() <= 0)
        {
            return;
        }
        
        int size = sheetItemList.size();
        
        if (size >= 7)
        {
            LinearLayout.LayoutParams params = (LayoutParams)sLayout_content.getLayoutParams();
            params.height = display.getHeight() / 2;
            sLayout_content.setLayoutParams(params);
        }
        
        for (int i = 1; i <= size; i++)
        {
            final int index = i;
            SheetItem sheetItem = sheetItemList.get(i - 1);
            String strItem = sheetItem.name;
            SheetItemColor color = sheetItem.color;
            int resid = sheetItem.resid;
            final OnSheetItemClickListener listener = (OnSheetItemClickListener)sheetItem.itemClickListener;
            View view = LayoutInflater.from(context).inflate(R.layout.item_ios_edit, null);
            
            ImageView imageView = (ImageView)view.findViewById(R.id.ios_edit_img);
            TextView textView = (TextView)view.findViewById(R.id.ios_edit_text);
            textView.setText(strItem);
            view.setBackgroundResource(R.drawable.actionsheet_middle_selector);
//            if (size == 1)
//            {
//                if (showTitle)
//                {
//                    view.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
//                }
//                else
//                {
//                    view.setBackgroundResource(R.drawable.actionsheet_single_selector);
//                }
//            }
//            else
//            {
//                if (showTitle)
//                {
//                    if (i >= 1 && i < size)
//                    {
//                        view.setBackgroundResource(R.drawable.actionsheet_middle_selector);
//                    }
//                    else
//                    {
//                        view.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
//                    }
//                }
//                else
//                {
//                    if (i == 1)
//                    {
//                        view.setBackgroundResource(R.drawable.actionsheet_top_selector);
//                    }
//                    else if (i < size)
//                    {
//                        view.setBackgroundResource(R.drawable.actionsheet_middle_selector);
//                    }
//                    else
//                    {
//                        view.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
//                    }
//                }
//            }
            
            if (color == null)
            {
                textView.setTextColor(Color.parseColor(SheetItemColor.Blue.getName()));
            }
            else
            {
                textView.setTextColor(Color.parseColor(color.getName()));
            }
            imageView.setImageResource(resid);
            
            float scale = context.getResources().getDisplayMetrics().density;
            int height = (int)(45 * scale + 0.5f);
            view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height));
            
            view.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onClick(index);
                    dialog.dismiss();
                }
            });
            lLayout_content.addView(view);
        }
    }
    
    public void show()
    {
        setSheetItems();
        dialog.show();
    }
    
    public interface OnSheetItemClickListener
    {
        void onClick(int which);
    }
    
    public class SheetItem
    {
        String name;
        
        OnSheetItemClickListener itemClickListener;
        
        SheetItemColor color;
        
        int resid;
        
        public SheetItem(String name, SheetItemColor color, int resid, OnSheetItemClickListener itemClickListener)
        {
            this.name = name;
            this.color = color;
            this.resid = resid;
            this.itemClickListener = itemClickListener;
        }
    }
    
    public enum SheetItemColor
    {
        Blue("#037BFF"), Red("#FD4A2E"), GRAY("#8C8C8C");
        
        private String name;
        
        private SheetItemColor(String name)
        {
            this.name = name;
        }
        
        public String getName()
        {
            return name;
        }
        
        public void setName(String name)
        {
            this.name = name;
        }
    }
}
