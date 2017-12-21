package com.vomont.yundudao.view.dialog;

import com.vomont.yundudao.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ProDialog extends ProgressDialog
{
    private String progressingText;
    
    private TextView progressingTextView;
    
    public ProDialog(Context context)
    {
        super(context, R.style.ActionSheetDialogAnimation);
        // TODO Auto-generated constructor stub
    }
    
    public ProDialog(Context context, int progressingTextRes)
    {
        this(context);
        this.progressingText = context.getString(progressingTextRes);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progressing_dialog);
        progressingTextView = (TextView)findViewById(R.id.progressing_text);
        if (progressingText != null)
        {
            progressingTextView.setVisibility(View.VISIBLE);
            progressingTextView.setText(progressingText);
        }
    }
    
    public void setProgressText(String progressingText)
    {
        this.progressingText = progressingText;
    }
}
