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
    version = 8,
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

//        fun getDatabase(context: Context): MyTripsManagerDb {
//            return Instance ?: synchronized(this) {
//                Room.databaseBuilder(context, MyTripsManagerDb::class.java, "my_trips_manager_database")
//                    .fallbackToDestructiveMigration()// TODO: Remove this in production
//                    .addCallback(object : Callback() {
//                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            super.onCreate(db)
//                            // Use a coroutine to insert the initial data on a background thread
//                            CoroutineScope(Dispatchers.IO).launch {
//                                Instance?.let { database ->
//                                    database.countryDao().insertAll(InitialData.getCountries())
//                                    database.expenseTypeDao().insertAll(InitialData.getExpenseTypes())
//                                    database.providerDao().insertAll(InitialData.getProviders())
//
//                                    // Now fetch countries with generated IDs
//                                    val countries = database.countryDao().getAllCountriesOnce()
//                                    val countryIdMap = countries.associate { it.name to it.id }
//                                    // Insert cities using real IDs
//                                    database.cityDao().insertAll(
//                                        InitialData.getEuropeanCities(countryIdMap)
//                                    )
//                                }
//                            }
//                        }
//                    })
//                    .build()
//                    .also { Instance = it }
//            }
//        }
//        Working getDatabase function after fixing timing issue with "instance" object
//fun getDatabase(context: Context): MyTripsManagerDb {
//    return Instance ?: synchronized(this) {
//
//        val builder = Room.databaseBuilder(
//            context.applicationContext,
//            MyTripsManagerDb::class.java,
//            "my_trips_manager_database"
//        )
//            .fallbackToDestructiveMigration()
//
//        val instance = builder.build()
//
//        instance.openHelper.writableDatabase // Forces creation
//
//        CoroutineScope(Dispatchers.IO).launch {
//            instance.countryDao().insertAll(InitialData.getCountries())
//            instance.expenseTypeDao().insertAll(InitialData.getExpenseTypes())
//            instance.providerDao().insertAll(InitialData.getProviders())
//
//            val countries = instance.countryDao().getAllCountriesOnce()
//            val countryIdMap = countries.associate { it.name to it.id }
//
//            instance.cityDao().insertAll(
//                InitialData.getEuropeanCities(countryIdMap)
//            )
//        }
//
//        Instance = instance
//        instance
//    }
//}
//fun getDatabase(context: Context): MyTripsManagerDb {
//    return Instance ?: synchronized(this) {
//
//        val instance = Room.databaseBuilder(
//            context.applicationContext,
//            MyTripsManagerDb::class.java,
//            "my_trips_manager_database"
//        )
//            .fallbackToDestructiveMigration()
//            .build()
//
//        CoroutineScope(Dispatchers.IO).launch {
//
//            if (instance.countryDao().count() == 0) {
//
//                instance.countryDao().insertAll(InitialData.getCountries())
//                instance.expenseTypeDao().insertAll(InitialData.getExpenseTypes())
//                instance.providerDao().insertAll(InitialData.getProviders())
//
//                val countries = instance.countryDao().getAllCountriesOnce()
//                val countryIdMap = countries.associate { it.name to it.id }
//
//                instance.cityDao().insertAll(
//                    InitialData.getEuropeanCities(countryIdMap)
//                )
//            }
//        }
//
//        Instance = instance
//        instance
//    }
//}
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