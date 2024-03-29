import 'dart:html_common';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:dcc_header_scan/dcc_header_scan.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  // static const EventChannel _eventChannel = EventChannel('header_can');

  @override
  void initState() {
    super.initState();
    initPlatformState();
                 DccHeaderScan.readInfo(
                    acctionCodeReceived:"com.yt.action.scan",
                    dataKey:"text"
                  ).listen((value) {
                    print("获取到扫描头数据>>>>>>>>>>$value");
                });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await DccHeaderScan.platformVersion ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {

    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body:GestureDetector(
          child:  Center(
          child: Container(
            child:Text('Running on: $_platformVersion\n'),
          )
        ),
        ),
      ),
    );
  }
}
