package com.NavigationBarSwitch;

import android.app.*;
import android.os.*;
import java.io.*;
import android.content.*;
import android.content.res.*;
import java.lang.reflect.*;
import android.view.*;
import android.widget.*;
import com.NavigationBarSwitch.shell;



public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
 		init();
		

	 }
	
		
	 
	//调用按钮方法
	
	public void on(View view){
		//开启按钮
		String oncmds[] = {"mount -o rw,remount /system","sed -i '/qemu/d' /system/build.prop","echo qemu.hw.mainkeys=0 >> /system/build.prop"};
		new shell().execCommand(oncmds,true);
		setTitle("导航栏即将开启(重启后生效)");
		
	}
	public void off(View view){
		//关闭按钮
		String offcmds[] = {"mount -o rw,remount /system","sed -i '/qemu/d' /system/build.prop","echo qemu.hw.mainkeys=1 >> /system/build.prop"};
		new shell().execCommand(offcmds,true);
		setTitle("导航栏即将关闭(重启后生效)");
		
	}
	 
	 
	 
	 
	//菜单 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.key:

				new AlertDialog.Builder(this)
					.setTitle("警告")
					.setMessage("这个功能MIUI是自带的\n在通知栏开关里\n其他机型为实验性测试,所有后果自己承担\n测试机型列表:\n小米5X(MIUI9):测试中")
					.setPositiveButton("启用物理键", new DialogInterface.OnClickListener() { 
						@Override 
						public void onClick(DialogInterface dialog, int which) { 
							String cmd[]={"settings put global policy_control null*"};
							new shell().execCommand(cmd,true);
							setTitle("物理键即将启用(重启后生效)");
						
						
					
							} 
					}) 
					.setNeutralButton("禁用物理键", new DialogInterface.OnClickListener() { 
						@Override 
						public void onClick(DialogInterface dialog, int which) { 
				   
							String cmd[]={"settings put global policy_control immersive.navigation=*"};
						  new shell().execCommand(cmd,true);
							setTitle("物理键即将禁用(重启后生效)");
						
						
						} 
					}).show();		
				return true;	
			case R.id.about:
		
				new AlertDialog.Builder(this)
					.setTitle("关于")
					.setMessage("1、没root权限就卸载本软件吧\n      (官方root和没有解锁system分区都有可能导致失败)\n      厂家自带的导航栏别问我为什么关不了\n2、本软件原理是通过Shell命令来修改Build.prop信息\n      使隐藏的原生导航栏显示或关闭\n      厂家自带的导航栏不行\n3、添加通知栏磁贴(正在绘制磁贴图标,下个版本添加)")
					.show();
				return true;
				case R.id.reboot:
				
					
				new AlertDialog.Builder(this)
					.setTitle("警告")
					.setMessage("任何修改系统文件的操作都需要重启生效\n点击确认重启")
				
				.setPositiveButton("确定", new DialogInterface.OnClickListener() { 

						@Override 
						public void onClick(DialogInterface dialog, int which) { 
							// 点击“确认”后的操作 
							String cmd[] = {"reboot"};
							new shell().execCommand(cmd,true);
						} 
					}) 
					.setNegativeButton("返回", new DialogInterface.OnClickListener() { 

						@Override 
						public void onClick(DialogInterface dialog, int which) { 
							// 点击“返回”后的操作,这里不设置没有任何操作 
						} 
					}).show();
					
					
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	} 
	 
	 
	 
	 
	 
	 
	 
	//检测导航栏操作
	public static boolean checkDeviceHasNavigationBar(Context context)
	{
		boolean hasNavigationBar = false;
		Resources rs = context.getResources();
		int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
		if (id > 0)
		{
			hasNavigationBar = rs.getBoolean(id);
		}
		try
		{
			Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
			Method m = systemPropertiesClass.getMethod("get", String.class);
			String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
			if ("1".equals(navBarOverride))
			{
				hasNavigationBar = false;
			}
			else if ("0".equals(navBarOverride))
			{
				hasNavigationBar = true;
			}
		}
		catch (Exception e)
		{

		}
		return hasNavigationBar;
	}

	public void init(){
		//启动时方法
		if (checkDeviceHasNavigationBar(this) == true)
		{
			setTitle("导航栏当前状态:已开启");
		}
		else{
			setTitle("导航栏当前状态:未开启");
		}	



		try
		{
			Runtime.getRuntime().exec("su");
		}
		catch (IOException e)
		{}			
}
	


public void alert(String t){
	Toast.makeText(this,t, Toast.LENGTH_SHORT).show();
}
	
	
	
	
	
}
