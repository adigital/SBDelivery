package ru.skillbranch.sbdelivery.ui.fragments.notices

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.extensions.notifyShort
import ru.skillbranch.sbdelivery.viewmodels.fragments.NoticesViewModel
import java.util.*

class NoticesFragment : Fragment() {

    private lateinit var noticesViewModel: NoticesViewModel
    private lateinit var noticesAdapter: NoticesAdapter

    private lateinit var layoutManager: LinearLayoutManager

//    private var title = ""
//    private var message = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        noticesViewModel = ViewModelProvider(this).get(NoticesViewModel::class.java)

        setHasOptionsMenu(true)

        // FCM
        // Get token
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("My", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            Log.d("My", "FCM registration Token: ${task.result}")
        })

//        if (activity?.intent?.extras != null) {
//            for (key in requireActivity().intent.extras!!.keySet()) {
//                if (key == "title") {
//                    title = requireActivity().intent.extras!!.getString("title", "")
//                }
//                if (key == "message") {
//                    message = requireActivity().intent.extras!!.getString("message", "")
//                }
//            }
//        }
//
//        if (title != "" && message != "") {
//            noticesViewModel.insertData(
//                App.context,
//                UUID.randomUUID().toString(),
//                true,
//                title,
//                message
//            )
//        }

        return inflater.inflate(R.layout.fragment_notice, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuItem = menu.findItem(R.id.action_menu)
        menuItem.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(view)
    }

    private fun initView(view: View) {
        val tvNoticesEmpty: TextView = view.findViewById(R.id.tvNoticesEmpty)
        val rvNotices: RecyclerView = view.findViewById(R.id.rvNotices)

        noticesAdapter = NoticesAdapter()
        rvNotices.adapter = noticesAdapter

        noticesViewModel.getNotices().observe(viewLifecycleOwner, {
            noticesAdapter.updateData(it)

            if (it.isEmpty()) {
                tvNoticesEmpty.visibility = View.VISIBLE
                rvNotices.visibility = View.GONE
            } else {
                tvNoticesEmpty.visibility = View.GONE
                rvNotices.visibility = View.VISIBLE
            }
        })


        layoutManager = rvNotices.layoutManager as LinearLayoutManager

        rvNotices
            .onScrollAsFlow()
            .debounce(1000)
            .onEach { scrollVisiblePosition ->
                for (i in scrollVisiblePosition.firstCompletelyVisibleItemPosition..scrollVisiblePosition.lastCompletelyVisibleItemPosition) {
                    noticesViewModel.readNotice(i)
                }
            }
            .launchIn(lifecycleScope)

        //test
        val fabTest: FloatingActionButton = view.findViewById(R.id.fabTest)

        fabTest.setOnClickListener {
            noticesViewModel.deleteAllNotices()

            (0..20).forEach { i ->
                noticesViewModel.insertNotices(
                    UUID.randomUUID().toString(),
                    true,
                    "Заказ №56787 доставляется",
                    "Ваш заказ на сумму 1300 руб. доставляется курьером по адресу Москва, ул. Тверская, 7. Ожидайте!"
                )
            }

//            noticesViewModel.setAllNoticesNotRead()
            notifyShort("Test message generated!")
        }
        // -test
    }
}

fun RecyclerView.onScrollAsFlow() = callbackFlow {
    val callback = object : RecyclerView.OnScrollListener() {
        val lm = layoutManager as LinearLayoutManager

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            trySend(
                ScrollVisiblePosition(
                    lm.findFirstCompletelyVisibleItemPosition(),
                    lm.findLastCompletelyVisibleItemPosition()
                )
            ).isSuccess
        }
    }

    addOnScrollListener(callback)

    awaitClose { removeOnScrollListener(callback) }
}

data class ScrollVisiblePosition(
    val firstCompletelyVisibleItemPosition: Int,
    val lastCompletelyVisibleItemPosition: Int
)