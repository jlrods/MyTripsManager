package io.github.jlrods.mytripsmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
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
    version = 10,
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

        // =========================================================
        // DATABASE MIGRATION 9 -> 10
        // =========================================================

        val MIGRATION_9_10 = object : Migration(9, 10) {

            override fun migrate(db: SupportSQLiteDatabase) {

                // ---------------------------------------------------------
                // 1. Add tripDay to expenses
                // ---------------------------------------------------------

                db.execSQL(
                    """
                    ALTER TABLE expenses
                    ADD COLUMN tripDay INTEGER NOT NULL DEFAULT 1
                    """.trimIndent()
                )

                // ---------------------------------------------------------
                // 2. Populate tripDay for existing expenses
                //
                // Day 1 = trip start date
                // Day 2 = trip start + 1 day
                // etc.
                // ---------------------------------------------------------

                db.execSQL(
                                        """
                        UPDATE expenses
                        SET tripDay =
                            CASE
                    
                                -- Expense entered before or on trip start
                                WHEN date <= (
                                    SELECT start
                                    FROM trips
                                    WHERE trips.id = expenses.tripId
                                )
                                THEN 1
                    
                                -- Expense entered after the trip ended
                                WHEN date >= (
                                    SELECT end
                                    FROM trips
                                    WHERE trips.id = expenses.tripId
                                )
                                THEN CAST(
                                    (
                                        (
                                            SELECT end
                                            FROM trips
                                            WHERE trips.id = expenses.tripId
                                        )
                                        -
                                        (
                                            SELECT start
                                            FROM trips
                                            WHERE trips.id = expenses.tripId
                                        )
                                    ) / 86400000
                                    AS INTEGER
                                ) + 1
                    
                                -- Expense entered during the trip
                                ELSE CAST(
                                    (
                                        date -
                                        (
                                            SELECT start
                                            FROM trips
                                            WHERE trips.id = expenses.tripId
                                        )
                                    ) / 86400000
                                    AS INTEGER
                                ) + 1
                    
                            END
                        """.trimIndent()
                )

                // ---------------------------------------------------------
                // 3. Add generic providers
                //
                // LOWER(name) prevents duplicates if one was already
                // manually created using different capitalization.
                // ---------------------------------------------------------

                db.execSQL(
                    """
                    INSERT INTO providers (name, logoRes, logoUri)
                    SELECT 'Generic Restaurant', NULL, NULL
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM providers
                        WHERE LOWER(name) = LOWER('Generic Restaurant')
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT INTO providers (name, logoRes, logoUri)
                    SELECT 'Generic Store', NULL, NULL
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM providers
                        WHERE LOWER(name) = LOWER('Generic Store')
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT INTO providers (name, logoRes, logoUri)
                    SELECT 'Generic Pharmacy', NULL, NULL
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM providers
                        WHERE LOWER(name) = LOWER('Generic Pharmacy')
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT INTO providers (name, logoRes, logoUri)
                    SELECT 'Generic Hotel', NULL, NULL
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM providers
                        WHERE LOWER(name) = LOWER('Generic Hotel')
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT INTO providers (name, logoRes, logoUri)
                    SELECT 'Generic Taxi', NULL, NULL
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM providers
                        WHERE LOWER(name) = LOWER('Generic Taxi')
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT INTO providers (name, logoRes, logoUri)
                    SELECT 'Generic Attraction', NULL, NULL
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM providers
                        WHERE LOWER(name) = LOWER('Generic Attraction')
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT INTO providers (name, logoRes, logoUri)
                    SELECT 'Generic Supermarket', NULL, NULL
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM providers
                        WHERE LOWER(name) = LOWER('Generic Supermarket')
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT INTO providers (name, logoRes, logoUri)
                    SELECT 'Generic Transport', NULL, NULL
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM providers
                        WHERE LOWER(name) = LOWER('Generic Transport')
                    )
                    """.trimIndent()
                )
            }
        }

        @Volatile
        private var Instance: MyTripsManagerDb? = null

        fun getDatabase(context: Context): MyTripsManagerDb {

            return Instance ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyTripsManagerDb::class.java,
                    "my_trips_manager_database"
                )
//                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_9_10)
                    .addCallback(object : Callback() {

                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            CoroutineScope(Dispatchers.IO).launch {

                                val database = Instance ?: return@launch

                                database.countryDao().insertAll(
                                    InitialData.getCountries()
                                )

                                database.expenseTypeDao().insertAll(
                                    InitialData.getExpenseTypes()
                                )

                                database.providerDao().insertAll(
                                    InitialData.getProviders()
                                )

                                val countries =
                                    database.countryDao().getAllCountriesOnce()

                                val countryIdMap =
                                    countries.associate { it.name to it.id }

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