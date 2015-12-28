package com.devin.bangsheng.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.devin.bangsheng.R;
import com.devin.bangsheng.common.Const;
import com.zxing.camera.PlanarYUVLuminanceSource;

public class MapNavigationActivity extends BaseActivity{
	
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	
	boolean isFirstLoc = true;// 是否首次定位
	private String mDesAddr = "";	//送达地
	private LatLng mCurrPosition;
	
	OverlayManager routeOverlay = null;
	int nodeIndex = -1;//节点索引,供浏览节点时使用
	RouteLine route = null;
    RoutePlanSearch mRoutePlanSearch = null;    // 搜索模块，也可去掉地图模块独立使用
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //注意该方法要再setContentView方法之前实现  
        setContentView(R.layout.activity_map);  
        initTopbar("地图导航");
        mDesAddr = getIntent().getStringExtra(Const.ExtraConst.ADDRESS);
        initViews();
	}
	
	private void initViews() {
		//获取地图控件引用  
        mMapView = (MapView) findViewById(R.id.bmapView); 
        mBaiduMap = mMapView.getMap();
        mCurrentMode = LocationMode.NORMAL;
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
						mCurrentMode, true, mCurrentMarker));
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		// 初始化搜索模块，注册事件监听
//		mSearch = GeoCoder.newInstance();
//		mSearch.setOnGetGeoCodeResultListener(new MyOnGetGeoCoderResultListener());
//		mSearch.geocode(new GeoCodeOption().city("南京").address(mDesAddr));
		// 初始化路线规划搜索模块，注册事件监听
		mRoutePlanSearch = RoutePlanSearch.newInstance();
		mRoutePlanSearch.setOnGetRoutePlanResultListener(new MyOnGetRoutePlanResultListener());
		
	}
	
	/**
     * 发起路线规划搜索示例
     *
     * @param v
     */
    public void planRoute(PlanNode stNode, PlanNode enNode) {
        //重置浏览节点的路线数据
        route = null;
        mBaiduMap.clear();
        // 实际使用中请对起点终点城市进行正确的设定
        mRoutePlanSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(stNode)
                .to(enNode));
    }

	@Override  
    protected void onDestroy() {  
		// 退出时销毁定位
		mLocClient.stop();
		mRoutePlanSearch.destroy();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
        super.onDestroy();  
    }  
    @Override  
    protected void onResume() {  
        super.onResume();  
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mMapView.onResume();  
    }  
    @Override  
    protected void onPause() {  
        super.onPause();  
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mMapView.onPause();  
    }  
	
	@Override
	public void onResponse(Message msg) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				mCurrPosition = new LatLng(location.getLatitude(),
						location.getLongitude());
//				MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(mCurrPosition, 16);
//				mBaiduMap.animateMapStatus(u);
				PlanNode stNode = PlanNode.withLocation(mCurrPosition);
				PlanNode enNode = PlanNode.withCityNameAndPlaceName("南京", mDesAddr);
				planRoute(stNode, enNode);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	class MyOnGetGeoCoderResultListener implements OnGetGeoCoderResultListener {

		@Override
		public void onGetGeoCodeResult(GeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(MapNavigationActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
						.show();
				return;
			}
			mBaiduMap.clear();
			mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)));
			List<LatLng> points = new ArrayList<LatLng>();
			points.add(mCurrPosition);
			points.add(result.getLocation());
			mBaiduMap.addOverlay(new PolylineOptions()
					.width(10)
					.color(0xAAFF0000)
					.points(points));
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
					.getLocation()));
			String strInfo = String.format("纬度：%f 经度：%f",
					result.getLocation().latitude, result.getLocation().longitude);
//			Toast.makeText(MapNavigationActivity.this, strInfo, Toast.LENGTH_LONG).show();
		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(MapNavigationActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
						.show();
				return;
			}
			mBaiduMap.clear();
			mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.icon_marka)));
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
					.getLocation()));
			Toast.makeText(MapNavigationActivity.this, result.getAddress(),
					Toast.LENGTH_LONG).show();

		}
	}
	
	class MyOnGetRoutePlanResultListener implements OnGetRoutePlanResultListener {

		@Override
		public void onGetDrivingRouteResult(DrivingRouteResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetTransitRouteResult(TransitRouteResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
	    public void onGetWalkingRouteResult(WalkingRouteResult result) {
	        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
	            Toast.makeText(MapNavigationActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
	        }
	        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
	            //result.getSuggestAddrInfo()
	            return;
	        }
	        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	            nodeIndex = -1;
	            route = result.getRouteLines().get(0);
	            WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
	            mBaiduMap.setOnMarkerClickListener(overlay);
	            routeOverlay = overlay;
	            overlay.setData(result.getRouteLines().get(0));
	            overlay.addToMap();
	            overlay.zoomToSpan();
	        }

	    }
		
	}
}
