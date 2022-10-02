import 'dart:convert';

List<WifiModel> wifiModelFromJson(String str) =>
    List<WifiModel>.from(json.decode(str).map((x) => WifiModel.fromJson(x)));

class WifiModel {
  WifiModel({
    this.bssid,
    this.ssid,
    this.anqpDomainId,
    this.capabilities,
    this.centerFreq0,
    this.centerFreq1,
    this.channelWidth,
    this.distanceCm,
    this.distanceSdCm,
    this.flags,
    this.frequency,
    this.hessid,
    this.is80211McRttResponder,
    this.level,
    this.numUsage,
    this.operatorFriendlyName,
    this.seen,
    this.timestamp,
    this.untrusted,
    this.venueName,
    this.wifiSsid,
  });

  final String? bssid;
  final String? ssid;
  final int? anqpDomainId;
  final String? capabilities;
  final int? centerFreq0;
  final int? centerFreq1;
  final int? channelWidth;
  final int? distanceCm;
  final int? distanceSdCm;
  final int? flags;
  final int? frequency;
  final int? hessid;
  final bool? is80211McRttResponder;
  final int? level;
  final int? numUsage;
  final String? operatorFriendlyName;
  final int? seen;
  final int? timestamp;
  final bool? untrusted;
  final String? venueName;
  final WifiSsid? wifiSsid;

  factory WifiModel.fromJson(Map<String?, dynamic> json) => WifiModel(
        bssid: json["BSSID"],
        ssid: json["SSID"],
        anqpDomainId: json["anqpDomainId"],
        capabilities: json["capabilities"],
        centerFreq0: json["centerFreq0"],
        centerFreq1: json["centerFreq1"],
        channelWidth: json["channelWidth"],
        distanceCm: json["distanceCm"],
        distanceSdCm: json["distanceSdCm"],
        flags: json["flags"],
        frequency: json["frequency"],
        hessid: json["hessid"],
        is80211McRttResponder: json["is80211McRTTResponder"],
        level: json["level"],
        numUsage: json["numUsage"],
        operatorFriendlyName: json["operatorFriendlyName"],
        seen: json["seen"],
        timestamp: json["timestamp"],
        untrusted: json["untrusted"],
        venueName: json["venueName"],
        wifiSsid: WifiSsid.fromJson(json["wifiSsid"]),
      );
}

class WifiSsid {
  WifiSsid({
    this.octets,
  });

  final Octets? octets;

  factory WifiSsid.fromJson(Map<String?, dynamic> json) => WifiSsid(
        octets: Octets.fromJson(json["octets"]),
      );

  Map<String?, dynamic> toJson() => {
        "octets": octets!.toJson(),
      };
}

class Octets {
  Octets({
    this.buf,
    this.count,
  });

  final List<int?>? buf;
  final int? count;

  factory Octets.fromJson(Map<String?, dynamic> json) => Octets(
        buf: List<int?>.from(json["buf"].map((x) => x)),
        count: json["count"],
      );

  Map<String?, dynamic> toJson() => {
        "buf": List<dynamic>.from(buf!.map((x) => x)),
        "count": count,
      };
}
