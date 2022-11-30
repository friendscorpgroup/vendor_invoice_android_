package com.frenzin.invoice.teacategory.bottombar.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.frenzin.invoice.R
import com.frenzin.invoice.databinding.ActivityHomeScreenTeaBinding
import com.frenzin.invoice.databinding.FragmentProfileTeaBinding
import com.frenzin.invoice.roomdatabase.DatabaseClass
import com.frenzin.invoice.roomdatabase.UserModelOwnerData
import com.frenzin.invoice.teacategory.bottombar.dashboard.AddCustomerTeaActivity.Companion.TAG
import com.frenzin.invoice.teacategory.ui.HomeScreenActivityTea
import com.frenzin.invoice.watercategory.ui.HomeScreenActivityWater
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout.TabGravity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ProfileTeaFragment : Fragment() {

    private var _binding: FragmentProfileTeaBinding? = null
    private val binding get() = _binding!!
    val RequestPermissionCode = 1
    val CAMERA_REQUEST = 1888
    private var list : List<UserModelOwnerData> ? = null
    var mediaFile : File ? = null
    var uri : Uri ? = null
    var imagePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileTeaBinding.inflate(inflater, container, false)
        container!!.removeAllViews()

        setOnClick()
        EnableRuntimePermission()
        getData()

        return binding.root
    }

    private fun setOnClick() {
        binding.btnEditProfile.setOnClickListener(onClickListener)
        binding.imgCamera.setOnClickListener(onClickListener)
        binding.btnSaveProfile.setOnClickListener(onClickListener)
        binding.switchCompatTea.setOnClickListener(onClickListener)
    }

    private val onClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view!!.id) {
                R.id.btnEditProfile -> {
                    initEditProfileView()
                }
                R.id.imgCamera -> {
                    cameraIntent()
                }
                R.id.switchCompatTea -> {
                    if(!binding.switchCompatTea.isChecked){
                        openAlertDialog()
                    }
                    else
                    {
                        return
                    }
                }
                R.id.btnSaveProfile -> {
                    if (checkAllFields()){
                        saveDataInDB()
                    }
                    else
                    {
                        return
                    }
                }
            }
        }
    }

    private fun openAlertDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Alert!")
            .setMessage("Are you sure you want to switch to water delivery section ?")
            .setCancelable(false)
            .setPositiveButton("Yes"
            ) { dialog, id -> switchToWaterCategory() }
            .setNegativeButton("No"
            ) { dialog, id -> dialog.cancel()
                binding.switchCompatTea.isChecked = true}

        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun switchToWaterCategory() {
       val intent = Intent(requireContext(), HomeScreenActivityWater::class.java)
        startActivity(intent)
    }


    private fun checkAllFields() : Boolean{
        if (binding.etOwnerName.text.toString().isEmpty()) {
            binding.etOwnerName.error = "Please enter your name"
            binding.etOwnerName.requestFocus()
            return false
        }

        if (binding.etShopName.text.toString().isEmpty()) {
            binding.etShopName.error = "Please enter shop name"
            binding.etShopName.requestFocus()
            return false
        }

        if (binding.etPhoneNumber.text.toString().isEmpty()) {
            binding.etPhoneNumber.error = "Please enter mobile number"
            binding.etPhoneNumber.requestFocus()
            return false
        }

        if (binding.etAddress.text.toString().isEmpty()) {
            binding.etAddress.error = "Please enter address"
            binding.etAddress.requestFocus()
            return false
        }

        return true
    }

    private fun saveDataInDB() {

        val ownerName: String = binding.etOwnerName.text.toString()
        val shopName: String = binding.etShopName.text.toString()
        val phoneNumber: String = binding.etPhoneNumber.text.toString()
        val address: String = binding.etAddress.text.toString()
        val gstNumber: String = binding.etGSTnumber.text.toString()

        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = df.format(c)

        list = ArrayList()
        list = DatabaseClass.getDatabase(requireContext())!!.dao!!.getOwnerData( "TEA")

        Log.e(TAG, "list ${list!!.size}")
        Log.e(TAG, "list ${list}")

        if (uri.toString().equals(null)){
            uri = list!![0].qrCodeImage?.toUri()
        }
        Log.e(TAG, "uri ${uri}")

        if (list!!.size == 1){
            DatabaseClass.getDatabase(requireContext())!!.dao!!.updateOwnerDataTea(
                ownerName, shopName, phoneNumber, address, gstNumber,
                uri.toString(),"TEA")

        }

        else if (list!!.size == 0){
            val model = UserModelOwnerData()
            model.customerType = "TEA"
            model.createdDate = formattedDate
            model.ownerName = ownerName
            model.shopName = shopName
            model.phonenumber = phoneNumber
            model.address = address
            model.gstNumber = gstNumber
            model.qrCodeImage = uri.toString()

            DatabaseClass.getDatabase(requireContext())!!.dao!!.insertOwnerData(model)
        }

        showAlertDialog()

        binding.etOwnerName.setText("")
        binding.etShopName.setText("")
        binding.etPhoneNumber.setText("")
        binding.etAddress.setText("")
        binding.etGSTnumber.setText("")
    }

    private fun getData()  {
        list = ArrayList()
        list = DatabaseClass.getDatabase(requireContext())!!.dao!!.getOwnerData("TEA")

        Log.e(TAG, "list ${list!!.size}")
        Log.e(TAG, "list ${list.toString()}")

        if (list!!.size > 0)
        {
            binding.etOwnerName.setText(list!![0].ownerName)
            binding.etShopName.setText(list!![0].shopName)
            binding.etPhoneNumber.setText(list!![0].phonenumber)
            binding.etAddress.setText(list!![0].address)
            binding.etGSTnumber.setText(list!![0].gstNumber)
            binding.imgQRCODEfinal.setImageURI(list!![0].qrCodeImage!!.toUri())
        }
        else if (list!!.isEmpty()){
            initEditProfileView()
        }

    }

    private fun initEditProfileView() {
        binding.btnEditProfile.visibility = View.GONE
        binding.imgQRCode.visibility = View.GONE
        binding.switchCompatTeaLayout.visibility = View.GONE
        binding.btnSaveProfile.visibility = View.VISIBLE
        binding.backBtnProfile.visibility = View.VISIBLE
        binding.imgCamera.visibility = View.VISIBLE
        binding.etOwnerName.isEnabled = true
        binding.etShopName.isEnabled = true
        binding.etPhoneNumber.isEnabled = true
        binding.etAddress.isEnabled = true
        binding.etGSTnumber.isEnabled = true
        binding.tvProfile.text = getString(R.string.edit_profile)
    }

    private fun initProfileView() {
        binding.btnEditProfile.visibility = View.VISIBLE
        binding.imgQRCode.visibility = View.VISIBLE
        binding.switchCompatTeaLayout.visibility = View.VISIBLE
        binding.btnSaveProfile.visibility = View.GONE
        binding.backBtnProfile.visibility = View.GONE
        binding.imgCamera.visibility = View.GONE
        binding.etOwnerName.isEnabled = false
        binding.etShopName.isEnabled = false
        binding.etPhoneNumber.isEnabled = false
        binding.etAddress.isEnabled = false
        binding.etGSTnumber.isEnabled = false
        binding.tvProfile.text = getString(R.string.profile)
        getData()
    }

    fun EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.CAMERA
            )
        ) {
            Toast.makeText(
                requireContext(),
                "CAMERA permission allows us to Access CAMERA app",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.CAMERA
                ), RequestPermissionCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        result: IntArray
    ) {
        when (requestCode) {
            RequestPermissionCode -> if (result.size > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    requireContext(),
                    "Permission Granted, Now your application can access CAMERA.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission Canceled, Now your application cannot access CAMERA.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun cameraIntent() {
//        if (hasPermission(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            mediaFile = getOutputMediaFile()
            uri = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().packageName + ".provider",
                    mediaFile!!
                )
            } else {
                Uri.fromFile(mediaFile)
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(intent, CAMERA_REQUEST)

//        } else {
            /*requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1
            )*/
//        }
    }

    fun getOutputMediaFile(): File? {
        val mediaStorageDir = File(
            Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "cache_image"
        )
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(
                    "Merchant", "Oops! Failed create "
                            + "cache_image" + " directory"
                )
                return null
            }
        }
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        return File(
            mediaStorageDir.path + File.separator
                    + "IMG_" + timeStamp + "_" + Random().nextInt() + ".jpg"
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            imagePath = mediaFile!!.absolutePath
        }
    }

    private fun showAlertDialog() {

        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.RoundedCornersDialog)
        val customLayout: View = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
        builder.setView(customLayout)
        builder.setCancelable(false)

        val btnDone: MaterialButton = customLayout.findViewById(R.id.btnDone)
        val btnAddMore: MaterialButton = customLayout.findViewById(R.id.btnAddMore)
        val tvAdded: TextView = customLayout.findViewById(R.id.tvAdded)

        tvAdded.text = "Profile added successfully"
        btnAddMore.visibility = View.GONE

        val dialog: AlertDialog = builder.create()
        dialog.show()

        btnDone.setOnClickListener {
            dialog.dismiss()
            initProfileView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}