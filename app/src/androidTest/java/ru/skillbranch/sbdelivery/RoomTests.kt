package ru.skillbranch.sbdelivery

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import ru.skillbranch.sbdelivery.data.local.AppDatabase

@RunWith(AndroidJUnit4::class)
class RoomTests {
    private lateinit var testDb: AppDatabase

    @Before
    fun createDb() {
        testDb = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() {
        testDb.close()
    }

    fun test_insert_list() {
        //        (0..20).forEach { i ->
//            noticesViewModel.insertData(
//                App.context,
//                UUID.randomUUID().toString(),
//                true,
//                "Заказ №56787 доставляется",
//                "Ваш заказ на сумму 1300 руб. доставляется курьером по адресу Москва, ул. Тверская, 7. Ожидайте!"
//            )
//        }
    }
}