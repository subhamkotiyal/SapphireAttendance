package com.orien.bronsonjones.utils.customImagePickers

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.DialogFragment
import com.webgurus.Constants
import com.webgurus.attendanceportal.R
import kotlinx.android.synthetic.main.dialog_image_picker.*

class GalleryPickerDialog : DialogFragment(), View.OnClickListener {


    companion object {

        fun newInstance(): GalleryPickerDialog {

            return GalleryPickerDialog()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)

    }


    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogZoomAnim

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = false
        return inflater.inflate(R.layout.dialog_image_picker, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txt_dialog_choose_photo.setOnClickListener(this)
        txt_dialog_choose_photo.performClick()
        setListener()
    }

    fun setListener() {

    }

    override fun onResume() {
        super.onResume()
        val window = dialog!!.window
        val size = Point()
        val display = window!!.windowManager.defaultDisplay
        display.getSize(size)
        val width = size.x
        window.setLayout((width * 0.85).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
    }


    override fun onClick(view: View?) {
        when (view?.id) {
    
            R.id.txt_dialog_choose_photo -> {
                val galleryIntent = Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activity?.startActivityForResult(galleryIntent, Constants.REQUEST_GALLERY)
                dismiss()
            }
        }
    }


}