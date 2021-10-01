package ru.skillbranch.sbdelivery

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import ru.skillbranch.sbdelivery.extensions.showDialogNoYes
import ru.skillbranch.sbdelivery.viewmodels.RootViewModel


class RootActivity : AppCompatActivity() {

    private lateinit var viewModel: RootViewModel

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var menuCart: View
    private lateinit var menuNotices: View
    private lateinit var tvCartItemsCount: TextView
    private lateinit var tvNoticesCount: TextView
    private lateinit var btnLogOut: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SBDelivery_NoActionBar)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(RootViewModel::class.java)

        initView()

        viewModel.getCartItemsCount().observe(this, { count ->
            if (count != 0) tvCartItemsCount.text = "+$count" else tvCartItemsCount.text = ""
        })

        viewModel.getNewNoticesCount().observe(this, { count ->
            if (count != 0) tvNoticesCount.text = "+$count" else tvNoticesCount.text = ""
        })


        viewModel.authState.observe(this, { isAuth ->
            btnLogOut.visibility = if (isAuth) View.VISIBLE else View.GONE
        })
    }

    private fun initView() {
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_menu,
                R.id.nav_favorites,
                R.id.nav_cart,
                R.id.nav_profile,
                R.id.nav_orders,
                R.id.nav_notice,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        menuCart = findViewById(R.id.menuCart)
        tvCartItemsCount = menuCart.findViewById(R.id.tvCount) as TextView

        menuNotices = findViewById(R.id.menuNotices)
        tvNoticesCount = menuNotices.findViewById(R.id.tvCount) as TextView


        btnLogOut = findViewById(R.id.btnLogOut)
        btnLogOut.setOnClickListener {
            showDialogNoYes(
                this,
                "Вы действительно хотите выйти из аккаунта?",
                { viewModel.logOut() },
                {})
        }


        prepareMainMenu()

        prepareMainMenuHeader()
    }

    private fun prepareMainMenu() {
        prepareMainMenuItem(
            R.id.menuHome,
            R.drawable.ic_menu_home,
            R.string.menu_home,
            R.id.nav_home
        )

        prepareMainMenuItem(
            R.id.menuMenu,
            R.drawable.ic_menu_menu,
            R.string.menu_menu,
            R.id.nav_menu
        )

        prepareMainMenuItem(
            R.id.menuFavorites,
            R.drawable.ic_menu_favorites,
            R.string.menu_favorites,
            R.id.nav_favorites
        )

        prepareMainMenuItem(
            R.id.menuCart,
            R.drawable.ic_menu_cart,
            R.string.menu_cart,
            R.id.nav_cart
        )

        prepareMainMenuItem(
            R.id.menuProfile,
            R.drawable.ic_menu_profile,
            R.string.menu_profile,
            R.id.nav_profile
        )

        prepareMainMenuItem(
            R.id.menuOrders,
            R.drawable.ic_menu_orders,
            R.string.menu_orders,
            R.id.nav_orders
        )

        prepareMainMenuItem(
            R.id.menuNotices,
            R.drawable.ic_menu_notice,
            R.string.menu_notice,
            R.id.nav_notice
        )

        prepareMainMenuItem(
            R.id.menuAbout,
            R.drawable.ic_menu_about,
            R.string.menu_about,
            R.id.nav_about
        )
    }

    private fun prepareMainMenuItem(item: Int, icon: Int, name: Int, fragment: Int) {
        val menuHome = findViewById<View>(item)
        val menuHomeIvIcon: ImageView = menuHome.findViewById<View>(R.id.ivIcon) as ImageView
        val menuHomeTvName: TextView = menuHome.findViewById<View>(R.id.tvName) as TextView

        menuHomeIvIcon.setImageResource(icon)
        menuHomeTvName.setText(name)
        menuHome.setOnClickListener {
            navController.navigate(fragment)
            drawerLayout.closeDrawer(Gravity.LEFT)
        }
    }

    private fun prepareMainMenuHeader() {
        val tvName = findViewById<TextView>(R.id.tvName)
        val tvEmail = findViewById<TextView>(R.id.tvAuthEmail)

        var firsName = ""
        var lastName = ""

        viewModel.getUserFirstName().observe(this, { t ->
            firsName = t
            tvName.text = "$t $lastName"
        })
        viewModel.getUserLastName().observe(this, { t ->
            lastName = t
            tvName.text = "$firsName $t"
        })
        viewModel.getUserEmail().observe(this, { t ->
            tvEmail.text = t
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.action_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val menuItem = menu.findItem(R.id.action_menu)

        menuItem.setOnMenuItemClickListener {
            navController.navigate(R.id.nav_cart)
            true
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onBackPressed() {
        val navDestination: NavDestination? = navController.currentDestination
        if (navDestination != null && navDestination.id == R.id.nav_home) {
            finish()
            return
        }

        super.onBackPressed()
    }
}