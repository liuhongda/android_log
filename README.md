# android_log 具体用法卡查看LogUtil源码
<b>此类可以灵活的控制log输出,解决了因不同的人开发的时候打印不同的log造成程序log打印非常混乱,只需做一些简单的配置
 * 就可屏蔽log输出和打印自己想要的log输出</b>
 * <p><br>1.在配置文件中配置"类全名"=true/false;"包名"=true/false
 * <br>2.如果类配置和包配置有冲突就以类配置为准
 * <br>3.父包和子包配置有冲突就以子包为准</p>
 * <p>和assets文件夹中的log.properties文件配合控制log的打印输出
 * 在文件中配置类的全名和log打印模式(true表为debug模式,打印log;false表示非debug模式,即不打印log)
 * 在文件中配置类的log打印模式是永久生效的,也可直接调用debugAll()或者debug()方法来动态改变文件的配置
 * 不过调用这两个方法改变的打印模式只在内存中生效,不会写入配置文件中
 * addDebug()是动态添加新的类的打印模式,只在内存中生效,不写入配置文件</p>
在项目工程的assets目录下建立log.properties文件，文件中做如下配置
com.xtc.watch=true # app包名默认打印所有log
saveFile=true #log自动保存到文件中
com.xtc.srvWatchAccount=true # 指定包名打印日志
com.xtc.watch.service.weichat=false # 指定包名不打印日志
com.xtc.watch.MainActivity=true #指定类打印日志
v=false #v级别的日志是否保存到文件中
d=false #d级别的日志是否保存到文件中
i=false #i级别的日志是否保存到文件中
w=false #w级别的日志是否保存到文件中
e=true #e级别的日志是否保存到文件中
