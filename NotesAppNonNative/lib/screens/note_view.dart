import 'package:flutter/material.dart';
import 'package:flutter_notes/models/note_model.dart';

class NoteView extends StatefulWidget {
  const NoteView({
    Key? key,
    required this.note,
    required this.index,
    required this.onNoteDeleted,
    required this.onNoteUpdated,
  }) : super(key: key);

  final Note note;
  final int index;
  final Function(int) onNoteDeleted;
  final Function(Note, int) onNoteUpdated;

  @override
  _NoteViewState createState() => _NoteViewState();
}

class _NoteViewState extends State<NoteView> {
  late TextEditingController titleController;
  late TextEditingController bodyController;
  bool isEditing = false;

  @override
  void initState() {
    super.initState();
    titleController = TextEditingController(text: widget.note.title);
    bodyController = TextEditingController(text: widget.note.body);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(" "),
        actions: [
          if (!isEditing)
            IconButton(
              onPressed: () {
                setState(() {
                  isEditing = true;
                });
              },
              icon: const Icon(Icons.edit),
            ),
          IconButton(
            onPressed: () {
              showDialog(
                context: context,
                builder: (context) {
                  return AlertDialog(
                    title: const Text("Delete This?"),
                    content: Text("Note ${widget.note.title} will be deleted!"),
                    actions: [
                      TextButton(
                        onPressed: () {
                          Navigator.of(context).pop();
                          widget.onNoteDeleted(widget.note.id);
                          Navigator.of(context).pop();
                        },
                        child: const Text("DELETE"),
                      ),
                      TextButton(
                        onPressed: () {
                          Navigator.of(context).pop();
                        },
                        child: const Text("CANCEL"),
                      )
                    ],
                  );
                },
              );
            },
            icon: const Icon(Icons.delete),
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(10.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            if (isEditing)
              TextFormField(
                controller: titleController,
                style: const TextStyle(fontSize: 26),
                decoration: const InputDecoration(
                  border: InputBorder.none,
                ),
              )
            else
              Text("Title: \n${widget.note.title}",
                  style: const TextStyle(
                      fontSize: 26, fontWeight: FontWeight.bold)),
            const SizedBox(height: 20),
            if (isEditing)
              TextFormField(
                controller: bodyController,
                style: const TextStyle(fontSize: 26),
                decoration: const InputDecoration(
                  border: InputBorder.none,
                ),
              )
            else
              Text("Content: \n${widget.note.body}",
                  style: const TextStyle(
                      fontSize: 26, fontWeight: FontWeight.bold)),
          ],
        ),
      ),
      floatingActionButton: isEditing
          ? FloatingActionButton(
              onPressed: () {
                if (titleController.text.isEmpty) {
                  return;
                }
                if (bodyController.text.isEmpty) {
                  return;
                }

                final updatedNote = Note(
                  title: titleController.text,
                  body: bodyController.text,
                  createdAt: widget.note.createdAt,
                  updatedAt: DateTime.now(),
                );

                widget.onNoteUpdated(updatedNote, widget.index);
                Navigator.of(context).pop();

                // Close the editing mode
                setState(() {
                  isEditing = false;
                });
              },
              backgroundColor: const Color(0xFF000000),
              child: const Icon(Icons.save, color: Color(0xFFFF9800)),
            )
          : null,
    );
  }

  @override
  void dispose() {
    titleController.dispose();
    bodyController.dispose();
    super.dispose();
  }
}
