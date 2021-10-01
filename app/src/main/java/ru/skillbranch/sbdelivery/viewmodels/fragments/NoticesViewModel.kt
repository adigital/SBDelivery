package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.sbdelivery.data.local.entities.Notice
import ru.skillbranch.sbdelivery.data.repositories.NoticesRepository

class NoticesViewModel : ViewModel() {

    private val noticesRepository = NoticesRepository
    private lateinit var noticesItems: LiveData<List<Notice>>

    fun insertNotices(id: String, isNew: Boolean, header: String, message: String) {
        noticesRepository.insertNoticesDao(Notice(id, isNew, header, message))
    }

    fun getNotices(): LiveData<List<Notice>> {
        noticesItems = noticesRepository.getNoticesDao()
        return noticesItems
    }

    fun readNotice(itemIndex: Int) {
        if (noticesItems.value?.get(itemIndex)?.isNew == true) {
            noticesRepository.readNoticeDao(noticesItems.value!![itemIndex].id)
        }
    }

    // test
    fun setAllNoticesNotRead() {
        noticesRepository.setAllNoticesNotRead()
    }

    fun deleteAllNotices() {
        noticesRepository.deleteAllNotices()
    }

}