package com.webgurus.network;

import com.google.gson.JsonObject;
import com.webgurus.attendanceportal.pojo.AddBillPojo;
import com.webgurus.attendanceportal.pojo.AddCategoryPojo;
import com.webgurus.attendanceportal.pojo.AddOrderPojo;
import com.webgurus.attendanceportal.pojo.AddPermissionPojo;
import com.webgurus.attendanceportal.pojo.AddProductPojo;
import com.webgurus.attendanceportal.pojo.AddRolePojo;
import com.webgurus.attendanceportal.pojo.AddUnitPojo;
import com.webgurus.attendanceportal.pojo.AgeingReportPojo;
import com.webgurus.attendanceportal.pojo.AttendanceListPojo;
import com.webgurus.attendanceportal.pojo.AttendanceReportPojo;
import com.webgurus.attendanceportal.pojo.BillUpdatePojo;
import com.webgurus.attendanceportal.pojo.CreateCustomerPojo;
import com.webgurus.attendanceportal.pojo.CreateuserPojo;
import com.webgurus.attendanceportal.pojo.CustomerDeletePojo;
import com.webgurus.attendanceportal.pojo.CustomerMappingPojo;
import com.webgurus.attendanceportal.pojo.CustomerUpdatedPojo;
import com.webgurus.attendanceportal.pojo.DeleteBillsPojo;
import com.webgurus.attendanceportal.pojo.DeleteCategoryPojo;
import com.webgurus.attendanceportal.pojo.DeleteOrderPojo;
import com.webgurus.attendanceportal.pojo.DeleteVariantPojo;
import com.webgurus.attendanceportal.pojo.DispatchReportPojo;
import com.webgurus.attendanceportal.pojo.ExportOrderPojo;
import com.webgurus.attendanceportal.pojo.FiledManagersPojo;
import com.webgurus.attendanceportal.pojo.GetAllBillsPojo;
import com.webgurus.attendanceportal.pojo.GetCategoryListingPojo;
import com.webgurus.attendanceportal.pojo.GetCustomerLisiting;
import com.webgurus.attendanceportal.pojo.GetCustomerOutstandingPojo;
import com.webgurus.attendanceportal.pojo.GetFieldManagerPojo;
import com.webgurus.attendanceportal.pojo.GetLatLongBydate;
import com.webgurus.attendanceportal.pojo.GetManagerListingPojo;
import com.webgurus.attendanceportal.pojo.GetRolesPojo;
import com.webgurus.attendanceportal.pojo.GetUserByRole;
import com.webgurus.attendanceportal.pojo.GetUserTargetPojo;
import com.webgurus.attendanceportal.pojo.GpaStatusPojo;
import com.webgurus.attendanceportal.pojo.ImportCustomer;
import com.webgurus.attendanceportal.pojo.ItemReportPojo;
import com.webgurus.attendanceportal.pojo.LinkRole;
import com.webgurus.attendanceportal.pojo.LogOutPojo;
import com.webgurus.attendanceportal.pojo.Loginpojo;
import com.webgurus.attendanceportal.pojo.ManagerAssignPojo;
import com.webgurus.attendanceportal.pojo.MarkAttendancePojo;
import com.webgurus.attendanceportal.pojo.NewLocationPojo;
import com.webgurus.attendanceportal.pojo.OrderDetailsPojo;
import com.webgurus.attendanceportal.pojo.OrderLabelPojo;
import com.webgurus.attendanceportal.pojo.OrderListingPojo;
import com.webgurus.attendanceportal.pojo.OrderReturnPojo;
import com.webgurus.attendanceportal.pojo.PaymentAdedPojo;
import com.webgurus.attendanceportal.pojo.PermissionListPojo;
import com.webgurus.attendanceportal.pojo.PermissionPojo;
import com.webgurus.attendanceportal.pojo.ProductDeletpojo;
import com.webgurus.attendanceportal.pojo.ProductListingPojo;
import com.webgurus.attendanceportal.pojo.RoleDeletedPojo;
import com.webgurus.attendanceportal.pojo.SessionOutPojo;
import com.webgurus.attendanceportal.pojo.SettingPojo;
import com.webgurus.attendanceportal.pojo.TopPerformancePojo;
import com.webgurus.attendanceportal.pojo.TopPerformerPojo;
import com.webgurus.attendanceportal.pojo.TrackerStatus;
import com.webgurus.attendanceportal.pojo.UnitListPojo;
import com.webgurus.attendanceportal.pojo.UpdateCategoryPojo;
import com.webgurus.attendanceportal.pojo.UpdateLocationPojo;
import com.webgurus.attendanceportal.pojo.UpdateOrderStatusPojo;
import com.webgurus.attendanceportal.pojo.UpdateProductPojo;
import com.webgurus.attendanceportal.pojo.UpdateRolePojo;
import com.webgurus.attendanceportal.pojo.UpdatedProfilePojo;
import com.webgurus.attendanceportal.pojo.UpdateprofilePojo;
import com.webgurus.attendanceportal.pojo.UserCurrentLocationPojo;
import com.webgurus.attendanceportal.pojo.UserDeletePojo;
import com.webgurus.attendanceportal.pojo.UserInforDatum;
import com.webgurus.attendanceportal.pojo.UserListingPojo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface GetDataService {

    @GET("/posts/1")
    Call<JsonObject> getAlList();

    @FormUrlEncoded
    @POST("api/login")
    Call<Loginpojo> loginAuth(@Field("login") String login, @Field("password") String password , @Field("device_token")  String access_token );

    @FormUrlEncoded
    @POST("api/markOut")
    Call<SessionOutPojo> sessionCheck(@Field("id") String userID);


    @GET("api/details")
    Call<UpdateprofilePojo> getUserInfo(@Header("Authorization") String tokenvalue, @Header("Accept") String accept);


    @GET("api/bills")
    Call<GetAllBillsPojo> getBills(@Header("Authorization") String tokenvalue, @Header("Accept") String accept);


    @FormUrlEncoded
    @POST("api/bill/delete")
    Call<DeleteBillsPojo> deleteBills(@Header("Authorization") String tokenvalue,@Field("id") Integer billID);


    @FormUrlEncoded
    @POST("api/bill/add")
    Call<AddBillPojo> addBills(@Header("Authorization") String tokenvalue,
                               @Field("bill_no") String billID,
                               @Field("total_payment") String total_payment,
                               @Field("received") String received,
                               @Field("pending") String pending,
                               @Field("customer_id") String customer_id

    );


    @FormUrlEncoded
    @POST("api/bill/update")
    Call<BillUpdatePojo> updateBills(@Header("Authorization") String tokenvalue,
                                     @Field("bill_no") String billID,
                                     @Field("total_payment") String total_payment,
                                     @Field("received") String received,
                                     @Field("pending") String pending,
                                     @Field("customer_id") String customer_id,
                                     @Field("id") String billid

    );

    @GET("api/role")
    Call<GetRolesPojo> getRoles(@Header("Authorization") String tokenvalue);


    @GET("api/report/orderVsDelivered")
    Call<TopPerformerPojo> getTopPerformer(@Header("Authorization") String tokenvalue);

    @GET("api/getManagerList")
    Call<GetManagerListingPojo> getMagersList(@Header("Authorization") String tokenvalue);


    @GET("api/customerList")
    Call<GetCustomerLisiting> getCustomerList(@Header("Authorization") String tokenvalue);

    @GET("api/fieldManager")
    Call<GetFieldManagerPojo> getFiledManager(@Header("Authorization") String tokenvalue);


    @FormUrlEncoded
    @POST("api/filterOrder")
    Call<OrderLabelPojo> filterOrder(@Header("Authorization") String tokenvalue, @Field("id") Integer id, @Field("customer_id") Integer customer_id, @Field("agent_id") Integer agent_id);


    @FormUrlEncoded
    @POST("api/userGraph")
    Call<OrderLabelPojo> filterUser(@Header("Authorization") String tokenvalue, @Field("id") Integer id);

    @FormUrlEncoded
    @POST("api/customerGraph")
    Call<OrderLabelPojo> filterCustomer(@Header("Authorization") String tokenvalue, @Field("id")
            Integer id);

    @FormUrlEncoded
    @POST("api/attendanceGraph")
    Call<AttendanceReportPojo> filterAttendance(@Header("Authorization") String tokenvalue,
                                                @Field("id") Integer id,
                                                @Field("agent_id") Integer agent_id);


    @GET("api/userList")
    Call<UserListingPojo> getUserList(@Header("Authorization") String tokenvalue);

    @GET("api/agingReport")
    Call<AgeingReportPojo> agingReport(@Header("Authorization") String tokenvalue);

    @GET("api/performer")
    Call<TopPerformancePojo> top_performance_report(@Header("Authorization") String tokenvalue);

    @GET("api/dispatchReport")
    Call<DispatchReportPojo> dispatch_report(@Header("Authorization") String tokenvalue);



    @GET("api/report/itemReport")
    Call<ItemReportPojo> item_report(@Header("Authorization") String tokenvalue);



    @FormUrlEncoded
    @POST("api/users_by_role")
    Call<GetUserByRole> getuserByRole(@Header("Authorization") String tokenvalue  ,@Field("role") Integer role);




    @FormUrlEncoded
    @POST("api/deleteUser")
    Call<UserDeletePojo> deleteUser(@Header("Authorization") String tokenvalue,
                                    @Field("id") Integer userID);


    @GET("api/role")
    Call<GetRolesPojo> getRoleListing(@Header("Authorization") String tokenvalue);

    @GET("api/roleExport")
    Call<LinkRole> getRolelink(@Header("Authorization") String tokenvalue);


    @Multipart
    @POST("api/roleImport")
    Call<LinkRole> addcsv(@Header("Authorization") String tokenvalue,

                          @Part MultipartBody.Part file);


    @Multipart
    @POST("api/customerImport")
    Call<LinkRole> addcsvforcustomer(@Header("Authorization") String tokenvalue,

                                     @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/addRole")
    Call<AddRolePojo> addRoles(@Header("Authorization") String tokenvalue,
                               @Header("Accept") String accept,
                               @Field("role") String role);

    @FormUrlEncoded
    @POST("api/addOrder")
    Call<AddOrderPojo> addOrder(@Header("Authorization") String tokenvalue,
                                @Field(" product_id[0]") Integer product_id,
                                @Field(" quantity[0]") String quantity,
                                @Field(" price[0]") String price,
                                @Field(" customer_id") Integer customer_id,
                                @Field(" address") String address,
                                @Field(" instructions") String instruction);


    @FormUrlEncoded
    @POST("api/roleDelete")
    Call<RoleDeletedPojo> deleteRole(@Header("Authorization") String tokenvalue, @Header("Accept") String accept,
                                     @Field("id") Integer roleid);


    @FormUrlEncoded
    @POST("api/updateRole")
    Call<UpdateRolePojo> updateRole(@Header("Authorization") String tokenvalue, @Header("Accept") String accept,
                                    @Field("id") Integer roleid,
                                    @Field("role") String role);


    @GET("api/categoryList")
    Call<GetCategoryListingPojo> getCategoryListing(@Header("Authorization") String tokenvalue, @Header("Accept") String accept
    );


    @FormUrlEncoded
    @POST("api/order/view")
    Call<OrderDetailsPojo> viewOrder(@Header("Authorization") String tokenvalue,
                                     @Field("id") Integer orderid);


    @FormUrlEncoded
    @POST("api/addCategory")
    Call<AddCategoryPojo> addCategory(@Header("Authorization") String tokenvalue,
                                      @Header("Accept") String accept,
                                      @Field("name") String role);


    @FormUrlEncoded
    @POST("api/deleteCategory")
    Call<DeleteCategoryPojo> deleteCategory(@Header("Authorization") String tokenvalue, @Header("Accept") String accept,
                                            @Field("id") Integer catID);


    @FormUrlEncoded
    @POST("api/updateCategory")
    Call<UpdateCategoryPojo> updateCategory(@Header("Authorization") String tokenvalue,
                                            @Header("Accept") String accept,
                                            @Field("id") Integer catId,
                                            @Field("name") String catName);


    @GET("api/unitList")
    Call<UnitListPojo> getUnitList(@Header("Authorization") String tokenvalue);

    @GET("api/productVariant/{id}")
    Call<UnitListPojo> getproductVariant(@Header("Authorization") String tokenvalue, @Path("id") Integer id);

    @FormUrlEncoded
    @POST("api/addUnit")
    Call<AddUnitPojo> getAddUnit(@Header("Authorization") String tokenvalue, @Field("unit") String unit);


    @FormUrlEncoded
    @POST("api/unitDelete")
    Call<AddUnitPojo> getDeleteUnit(@Header("Authorization") String tokenvalue, @Field("id") Integer unitid);


    @FormUrlEncoded
    @POST("api/updateUnit")
    Call<AddUnitPojo> getUpdateUnit(@Header("Authorization") String tokenvalue, @Field("id") Integer unitid,
                                    @Field("unit") String value);


    @GET("api/productList")
    Call<ProductListingPojo> getProductLisitng(@Header("Authorization") String tokenvalue, @Header("Accept") String accept
    );

    @FormUrlEncoded
    @POST("api/productDelete")
    Call<ProductDeletpojo> delteProduct(@Header("Authorization") String tokenvalue, @Header("Accept") String accept,
                                        @Field("id") Integer productID);

    @FormUrlEncoded
    @POST("api/variantDelete")
    Call<DeleteVariantPojo> delteVariant(@Header("Authorization") String tokenvalue,
                                         @Field("id") Integer productID);


    @FormUrlEncoded
    @POST("api/orderDelete")
    Call<DeleteOrderPojo> deleteOrder(@Header("Authorization") String tokenvalue,
                                      @Field("id") Integer id);



    @FormUrlEncoded
    @POST("api/orderReturn")
    Call<OrderReturnPojo> orderReturn(@Header("Authorization") String tokenvalue,
                                      @Field("id") Integer id);


    @FormUrlEncoded
    @POST("api/addProduct")
    Call<AddProductPojo> addProduct(@Header("Authorization") String tokenvalue,
                                    @Header("Accept") String accept,
                                    @Field("category_id[]") Integer category_id,
                                    @Field("unit_id[]") Integer unit_id,
                                    @Field("min_price[]") String min_price,
                                    @Field("max_price[]") String max_price,
                                    @Field("price[]") String price,
                                    @Field("quantity_left[]") String quantity_left,
                                    @Field("name") String productname,
                                    @Field("in_stock[]") Integer in_stock


    );


    @FormUrlEncoded
    @POST("api/updateProduct")
    Call<UpdateProductPojo> updateProduct(@Header("Authorization") String tokenvalue,
                                          @Header("Accept") String accept,
                                          @Field("category_id") Integer category_id,
                                          @Field("unit_id") Integer unit_id,
                                          @Field("min_price") String min_price,
                                          @Field("max_price") String max_price,
                                          @Field("price") String price,
                                          @Field("quantity_left") String quantity_left,
                                          @Field("name") String productname,
                                          @Field("in_stock") Integer in_stock,
                                          @Field("variant_id") Integer variant_id


    );


    @FormUrlEncoded
    @POST("api/update")
    Call<UpdatedProfilePojo> updateprofile(@Header("Authorization") String tokenvalue,
                                           @Field("phone_number") String phone_number,
                                           @Field("dob") String dob,
                                           @Field("city") String city,
                                           @Field("name") String username,
                                           @Field("email") String email);


    @FormUrlEncoded
    @POST("api/addRole")
    Call<AddRolePojo> addRole(@Header("Authorization") String tokenvalue,
                              @Field("role") String phone_number);


    @FormUrlEncoded
    @POST("api/createUser")
    Call<CreateuserPojo> createUser(@Header("Authorization") String tokenvalue,
                                    @Field("name") String name,
                                    @Field("email") String email,
                                    @Field("phone_number") String phone_number,
                                    @Field("password") String password,
                                    @Field("role_id") String role_id,
                                    @Field("dob") String dob,
                                    @Field("manager_id") String manager_id,
                                    @Field("city") String city,
                                    @Field("state") String state


    );

    @FormUrlEncoded
    @POST("api/gpsStatus")
    Call<CreateuserPojo> gpsStatus(@Header("Authorization") String tokenvalue,
                                   @Field("name") String name

    );


    @FormUrlEncoded
    @POST("api/updateUser")
    Call<CreateuserPojo> updateUser(@Header("Authorization") String tokenvalue,
                                    @Field("name") String name,
                                    @Field("email") String email,
                                    @Field("phone_number") String phone_number,
                                    @Field("password") String password,
                                    @Field("role_id") String role_id,
                                    @Field("dob") String dob,
                                    @Field("manager_id") String manager_id,
                                    @Field("city") String city,
                                    @Field("state") String state,
                                    @Field("id") int id


    );


    @FormUrlEncoded
    @POST("api/addCustomer")
    Call<CreateCustomerPojo> createCustomer(@Header("Authorization") String tokenvalue,
                                            @Field("first_name") String first_name,
                                            @Field("middle_name") String middle_name,
                                            @Field("last_name") String last_name,
                                            @Field("email") String email,
                                            @Field("phone_number") String phone_number,
                                            @Field("address") String address,
                                            @Field("secondary_address") String secondary_address,
                                            @Field("state") String state,
                                            @Field("city") String city,
                                            @Field("dob") String dob,
                                            @Field("pincode") String pincode

    );

    @FormUrlEncoded
    @POST("api/updateCustomer")
    Call<CustomerUpdatedPojo> updateCustomer(@Header("Authorization") String tokenvalue,
                                             @Field("first_name") String first_name,
                                             @Field("middle_name") String middle_name,
                                             @Field("last_name") String last_name,
                                             @Field("email") String email,
                                             @Field("phone_number") String phone_number,
                                             @Field("address") String address,
                                             @Field("secondary_address") String secondary_address,
                                             @Field("state") String state,
                                             @Field("city") String city,
                                             @Field("dob") String dob,
                                             @Field("pincode") String pincode,
                                             @Field("id") Integer customerID

    );


    @FormUrlEncoded
    @POST("api/customerDelete")
    Call<CustomerDeletePojo> deleteCustomer(@Header("Authorization") String tokenvalue,
                                            @Field("id") Integer id);

    @Multipart
    @POST("api/update")
    Call<JsonObject> updateProfiless(@Header("Authorization") RequestBody accessToken,
                                     @Part("phone_number") RequestBody phone_number,
                                     @Part("dob") RequestBody dob,
                                     @Part("city") RequestBody city,
                                     @Part("username") RequestBody username,
                                     @Part("email") RequestBody email,
                                     @Part MultipartBody.Part image
    );


    @FormUrlEncoded
    @POST("api/mark")
    Call<MarkAttendancePojo> markAttendanceForTimeIn(@Header("Authorization") String tokenvalue,
                                                     @Field("lat") String lat,
                                                     @Field("long") String longit,
                                                     @Field("in_time") String in_time,
                                                     @Field("user_id") String user_id,
                                                     @Field("type") String type,
                                                     @Field("battery") String battery);


    @FormUrlEncoded
    @POST("api/mark")
    Call<MarkAttendancePojo> markAttendanceForTimeOut(@Header("Authorization") String tokenvalue,
                                                      @Field("lat") String lat,
                                                      @Field("long") String longit,
                                                      @Field("out_time") String in_time,
                                                      @Field("user_id") String user_id,
                                                      @Field("type") String type,
                                                      @Field("battery") String battery,
                                                      @Field("id") String attendance_id,
                                                      @Field("remarks") String remarks);


    @FormUrlEncoded
    @POST("api/postLocation")
    Call<UpdateLocationPojo> updateLatLong(@Header("Authorization") String tokenvalue,
                                           @Field("lat") String lat,
                                           @Field("long") String longit,
                                           @Field("attendance_id") String attendance_id);

    @FormUrlEncoded
    @POST("api/postLocation")
    Call<UpdateLocationPojo> updateLatLongforUserID(@Header("Authorization") String tokenvalue,
                                                    @Field("lat") String lat,
                                                    @Field("long") String longit,
                                                    @Field("user_id") String userID,
                                                    @Field("battery") String battery,
                                                    @Field("attendance_id") String attendance_id);

//    @FormUrlEncoded
//    @POST("api/postLocationBulk")
//    Call<NewLocationPojo> updateLatLongforUserIDBulk(@Header("Authorization") String tokenvalue,
//                                                     @Field("location") String jsonlocation);

    @FormUrlEncoded
    @POST("api/postLocationGlobal")
    Call<NewLocationPojo> updateLatLongforUserIDBulk(@Header("Authorization") String tokenvalue,
                                                     @Field("location") String jsonlocation);
    @FormUrlEncoded
    @POST("api/postLocationGlobalTemp")
    Call<NewLocationPojo> updateLatLongforUserIDBulkTemp(@Header("Authorization") String tokenvalue,
                                                     @Field("location") String jsonlocation);

    @GET("api/logout")
    Call<LogOutPojo> logout(@Header("Authorization") String tokenvalue ) ;

    @FormUrlEncoded
    @POST("api/gpsStatus")
    Call<GpaStatusPojo> updateGpsStatus(@Header("Authorization") String tokenvalue,
                                        @Field("gps_status") Integer gps_status,
                                        @Field("user_id") String user_id,
                                        @Field("lat") String lattitude,
                                        @Field("long") String longitude,
                                        @Field("battery_status") Integer battery,
                                        @Field("address") String address,
                                        @Field("version") String version
    );


    @FormUrlEncoded
    @POST("api/user_current_location")
    Call<UserCurrentLocationPojo> user_current_location(@Header("Authorization") String tokenvalue,
                                                        @Field("user_id") String user_id
    );



    @FormUrlEncoded
    @POST("api/report/customerMap")
    Call<CustomerMappingPojo> get_customermapping(@Header("Authorization") String tokenvalue,
                                                    @Field("id") Integer user_id
    );


    @GET("api/attendance")
    Call<AttendanceListPojo> getAllAttendanceList(@Header("Authorization") String tokenvalue);


    @GET("api/dashboard")
    Call<UserInforDatum> getAllUserInfo(@Header("Authorization") String tokenvalue);


    @GET("api/attendance/{id}")
    Call<AttendanceListPojo> getSingleUserAttendanceList(@Header("Authorization") String tokenvalue, @Path("id") Integer version);


    @GET("api/status")
    Call<TrackerStatus> getTrackerStatus(@Header("Authorization") String tokenvalue);

    @FormUrlEncoded
    @POST("api/location")
    Call<GetLatLongBydate> getLatLongByDate(@Header("Authorization") String tokenvalue, @Field("attendance_id") String attendance_id);


    @GET("api/orderList")
    Call<OrderListingPojo> getOrderListing(@Header("Authorization") String tokenvalue);


    @GET("api/orderStatus/7")
    Call<OrderListingPojo> getdispatchedreadyListing(@Header("Authorization") String tokenvalue);


    @GET("api/orderStatus/3")
    Call<OrderListingPojo> getdispatchedItemsListing(@Header("Authorization") String tokenvalue);

    @GET("api/orderStatus/2")
    Call<OrderListingPojo> getapprovedOrder(@Header("Authorization") String tokenvalue);


    @GET("api/permissionList")
    Call<PermissionListPojo> getPermissionList(@Header("Authorization") String tokenvalue);


    @FormUrlEncoded
    @POST("api/payment")
    Call<PaymentAdedPojo> addPayment(@Header("Authorization") String tokenvalue,
                                     @Field("id") String id,
                                     @Field("amount") String amount


    );



    @FormUrlEncoded
    @POST("api/setting")
    Call<SettingPojo> addNumberofDays(@Header("Authorization") String tokenvalue,
                                      @Field("days") String days


    );


    @FormUrlEncoded
    @POST("api/changeStatus")
    Call<UpdateOrderStatusPojo> changeStatus(@Header("Authorization") String tokenvalue,
                                             @Field("id") Integer id,
                                             @Field("remarks") String remarks,
                                             @Field("status") String status

    );


    @FormUrlEncoded
    @POST("api/report/userGetDetail")
    Call<GetUserTargetPojo> getUserTraget(@Header("Authorization") String tokenvalue,
                                         @Field("id") Integer id,
                                         @Field("type") String type,
                                         @Field("date") String date

    );


    @GET("api/fieldManager")
    Call<FiledManagersPojo> getFieldManager(@Header("Authorization") String tokenvalue);

    @FormUrlEncoded
    @POST("api/assign")
    Call<ManagerAssignPojo> assignmanager(@Header("Authorization") String tokenvalue,
                                          @Field("user_id") Integer user_id,
                                          @Field("customer_id") String customer_id

    );


    @GET("api/customerExport")
    Call<ImportCustomer> getCustomerExport(@Header("Authorization") String tokenvalue);


    @Multipart
    @POST("api/userImport")
    Call<ImportCustomer> addUserImport(@Header("Authorization") String tokenvalue, @Part MultipartBody.Part file);


    @GET("api/userExport")
    Call<ImportCustomer> getUserExport(@Header("Authorization") String tokenvalue);

    @GET("api/categoryExport")
    Call<ImportCustomer> getCategoryExport(@Header("Authorization") String tokenvalue);


    @Multipart
    @POST("api/categoryImport")
    Call<ImportCustomer> addCategoryImport(@Header("Authorization") String tokenvalue,

                                           @Part MultipartBody.Part file);


    @GET("api/roleExport")
    Call<ImportCustomer> getExportUnit(@Header("Authorization") String tokenvalue);

    @Multipart
    @POST("api/roleImport")
    Call<ImportCustomer> addImportUnit(@Header("Authorization") String tokenvalue,

                                       @Part MultipartBody.Part file);

    @Multipart
    @POST("api/productImport")
    Call<ImportCustomer> addProductImport(@Header("Authorization") String tokenvalue, @Part MultipartBody.Part file);

    @GET("api/productExport")
    Call<ImportCustomer> getProductExport(@Header("Authorization") String tokenvalue);


    @GET("api/orderExport")
    Call<ExportOrderPojo> getOrderExport(@Header("Authorization") String tokenvalue);


    @Multipart
    @POST("api/orderImport")
    Call<ImportCustomer> addOrderImport(@Header("Authorization") String tokenvalue, @Part MultipartBody.Part file);


    @GET("api/moduleList")
    Call<PermissionPojo> getAllPermissions(@Header("Authorization") String tokenvalue);

    @FormUrlEncoded
    @POST("api/addPermission")
    Call<AddPermissionPojo> addPermission(@Header("Authorization") String tokenvalue,
                                          @Field("role_id") Integer role_id,
                                          @Field("permission") String permission

    );


    @GET("api/customerOutstanding")
    Call<GetCustomerOutstandingPojo> getCustomerOutstanding(@Header("Authorization") String tokenvalue);


}