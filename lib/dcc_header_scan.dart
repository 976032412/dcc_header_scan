import 'dart:async';

import 'package:flutter/services.dart';

class DccHeaderScan {
  static const MethodChannel _channel = const MethodChannel('dcc_header_scan');
  static const EventChannel _eventChannel = EventChannel('header_can');
  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Stream<dynamic> readInfo({String acctionCodeReceived="",String dataKey="text"}) {
    _channel.invokeMethod('initConfig', acctionCodeReceived);
    _channel.invokeMethod('initDataKey', dataKey);
    return _eventChannel.receiveBroadcastStream();
  }
}
