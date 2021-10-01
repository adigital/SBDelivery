package ru.skillbranch.sbdelivery.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.skillbranch.sbdelivery.App
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.local.PrefManager
import ru.skillbranch.sbdelivery.data.remote.res.AddressRes
import ru.skillbranch.sbdelivery.data.remote.res.isFull
import ru.skillbranch.sbdelivery.data.remote.res.toAddressString
import ru.skillbranch.sbdelivery.extensions.dpToIntPx
import ru.skillbranch.sbdelivery.extensions.notifyShort
import ru.skillbranch.sbdelivery.viewmodels.fragments.MapsViewModel


class MapsFragment : Fragment() {

    private lateinit var viewModel: MapsViewModel

    private var isLocationPermissionGranted: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val preferences = PrefManager
    private lateinit var defaultLocation: LatLng
    private lateinit var addressRes: AddressRes
    private var marker: Marker? = null
    //    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    //    private var lastKnownLocation: Location? = null

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        context?.let {
            googleMap.setPadding(
                it.dpToIntPx(16),
                it.dpToIntPx(80),
                it.dpToIntPx(16),
                it.dpToIntPx(72)
            )
        }

        isLocationPermissionGranted.observe(viewLifecycleOwner, {
            googleMap.isMyLocationEnabled = it
        })

        marker = googleMap.addMarker(
            MarkerOptions()
                .position(defaultLocation)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
        )

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f))

        googleMap
            .onMapClickAsFlow()
            .onEach {
                marker?.position = LatLng(it.latitude, it.longitude)
            }
            .debounce(500)
            .onEach {
                requestAddress(it.latitude, it.longitude)
            }
            .launchIn(lifecycleScope)

        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(p0: Marker) {
            }

            override fun onMarkerDrag(p0: Marker) {
            }

            override fun onMarkerDragEnd(p0: Marker) {
                marker?.position = LatLng(p0.position.latitude, p0.position.longitude)
                requestAddress(p0.position.latitude, p0.position.longitude)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuItem = menu.findItem(R.id.action_menu)
        menuItem.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) { // Доступ к местоположению разрешен
                    isLocationPermissionGranted.value = true
                } else { // Доступ к местоположению запрещен
                    notifyShort("Вы запретили определение местоположения!")
                }
            }

        when {
            isPermissionsGranted() -> { // Разрешения получены
                isLocationPermissionGranted.value = true
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> { // Разрешения ранее были запрещены
                askUserPermissions()
            }

            else -> { // Запросим разрешения у пользователя первый раз
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }


        viewModel.authState.observe(viewLifecycleOwner, { isAuth ->
            if (!isAuth) {
                findNavController().navigate(MapsFragmentDirections.actionNavMapsToNavHome())
            }
        })

//        fusedLocationProviderClient =
//            LocationServices.getFusedLocationProviderClient(requireContext())

        initView(view)
    }

    override fun onResume() {
        super.onResume()

        if (isLocationPermissionGranted.value != null) {
            if (!isLocationPermissionGranted.value!!) {
                if (isPermissionsGranted()) {
                    isLocationPermissionGranted.value = true

                    notifyShort("Вы разрешили определение местоположения!")
                }
            }
        } else {
            isLocationPermissionGranted.value = false
        }
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            App.context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            App.context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun askUserPermissions() {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            "Разрешите определение местоположения в настройках?",
            10000
        ).setAction("Да") {
            openAppSettings()
        }.show()
    }

    private fun openAppSettings() {
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        })
    }

    private fun initView(view: View) {
        val etMapAddress: EditText = view.findViewById(R.id.etMapAddress)
        val btnMapSetAddress: AppCompatButton = view.findViewById(R.id.btnMapSetAddress)

        defaultLocation =
            if (preferences.lastKnownLocationLatitude.isNotBlank() && preferences.lastKnownLocationLongitude.isNotBlank()) {
                LatLng(
                    preferences.lastKnownLocationLatitude.toDouble(),
                    preferences.lastKnownLocationLongitude.toDouble()
                )
            } else {
                LatLng(55.753215, 37.622504) // Москва
            }

        requestAddress(defaultLocation.latitude, defaultLocation.longitude)

        viewModel.address.observe(viewLifecycleOwner, {
            etMapAddress.setText(it.toAddressString())
            btnMapSetAddress.isEnabled = it.isFull()
            if (btnMapSetAddress.isEnabled) {
                addressRes = it
            }
        })

        btnMapSetAddress.setOnClickListener {
            preferences.lastKnownLocationLatitude = defaultLocation.latitude.toString()
            preferences.lastKnownLocationLongitude = defaultLocation.longitude.toString()

            findNavController().navigate(
                MapsFragmentDirections.actionMapsFragmentToOrderFragment(
                    addressRes.city,
                    addressRes.street,
                    addressRes.house
                )
            )
        }
    }

//    private fun getDeviceLocation(googleMap: GoogleMap) {
//        try {
//            val locationResult = fusedLocationProviderClient.lastLocation
//            locationResult.addOnCompleteListener(context as Activity) { task ->
//                if (task.isSuccessful) {
//                    lastKnownLocation = task.result
//
//                    lastKnownLocation?.also {
//                        setMarkerPosition(it.latitude, it.longitude)
//
//                        googleMap.moveCamera(
//                            CameraUpdateFactory.newLatLngZoom(
//                                LatLng(it.latitude, it.longitude),
//                                15f
//                            )
//                        )
//                    }
//                } else {
//                    googleMap.moveCamera(
//                        CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f)
//                    )
//                }
//            }
//        } catch (e: SecurityException) {
//            Toast.makeText(App.context, e.localizedMessage, Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun requestAddress(latitude: Double, longitude: Double) {
        viewModel.checkCoordinatesNet(latitude, longitude)
        defaultLocation = LatLng(latitude, longitude)
    }
}

fun GoogleMap.onMapClickAsFlow() = callbackFlow {
    val callback = GoogleMap.OnMapClickListener { result ->
        this.trySend(result).isSuccess
    }

    setOnMapClickListener(callback)

    awaitClose { setOnMapClickListener(null) }
}