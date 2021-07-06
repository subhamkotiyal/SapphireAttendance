package com.orien.bronsonjones.utils.customImagePickers

import android.content.ContentValues
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.DialogFragment
import com.webgurus.Constants
import com.webgurus.attendanceportal.R
import kotlinx.android.synthetic.main.dialog_image_picker.*

class VideoPickerDialog : DialogFragment(), View.OnClickListener {


    companion object {

        fun newInstance(): VideoPickerDialog {
            return VideoPickerDialog()
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

        setListener()
    }

    fun setListener() {
        txt_dialog_take_photo.setOnClickListener(this)
        txt_dialog_take_photo.performClick()
        dismiss()
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
            R.id.txt_dialog_take_photo -> {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "NEW")
                val capturedImage = activity?.contentResolver
                        ?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                val takePicktureIntent = Intent()

                takePicktureIntent.type="video/*"
                takePicktureIntent.action=Intent.ACTION_GET_CONTENT

                takePicktureIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImage)
                activity?.startActivityForResult(takePicktureIntent, Constants.REQUEST_VIDEO)
                dismiss()
            }
           
        }
    }


}