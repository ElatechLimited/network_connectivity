import 'package:flutter/material.dart';
import 'package:wificonnect/wifi_model.dart';

class AccessPointItems extends StatelessWidget {
  final WifiModel model;
  final Function(bool) onConnected;
  const AccessPointItems({
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
          const Icon(Icons.wifi_2_bar),
          const Icon(Icons.more_vert)
        ],
      ),
    );
  }
}
