import 'package:flutter/material.dart';

class Rounded extends StatelessWidget {
  final Widget child;
  const Rounded({super.key, required this.child});

  @override
  Widget build(BuildContext context) {
    return Container(
        height: 30,
        width: 30,
        decoration: const BoxDecoration(
            color: Color.fromARGB(255, 30, 30, 30), shape: BoxShape.circle),
        child: child);
  }
}
