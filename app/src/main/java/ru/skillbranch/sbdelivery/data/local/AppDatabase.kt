package ru.skillbranch.sbdelivery.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.skillbranch.sbdelivery.App.Companion.context
import ru.skillbranch.sbdelivery.BuildConfig
import ru.skillbranch.sbdelivery.data.local.dao.*
import ru.skillbranch.sbdelivery.data.local.entities.*

object DbManager {
    val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        BuildConfig.APPLICATION_ID + ".db"
    ).build()
}

@Database(
    entities = [Notice::class, Dish::class, Category::class, Search::class, Cart::class,
        CartItem::class, Review::class, Order::class, OrderItem::class, Status::class, ReviewExpires::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noticesDao(): NoticeDao
    abstract fun dishesDao(): DishDao
    abstract fun categoriesDao(): CategoryDao
    abstract fun searchDao(): SearchDao
    abstract fun cartDao(): CartDao
    abstract fun cartItemsDao(): CartItemDao
    abstract fun reviewDao(): ReviewDao
    abstract fun ordersDao(): OrderDao
    abstract fun ordersItemsDao(): OrderItemDao
    abstract fun statusesDao(): StatusDao
    abstract fun reviewExpiresDao(): ReviewExpiresDao
}