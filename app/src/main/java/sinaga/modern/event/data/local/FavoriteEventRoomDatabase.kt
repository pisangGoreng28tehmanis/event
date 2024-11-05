package sinaga.modern.event.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FavoriteEvent::class], version = 2, exportSchema = false)
abstract class FavoriteEventRoomDatabase : RoomDatabase() {
    abstract fun favoriteEventDao(): FavoriteEventDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteEventRoomDatabase? = null

        // Define your migration here
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create a new table with the new schema
                database.execSQL("""
                    CREATE TABLE FavoriteEvent_new (
                        id TEXT PRIMARY KEY NOT NULL,
                        name TEXT,
                        mediaCover TEXT,
                        description TEXT,
                        cityName TEXT,
                        ownerName TEXT,
                        beginTime TEXT,
                        endTime TEXT,
                        quota INTEGER,
                        registrants INTEGER,
                        link TEXT
                    )
                """.trimIndent())

                // Copy the data from the old table to the new table
                database.execSQL("""
                    INSERT INTO FavoriteEvent_new (id, name, mediaCover)
                    SELECT id, name, mediaCover FROM FavoriteEvent
                """.trimIndent())

                // Drop the old table
                database.execSQL("DROP TABLE FavoriteEvent")

                // Rename the new table to the old table name
                database.execSQL("ALTER TABLE FavoriteEvent_new RENAME TO FavoriteEvent")
            }
        }

        @JvmStatic
        fun getDatabase(context: Context): FavoriteEventRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteEventRoomDatabase::class.java,
                    "favorite_event_database"
                )
                    .addMigrations(MIGRATION_1_2) // Add migration here
                    .fallbackToDestructiveMigration() // This line allows for destructive migration if needed
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
