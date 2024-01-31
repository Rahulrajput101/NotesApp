package com.ondevop.notesapp.di

import android.app.Application
import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.room.Room
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.ondevop.core_data.prefrences.DefaultPreferences
import com.ondevop.core_data.prefrences.dataStore
import com.ondevop.core_domain.prefernces.Preferences
import com.ondevop.notesapp.feature_note.data.data_source.NoteDatabase
import com.ondevop.notesapp.feature_note.data.repository.NoteRepositoryImp
import com.ondevop.notesapp.feature_note.domain.repository.NoteRepository
import com.ondevop.notesapp.feature_note.domain.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
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

    @Singleton
    @Provides
    fun provideFirebaseAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    @Provides
    @Singleton
    fun provideNoteRepository(db : NoteDatabase, firebaseAuth: FirebaseAuth) : NoteRepository{
        return NoteRepositoryImp(db.noteDao, firebaseAuth)
    }


    @Provides
    @Singleton
    fun provideNoteUseCase(repository: NoteRepository) : NotesUseCases{
        return NotesUseCases(
            getNotesUseCase = GetNotesUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository),
            addNoteUsesCase = AddNoteUseCase(repository),
            getNoteUseCase = GetNoteUseCase(repository),
            signInWithGoogle = SignInWithGoogle(repository)
        )
    }

    @Provides
    @Singleton
    fun providePreference(
        app: Application
    ): Preferences {
        return DefaultPreferences(app.dataStore)
    }
    @Provides
    @Named("webClientId")
    fun provideWebClientId(): String {
        return ""
    }
    @Singleton
    @Provides
    fun provideGoogleSignClient(
        app: Application,
        @Named("webClientId") webClientId: String
    ) : GoogleSignInClient {
        return GoogleSignIn.getClient(
            app,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        )
    }

}