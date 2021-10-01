package ru.skillbranch.sbdelivery.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.local.DbManager
import ru.skillbranch.sbdelivery.data.local.entities.Notice

object NoticesRepository {

    private var noticesDao = DbManager.db.noticesDao()

    fun insertNoticesDao(notice: Notice) {
        GlobalScope.launch(Dispatchers.IO) {
            noticesDao.insertNotice(notice)
        }
    }

    fun getNoticesDao() = noticesDao.getNotices()

    fun getNewNoticesCountDao() = noticesDao.getNewNoticesCount()

    fun readNoticeDao(id: String) {
        GlobalScope.launch(Dispatchers.IO) {
            noticesDao.readNotice(id)
        }
    }

    // test
    fun setAllNoticesNotRead() {
        GlobalScope.launch(Dispatchers.IO) {
            noticesDao.setAllNoticesNotRead()
        }
    }

    fun deleteAllNotices() {
        GlobalScope.launch(Dispatchers.IO) {
            noticesDao.deleteAllNotices()
        }
    }
}