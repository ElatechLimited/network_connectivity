import 'package:flutter/material.dart';

class Capsule extends StatelessWidget {
  const Capsule({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 6,
      width: 70,
      decoration: BoxDecoration(
          color: const Color.fromARGB(255, 30, 30, 30),
          borderRadius: BorderRadius.circular(14)),
    );
  }
}
