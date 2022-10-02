import 'package:dotted_border/dotted_border.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:gap/gap.dart';
import 'package:wificonnect/wifi_controller.dart';
import 'package:wificonnect/wifi_model.dart';
import 'package:wificonnect/wificonnect.dart';
import 'package:wificonnect_example/access_point_itmes.dart';
import 'package:wificonnect_example/capsule.dart';
import 'package:wificonnect_example/open_container.dart';
import 'package:wificonnect_example/rounded.dart';

void main() {
  runApp(App());
}

class App extends StatelessWidget {
  const App({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(home: MyApp());
  }
}

class MyApp extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _MyApp();
  }
}

class _MyApp extends State<MyApp> {
  bool connected = false;
  bool isLocationEnabled = false;
  ValueNotifier gpsNotifier = ValueNotifier(0);
  ValueNotifier wifiNotifier = ValueNotifier(0);
  List<WifiModel> allAccessPoint = [];
  late final WifiController controller;

  _MyApp() {
    controller = WifiController(wifiStateChanged: (state) {
      if (state) {
        wifiNotifier.value = 2;
        return;
      }
      wifiNotifier.value = 0;
    }, locationStateChanged: (state) {
      if (state) {
        gpsNotifier.value = 2;
        return;
      }
      gpsNotifier.value = 0;
    }, onWifiListChanged: (List<WifiModel> listData) {
      allAccessPoint = listData;
      setState(() {});
    });
  }
  @override
  void initState() {
    gpsEnabled();
    wifiEnabled();
    super.initState();
  }

  void gpsEnabled() async {
    var value = await WifiConnect.getInstance(controller: controller)
        .isLocationEnabled();
    if (value) {
      gpsNotifier.value = 2;
      return;
    }
    gpsNotifier.value = 0;
  }

  void wifiEnabled() async {
    var value =
        await WifiConnect.getInstance(controller: controller).isWifiEnabled();
    if (value) {
      wifiNotifier.value = 2;
      return;
    }
    wifiNotifier.value = 0;
  }

  @override
  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Scaffold(
      appBar: PreferredSize(
          preferredSize: Size.zero,
          child:
              AppBar(backgroundColor: const Color.fromARGB(255, 30, 30, 30))),
      body: Column(
        children: [
          Container(
              height: 300,
              width: size.width,
              color: const Color.fromARGB(255, 18, 18, 18),
              child: Column(
                children: [
                  const SizedBox(height: 30),
                  !connected
                      ? const Icon(Icons.wifi_off_outlined,
                          color: Colors.grey, size: 180)
                      : const Icon(Icons.wifi_outlined,
                          color: Colors.grey, size: 180),
                  const SizedBox(height: 20),
                  !connected
                      ? const Text("Not Connected",
                          style: TextStyle(color: Colors.grey, fontSize: 22))
                      : const Text("Connected",
                          style: TextStyle(color: Colors.grey, fontSize: 22))
                ],
              )),
          const SizedBox(height: 20),
          allAccessPoint.isEmpty
              ? const Center(
                  child: Text("No Available Network",
                      style: TextStyle(fontSize: 16)))
              : Expanded(
                  child: ListView.builder(
                  itemCount: allAccessPoint.length,
                  itemBuilder: (context, index) {
                    return Padding(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 16, vertical: 15),
                      child: AccessPointItems(
                        model: allAccessPoint[index],
                        onConnected: (val) {
                          if (val) {
                            connected = true;
                            setState(() {});
                          }
                        },
                      ),
                    );
                  },
                ))
        ],
      ),
      bottomSheet: Column(mainAxisSize: MainAxisSize.min, children: [
        Container(
            height: 50,
            width: double.infinity,
            margin: const EdgeInsets.symmetric(horizontal: 20),
            child: ElevatedButton(
              onPressed: () {
                showModalBottomSheet(
                    isScrollControlled: true,
                    shape: const RoundedRectangleBorder(
                        borderRadius: BorderRadius.only(
                            topLeft: Radius.circular(14),
                            topRight: Radius.circular(14))),
                    context: context,
                    builder: (context) {
                      return Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 10),
                        child: Column(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            const Gap(10),
                            const Capsule(),
                            const Gap(20),
                            Row(
                              children: [
                                const Rounded(
                                    child: Icon(Icons.location_on,
                                        size: 18, color: Colors.white)),
                                const Gap(10),
                                const Text("Open GPS",
                                    style: TextStyle(fontSize: 16)),
                                const Spacer(),
                                OpenContainer(
                                    onPressed: () async {
                                      var value = await WifiConnect.getInstance(
                                              controller: controller)
                                          .enabledLocation();
                                      if (value) {
                                        gpsNotifier.value = 2;
                                        return;
                                      }
                                      gpsNotifier.value = 0;
                                    },
                                    state: gpsNotifier),
                              ],
                            ),
                            const Gap(25),
                            Row(
                              children: [
                                const Rounded(
                                    child: Icon(Icons.wifi_outlined,
                                        size: 18, color: Colors.white)),
                                const Gap(10),
                                const Text("Open WLAN",
                                    style: TextStyle(fontSize: 16)),
                                const Spacer(),
                                OpenContainer(
                                    onPressed: () async {
                                      await WifiConnect.getInstance(
                                              controller: controller)
                                          .enableWifi();
                                      wifiNotifier.value = 0;
                                    },
                                    state: wifiNotifier),
                              ],
                            ),
                            const Gap(35),
                            DottedBorder(
                                padding: const EdgeInsets.symmetric(
                                    horizontal: 8, vertical: 8),
                                child: Row(
                                  children: const [
                                    Icon(Icons.warning_sharp,
                                        color: Colors.red),
                                    Gap(10),
                                    Expanded(
                                      child: Text(
                                          "Based on Android technology, this app need WLAN(WI-FI) + Location(GPS) Permissions.",
                                          style: TextStyle(fontSize: 12)),
                                    )
                                  ],
                                )),
                            const Gap(20)
                          ],
                        ),
                      );
                    });
              },
              style: ButtonStyle(
                  backgroundColor: MaterialStateProperty.all(
                      const Color.fromARGB(255, 30, 30, 30))),
              child: const Text("Turn On WI-FI"),
            )),
        const SizedBox(height: 20),
      ]),
    );
  }
}
