import 'package:flutter/material.dart';
import 'package:flutter_notes/models/note_model.dart';
import 'package:flutter_notes/screens/create_note.dart';
import 'package:flutter_notes/screens/widgets/note_card.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  List<Note> notes = [
    Note(title: 'Edi`s Title', body: 'Haha!'),
    Note(title: 'Mama', body: 'Tata'),
    Note(title: 'Flutter', body: 'App'),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Notes",
            style: TextStyle(fontSize: 32, fontWeight: FontWeight.bold)),
      ),
      body: ListView.builder(
        itemCount: notes.length,
        itemBuilder: (context, index) {
          return NoteCard(
              note: notes[index],
              index: index,
              onNoteDeleted: onNoteDeleted,
              onNoteUpdated: onNoteUpdated);
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Navigator.of(context).push(MaterialPageRoute(
              builder: (context) => CreateNote(
                    onNewNoteCreated: onNewNoteCreated,
                  )));
        },
        backgroundColor: const Color(0xFF000000),
        child: const Icon(
          Icons.add,
          size: 32,
          color: Color(0xFFFF9800),
        ),
      ),
    );
  }

  void onNewNoteCreated(Note note) {
    notes.add(note);
    setState(() {});
  }

  void onNoteUpdated(Note note, int index) {
    notes[index].title = note.title;
    notes[index].body = note.body;
    notes[index].updatedAt = note.updatedAt;
    setState(() {});
  }

  void onNoteDeleted(int id) {
    for (int i = 0; i < notes.length; i++) {
      if (notes[i].id == id) {
        notes.removeAt(i);
        break;
      }
    }
    setState(() {});
  }
}
