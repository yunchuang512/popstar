package com.yun.popstar;

import com.yun.help.SharePre;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewGameDialog extends Activity {

	private Button ok;
	private Button cancel;
 private SharePre sharePre;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialognewgame);
		sharePre = SharePre.getInstance(this);
		ok=(Button)findViewById(R.id.ok);
		cancel=(Button)findViewById(R.id.cancel);
		ok.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sharePre.setInt("level", 0);
				Intent intent=new Intent();
				intent.setClass(getApplicationContext(), GameActivity.class);
				startActivity(intent);
				finish();
			}
			
		});cancel.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
	}

}
