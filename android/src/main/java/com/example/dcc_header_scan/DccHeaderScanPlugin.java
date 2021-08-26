package com.example.dcc_header_scan;

import androidx.annotation.NonNull;
import android.content.BroadcastReceiver;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.EventChannel;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;



/** DccHeaderScanPlugin */
public class DccHeaderScanPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private EventChannel eventChannel;
  private Context applicationContext;
  // private static final String ACTION_DATA_CODE_RECEIVED = "com.yt.action.scan";
  private static  String DATA = "text";
  private static  String ACTION_DATA_CODE_RECEIVED = "";
  private static final String CHARGING_CHANNEL = "header_can";

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "dcc_header_scan");
    channel.setMethodCallHandler(this);

      eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), CHARGING_CHANNEL);
      applicationContext = flutterPluginBinding.getApplicationContext();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("initConfig")) {
          if (null != call.arguments){
            // "Java收到：" + call.arguments.toString()
             ACTION_DATA_CODE_RECEIVED = call.arguments.toString();
             initReadInfo();
          }
    }if(call.method.equals("initDataKey")){
        if (null != call.arguments){
               DATA = call.arguments.toString();
          }
    }else if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }



 private void initReadInfo(){
        //  DATA = dataKey;
        eventChannel.setStreamHandler(new EventChannel.StreamHandler() {
            private BroadcastReceiver chargingStateChangeReceiver;
            @Override
            public void onListen(Object arguments, EventChannel.EventSink events) {
                chargingStateChangeReceiver = createChargingStateChangeReceiver(events);
                IntentFilter filter = new IntentFilter();
                filter.addAction(ACTION_DATA_CODE_RECEIVED);
                applicationContext.registerReceiver(
                        chargingStateChangeReceiver, filter);
            }

            @Override
            public void onCancel(Object arguments) {
                applicationContext.unregisterReceiver(chargingStateChangeReceiver);
                chargingStateChangeReceiver = null;
            }
        });
 }


 private BroadcastReceiver createChargingStateChangeReceiver(final EventChannel.EventSink events) {
    return new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        String result = intent.getStringExtra(DATA);
        if (result != null && !result.isEmpty()) {
          events.success(result);
        } else {
          events.error("10000", "扫描超时", "请重新扫描");
        }
      }
    };
  }

}
