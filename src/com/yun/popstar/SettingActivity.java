package com.yun.popstar;

import com.yun.help.SharePre;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SettingActivity extends Activity {

	private RadioGroup group;
	private RadioButton ra1, ra2, ra3;
	private SharePre sharePre;
	private int easy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		sharePre = SharePre.getInstance(this);
		group = (RadioGroup) this.findViewById(R.id.radioGroup);
		ra1 = (RadioButton) findViewById(R.id.radio1);
		ra2 = (RadioButton) findViewById(R.id.radio2);
		ra3 = (RadioButton) findViewById(R.id.radio3);
		easy = sharePre.getInt("easy", 4);
		if (easy == 3) {
			ra1.setChecked(true);
		} else if (easy == 4) {
			ra2.setChecked(true);
		} else if (easy == 5) {
			ra3.setChecked(true);
		}
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				if (radioButtonId == R.id.radio1) {
					sharePre.setInt("easy", 3);
				} else if (radioButtonId == R.id.radio2) {
					sharePre.setInt("easy", 4);
				} else if (radioButtonId == R.id.radio3) {
					sharePre.setInt("easy", 5);
				}
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(sharePre.getInt("easy", 4)!=easy){
			sharePre.setInt("level", 0);
		}
		super.onPause();
	}
	

}
