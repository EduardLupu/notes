# notes
Replication of the Notes app from iOS done in Android Studio using Kotlin and Flutter using Dart.

The application was done as assignments for the course Mobile Applications.

The application represent a simple notes app, where the user can read all the saved notes, edit them, preview them, delete them or search them by title.

Domain:
-A note has:
	-id
	-title
	-content
	-date saved
	-date updated

The data will be first saved in the memory, then will be implemented a database on the server.


1. Create a Note:
   - Users can create new notes with a title and content.

2. Search Functionality:
   - Users should be able to search for specific notes based on title. The search feature should provide relevant results quickly.

3. Delete a Note:
   - Users should have the ability to delete unwanted notes. Deleting a note should require confirmation to avoid accidental deletions.

4. Preview Mode:
   - Users can preview notes without editing them, allowing them to read the content without accidentally making changes.

5. Update a Note:
   - Users can edit and update the content of existing notes. 

6. See All Notes:
   - Users can access a "See All Notes" view that displays a list of all their notes for easy navigation and management.


When offline, the app works the same.
