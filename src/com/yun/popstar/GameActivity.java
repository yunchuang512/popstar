package com.yun.popstar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.yun.db.DbManager;
import com.yun.help.SharePre;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends Activity {

	private List<Star> myStar;
	private ImageView imageview;
	Resources r;
	private float swidth;
	private Bitmap bitmap1, bitmap2, bitmap3, bitmap4, bitmap5;
	private int myPoint = 0;
	private int myLevel = 1;
	private int myCount = 0;
	private int myTarget;
	private TextView Ttarget;
	private TextView Tpoint;
	private TextView Tlevel;
	private TextView Tscore;
	private SharePre sharePre;
	private DbManager dbM;
	private List<Star> datas;
	private int easy = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		sharePre = SharePre.getInstance(this);
		InitGame();
		StartGame();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		System.out.println("-----------hello");
		if (myStar.size() > 0) {
			dbM.deleteAll(DbManager.mygame.TABLE_NAME);
			for (int i = 0; i < myStar.size(); i++) {
				Star bean = myStar.get(i);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DbManager.mygame.COLUMN_ID, i + "");
				map.put(DbManager.mygame.COLUMN_X, bean.getX() + "");
				map.put(DbManager.mygame.COLUMN_Y, bean.getY() + "");
				map.put(DbManager.mygame.COLUMN_C, bean.getColor() + "");
				dbM.insert(DbManager.mygame.TABLE_NAME,
						DbManager.mygame.columns, map);

			}

		}
		sharePre.setInt("level", myLevel);
		sharePre.setInt("point", myPoint);
		super.onPause();
	}

	public int getTarget(int level) {
		int target = 0;
		for (int i = 1; i <= level; i++) {
			target +=( i / 5)*500 + 1000;
		}
		return target;
	}

	public void InitGame() {
		Ttarget = (TextView) findViewById(R.id.target);
		Tpoint = (TextView) findViewById(R.id.point);
		Tlevel = (TextView) findViewById(R.id.level);
		Tscore = (TextView) findViewById(R.id.score);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int widthPixels = dm.widthPixels;
		float density = dm.density;
		swidth = widthPixels * density;
		WindowManager wm = this.getWindowManager();
		swidth = wm.getDefaultDisplay().getWidth();
		imageview = (ImageView) findViewById(R.id.imageview);
		r = this.getResources();
		bitmap1 = Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.star1),
				30, 31, true);
		bitmap2 = Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.star2),
				30, 31, true);
		bitmap3 = Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.star3),
				30, 31, true);
		bitmap4 = Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.star4),
				30, 31, true);
		bitmap5 = Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.star5),
				30, 31, true);
		dbM = new DbManager(this);
		myStar = new ArrayList<Star>();
		easy = sharePre.getInt("easy", 4);

	}

	public void StartGame() {
		if (sharePre.getInt("level", 0) == 0) {
			myPoint = 0;
			myLevel = 1;
			myTarget = getTarget(myLevel);
			myRefresh();
			StartNewLevel();
		} else {
			LoadGame();
		}

	}

	public void LoadGame() {
		myLevel = sharePre.getInt("level", 1);
		myPoint = sharePre.getInt("point", 0);
		myTarget = getTarget(myLevel);
		myRefresh();
		ArrayList<HashMap<String, String>> datasLst = dbM.query(
				DbManager.mygame.TABLE_NAME, DbManager.mygame.columns);
		for (int i = 0; i < datasLst.size(); i++) {
			HashMap<String, String> map = datasLst.get(i);
			Star temp = new Star();
			temp.setX(Integer.parseInt(map.get(DbManager.mygame.COLUMN_X)));
			temp.setY(Integer.parseInt(map.get(DbManager.mygame.COLUMN_Y)));
			temp.setColor(Integer.parseInt(map.get(DbManager.mygame.COLUMN_C)));
			myStar.add(temp);
		}
		paintScreen();
	}

	public void myRefresh() {
		Tpoint.setText(myPoint + "");
		Tlevel.setText(myLevel + "");
		Ttarget.setText(myTarget + "");
		if (myCount != 0)
			Tscore.setText(myCount + "连消" + myCount * myCount * 5 + "分");
	}

	public boolean isLevelOver() {
		// myRefresh();
		for (int i = 0; i < myStar.size(); i++) {
			List<Star> res = findPopStar(myStar.get(i).getX(), myStar.get(i)
					.getY());
			if (res.size() >= 2) {
				return false;
			}
		}
		return true;
	}

	public void StartNewLevel() {
		myRefresh();
		myStar = new ArrayList<Star>();
		for (int i = 1; i <= 10; i++) {
			for (int j = 1; j <= 10; j++) {
				Star star = new Star((int) (Math.random() * easy) + 1, i, j);
				myStar.add(star);
			}
		}

		paintScreen();
	}

	public boolean onTouchEvent(MotionEvent event) {
		// 如果是按下操作
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// System.out.println("" + event.getX() + event.getY());
			int x = (int) (event.getX() / 72 + 1);
			int y = (int) ((event.getY() - 426) / 72 + 1);
			// System.out.println(x + "  " + y);
			Star star = findStar(x, y);
			if (star != null) {
				Star star1 = findStar(x - 1, y);
				if (star1 != null && star1.getColor() == star.getColor()) {
					popStar(x, y);
					return true;
				}
				star1 = findStar(x + 1, y);
				if (star1 != null && star1.getColor() == star.getColor()) {
					popStar(x, y);
					return true;
				}
				star1 = findStar(x, y - 1);
				if (star1 != null && star1.getColor() == star.getColor()) {
					popStar(x, y);
					return true;
				}
				star1 = findStar(x, y + 1);
				if (star1 != null && star1.getColor() == star.getColor()) {
					popStar(x, y);
					return true;
				}
			}

		}
		return super.onTouchEvent(event);
	}

	public void popStar(final int x, final int y) {
		List<Star> res = findPopStar(x, y);
		myCount = res.size();
		myPoint = myPoint + myCount * myCount * 5;
		myRefresh();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.what = 2;
				myHandler.sendMessage(msg);
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Star star = findStar(x, y);
				List<Star> res = findPopStar(x, y);
				for (int i = 0; i < res.size(); i++) {
					myStar.remove(res.get(i));
					Message msg = new Message();
					msg.what = 1;
					myHandler.sendMessage(msg);
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (int i = 0; i < res.size(); i++) {
					Star rstar = res.get(i);
					// myStar.remove(rstar);
					for (int j = 10; j >= 1; j--) {
						if (findStar(rstar.getX(), j) == null) {
							for (int n = j - 1; n >= 1; n--) {
								Star cstar = findStar(rstar.getX(), n);
								if (cstar != null) {
									cstar.setY(n + 1);
								}
							}
							Message msg1 = new Message();
							msg1.what = 1;
							myHandler.sendMessage(msg1);
							try {
								Thread.sleep(3);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				}
				for (int i = 10; i >= 1; i--) {
					// Star rstar = res.get(i);
					if (!isHaveStar(i)) {
						for (int k = i + 1; k <= 10; k++) {
							for (int n = 1; n <= 10; n++) {
								Star cstar = findStar(k, n);
								if (cstar != null) {
									cstar.setX(k - 1);
								}
							}
						}
						Message msg1 = new Message();
						msg1.what = 1;
						myHandler.sendMessage(msg1);
						try {
							Thread.sleep(3);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
				if (isLevelOver()) {
					Message msg1 = new Message();
					msg1.what = 3;
					myHandler.sendMessage(msg1);
				}
			}
		}).start();

	}

	public boolean isHaveStar(int x) {
		for (int i = 10; i >= 1; i--) {
			if (findStar(x, i) != null) {
				return true;
			}
		}
		return false;
	}

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				paintScreen();
			} else if (msg.what == 2) {
				Tscore.setText("");
			} else if (msg.what == 3) {

				if (myPoint < myTarget) {
					finish();
				} else {
					myLevel++;
					myTarget =getTarget(myLevel);
					myCount = 0;
					StartNewLevel();
				}
			}
			super.handleMessage(msg);
		}
	};

	public List<Star> findPopStar(int X, int Y) {
		List<Star> res = new ArrayList<Star>();
		res.add(findStar(X, Y));
		int pos = 0;
		while (pos < res.size()) {
			Star star = res.get(pos);
			int x = star.getX();
			int y = star.getY();
			Star star1 = findStar(x - 1, y);
			if (star1 != null && star1.getColor() == star.getColor()
					&& !res.contains(star1)) {
				res.add(star1);
			}
			star1 = findStar(x + 1, y);
			if (star1 != null && star1.getColor() == star.getColor()
					&& !res.contains(star1)) {
				res.add(star1);
			}
			star1 = findStar(x, y - 1);
			if (star1 != null && star1.getColor() == star.getColor()
					&& !res.contains(star1)) {
				res.add(star1);
			}
			star1 = findStar(x, y + 1);
			if (star1 != null && star1.getColor() == star.getColor()
					&& !res.contains(star1)) {
				res.add(star1);
			}
			pos++;
		}
		return res;

	}

	public Star findStar(int x, int y) {
		for (int i = 0; i < myStar.size(); i++) {
			if (myStar.get(i).getX() == x && myStar.get(i).getY() == y) {
				return myStar.get(i);
			}
		}
		return null;
	}

	public void paintScreen() {
		Bitmap newb = Bitmap.createBitmap(300, 300, Config.ARGB_8888);

		Canvas canvasTemp = new Canvas(newb);
		canvasTemp.drawColor(Color.TRANSPARENT);

		Paint p = new Paint();
		// canvasTemp.drawBitmap(bitmap2, 0, 0, p); // 画图
		for (int i = 0; i < myStar.size(); i++) {
			if (myStar.get(i).getColor() == 1) {
				canvasTemp.drawBitmap(bitmap1, myStar.get(i).getX() * 30 - 30,
						myStar.get(i).getY() * 30 - 30, p);
			} else if (myStar.get(i).getColor() == 2) {
				canvasTemp.drawBitmap(bitmap2, myStar.get(i).getX() * 30 - 30,
						myStar.get(i).getY() * 30 - 30, p);
			} else if (myStar.get(i).getColor() == 3) {
				canvasTemp.drawBitmap(bitmap3, myStar.get(i).getX() * 30 - 30,
						myStar.get(i).getY() * 30 - 30, p);
			} else if (myStar.get(i).getColor() == 4) {
				canvasTemp.drawBitmap(bitmap4, myStar.get(i).getX() * 30 - 30,
						myStar.get(i).getY() * 30 - 30, p);
			} else if (myStar.get(i).getColor() == 5) {
				canvasTemp.drawBitmap(bitmap5, myStar.get(i).getX() * 30 - 30,
						myStar.get(i).getY() * 30 - 30, p);
			}

		}
		imageview.setImageBitmap(newb);
	}

	public class Star {
		private int color;
		private int x;
		private int y;

		public Star(int C, int X, int Y) {
			color = C;
			x = X;
			y = Y;
		}

		public Star() {
		}

		public int getColor() {
			return color;
		}

		public void setColor(int color) {
			this.color = color;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public int onclick() {

			return 0;
		}

	}

}
