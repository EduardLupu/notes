class Note {
  static int _idCounter = 0;

  final int id;
  String title;
  String body;
  final DateTime createdAt;
  DateTime updatedAt;

  Note(
      {required this.title,
      required this.body,
      int? id,
      DateTime? createdAt,
      DateTime? updatedAt})
      : id = _idCounter++,
        createdAt = DateTime.now(),
        updatedAt = DateTime.now();
}
