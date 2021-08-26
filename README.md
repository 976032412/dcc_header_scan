# dcc_header_scan

header_scan

## Getting Started

防爆手机(带扫描头)接收红外扫描头发送的广播数据,返回到flutter app

pub.dev地址:[fly](https://pub.dev/packages/dcc_header_scan)

# 引入依赖
```dart
  dcc_header_scan:
    git:
      url: git@github.com:976032412/dcc_header_scan.git    
```
添加包到`pubspec.yaml`文件中
```dart
  flutter pub get   package
```


# 如何使用

引入插件后,在需要获取获取扫描头数据的页面,创建:
```dart
import 'package:dcc_header_scan/dcc_header_scan.dart';
```

在`initState` 初始化

```dart
  DccHeaderScan.readInfo(
                    acctionCodeReceived:"com.yt.action.scan", // 监听广播的key
                    dataKey:"text" // 扫描完毕后 读取的key
                  ).listen((value) {
                    print("获取到扫描头数据>>>>>>>>>>$value");
                });
```

