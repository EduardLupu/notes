import 'package:flutter/material.dart';
import 'package:flutter_notes/models/note_model.dart';
import 'package:flutter_notes/screens/note_view.dart';

class NoteCard extends StatelessWidget {
  const NoteCard({
    Key? key,
    required this.note,
    required this.onNoteDeleted,
    required this.index,
    required this.onNoteUpdated,
  }) : super(key: key);

  final Note note;
  final int index;
  final Function(int) onNoteDeleted;
  final Function(Note, int) onNoteUpdated;

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: () {
        Navigator.of(context).push(MaterialPageRoute(
          builder: (context) => NoteView(
            note: note,
            index: index,
            onNoteDeleted: onNoteDeleted,
            onNoteUpdated: onNoteUpdated,
          ),
        ));
      },
      child: Container(
        margin: const EdgeInsets.all(8.0),
        child: Card(
          color: Color(0xFF000000),
          child: Padding(
            padding: const EdgeInsets.all(10),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  "#${note.id}: ${note.title}",
                  style: const TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFFFF9800)),
                ),
                const SizedBox(height: 5),
                Text(
                  'Created: ${note.createdAt.toIso8601String().substring(0, 10)}             Updated: ${note.updatedAt.toIso8601String().substring(0, 10)}',
                  style: const TextStyle(
                    fontSize: 16,
                    fontStyle: FontStyle.italic,
                  ),
                ),
                const SizedBox(height: 10),
                Text(
                  'Content:\n ${note.body}',
                  style: const TextStyle(
                    fontSize: 18,
                  ),
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
