package com.ondevop.notesapp.feature_note.domain.use_cases

data class NotesUseCases(
    val getNotesUseCase: GetNotesUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    val addNoteUsesCase : AddNoteUseCase,
    val getNoteUseCase: GetNoteUseCase,
    val signInWithGoogle: SignInWithGoogle,

    val saveUserToFirebase: SaveUserToFirebase,
    val addNoteToFirebase: AddNoteToFirebase,
    val registerNotesRealTimeUpdates: RegisterNotesRealTimeUpdates,
    val unregisterNotesRealTimeUpdates: UnregisterNotesRealTimeUpdates,

    val getImages: GetImages
)
