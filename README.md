###目标问题
* service容易被系统杀死,无法处理一些耗时操作

###解决策略
* 提高app进程优先级
* 开守护进程保护工作进程
* 注册系统广播唤起service

###具体实现
* 使用service的startForeground方法传递空的通知,使service成为仅次于显示进程的优先级(API 18以上,需使用innerService的方式,开启通知,并停止,否则通知栏会有通知)

* 使用1px的Activity,在锁屏的同时,发送广播,启动该Activity,防止手机锁屏被系统杀掉

* 开启一个守护进程,与工作进程相互绑定,在守护进程中,定时的去检查工作进程的状态,需要重启的时候启动工作进程

* 5.0+使用jobscheduler,让系统定时去启动工作进程,4.0+使用alarmManager的方式去定时检查

* 注册一系列的广播,当网络发生改变,其他app被移除或者安装时,主动去检查当前工作进程的状态

* Service的onStartCommand返回START_STICKY,让Sercice具有自动重启的能力
  
###使用方法
* 引导用户将app加入白名单         
	IntentWrapper.whiteListMatters(context, reason);

* 守护相关service  
			  Numen.getInstance().init(applicationContext,Service.class); 
	1.context 必须为applicationContext;  
	2.service 继承BaseWorkService  
	在重写的work方法中执行后台耗时操作
	
###建议
* 引导用户为最优先考虑的方式,其次才是保活的,由于Android ROM多种多样,当用户加入白名单之后,绝大多数ROM才会减少对app的限制,app才可以接受各种广播,jobscheduler才可以正常使用,保活的策略才可以尽可能的提高成功率
			  

	