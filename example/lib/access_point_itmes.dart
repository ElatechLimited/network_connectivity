import 'package:flutter/material.dart';
import 'package:gap/gap.dart';
import 'package:wificonnect/wifi_model.dart';
import 'package:wificonnect/wificonnect.dart';
import 'package:wificonnect_example/capsule.dart';

class AccessPointItems extends StatelessWidget {
  final WifiModel model;
  final Function(bool) onConnected;
  final controller = TextEditingController();
  AccessPointItems({
    required this.onConnected,
    required this.model,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        showDialog(
            context: context,
            builder: (context) {
              return AlertDialog(
                title: const Text("Connect"),
                content: const Text("Are you sure you want to conect with  "),
                actions: [
                  TextButton(
                      onPressed: () {
                        Navigator.pop(context);
                      },
                      child: const Text("No",
                          style: TextStyle(color: Colors.black))),
                  TextButton(
                      onPressed: () async {
                        Navigator.pop(context);
                        if (containLock(model.capabilities ?? "")) {
                          await showBottomSheet(context);
                          if (controller.text.length < 6) {
                            ScaffoldMessenger.of(context).showSnackBar(
                                const SnackBar(
                                    content: Text("Invalid Password")));
                            return;
                          }
                          WifiConnect.getInstance().connectToNetwork(
                              model.ssid!,
                              model.bssid!,
                              controller.text,
                              model.capabilities!);
                          return;
                        }
                        WifiConnect.getInstance().connectToNetwork(model.ssid!,
                            controller.text, model.bssid!, model.capabilities!);
                      },
                      child: const Text("YES",
                          style: TextStyle(color: Colors.black)))
                ],
              );
            });
      },
      child: Row(
        children: [
          Expanded(
              child:
                  Text(model.ssid ?? "", style: const TextStyle(fontSize: 16))),
          Icon(containLock(model.capabilities!)
              ? Icons.wifi_lock
              : Icons.wifi_2_bar),
          const Icon(Icons.more_vert)
        ],
      ),
    );
  }

  bool containLock(String capability) {
    // WEP, WPA, WPA2, WPA_EAP, IEEE8021X
    if (capability.contains("WEP")) {
      return true;
    }
    if (capability.contains("WPA")) {
      return true;
    }
    if (capability.contains("WPA2")) {
      return true;
    }
    if (capability.contains("WPA_EAP")) {
      return true;
    }
    if (capability.contains("IEEE8021X")) {
      return true;
    }
    return false;
  }

  Future<void> showBottomSheet(BuildContext context) async {
    await showModalBottomSheet(
        isScrollControlled: true,
        shape: const RoundedRectangleBorder(
            borderRadius: BorderRadius.only(
                topLeft: Radius.circular(14), topRight: Radius.circular(14))),
        context: context,
        builder: (context) {
          return Padding(
              padding: MediaQuery.of(context).viewInsets,
              child: Column(mainAxisSize: MainAxisSize.min, children: [
                const Gap(15),
                const Capsule(),
                Padding(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 14, vertical: 0),
                  child: TextField(
                    controller: controller,
                    decoration: InputDecoration(
                        suffixIconConstraints:
                            const BoxConstraints(maxHeight: 40, maxWidth: 100),
                        suffix: ElevatedButton(
                            style: ButtonStyle(
                                backgroundColor:
                                    MaterialStateProperty.all(Colors.black)),
                            onPressed: () {
                              Navigator.pop(context);
                            },
                            child: const Text("Connect")),
                        border: InputBorder.none,
                        hintText: "Enter Wifi Password Here"),
                  ),
                ),
              ]));
        });
    return;
  }
}
