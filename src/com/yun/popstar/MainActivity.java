package com.yun.popstar;

import com.yun.help.SharePre;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity {

	public static MainActivity instance;
	private ImageView newgame;
	private ImageView loadinggame;
	private ImageView aboutgame;
	private ImageView exitgame;
	private SharePre sharePre;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		instance = this;
		sharePre = SharePre.getInstance(this);
		newgame = (ImageView) findViewById(R.id.newgame);
		loadinggame = (ImageView) findViewById(R.id.loadinggame);
		aboutgame = (ImageView) findViewById(R.id.about);
		exitgame = (ImageView) findViewById(R.id.exit);

		newgame.setOnClickListener(new MyOnClick());
		loadinggame.setOnClickListener(new MyOnClick());
		aboutgame.setOnClickListener(new MyOnClick());
		exitgame.setOnClickListener(new MyOnClick());

	}

	public class MyOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.newgame) {
				if (sharePre.getInt("level", 0) == 0) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							GameActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							NewGameDialog.class);
					startActivity(intent);
				}
			} else if (v.getId() == R.id.loadinggame) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), GameActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.about) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), SettingActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.exit) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ExitDialog.class);
				startActivity(intent);
			}

		}

	}
}
