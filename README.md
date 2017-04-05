# 1 代码规范
## 1.1     Class 命名
 任何类名都应该使用 UpperCamelCase （驼峰法）命名, 例如:

      AndroidActivity, NetworkHelper, UserFragment, PerActivity

## 1.2 变量名。

 -  变量名：静态常量使用大写字母，使用下划线“_”分词。
 -  非静态成员变量、局部变量、首字母小写，驼峰式分词。
 -  Activity、Fragment、Adapter、View 的子类的成员变量：m开头、驼峰式分词。
 -  方法名：首字母小写，驼峰式分词。

# 1.3 成员变量定义顺序（建议）

- 公用静态常量
- 公用静态变量
- 私用静态常量
- 私用静态变量
- 私有非静态变量

## 1.4 禁止”魔术数”：

    switch(i) {
      case 1:
        mManager.updloadTag();
      break;
        mManager.uploadTabMore();
      case 2:
        // do something
      break;
       // ...
      case 3:
       // ....
      break;
    }

代码中的 case 后的数字即为魔术数，应该使用有明示意义的常量代替。

## 1.5 禁止忽略异常：

  public void setUserId(String id) {
      try {
          mUserId = Integer.parseInt(id);
      } catch (NumberFormatException e) { }
  }


## 1.6 避免UI线程IO：

 不要在主线程执行IO或网络的操作 ( 卡顿工具检查主线程的方法时间设置阀值，超过用子线程去执行 )。

# 1.7 非静态匿名内部类请用WeakReference 方式持有外部对象的引用

    Runnable timeCallback = new Runnable () {
      if(mRef ! = null) {
        Activity activity = mRef.get();
        if(activity != null) {
         // do stuff
        }
      }

    }


# 1.8 长度限制

-  一行代码的长度：不要超过160个字符。
-  一个方法的长度：不要超过：80行。
- 一个文件的长度不超过：1000行。
- 一个方法的参数列表不要超过：7个。
- if 嵌套层次：不要超过4层。
- 无用代码
- 禁止无用的import。
- 禁止import ＊。
- 禁止无用变量。

# 2  Resource 命名

## 2.1 Activity、Fragment、Dialog的布局命名：activity/fragment/dialog+模块，小写字母使用下划线 ”_ ”分词。
 例如：

     activity_main, fragment_user,dialog_login_input.xml


## 2.2 控件布局命名：模块名＋布局类型，小写字母使用下划线 ”_ ”分词。例如：goods_list_item。

## 2.3 图片资源

不再使用的布局资源及时删除。

## 2.2 图片

图片以ic_为前缀，与功能、状态一起命名。例如：

    ic_accept

其他 drawable 文件应该使用相应的前缀，例如：

|类型    |	前缀	    |例如   |
|----- |---- |---- |
|Selector	|selector_	|selector_button_cancel|
|Background	|bg_	|bg_rounded_button|
|Circle	|circle_	|circle_white|
|Progress	|progress_	|progress_circle_purple|
|Divider	|divider_	|divider_grey|

## 2.3 字符串命名

相同英文含义，小写字母使用下划线 ”_ ”分词。

## 2.4 其他
避免使用"px"作为单位。