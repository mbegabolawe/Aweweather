package com.example.aweweather.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.aweweather.R
import com.example.aweweather.data.models.Coord
import com.example.aweweather.databinding.ActivityMainBinding
import com.example.aweweather.ui.view.viewmodel.MainActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainActivityViewModel by viewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)


        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkPermissions()

        viewModel.title.observe(this) {
                binding.toolbar.title = it
        }

        //Use dynamic primary color to set Toolbar and Status bar colors
        viewModel.primaryColor.observe(this) {
            binding.toolbar.setBackgroundColor(ContextCompat.getColor(this@MainActivity, it))

            with (window) {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = ContextCompat.getColor(this@MainActivity, it)
            }
        }

        viewModel.loading.observe(this) {
            binding.loader.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) {
            val builder = AlertDialog.Builder(this)
                .setTitle(getString(R.string.error))
                .setMessage(it)
                .setPositiveButton(getString(R.string.ok)) { dialogInterface, i ->
//                    finish() //Handle errors differently for no connectivity
                    dialogInterface.dismiss()
                }
            builder.create().show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchWeatherByLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            viewModel.getWeatherModel(Coord(it.latitude, it.longitude))
            //Add: Store location & handle failure
        }
    }

    //Show rationale to user for permission requirement before requesting directly
    private fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                fetchWeatherByLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                AlertDialog.Builder(this)
                    .setTitle("Location permission required")
                    .setMessage("Location access is required to ensure a top notch experience, weather right where you are")
                    .setPositiveButton(getString(R.string.ok)) { dialogInterface, i ->
                        requestPermissionLauncher.launch(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    }
                    .setNegativeButton(getString(R.string.cancel)) {dialogInterface, i ->
                        dialogInterface.dismiss()
                        viewModel.getWeatherByCity("Cape Town")
                    }
                    .show()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted: Boolean ->
        if (isGranted) {
            fetchWeatherByLocation()
        } else {
            //Redirect user to Settings and not launch permissions request
            val builder = AlertDialog.Builder(this)
                .setTitle(getString(R.string.permission_header))
                .setMessage(getString(R.string.permission_body))
                .setPositiveButton(getString(R.string.ok)) { dialogInterface, i ->
                    dialogInterface.dismiss()
                    finish()
                }
                .setNegativeButton(getString(R.string.cancel))  { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                    viewModel.getWeatherByCity("Cape Town")
                }
            builder.create().show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}