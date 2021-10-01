package ru.skillbranch.sbdelivery.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.App
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.extensions.isOnline
import ru.skillbranch.sbdelivery.extensions.notifyLong
import ru.skillbranch.sbdelivery.viewmodels.fragments.SplashViewModel

class SplashFragment : Fragment() {

    private lateinit var viewModel: SplashViewModel
    private var isFinishSplash: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val iv: ImageView = view.findViewById(R.id.iv)
//        val d = ContextCompat.getDrawable(view.context, R.drawable.ic_background_pattern)
//        iv.setImageDrawable(TileDrawable(d!!, Shader.TileMode.REPEAT))

        loadData()

        GlobalScope.launch {
            delay(3000)
            GlobalScope.launch(Dispatchers.Main) {
                if (!isFinishSplash) {
                    isFinishSplash = true

                    findNavController().navigate(SplashFragmentDirections.actionNavSplashToNavHome())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
    }

    private fun loadData() {
        if (App.context.isOnline()) {
            viewModel.loadData().observe(viewLifecycleOwner, { isFinished ->
                if (isFinished) {
                    if (!isFinishSplash) {
                        isFinishSplash = true

                        findNavController().navigate(SplashFragmentDirections.actionNavSplashToNavHome())
                    }
                }
            })
        } else {
            notifyLong("Сеть недоступна")
            isFinishSplash = true
            viewModel.resetRecommendedDishes()

            findNavController().navigate(SplashFragmentDirections.actionNavSplashToNavHome())
        }
    }
}