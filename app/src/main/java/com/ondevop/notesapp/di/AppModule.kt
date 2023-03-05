package com.ondevop.notesapp.di

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.room.Room
import com.ondevop.notesapp.feature_note.data.data_source.NoteDatabase
import com.ondevop.notesapp.feature_note.data.repository.NoteRepositoryImp
import com.ondevop.notesapp.feature_note.domain.repository.NoteRepository
import com.ondevop.notesapp.feature_note.domain.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun noteDatabase(
        @ApplicationContext app : Context
    ) = Room.databaseBuilder(
        app,
        NoteDatabase::class.java,
         NoteDatabase.NOTEDATABASE_NAME
    ).build()


    @Provides
    @Singleton
    fun provideNoteRepository(db : NoteDatabase) : NoteRepository{
        return NoteRepositoryImp(db.noteDao)
    }


    @Provides
    @Singleton
    fun provideNoteUseCase(repository: NoteRepository) : NotesUseCases{
        return NotesUseCases(
            getNotesUseCase = GetNotesUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository),
            addNoteUsesCase = AddNoteUseCase(repository),
            getNoteUseCase = GetNoteUseCase(repository)

        )


    }

}