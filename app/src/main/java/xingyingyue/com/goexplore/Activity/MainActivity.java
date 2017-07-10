package xingyingyue.com.goexplore.Activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import java.util.ArrayList;
import java.util.List;

import xingyingyue.com.goexplore.Bean.Achievement;
import xingyingyue.com.goexplore.Bean.Pass;
import xingyingyue.com.goexplore.Bean.User;
import xingyingyue.com.goexplore.Dao.AchievementDao;
import xingyingyue.com.goexplore.Dao.PassDao;
import xingyingyue.com.goexplore.Dao.UserDao;
import xingyingyue.com.goexplore.R;
import xingyingyue.com.goexplore.Util.SensorEventHelper;
import xingyingyue.com.goexplore.View.MyToast;

/**
 * Created by huanghaojian on 17/6/15.
 */

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,LocationSource,AMapLocationListener,AMap.OnMarkerClickListener{
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private LinearLayout roleInfo;
    private TextView roleName;
    private TextView level;
    private TextView needExp;
    private NavigationView navigationView;

    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private TextView mLocationErrText;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private boolean mFirstFix = false;
    private Marker mLocMarker;
    private SensorEventHelper mSensorHelper;
    private Circle mCircle;
    public static final String LOCATION_MARKER_FLAG = "mylocation";

    private PassDao passDao=new PassDao();
    private List<Marker> markerList=new ArrayList<>();
    private List<Marker> randomMarkerList=new ArrayList<>();
    private String userAccount;
    private String password;

    private GeoFenceClient geoFenceClient;
    public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";
    public static final String MODIFYROLE_BROADCAST_ACTION="xingyingyue.modifyRoleInfoback.BROADCAST";
    private IntentFilter intentFilter;
    private IntentFilter intentFilter1;
    private ModifyRoleInfoBackBroadcastReceiver modifyRoleInfoBackBroadcastReceiver;
    private FenceBroadcastReceiver fenceBroadcastReceiver;
    private String customId;
    private UserDao userDao=new UserDao();
    private AchievementDao achievementDao=new AchievementDao();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        setContentView(R.layout.layout_main);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        //获取从登录界面传来的账号密码
        Intent intent=getIntent();
        if (intent.getExtras().getString("from_receiver")==null) {
            userAccount = intent.getStringExtra("userAccount");
            password = intent.getStringExtra("password");
        }
        loadData();
        initMap(savedInstanceState);
        initView();

        //创建围栏
        createFence(this);
         //设置围栏创建的回调监听
        geoFenceClient.setGeoFenceListener(new GeoFenceListener() {
            @Override
            public void onGeoFenceCreateFinished(List<GeoFence> list, int errorCode, String s) {
                if(errorCode == GeoFence.ADDGEOFENCE_SUCCESS){//判断围栏是否创建成功
                    //Toast.makeText(MainActivity.this,"添加围栏成功",Toast.LENGTH_LONG).show();
                    //geoFenceList就是已经添加的围栏列表，可据此查看创建的围栏
                } else {
                    //geoFenceList就是已经添加的围栏列表
                   // Toast.makeText(MainActivity.this,"添加围栏失败",Toast.LENGTH_LONG).show();
                }
            }
        });
        //创建并设置PendingIntent
        geoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
        //动态注册广播接收器
        intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(GEOFENCE_BROADCAST_ACTION);
        fenceBroadcastReceiver=new FenceBroadcastReceiver();
        registerReceiver(fenceBroadcastReceiver,intentFilter);

        intentFilter1=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter1.addAction(MODIFYROLE_BROADCAST_ACTION);
        modifyRoleInfoBackBroadcastReceiver=new ModifyRoleInfoBackBroadcastReceiver();
        registerReceiver(modifyRoleInfoBackBroadcastReceiver,intentFilter1);

    }
    private void initMap(Bundle savedInstanceState){
        mapView=(MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);//创建地图
        if (aMap == null) {
            aMap = mapView.getMap();
        }

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
       // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.moveCamera(CameraUpdateFactory.zoomTo(20));
        aMap.setOnMarkerClickListener(this);

        markerList=passDao.showMarker(aMap,this);

        randomMarkerList=passDao.generatePass(aMap,this);

    }
    private void initView(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.main_drawerLayout);
        navigationView=(NavigationView)findViewById(R.id.main_menu_left);
        roleName=(TextView)findViewById(R.id.main_user_nickname);
        level=(TextView)findViewById(R.id.main_user_level);
        needExp=(TextView)findViewById(R.id.main_experience_need);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_personal_info_setting);
        navigationView.setNavigationItemSelectedListener(this);

        showRoleInfo();
    }
    private void loadData(){
        List<User>userList=userDao.queryRoleInfoByUserAccount(MainActivity.this,userAccount);
        if (userList.size()<0||userList.size()==0){
            userDao.loadRoleInfo(MainActivity.this,userAccount);
        }
        List<Achievement>achievementList=achievementDao.getAllAchievement(MainActivity.this);
        if (achievementList.size()<0||achievementList.size()==0){
            achievementDao.loadAchievement(MainActivity.this);
        }
        //achievementDao.updateAchievement(MainActivity.this,userAccount);
    }
    private void showRoleInfo(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<User>userList=userDao.queryRoleInfoByUserAccount(MainActivity.this,userAccount);
                if (userList.size()>0){
                    User user=userList.get(0);
                    roleName.setText(user.getUserName());
                    level.setText("lv."+user.getLevel());
                    int exp=100-user.getExperience();
                    needExp.setText("升级所需经验值："+exp);
                }else{
                    roleName.setText("暂无昵称");
                    level.setText("lv."+1);
                    needExp.setText("升级所需经验值："+100);
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_personal_info_setting:
                ModifyAccountInfoActivity.actionStart(MainActivity.this,userAccount,password);
                break;
            case R.id.nav_role_setting:
                ModifyRoleInfoActivity.actionStart(MainActivity.this,userAccount,password);
                break;
            case R.id.nav_publish_record:
                ShareContentListActivity.actionStart(MainActivity.this);
                break;
            case R.id.nav_achievement:
                AchievementListActivity.actionStart(MainActivity.this);
                break;
            default:
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //marker的点击事件
    @Override
    public boolean onMarkerClick(Marker marker){
        LatLng latLng=marker.getPosition();
        PassInfomationActivity.actionStart(MainActivity.this,latLng.latitude,latLng.longitude);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.edit_content:
                EditContentActivity.actionStart(MainActivity.this,userAccount,password);
                break;
            default:
        }
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if (mLocMarker != null) {
            mLocMarker.destroy();
        }
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        unregisterReceiver(fenceBroadcastReceiver);
        unregisterReceiver(modifyRoleInfoBackBroadcastReceiver);
        geoFenceClient.removeGeoFence();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
        deactivate();
        mFirstFix = false;

    }
    @Override
    protected void onStop(){
        super.onStop();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }
    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {

                mLocationErrText.setVisibility(View.GONE);
                LatLng location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());

                if (!mFirstFix) {
                    mFirstFix = true;
                    addCircle(location, amapLocation.getAccuracy());//添加定位精度圆
                    addMarker(location);//添加定位图标
                    mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,18));
                } else {
                    mCircle.setCenter(location);
                    mCircle.setRadius(amapLocation.getAccuracy());
                    mLocMarker.setPosition(location);
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(location));
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                mLocationErrText.setVisibility(View.VISIBLE);
                mLocationErrText.setText(errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
    //给节点创建地理围栏
    public void createFence(Context context){
        geoFenceClient=new GeoFenceClient(context);
        geoFenceClient.setActivateAction(GeoFenceClient.GEOFENCE_IN|GeoFenceClient.GEOFENCE_STAYED);
        //给关节点创建地理围栏
        List<Pass> passList=passDao.queryPassList(context);
        for (int i=0;i<passList.size();i++){
            Pass pass=passList.get(i);
            DPoint centerPoint=new DPoint();
            centerPoint.setLongitude(pass.getLongitude());
            centerPoint.setLatitude(pass.getLatitude());
            geoFenceClient.addGeoFence(centerPoint,100F,pass.getPassId());
        }
        //给随机百宝箱创建地理围栏
        for(int i=0;i<randomMarkerList.size();i++){
            Marker marker=randomMarkerList.get(i);
            LatLng latLng=marker.getPosition();
            DPoint centerPoint=new DPoint();
            centerPoint.setLongitude(latLng.longitude);
            centerPoint.setLatitude(latLng.latitude);
            geoFenceClient.addGeoFence(centerPoint,50F,Integer.toString(i));
        }
    }

    //到达关节点或者随机百宝箱的所在地后进行的操作
    public void operationAfterArrivePass(final String customId){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Pass>passList=passDao.queryPassById(MainActivity.this,customId);
                if(passList.size()>0){
                    Pass pass=passList.get(0);
                    if(pass.getState()==0){
                        passDao.updatePassInfo(MainActivity.this,customId);
                        userDao.getExperience(MainActivity.this,userAccount);
                        achievementDao.updateAchievement(MainActivity.this,userAccount);
                        List<User>userList=userDao.queryRoleInfoByUserAccount(MainActivity.this,userAccount);
                        User user=userList.get(0);
                        level.setText("lv."+user.getLevel());
                        int exp=100-user.getExperience();
                        needExp.setText("升级所需经验值："+exp);
                        MyToast.makeText(MainActivity.this,"已点亮"+pass.getAddress(),Toast.LENGTH_LONG).show();
                    }
                }else{
                    userDao.getExperience(MainActivity.this,userAccount);
                    achievementDao.updateAchievement(MainActivity.this,userAccount);
                    List<User>userList=userDao.queryRoleInfoByUserAccount(MainActivity.this,userAccount);
                    User user=userList.get(0);
                    level.setText("lv."+user.getLevel());
                    int exp=100-user.getExperience();
                    needExp.setText("升级所需经验值："+exp);
                    Marker marker=randomMarkerList.get(Integer.parseInt(customId));
                    marker.remove();
                    randomMarkerList.remove(Integer.parseInt(customId));
                    MyToast.makeText(MainActivity.this,"找到百宝箱一个，获得经验值10",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //角色信息修改后对主界面的角色信息同步
    public void modifyRoleInfo(final String userName){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roleName.setText(userName);
            }
        });
    }
    private void addCircle(LatLng latlng, double radius) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latlng);
        options.radius(radius);
        mCircle = aMap.addCircle(options);
    }

    private void addMarker(LatLng latlng) {
        if (mLocMarker != null) {
            return;
        }
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.navi_map_gps_locked)));
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);
        mLocMarker.setTitle(LOCATION_MARKER_FLAG);
    }
    //接收围栏触发事件广播的广播接收器
    private class FenceBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
                //解析广播内容
                //获取Bundle
                Bundle bundle = intent.getExtras();
                //获取围栏行为：
                int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                //获取自定义的围栏标识：
                customId = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                operationAfterArrivePass(customId);
                Log.e("customid",customId);
                //获取围栏ID:
                String fenceId = bundle.getString(GeoFence.BUNDLE_KEY_FENCEID);
                Log.e("fenceid",fenceId);
                //获取当前有触发的围栏对象：
                GeoFence fence = bundle.getParcelable(GeoFence.BUNDLE_KEY_FENCE);
            }
        }
    }
    //接收角色信息修改事件广播的广播接收器
    private class ModifyRoleInfoBackBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MODIFYROLE_BROADCAST_ACTION)) {
                String userName=intent.getStringExtra("userName");
                modifyRoleInfo(userName);
            }
        }
    }
    public static void actionStart(Context context,String userAccount,String password){
        Intent intent=new Intent(context,MainActivity.class);
        intent.putExtra("userAccount",userAccount);
        intent.putExtra("password",password);
        context.startActivity(intent);
    }
}
