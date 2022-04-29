package com.example.android.finalyearproject

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.finalyearproject.databinding.ActivityMainBinding
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.UserRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    val EXTRA_USER = "com.example.android.finalyearproject.sign.User"
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var mainActivityViewModelFactory: MainActivityViewModelFactory
    private lateinit var userData: User
    private var user: FirebaseUser? = null

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

//    override fun onBackPressed() {
//        if (supportFragmentManager.backStackEntryCount > 0) {
//            supportFragmentManager.popBackStack()
//        } else {
//            super.onBackPressed()
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repo = UserRepository()
        mainActivityViewModelFactory = MainActivityViewModelFactory(repo)
        mainActivityViewModel = ViewModelProvider(this, mainActivityViewModelFactory)
            .get(MainActivityViewModel::class.java)
        user = FirebaseAuth.getInstance().currentUser
        initObserve()
        if (user != null) {
            mainActivityViewModel.getUser(user!!.uid)
        } else {
            //TODO No user is signed in
        }
        Log.e("FindCustomer", "MainActivity onCreate complete")

    }

    private fun initObserve() {
        mainActivityViewModel.getUserData.observe(this) {
            didCreateUser(it)
        }
        Log.e("FindCustomer", "MainActivity initObserve complete")

    }

    private fun didCreateUser(user: User) {
        if (user.uId != null) {
            userData = user
            userData.role?.let { setNavigation(it) }
        } else {
            userData = intent.getParcelableExtra(EXTRA_USER)!!
            createUser()
            userData.role?.let { setNavigation(it) }
        }
    }

    private fun createUser() {
        val data = userData.also { it.uId = user?.uid }
        val userRepo = UserRepository()
        userRepo.addUser(data)
    }

    fun setNavigation(role: String) {
        when (role) {
            "ลูกค้า" -> {
                val navHostFragment: NavHostFragment = supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
                navHostFragment.findNavController().setGraph(R.navigation.customer_navigation)

                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                // Passing each menu ID as a set of Ids because each
                // menu should be considered as top level destinations.
                val appBarConfiguration = AppBarConfiguration(setOf(
                    R.id.navigation_appoint,
                    R.id.navigation_find_mechanic,
                    R.id.navigation_profile))
                setupActionBarWithNavController(navController, appBarConfiguration)

                val navView: BottomNavigationView = binding.navView
                navView.inflateMenu(R.menu.bottom_nav_menu)
                navView.setupWithNavController(navController)

            }
            "ช่าง" -> {
                val navHostFragment: NavHostFragment = supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
                navHostFragment.findNavController().setGraph(R.navigation.mechanic_navigation)
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                val appBarConfiguration = AppBarConfiguration(setOf(
                    R.id.navigation_appoint_mech,
                    R.id.navigation_find_customer_mech,
                    R.id.navigation_profile_mech))
                setupActionBarWithNavController(navController, appBarConfiguration)
                val navView: BottomNavigationView = binding.navView
                navView.inflateMenu(R.menu.bottom_nav_menu_mech)
                navView.setupWithNavController(navController)
            }
        }
    }
}