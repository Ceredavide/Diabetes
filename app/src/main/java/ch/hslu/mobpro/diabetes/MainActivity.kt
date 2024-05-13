package ch.hslu.mobpro.diabetes

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import ch.hslu.mobpro.diabetes.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase
    private lateinit var productDao: ProductDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityMainBinding.inflate(layoutInflater)
     setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home,
            R.id.navigation_dashboard,
            R.id.navigation_notifications,
            R.id.navigation_enter_manual))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_enter_manual -> {
                    navController.navigate(R.id.navigation_enter_manual)
                    true
                }
                else -> false
            }
        }

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            AppDatabase.NAME
        ).build()

        productDao = db.productDao()

        // Test code
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {

                    //insertSampleProduct()
                    logAllProducts()
                }
            } catch (e: Exception) {
                Log.e("DatabaseError", "Error: ${e.message}")
            }
        }
    }

    private suspend fun insertSampleProduct() {

        productDao.insertProduct(Product(1, "Apple", 30))
    }

    private suspend fun logAllProducts() {

        val products = productDao.getAll()
        products.forEach { product ->
            Log.d("MINE", product.name ?: "Unknown product")
        }
    }
}