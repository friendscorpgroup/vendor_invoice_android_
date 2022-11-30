package com.frenzin.invoice.watercategory.bottombar.profile

import android.Manifest
import android.app.Activity
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
import com.frenzin.invoice.databinding.FragmentProfileWaterBinding
import com.frenzin.invoice.roomdatabase.DatabaseClass
import com.frenzin.invoice.roomdatabase.UserModelOwnerData
import com.frenzin.invoice.teacategory.bottombar.dashboard.AddCustomerTeaActivity.Companion.TAG
import com.frenzin.invoice.teacategory.ui.HomeScreenActivityTea
import com.frenzin.invoice.watercategory.ui.HomeScreenActivityWater
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileWaterFragment : Fragment() {

    private var _binding: FragmentProfileWaterBinding? = null
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

        _binding = FragmentProfileWaterBinding.inflate(inflater, container, false)
        container!!.removeAllViews()

        setOnClick()
        EnableRuntimePermission()
        getData()

        return binding.root
    }

    private fun setOnClick() {
        binding.btnEditProfileWater.setOnClickListener(onClickListener)
        binding.imgCameraWater.setOnClickListener(onClickListener)
        binding.btnSaveProfileWater.setOnClickListener(onClickListener)
        binding.switchCompatWater.setOnClickListener(onClickListener)
    }

    private val onClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view!!.id) {
                R.id.btnEditProfileWater -> {
                    initEditProfileView()
                }
                R.id.imgCameraWater -> {
                    cameraIntent()
                }
                R.id.switchCompatWater -> {
                    if(!binding.switchCompatWater.isChecked){
                        openAlertDialog()
                    }
                    else
                    {
                        return
                    }
                }
                R.id.btnSaveProfileWater -> {
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

    private fun checkAllFields() : Boolean{
        if (binding.etOwnerNameWater.text.toString().isEmpty()) {
            binding.etOwnerNameWater.error = "Please enter your name"
            binding.etOwnerNameWater.requestFocus()
            return false
        }

        if (binding.etShopNameWater.text.toString().isEmpty()) {
            binding.etShopNameWater.error = "Please enter shop name"
            binding.etShopNameWater.requestFocus()
            return false
        }

        if (binding.etPhoneNumberWater.text.toString().isEmpty()) {
            binding.etPhoneNumberWater.error = "Please enter mobile number"
            binding.etPhoneNumberWater.requestFocus()
            return false
        }

        if (binding.etAddressWater.text.toString().isEmpty()) {
            binding.etAddressWater.error = "Please enter address"
            binding.etAddressWater.requestFocus()
            return false
        }

        return true
    }

    private fun openAlertDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Alert!")
            .setMessage("Are you sure you want to switch to tea delivery section ?")
            .setCancelable(false)
            .setPositiveButton("Yes"
            ) { dialog, id -> switchToTeaCategory() }
            .setNegativeButton("No"
            ) { dialog, id -> dialog.cancel()
                binding.switchCompatWater.isChecked = true}

        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun switchToTeaCategory() {
        val intent = Intent(requireContext(), HomeScreenActivityTea::class.java)
        startActivity(intent)
    }


    private fun saveDataInDB() {

        val ownerName: String = binding.etOwnerNameWater.text.toString()
        val shopName: String = binding.etShopNameWater.text.toString()
        val phoneNumber: String = binding.etPhoneNumberWater.text.toString()
        val address: String = binding.etAddressWater.text.toString()
        val gstNumber: String = binding.etGSTnumberWater.text.toString()

        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = df.format(c)

        list = ArrayList()
        list = DatabaseClass.getDatabase(requireContext())!!.dao!!.getOwnerData("WATER")

      
        if (uri.toString().equals(null)){
            uri = list!![0].qrCodeImage?.toUri()
        }


        if (list!!.size == 1){
            DatabaseClass.getDatabase(requireContext())!!.dao!!.updateOwnerDataWater(
                ownerName, shopName, phoneNumber, address, gstNumber,
                uri.toString(), "WATER")
        }
        else if (list!!.size == 0){
            val model = UserModelOwnerData()
            model.customerType = "WATER"
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

        binding.etOwnerNameWater.setText("")
        binding.etShopNameWater.setText("")
        binding.etPhoneNumberWater.setText("")
        binding.etAddressWater.setText("")
        binding.etGSTnumberWater.setText("")
    }

    private fun getData()  {
        list = ArrayList()
        list = DatabaseClass.getDatabase(requireContext())!!.dao!!.getOwnerData("WATER")

        if (list!!.size > 0)
        {
            binding.etOwnerNameWater.setText(list!![0].ownerName)
            binding.etShopNameWater.setText(list!![0].shopName)
            binding.etPhoneNumberWater.setText(list!![0].phonenumber)
            binding.etAddressWater.setText(list!![0].address)
            binding.etGSTnumberWater.setText(list!![0].gstNumber)
            binding.imgQRCODEfinalWater.setImageURI(list!![0].qrCodeImage!!.toUri())
        }
        else if (list!!.isEmpty()){
            initEditProfileView()
        }
    }

    private fun initEditProfileView() {
        binding.btnEditProfileWater.visibility = View.GONE
        binding.imgQRCodeWater.visibility = View.GONE
        binding.switchCompatWaterLayout.visibility = View.GONE
        binding.btnSaveProfileWater.visibility = View.VISIBLE
        binding.backBtnProfileWater.visibility = View.VISIBLE
        binding.imgCameraWater.visibility = View.VISIBLE
        binding.etOwnerNameWater.isEnabled = true
        binding.etShopNameWater.isEnabled = true
        binding.etPhoneNumberWater.isEnabled = true
        binding.etAddressWater.isEnabled = true
        binding.etGSTnumberWater.isEnabled = true
        binding.tvProfileWater.text = getString(R.string.edit_profile)
    }

    private fun initProfileView() {
        binding.btnEditProfileWater.visibility = View.VISIBLE
        binding.imgQRCodeWater.visibility = View.VISIBLE
        binding.imgQRCODEfinalWater.visibility = View.VISIBLE
        binding.switchCompatWaterLayout.visibility = View.VISIBLE
        binding.btnSaveProfileWater.visibility = View.GONE
        binding.backBtnProfileWater.visibility = View.GONE
        binding.imgCameraWater.visibility = View.GONE
        binding.etOwnerNameWater.isEnabled = false
        binding.etShopNameWater.isEnabled = false
        binding.etPhoneNumberWater.isEnabled = false
        binding.etAddressWater.isEnabled = false
        binding.etGSTnumberWater.isEnabled = false
        binding.tvProfileWater.text = getString(R.string.profile)
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
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
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
            getData()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}