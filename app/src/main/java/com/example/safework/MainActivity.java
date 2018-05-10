package com.example.safework;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.MessageAdapter;
import ConnectServe.ConnectServe;
import ConnectServe.WorkInfo;


public class MainActivity extends FragmentActivity implements FriendFragment.OnFragmentInteractionListener,RelationFragment.OnFragmentInteractionListener {
    //客服中心
    private ViewPager myViewPager;
    private List<View> list;
    private MessageAdapter messageAdapter;
    private TabLayout tab;
    //用于显示多条数据
    private LinearLayout mColumnsLinear;
    private LayoutInflater inflater;
    //创建线性布局
    private LinearLayout layout;
    //用户名
    private String username;
    private Fragment mCurrentFragment;
    private int REQUEST_CODE = 1;
    private ViewPager viewPager;
    private MenuItem menuItem;
    private List<View> mList;
    //上传头像所用变量
    private ImageView mImage;
    private Bitmap mBitmap;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    final List<WorkInfo> workInfos = new ArrayList<WorkInfo>();
                    String url = "http://10.0.2.2:8080/WorkInfo";
                    Handler mHandler = new Handler(){
                        JSONArray jsonArray;
                        @Override
                        public void handleMessage(Message msg) {//覆盖handleMessage方法
                            super.handleMessage(msg);
                            String data = msg.obj.toString();
                            try {
                                jsonArray = new JSONArray(data);
                                //取出对象里面的数据
                                for(int i=0;i<jsonArray.length();i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    workInfos.add(new WorkInfo(Integer.parseInt(jsonObject.getString("workID")), jsonObject.getString("workInfo"), Integer.parseInt(jsonObject.getString("salary")), jsonObject.getString("deliverDate"), jsonObject.getString("workDate"),Integer.parseInt(jsonObject.getString("companyID")),jsonObject.getString("address")));
                                }
                                //获取LayoutInflater对象
                                inflater = LayoutInflater.from(MainActivity.this);
                                //初始化组件
                                mColumnsLinear = (LinearLayout) findViewById(R.id.columns_linear);
                                //创建线性布局
                                LinearLayout layout = new LinearLayout(MainActivity.this);
                                //设置LayoutParams
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                //设置为水平布局
                                layout.setOrientation(LinearLayout.VERTICAL);
                                //为线性布局这只LayoutParams
                                layout.setLayoutParams(lp);
                                //循环添加
                                int j ;
                                for (  j=0;j < workInfos.size() ; j++) {
                                    //获取需要 添加的布局文件
                                    View view = inflater.inflate(R.layout.item_column, null, false);
                                    //获取组件
                                    TextView tv = (TextView) view.findViewById(R.id.text);
                                    //设值
                                    tv.setText(workInfos.get(j).getWorkInfo());

                                    TextView SalaryTv = (TextView) view.findViewById(R.id.Salary);
                                    SalaryTv.setText(workInfos.get(j).getSalary()+"元/时");
                                    //绑定点击事件
                                    final WorkInfo workInfo = workInfos.get(j);
                                    view.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(MainActivity.this, WorkInfoActivity.class);
                                            intent.putExtra("workinfo", workInfo);
                                            intent.putExtra("workinfos", (Serializable) workInfos);
                                            startActivity(intent);
                                            Log.e("s", "1");
                                        }
                                    });
                                    //添加到创建的线性布局中
                                    layout.addView(view);
                                }
                                //添加到显示的父线性布局中
                                mColumnsLinear.removeAllViews();
                                mColumnsLinear.addView(layout);
                            }catch (Exception e){
                                Log.e("s", "wwwwwww");
                            }
                            }
                    };
                    //获取工作信息
                    ConnectServe connectServe = new ConnectServe(url ,mHandler);
                    break;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    //初始化
                    EMOptions options = new EMOptions();
                     //默认添加好友时，是不需要验证的，改成需要验证
                    options.setAcceptInvitationAlways(false);
                    EaseUI.getInstance().init(getApplicationContext(), options);

                   //设置item点击事件
                    ViewPager mviewpager = (ViewPager)findViewById(R.id.pager);
                    tab = (TabLayout) findViewById(R.id.tab);
                    String[] arrTabTitles = new String[3];
                    arrTabTitles[0] = "会话";
                    arrTabTitles[1] = "通讯录";
                    arrTabTitles[2] = "设置";
                    RelationFragment relationFragment = new RelationFragment();
                    List<Fragment> fragments = new ArrayList<>();
                    //联系人对话框
                    EaseContactListFragment contactListFragment= new EaseContactListFragment();
                      //需要设置联系人列表才能启动fragment
                    contactListFragment.setContactsMap(getContacts());
                    //会话fragment
                    EaseConversationListFragment conversationListFragment = new EaseConversationListFragment();
                    fragments.add(contactListFragment);
                    fragments.add(conversationListFragment);
                    fragments.add(relationFragment);

                    mviewpager.setAdapter(new MessageAdapter(getSupportFragmentManager(), fragments, arrTabTitles));
                    tab.setupWithViewPager(mviewpager);





















                    break;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    initUI(); //调用上传头像的方法
                    initListeners();
                    TextView usernametext = (TextView) findViewById(R.id.username);
                    usernametext.setText(username);
                    Button deliveredbutton = (Button)findViewById(R.id.delivered);
                    Button checkedbuuton = (Button)findViewById(R.id.checked);
                    Button employedbutton = (Button)findViewById(R.id.employed);
                    Button refusedbutton = (Button)findViewById(R.id.refused);

                    deliveredbutton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            //从数据库获取已经投递了的工作简历
                            Intent intent = new Intent(MainActivity.this, ResumeActivity.class);
                            startActivity(intent);
                        }
                    });
                    checkedbuuton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ResumeActivity.class);
                            startActivity(intent);
                        }
                    });
                    employedbutton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ResumeActivity.class);
                            startActivity(intent);
                        }
                    });
                    refusedbutton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ResumeActivity.class);
                            startActivity(intent);
                        }
                    });
                    Log.v("wo hen shuai","wo hen shuai" );
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getIntent().getStringExtra("username");
        setContentView(R.layout.activity_main);
        final List<WorkInfo> workInfos = new ArrayList<WorkInfo>();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        LayoutInflater li = getLayoutInflater();
        mList = new ArrayList<View>();
        mList.add(li.inflate(R.layout.fragment_base,null,false));
        mList.add(li.inflate(R.layout.layout_one,null,false));
        mList.add(li.inflate(R.layout.layout_two,null,false));
        ViewPagerAdapter adapter = new ViewPagerAdapter(mList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        String url = "http://10.0.2.2:8080/WorkInfo";
        Handler mHandler = new Handler(){
            JSONArray jsonArray;
            @Override
            public void handleMessage(Message msg) {//覆盖handleMessage方法
                super.handleMessage(msg);
                String data = msg.obj.toString();
                try {
                    jsonArray = new JSONArray(data);
                    //取出对象里面的数据
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        workInfos.add(new WorkInfo(Integer.parseInt(jsonObject.getString("workID")), jsonObject.getString("workInfo"), Integer.parseInt(jsonObject.getString("salary")), jsonObject.getString("deliverDate"), jsonObject.getString("workDate"),Integer.parseInt(jsonObject.getString("companyID")),jsonObject.getString("address")));
                    }
                    //获取LayoutInflater对象
                    inflater = LayoutInflater.from(MainActivity.this);
                    //初始化组件
                    mColumnsLinear = (LinearLayout) findViewById(R.id.columns_linear);
                    //创建线性布局
                    LinearLayout layout = new LinearLayout(MainActivity.this);
                    //设置LayoutParams
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    //设置为水平布局
                    layout.setOrientation(LinearLayout.VERTICAL);
                    //为线性布局这只LayoutParams
                    layout.setLayoutParams(lp);
                    //循环添加
                    int j ;
                    for (  j=0;j < workInfos.size() ; j++) {
                        //获取需要 添加的布局文件
                        View view = inflater.inflate(R.layout.item_column, null, false);
                        //获取组件
                        TextView tv = (TextView) view.findViewById(R.id.text);
                        //设值
                        tv.setText(workInfos.get(j).getWorkInfo());

                        TextView SalaryTv = (TextView) view.findViewById(R.id.Salary);
                        SalaryTv.setText(workInfos.get(j).getSalary()+"元/时");
                        //绑定点击事件
                        final WorkInfo workInfo = workInfos.get(j);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MainActivity.this, WorkInfoActivity.class);
                                intent.putExtra("workinfo", workInfo);
                                intent.putExtra("workinfos", (Serializable) workInfos);
                                startActivity(intent);
                                Log.e("s", "1");
                            }
                        });
                        //添加到创建的线性布局中
                        layout.addView(view);
                    }
                    //添加到显示的父线性布局中
                    mColumnsLinear.addView(layout);
                }catch (Exception e){

                }
            }
        };
        //获取工作信息
        ConnectServe connectServe = new ConnectServe(url ,mHandler);
        //初次进行数据更新
        viewPager.setOffscreenPageLimit(2); //预加载剩余两页
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                menuItem = navigation.getMenu().getItem(position);
                menuItem.setChecked(true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
    }
    private void initUI() {
        mImage= (ImageView) findViewById(R.id.iv_image);
    }
    private void initListeners() {
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });
    }
    /**
     * 显示修改图片的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("添加图片");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            doTakePhotoIn7(new File(Environment.getExternalStorageDirectory(), "temp_image.jpg").getAbsolutePath());
                        } else {
                            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp_image.jpg"));
                            // 将拍照所得的相片保存到SD卡根目录
                            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                            startActivityForResult(openCameraIntent, TAKE_PICTURE);
                            Log.e("0","拍照");
                        }
                        break;
                }
            }
        });
        builder.show();
    }

    public void doTakePhotoIn7(String path) {
        Uri mCameraTempUri;
        try {
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            values.put(MediaStore.Images.Media.DATA, path);
            mCameraTempUri = mCurrentFragment.getActivity().getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePhoto(mCurrentFragment, TAKE_PICTURE, mCameraTempUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void takePhoto(Fragment fragment, int requestCode, Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (uri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        }
        fragment.startActivityForResult(intent, requestCode);
    }
    //裁剪后展示

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(tempUri); // 对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }
    /**
     * 裁剪图片方法实现
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            //这里图片是方形的，可以用一个工具类处理成圆形（很多头像都是圆形，这种工具类网上很多不再详述）
            mImage.setImageBitmap(mBitmap);//显示图片
            //在这个地方可以写上上传该图片到服务器的代码，后期将单独写一篇这方面的博客，敬请期待...
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    private Map<String, EaseUser> getContacts(){
        Map<String, EaseUser> contacts = new HashMap<String, EaseUser>();
        for(int i = 1; i <= 10; i++){
            EaseUser user = new EaseUser("easeuitest" + i);
            contacts.put("easeuitest" + i, user);
        }
        return contacts;
    }

}


