package com.ondevop.notesapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.ondevop.core_data.prefrences.DefaultPreferences
import com.ondevop.core_data.prefrences.dataStore
import com.ondevop.core_domain.prefernces.Preferences
import com.ondevop.notesapp.feature_note.data.data_source.NoteDatabase
import com.ondevop.notesapp.feature_note.data.repository.FirebaseNoteRepositoryImp
import com.ondevop.notesapp.feature_note.data.repository.FirebaseRepositoryImp
import com.ondevop.notesapp.feature_note.data.repository.NoteRepositoryImp
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseNoteRepository
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseRepository
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
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        NoteDatabase::class.java,
        NoteDatabase.NOTEDATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun firestoreInstance(): FirebaseFirestore = Firebase.firestore
    @Singleton
    @Provides
    fun firestoreStroageRef(): StorageReference= Firebase.storage.reference

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase, firebaseAuth: FirebaseAuth): NoteRepository {
        return NoteRepositoryImp(db.noteDao, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ): FirebaseRepository {
        return FirebaseRepositoryImp(firebaseFirestore, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideFirebaseNoteRepository(
        firebaseAuth: FirebaseAuth,
        firebaseStorageRef: StorageReference,
        firebaseFirestore: FirebaseFirestore
    ): FirebaseNoteRepository {
        return FirebaseNoteRepositoryImp(firebaseFirestore, firebaseStorageRef,firebaseAuth)
    }


    @Provides
    @Singleton
    fun provideNoteUseCase(
        repository: NoteRepository,
        firebaseRepository: FirebaseRepository,
        firebaseNoteRepository: FirebaseNoteRepository
    ): NotesUseCases {
        return NotesUseCases(
            getNotesUseCase = GetNotesUseCase(repository,firebaseNoteRepository),
            deleteNoteUseCase = DeleteNoteUseCase(repository,firebaseNoteRepository),
            addNoteUsesCase = AddNoteUseCase(repository,firebaseNoteRepository),
            getNoteUseCase = GetNoteUseCase(repository, firebaseNoteRepository),
            signInWithGoogle = SignInWithGoogle(repository),

            saveUserToFirebase = SaveUserToFirebase(firebaseRepository),
            addNoteToFirebase = AddNoteToFirebase(firebaseNoteRepository),
            registerNotesRealTimeUpdates = RegisterNotesRealTimeUpdates(firebaseNoteRepository),
            unregisterNotesRealTimeUpdates = UnregisterNotesRealTimeUpdates(firebaseNoteRepository)
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
        return "63511061613-tgfp26ci5f4tqnlgd8ljiftfns8hlp6k.apps.googleusercontent.com"
    }

    @Singleton
    @Provides
    fun provideGoogleSignClient(
        app: Application,
        @Named("webClientId") webClientId: String
    ): GoogleSignInClient {
        return GoogleSignIn.getClient(
            app,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build()
        )
    }

}