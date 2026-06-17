package io.github.jlrods.mytripsmanager.database
import kotlinx.coroutines.runBlocking

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
    version = 9,
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

        val instance = Room.databaseBuilder(
            context.applicationContext,
            MyTripsManagerDb::class.java,
            "my_trips_manager_database"
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    CoroutineScope(Dispatchers.IO).launch {

                        val database = Instance ?: return@launch

                        database.countryDao().insertAll(InitialData.getCountries())
                        database.expenseTypeDao().insertAll(InitialData.getExpenseTypes())
                        database.providerDao().insertAll(InitialData.getProviders())

                        val countries = database.countryDao().getAllCountriesOnce()
                        val countryIdMap = countries.associate { it.name to it.id }

                        database.cityDao().insertAll(
                            InitialData.getEuropeanCities(countryIdMap)
                        )
                    }
                }
            })
            .build()

        Instance = instance
        instance
    }
}
    }
}