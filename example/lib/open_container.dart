import 'package:flutter/material.dart';

class OpenContainer extends StatelessWidget {
  final Function onPressed;
  final ValueNotifier state;
  OpenContainer({super.key, required this.onPressed, required this.state});

  @override
  Widget build(BuildContext context) {
    return ValueListenableBuilder(
        valueListenable: state,
        builder: (context, widget, child) {
          return state.value == 1
              ? const Padding(
                  padding: EdgeInsets.only(right: 10),
                  child: SizedBox(
                    height: 20,
                    width: 20,
                    child: CircularProgressIndicator(
                      color: Colors.black,
                      strokeWidth: 2,
                    ),
                  ),
                )
              : state.value == 2
                  ? const Padding(
                      padding: EdgeInsets.only(right: 10),
                      child: Icon(Icons.check))
                  : GestureDetector(
                      onTap: () {
                        state.value = 1;
                        onPressed();
                      },
                      child: Container(
                          padding: const EdgeInsets.symmetric(horizontal: 24),
                          alignment: Alignment.center,
                          height: 30,
                          decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(20),
                              border: Border.all(
                                  color:
                                      const Color.fromARGB(255, 30, 30, 30))),
                          child: const Text("Open")),
                    );
        });
  }
}
