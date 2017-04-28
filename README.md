# CollapsingImgText
use in AppBarLayout, Supports picture and text offset, zoom and fixed in title. it's a RelativeLayout

# example
![image](https://github.com/hu5080126/CollapsingImgText/blob/master/readmeImg/collapsing.gif)

# Installation
If you are using Gradle and the JCenter Maven Repository, installing the library is as simple as adding a new dependency statement.

```
compile 'com.yahui:collapsingImageText:0.1.2'
```

# usage
The library use in AppBarLayout, extends RelativeLayout 
```
<android.support.design.widget.AppBarLayout
        android:id="@+id/app_bar"
        android:layout_width="match_parent"
        android:layout_height="@dimen/app_bar_height"
        android:theme="@style/AppTheme.AppBarOverlay">

        <com.yahui.collapsingimagetext.CollapsingImageTextLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_scrollFlags="scroll|exitUntilCollapsed"
            app:title_id="@+id/test_title"
            app:text_id="@+id/test_text"
            app:text_scale="0.6"
            app:text_margin_left="50dp"
            app:img_id="@+id/test_img"
            app:img_margin_left="10dp"
            >
            <TextView
                android:id="@+id/test_title"
                android:layout_width="match_parent"
                android:layout_height="45dp"
                android:background="@android:color/holo_blue_light"
                android:gravity="center"
                android:text="title is here"
                />

            <ImageView
                android:id="@+id/test_img"
                android:layout_width="60dp"
                android:layout_height="60dp"
                android:scaleType="fitXY"
                android:src="@mipmap/ic_launcher"
                android:layout_centerVertical="true"
                android:layout_marginTop="30dp"
                android:layout_marginLeft="20dp"
                />

            <TextView
                android:id="@+id/test_text"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_toRightOf="@+id/test_img"
                android:text="MoveText"
                android:textSize="20sp"
                android:layout_marginTop="30dp"
                android:layout_marginLeft="30dp"
                android:layout_centerVertical="true"
                />

        </com.yahui.collapsingimagetext.CollapsingImageTextLayout>
       
    </android.support.design.widget.AppBarLayout>
```
### Xml Attributes
```
    app:title_id="@+id/test_title"  //provide a title that is fixed at the top
    app:text_id="@+id/test_text"    //provide a text view that is offset and zoom
    app:text_scale="0.6"            //The final size of the text
    app:text_margin_left="50dp"     //The text view final moving to the left margin of title
    app:text_margin_top="10dp"      //The text view final moving to the top margin of title
    app:img_id="@+id/test_img"      //provide a img view that is offset and zoom
    app:img_scale="0.6"             //The final size of the img
    app:img_margin_left="10dp"      //The text view final moving to the left margin of img
    app:img_margin_top="10dp"       //The text view final moving to the top margin of img
```
if you set  android:fitsSystemWindows="true", you may be need invoke following method 
```
    setTextTitleMarginTop(int top);
    setImgTitleMarginTop(int top);
```
set  (stateBar height + TitleMarginTop) for img and text

# Implementation details
Most of the code is copied from the original Support Library classes, it only add support two view move and zoom.
