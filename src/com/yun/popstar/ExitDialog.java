package com.yun.popstar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExitDialog extends Activity {

	private Button ok;
	private Button cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit);
		ok=(Button)findViewById(R.id.exitok);
		cancel=(Button)findViewById(R.id.exitcancel);
		ok.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				MainActivity.instance.finish();
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
