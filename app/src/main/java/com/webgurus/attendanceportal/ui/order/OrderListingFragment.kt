package com.webgurus.attendanceportal.ui.order

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.utils.Utils.getFileName
import com.example.baseproject.utils.Utils.snackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.listeners.RecyclerViewClickListenersForOrder
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.*
import com.webgurus.attendanceportal.ui.base.BaseFragment
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_addorder.*
import kotlinx.android.synthetic.main.custom_layout_statusorder.*
import kotlinx.android.synthetic.main.fragment_customers.*
import kotlinx.android.synthetic.main.fragment_orderlisting.*
import kotlinx.android.synthetic.main.fragment_pendingamount.*
import kotlinx.android.synthetic.main.fragment_productmanagement.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class OrderListingFragment : BaseFragment(), RecyclerViewRoleClickListeners,
    RecyclerViewClickListenersForOrder,
    AdapterView.OnItemSelectedListener {

    var sharedPreferences: SharedPreferences? = null
    var mvariantList: ArrayList<OrderVariant>? = ArrayList()
    var mOrderList: ArrayList<OrderData>? = ArrayList()
    var adapter: OrderListingAdapter? = null
    lateinit var spinner: Spinner
    var orderID = 0
    var statusID = 0
    val mListSttaus: ArrayList<String> = ArrayList()
    var dialogStatus: Dialog? = null
    var dialogPayment: Dialog? = null
    var dialogReturn: Dialog? = null
    var iv_backneworder: ImageView? = null

    private var fab_main_order: FloatingActionButton? = null
    private var fb_add_order: FloatingActionButton? = null
    private var fab1_import_order: FloatingActionButton? = null
    private var fab_export_order: FloatingActionButton? = null


    private var fab_open: Animation? = null
    private var fab_close: Animation? = null
    private var fab_clock: Animation? = null
    private var fab_anticlock: Animation? = null

    var textview_mail: TextView? = null
    var textview_share: TextView? = null
    var textview_addorder: TextView? = null
    var isOpen = false
    var selectedImageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_orderlisting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        listeners()
    }


    private fun openAnim() {
        textview_mail!!.setVisibility(View.INVISIBLE)
        textview_share!!.setVisibility(View.INVISIBLE)
        textview_addorder!!.setVisibility(View.INVISIBLE)
        fab_export_order!!.startAnimation(fab_close)
        fab1_import_order!!.startAnimation(fab_close)
        fb_add_order!!.startAnimation(fab_close)
        fab_main_order!!.startAnimation(fab_anticlock)
        fab_export_order!!.setClickable(false)
        fab1_import_order!!.setClickable(false)
        isOpen = false
    }

    private fun closeAnim() {
        textview_mail!!.setVisibility(View.VISIBLE)
        textview_share!!.setVisibility(View.VISIBLE)
        textview_addorder!!.setVisibility(View.VISIBLE)
        fab_export_order!!.startAnimation(fab_open)
        fab1_import_order!!.startAnimation(fab_open)
        fb_add_order!!.startAnimation(fab_open)
        fab_main_order!!.startAnimation(fab_clock)
        fab_export_order!!.setClickable(true)
        fab1_import_order!!.setClickable(true)
        isOpen = true
    }

    private fun listeners() {

        fab_main_order?.setOnClickListener(View.OnClickListener {

            if (isOpen) {
                openAnim()
            } else {
                closeAnim()
            }

        })


        fab_export_order?.setOnClickListener(View.OnClickListener {
            hitApiToGetLinkOrder()
        })

        fab1_import_order?.setOnClickListener(View.OnClickListener {
            selectCSVFile()

        })
        fb_add_order?.setOnClickListener(View.OnClickListener {
            openAnim()
            startActivity(Intent(requireContext(), AddNewOrderActivity::class.java))
        })

    }

    private fun selectCSVFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(Intent.createChooser(intent, "Open CSV"), 11)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 11) {

            if (resultCode == Activity.RESULT_OK) {

                selectedImageUri = data?.data
                val parcelFileDescriptor =

                    requireActivity().contentResolver.openFileDescriptor(
                        selectedImageUri!!,
                        "r", null
                    ) ?: return

                val inputStream =
                    FileInputStream(parcelFileDescriptor.fileDescriptor)
                val file = File(
                    requireActivity().cacheDir,
                    requireActivity().contentResolver.getFileName(selectedImageUri!!)
                )
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)
                val requestFile: RequestBody = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    data?.getData()?.getPath()
                )

                val multipartBody: MultipartBody.Part =
                    MultipartBody.Part.createFormData("import", file.getName(), requestFile)

                hitApitoPostCSV(multipartBody)
            } else {

            }
        }

    }


    private fun hitApitoPostCSV(multipartBody: MultipartBody.Part) {

        showLoading(true)
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.addOrderImport(
                "Bearer " + sharedPreferences!!.getString("Access_Token", ""),
                multipartBody
            )

        call.enqueue(object : retrofit2.Callback<ImportCustomer?> {
            override fun onResponse(
                call: Call<ImportCustomer?>,
                response: Response<ImportCustomer?>
            ) {
                hideProgress()
                try {
                    if (response.body() != null) {
                        rl_rootorder.snackbar(response.body()!!.message.toString())
                    } else {
                        rl_rootorder.snackbar(response.body()!!.message.toString())
                    }

                } catch (e: Exception) {
                    rl_rootorder.snackbar(e.toString())
                }

            }

            override fun onFailure(call: Call<ImportCustomer?>, t: Throwable) {
                hideProgress()
            }

        })
    }


    private fun hitApiToGetLinkOrder() {
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.getOrderExport("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
        call.enqueue(object : retrofit2.Callback<ExportOrderPojo?> {
            override fun onResponse(
                call: Call<ExportOrderPojo?>,
                response: Response<ExportOrderPojo?>
            ) {
                if (response.body() != null) {
                    val url = response.body()!!.message
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ExportOrderPojo?>, t: Throwable) {
                hideProgress()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        hitApitoGetOrderListing()
    }

    private fun initview() {
        fab_main_order = requireView().findViewById(R.id.fab_main_order)
        fab1_import_order = requireView().findViewById(R.id.fab1_import_order)
        fb_add_order = requireView().findViewById(R.id.fb_add_order)
        fab_export_order = requireView().findViewById(R.id.fab_export_order)
        fab_close = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_anticlock);
        textview_mail = requireView().findViewById(R.id.textview_mail)
        textview_share = requireView().findViewById(R.id.textview_share)
        textview_addorder = requireView().findViewById(R.id.textview_addorder)
        iv_backneworder = requireView().findViewById(R.id.iv_backneworder)
        sharedPreferences = requireActivity().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
    }

    private fun showDialogtoUpdateOrderStatus(Itemid: Int, status: Int) {
        mListSttaus.clear()
        mListSttaus.add("Received")
        mListSttaus.add("Approved")
        mListSttaus.add("Dispatched")
        mListSttaus.add("Delivered")
        mListSttaus.add("Cancelled")
        mListSttaus.add("Ready for Dispatched")
        dialogStatus = Dialog(requireContext())
        dialogStatus!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogStatus!!.setCancelable(false)
        dialogStatus!!.setContentView(R.layout.custom_layout_statusorder)
        spinner = dialogStatus!!.findViewById(R.id.sp_status) as Spinner
        val remarks = dialogStatus!!.findViewById(R.id.ed_remarks) as TextInputEditText
        val rl_status = dialogStatus!!.findViewById(R.id.rl_status) as RelativeLayout
        val iv_close = dialogStatus!!.findViewById(R.id.iv_close) as ImageView
        val btn_addstatus = dialogStatus!!.findViewById(R.id.btn_addstatus) as Button
        spinner = Spinner(requireContext())
        spinner.id = 1
        var aa = ArrayAdapter(requireContext(), R.layout.simple_spinner_list, mListSttaus)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val ll = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        iv_close.setOnClickListener(View.OnClickListener {
            dialogStatus!!.dismiss()
        })
        btn_addstatus.setOnClickListener(View.OnClickListener {
            if (remarks.text.toString().equals("")) {
                Toast.makeText(requireContext(), "Please enter the Remark.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                hitApitoAddStatus(Itemid, remarks.text.toString(), statusID.toString())
            }
        })
        rl_status.addView(spinner)
        with(spinner)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@OrderListingFragment
            layoutParams = ll
            prompt = "Select your favourite language"
        }
        if (status == 1) {
            spinner.setSelection(0)
            statusID = 1
        } else if (status == 2) {
            spinner.setSelection(1)
            statusID = 2
        } else if (status == 3) {
            spinner.setSelection(2)
            statusID = 3
        } else if (status == 4) {
            spinner.setSelection(3)
            statusID = 4
        } else if (status == 5) {
            spinner.setSelection(4)
            statusID = 5
        }
        else if (status == 6) {
            spinner.setSelection(5)
            statusID = 7
        }
        dialogStatus!!.show()

    }

    private fun showDialogtoAddPayment(orderID: Int) {
        dialogPayment = Dialog(requireContext())
        dialogPayment!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogPayment!!.setCancelable(false)
        dialogPayment!!.setContentView(R.layout.custom_layout_addpayment)
        val ed_amount = dialogPayment!!.findViewById(R.id.ed_amount) as TextInputEditText
        val iv_close = dialogPayment!!.findViewById(R.id.iv_close) as ImageView
        val btn_addpayment = dialogPayment!!.findViewById(R.id.btn_addpayment) as Button
        btn_addpayment.setOnClickListener(View.OnClickListener {
            if (ed_amount.text.toString().equals("")) {
                Toast.makeText(requireContext(), "Please enter the amount.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                hitApitoAddPayment(orderID.toString(), ed_amount.text.toString())
            }
        })
        iv_close.setOnClickListener(View.OnClickListener {
            dialogPayment!!.dismiss()
        })
        dialogPayment!!.show()

    }

    private fun hitApitoAddStatus(id: Int, remarks: String, status: String) {
        showLoading(true)
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.changeStatus(
            "Bearer " + sharedPreferences!!.getString("Access_Token", ""),
            id, remarks, status
        )
        call.enqueue(object : retrofit2.Callback<UpdateOrderStatusPojo> {
            override fun onResponse(
                call: Call<UpdateOrderStatusPojo?>,
                response: Response<UpdateOrderStatusPojo?>
            ) {
                if (response.body() != null) {
                    hideProgress()
                    if (response.body()!!.message.equals("Order Status Changed")) {
                        Toast.makeText(requireContext(), "Order Status Changed", Toast.LENGTH_SHORT)
                            .show()
                        dialogStatus!!.dismiss()
                        hitApitoGetOrderListing()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "" + response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        hideProgress()
                        dialogStatus!!.dismiss()
                    }

                }
            }

            override fun onFailure(call: Call<UpdateOrderStatusPojo>, t: Throwable) {
                Toast.makeText(requireContext(), "" + t.toString(), Toast.LENGTH_SHORT).show()
                hideProgress()
            }

        })

    }


    private fun hitApitoAddPayment(id: String, amount: String) {
        showLoading(true)
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.addPayment(
                "Bearer " + sharedPreferences!!.getString("Access_Token", ""),
                id,
                amount
            )
        call.enqueue(object : retrofit2.Callback<PaymentAdedPojo> {
            override fun onResponse(
                call: Call<PaymentAdedPojo?>,
                response: Response<PaymentAdedPojo?>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.message.equals("Payment added")) {
                        Toast.makeText(
                            requireContext(),
                            "Payment Added SucessFully",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialogPayment!!.dismiss()
                        hideProgress()
                        hitApitoGetOrderListing()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "" + response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        hideProgress()
                        dialogPayment!!.dismiss()
                    }

                }
            }

            override fun onFailure(call: Call<PaymentAdedPojo>, t: Throwable) {
                Toast.makeText(requireContext(), "" + t.toString(), Toast.LENGTH_SHORT).show()
                hideProgress()
                dialogPayment!!.dismiss()
            }

        })

    }


    private fun hitApitoGetOrderListing() {
        showLoading(true)
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.getOrderListing("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
        call.enqueue(object : retrofit2.Callback<OrderListingPojo> {
            override fun onResponse(
                call: Call<OrderListingPojo?>,
                response: Response<OrderListingPojo?>
            ) {
                if (response.body() != null) {
                    hideProgress()
                    mOrderList!!.clear()
                    mvariantList!!.clear()
                    mOrderList!!.addAll(response.body()!!.data)
                    for (i in 0 until mOrderList!!.size) {
                        mvariantList!!.addAll(mOrderList!!.get(i).variant)
                    }
                    rv_orderlisting.layoutManager =
                        LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                    adapter = OrderListingAdapter(
                        requireActivity(),
                        mOrderList,
                        this@OrderListingFragment,
                        this@OrderListingFragment
                    )
                    rv_orderlisting.adapter = adapter
                    if (mOrderList!!.size == 0) {
                        tv_nodatavailable.visibility = View.VISIBLE
                        rv_orderlisting.visibility = View.GONE
                    }

                }
            }

            override fun onFailure(call: Call<OrderListingPojo>, t: Throwable) {
                val dsg = ""
                hideProgress()
            }

        })
    }


    fun deleteItemDialogOrder(index: Int, roleID: Int) {
        val alertBuilder = AlertDialog.Builder(requireContext())
        alertBuilder.setTitle("Delete")
        alertBuilder.setMessage("Do you want to delete this Order ?")
        alertBuilder.setPositiveButton("Delete") { _, _ ->
            mOrderList!!.remove(mOrderList!!.get(index))
            adapter!!.notifyDataSetChanged()
            hitApitoDeleteOrder(roleID)
        }
        alertBuilder.setNegativeButton("No") { _, _ ->
        }
        alertBuilder.show()
    }


    override fun onClick(position: Int, roleID: Int) {
        deleteItemDialogOrder(position, roleID)
    }

    private fun hitApitoDeleteOrder(id: Int) {
        showLoading(true)
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.deleteOrder("Bearer " + sharedPreferences!!.getString("Access_Token", ""), id)
        // showProgressbar()
        call.enqueue(object : retrofit2.Callback<DeleteOrderPojo> {
            override fun onResponse(
                call: Call<DeleteOrderPojo?>,
                response: Response<DeleteOrderPojo?>
            ) {
                hideProgress()
                if (response.body()!!.message.equals("Order deleted")) {
                    Toast.makeText(
                        requireContext(),
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<DeleteOrderPojo>, t: Throwable) {
                Toast.makeText(requireContext(), "" + t.toString(), Toast.LENGTH_SHORT).show()
                hideProgress()
            }

        })
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (mListSttaus.get(position) == "Received") {
            statusID = 1
        } else if (mListSttaus.get(position) == "Approved") {
            statusID = 2
        } else if (mListSttaus.get(position) == "Dispatched") {
            statusID = 3
        } else if (mListSttaus.get(position) == "Delivered") {
            statusID = 4
        } else if (mListSttaus.get(position) == "Cancelled") {
            statusID = 5
        }
        else if (mListSttaus.get(position) == "Ready for Dispatched") {
            statusID = 7
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onClick(position: Int, id: Int, tittle: String) {
        if (tittle.equals("status")) {
            showDialogtoUpdateOrderStatus(id, position)
        } else if (tittle.equals("payment")) {
            showDialogtoAddPayment(id)
        } else if (tittle.equals("return")) {
            hitApitoReturnPayment(id,position)
            // showDialogtoReturnPayment(id)
        } else {
            Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hitApitoReturnPayment(orderID: Int,position: Int) {
        showLoading(true)
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.orderReturn("Bearer " + sharedPreferences!!.getString("Access_Token", ""), orderID)
        call.enqueue(object : retrofit2.Callback<OrderReturnPojo> {
            override fun onResponse(call: Call<OrderReturnPojo?>, response: Response<OrderReturnPojo?>) {
                hideProgress()
                if (response.body()!!.message.equals("Order Returned")) {
                    Toast.makeText(
                        requireContext(),
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
               //     mOrderList!!.remove(mOrderList!!.get(position))
                   // adapter!!.notifyDataSetChanged()

                } else {
                    Toast.makeText(
                        requireContext(),
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<OrderReturnPojo>, t: Throwable) {
                Toast.makeText(requireContext(), "" + t.toString(), Toast.LENGTH_SHORT).show()
                hideProgress()
            }

        })
    }

    private fun showDialogtoReturnPayment(id: Int) {
        dialogReturn = Dialog(requireContext())
        dialogReturn!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogReturn!!.setCancelable(false)
        dialogReturn!!.setContentView(R.layout.custom_layout_addpayment)
        val ed_amount = dialogPayment!!.findViewById(R.id.ed_amount) as TextInputEditText
        val iv_close = dialogPayment!!.findViewById(R.id.iv_close) as ImageView
        val btn_addpayment = dialogPayment!!.findViewById(R.id.btn_addpayment) as Button
        btn_addpayment.setOnClickListener(View.OnClickListener {
            if (ed_amount.text.toString().equals("")) {
                Toast.makeText(requireContext(), "Please enter the amount.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                hitApitoAddPayment(orderID.toString(), ed_amount.text.toString())
            }
        })
        iv_close.setOnClickListener(View.OnClickListener {
            dialogPayment!!.dismiss()
        })
        dialogPayment!!.show()
    }


}