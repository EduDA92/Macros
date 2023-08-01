package com.example.macrostracker.ui.diary.foodList.barcodeScanner

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.macrostracker.databinding.FragmentBarcodeScannerBinding
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BarcodeScannerFragment : Fragment() {

    private var _binding: FragmentBarcodeScannerBinding? = null
    private val binding get() = _binding!!

    private lateinit var barcodeScanner: BarcodeScanner


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarcodeScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        /* This will trigger the permission request and if granted launch the camera */
        val callPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (!granted) {
                    Toast.makeText(
                        requireContext(),
                        "Cannot open the camera without that permission",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    startCamera(view, navController)
                }
            }


        if (!isPermissionGranted(CAMERA)) {
            callPermissionRequest.launch(CAMERA)
        } else {
            startCamera(view, navController)
        }


        binding.backButton.setOnClickListener {
            navController.navigateUp()
        }

    }

    private fun startCamera(view: View, navController: NavController) {

        val cameraController = LifecycleCameraController(requireContext())
        val previewView = binding.cameraPreviewView

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_EAN_13)
            .build()
        previewView.overlay.add(ScannerReticle())
        barcodeScanner = BarcodeScanning.getClient(options)

        /* Documentation about this: https://developer.android.com/training/camerax/mlkitanalyzer */
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(requireContext()),
            MlKitAnalyzer(
                listOf(barcodeScanner),
                COORDINATE_SYSTEM_VIEW_REFERENCED,
                ContextCompat.getMainExecutor(requireContext())
            ) { result: MlKitAnalyzer.Result? ->
                val barcodeResults = result?.getValue(barcodeScanner)


                /* Create a Rectangle that has the same size of the canvas one, then check if the bounding
                * box of the barcode is inside of the reticle, if so, search for the food online and create
                * one with the online data */

                val viewCenter = Point(view.width / 2, view.height / 2)
                val width = 400
                val height = 400
                val reticle = Rect(
                    viewCenter.x - width, viewCenter.y - height,
                    viewCenter.x + width, viewCenter.y + height
                )


                val barcodeInCenter = barcodeResults?.firstOrNull { barcode ->
                    val boundingBox = barcode.boundingBox ?: return@firstOrNull false
                    reticle.contains(boundingBox)
                }


                if (barcodeInCenter != null) {
                    barcodeScanner.close()
                    val action =
                        BarcodeScannerFragmentDirections.actionBarcodeScannerFragmentToBarcodeFoodFragment(
                            barcodeInCenter.rawValue ?: ""
                        )

                    navController.navigate(action)
                }


            }

        )

        /* set Flash on Click Listener to turn ON/OFF the flashlight */
        binding.flashButton.setOnClickListener {
            if (cameraController.cameraInfo?.hasFlashUnit() == true) {
                if (cameraController.cameraInfo?.torchState?.value == 0) {
                    cameraController.cameraControl?.enableTorch(true)
                } else {
                    cameraController.cameraControl?.enableTorch(false)
                }
            }
        }

        cameraController.bindToLifecycle(this)
        previewView.controller = cameraController

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val CAMERA = Manifest.permission.CAMERA
    }

    private fun isPermissionGranted(name: String) = ContextCompat.checkSelfPermission(
        requireContext(), name
    ) == PackageManager.PERMISSION_GRANTED

}