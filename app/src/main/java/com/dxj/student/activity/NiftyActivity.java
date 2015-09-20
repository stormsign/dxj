package com.dxj.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dxj.student.R;
import com.dxj.student.base.BaseActivity;
import com.dxj.student.effectdialog.Effectstype;
import com.dxj.student.effectdialog.NiftyDialogBuilder;


public class NiftyActivity extends BaseActivity {

    private Effectstype effect;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nifty_main);
        initData();
        initView();
    }

    @Override
    public void initTitle() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    public void dialogShow(View v) {
        NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);

        switch (v.getId()) {
            case R.id.fadein:
                effect = Effectstype.Fadein;
                break;
        }

        dialogBuilder
                .withTitle("Modal Dialog")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage("This is a modal Dialog.")                     //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)                               //def
                .withIcon(getResources().getDrawable(R.mipmap.icon))
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(700)                                          //def
                .withEffect(effect)                                         //def Effectstype.Slidetop
                .withButton1Text("OK")                                      //def gone
                .withButton2Text("Cancel")                                  //def gone
                .setCustomView(R.layout.custom_view, v.getContext())         //.setCustomView(View or ResId,context)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "i'm btn1", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NiftyActivity.this,NiftyActivity.class);
                        startActivity(intent);
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "i'm btn2", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NiftyActivity.this,NiftyActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
        tv = (TextView) dialogBuilder.findViewById(R.id.tv);
        tv.setText("好了");
    }


}
