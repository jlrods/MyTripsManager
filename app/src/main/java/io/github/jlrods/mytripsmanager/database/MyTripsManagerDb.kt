package io.github.jlrods.mytripsmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Country::class,
        City::class,
        ExpenseType::class,
        Provider::class,
        Trip::class,
        Destination::class,
        Expense::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MyTripsManagerDb : RoomDatabase() {
    abstract fun countryDao(): CountryDao
    abstract fun cityDao(): CityDao
    abstract fun expenseTypeDao(): ExpenseTypeDao
    abstract fun providerDao(): ProviderDao
    abstract fun tripDao(): TripDao
    abstract fun destinationDao(): DestinationDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var Instance: MyTripsManagerDb? = null

        fun getDatabase(context: Context): MyTripsManagerDb {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MyTripsManagerDb::class.java, "my_trips_manager_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Use a coroutine to insert the initial data on a background thread
                            CoroutineScope(Dispatchers.IO).launch {
                                getDatabase(context).countryDao().insertAll(InitialData.getCountries())
                                getDatabase(context).expenseTypeDao().insertAll(InitialData.getExpenseTypes())
                            }
                        }
                    })
                    .build()
                    .also { Instance = it }
            }
        }
    }
}