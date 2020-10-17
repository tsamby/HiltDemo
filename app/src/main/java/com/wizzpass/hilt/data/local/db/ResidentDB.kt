package com.wizzpass.hilt.data.local.db

/**
 * Created by novuyo on 20,September,2020
 */
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wizzpass.hilt.data.local.db.dao.*
import com.wizzpass.hilt.data.local.db.entity.*


@Database(entities = [Supervisor::class, Guard::class, Resident::class, ResAddress::class, SecondaryDriver::class,Vehicles::class, Visitor::class], version = 4, exportSchema = false)

@TypeConverters(Converters::class)
abstract class ResidentDB : RoomDatabase() {
    /**
     * Connects the database to the DAO.
     */
    abstract val residentDao: ResidentDao
    abstract val guardDao : GuardDao
    abstract val supervisorDao : SupervisorDao
    abstract val resAddressDao: ResAddressDao
    abstract val secondaryDriverDao : SecondaryDriverDao
    abstract val vehiclesDao : VehiclesDao
    abstract val visitorDao : VisitorDao



    companion object {

        @Volatile
        private var INSTANCE: ResidentDB? = null

        fun getInstance(context: Context): ResidentDB {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {

                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE

                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ResidentDB::class.java,
                        "resident_database"
                    )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this lesson. You can learn more about
                        // migration with Room in this blog post:
                        // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                        .fallbackToDestructiveMigration()
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }

                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }
}