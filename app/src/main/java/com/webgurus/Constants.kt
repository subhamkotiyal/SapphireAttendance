package com.webgurus


/**
 * Created by android on 2/11/17.
 */
object Constants {
    const val CLICK_ON_LOGOUT = "clcik_on_logout"
    const val STATIC_HEIGHT = "static_height"
  //  const val BASE_URL = "https://bronsonstageapi.essentialdemo.com"
    const val TOKEN = "token"
    const val PICK_UP = 420
    const val Accident_id="accidentId"
    const val Accident_Date="accidentdate"
    const val LOGIN_API = "/connect/token"
    const val FORGOT_PASSWORD = "/api/Account/forgotpassword"
    const val SIGN_UP_API = "/api/Account/signup"
    const val Add_MEDIA_API = "/api/FileUpload/Upload"
    const val GET_ACCIDENT_DETAILS = "/api/Accident/GetAccidentInfoByUser"
    const val ADD_ACCIDENT_INFO = "/api/Accident/AddUpdAccidentInfo"
    const val Get_Claim = "/api/Account/claims"
    const val Verified_User = "/api/Account/verifyuser"
    const val AddContact = "/api/Contact/Post"
    const val Add_Tractor = "/api/Tracker/post"

    const val Province_api="/api/Province/all"
    const val Preferred_Offices="api/OfficeLocation/list"
    const val Book_Appointment="api/BookAppointment/Post"
    const val faq_api="api/Faq/get"
    const val Get_Symptoms=" /api/Status/get"
    const val TrackerBuUser=" /api/Status/gettrackerbyuserid"
    const val GetTrackerByID=" /api/Status/gettrackerbyid"
    const val PostStatusTrackerApi="api/Status/post"
    const val Get_Lookup="/api/LookUp/GetLookUpData"

    const val get_accident_info="api/Profile/getaccidentinfo"

    const val ADD_PROFILE_ACCIDENT_INFO = "/api/Profile/AddUpdAccidentInfo"




    const val REQUEST_CAMERA = 1
    const val REQUEST_VIDEO = 132
    const val REQUEST_GALLERY = 291
    const val RECORD_VIDEO = 292
    const val REQUEST_GPS_ENABLE_PERMISSION = 100
    const val LAT = "Lat"
    const val LNG = "lng"
    const val ISEDIT = "IsEditProfile"
    const val DIALOG_SHOWN = "dialog_shown"
    const val PERMISSION_READ_EXTERNAL_STORAGE = 123
    const val REQUEST_SELECT_PLACE = 500
    const val REQUEST_CODE_ASK_PERMISSIONS = 100
    const val REQUEST_CODE_CALENDAR_PERMISSION = 133
    const val PERMISSION_REQUEST_CODE = 98
    const val LOCATION_PERMISSION_REQUEST_CODE = 101
    const val TYPE_SIMPLE = 0
    const val TYPE_BREAKFAST = 1
    const val TYPE_LUNCH = 2
    const val TYPE_TASK = 3
    const val TYPE_UNVERIFIED_DOC = 4
    const val MODEL = "model"


    const val NOTIFICATION = "notification"
    const val SPLASH_TIME_OUT: Long = 5000
    const val HANDLER_DELAY_TIME: Long = 2000


    const val LOGIN = "1"
    const val SIGN_UP = "2"
    const val INVITATION = 101
    const val DELETE = 102
    const val REJECT = 103
    const val ACCEPT = 104
    const val RESEND_REQUEST = 105


    const val ACCESS_TOKEN = "access_token"
    const val REFRESH_TOKEN="refresh_token"
    const val Firebase_Token = "firebasetoken"
    const val User_ID = "user_id"
    const val Spinner_Position = "spinner_position"
    const val Status_UpdateTrackorData = "status_update_trackor"
    const val Status_Categories = "status_categories"


    const val APPOINTMENT = "appointment"
    const val APPOINTMENT_ID = "appointment_id"
    const val PRIORITY = "priority"
    const val ADDRESS_ID = "address_id"
    const val NOTI_COUNT = "noti_count"
    const val IS_LOCKED = "is_locked"
    const val TODO = "todo"
    const val ISSUCCESS = "is_success"
    const val PLANS = "plan"


    const val FROM_TODO = 91
    const val FROM_APPOINTMENT = 92
    const val FROM_REP_TEAM = 93


    var DATA_LIMIT = 100
    var DATA_DOCTOR_LIMIT = 20
    var LOAD_MORE_LIMIT = 20

    const val DATE_FORMAT = "MMM dd, yyyy"
    const val PDF_DATE_ = "pdfDate"
    const val PDF_TIME_START = "start_time"
    const val PDF_TIME_END = "end_time"
    const val PDF_SELECTED_LIST = "checked_list"
    const val ALL_SELECTED = "all_selected"


    const val BREAK_FAST = 1
    const val LUNCH = 2

    const val DOCTOR_PRIORITY_LOW = 1
    const val DOCTOR_PRIORITY_MEDIUM = 2
    const val DOCTOR_PRIORITY_HIGH = 3

    const val REQUST_FROM_SCREEN = "request_from_screen"
    const val ADD_UPDATE_DOCTOR_SCREEN = 1
    const val SUGGESTION_SCREEN = 2
    const val FROM_UNVERIFIED_DOCTOR_SCREEN = 3
    const val SUGGESTION_TYPE = "suggestion_type"
    const val PLAN = "list_plan"

    const val SUBSCRIPTION = "subscription"
    const val PROFILE_MODE = "profile_mode"
    const val DOCTOR_PRIORITY = "doctor_priority"
    const val PROFILE_MODE_CREATE = 1
    const val PROFILE_MODE_UPDATE = 2
    const val SUGGESTION_DATA = "suggestion_data"


    const val TIME_FORMAT = "h:mm a"
    const val APP_HIDDEN_FOLDER = "/.besttyme"


    //booking schedule with doctor
    const val BOOKING_TIME = "booking_time"
    const val DOCTOR_ID = "booking_time"
    const val START_TIME = "start_time"
    const val BOOKING_ITEM = "booking_item"
    const val SUCCESS_MESSAGE = "success_message"
    const val PLAN_EXPIRE = "plan_expire"
    const val ADD_CONTACT = "add_contact"

    const val FRAGMENT_TYPE = "fragment_type"
    const val BUNDLE_DATA = "bundle_data"
    const val DOCTOR_INFO = "doctor_info"
    const val CONTACT_ID = "contact_id"
    const val DATA_ITEM = "data_item"
    const val REP_DATA_ITEM = "rep_data_item"

    const val PLAN_DATA = "plan_data"


    //navigate to fragment
    const val VERIFIED_DOCTOR_FRAGMENT = 1
    const val UNVERIFIED_DOCTOR_FRAGMENT = 2
    const val ADD_DOCTOR_FRAGMENT = 3
    const val CREATE_NEW_APPOINTMENT = 4
    const val VERIFIED_DOCTOR_FRAGMENT_EDIT_APPOINTMENT = 5
    const val UNVERIFIED_DOCTOR_FRAGMENT_EDIT_APPOINTMENT = 6
    const val CHANGE_PASSWORD = 7
    const val TERMS_POLICY = 8
    const val FAQ = 9
    const val CONTACT_US = 10
    const val EDIT_PROFILE = 11
    const val CARD_PAYMENT = 15
    const val DOCTOR_FROM_PUSH_WITH_ADDTO_CONTACT = 12
    const val DOCTOR_FROM_PUSH = 13
    const val DOC_INVITATION = 199
    const val VIEW_DOCTOR = 200
    const val FOR_NOTIFICATION = 1000


    const val SELECTED_ARRAY_ID = "array"


    internal interface httpcodes {
        companion object {

            val STATUS_OK = 200
            val STATUS_BAD_REQUEST = 400
            val STATUS_SESSION_EXPIRED = 401
            val STATUS_PLAN_EXPIRED = 403
            val STATUS_VALIDATION_ERROR = 404
            val STATUS_SERVER_ERROR = 500
            val STATUS_UNKNOWN_ERROR = 503
            val STATUS_API_VALIDATION_ERROR = 422

        }
    }

    const val SNACK_BAR_DURATION = 2500

    var iscallfromworker = ""
    var iscallfromreferral = ""
    var iscallfromexpense = ""
    const val Postmedication="/api/MyData/postmedication"

    const val Add_MEDCATION_API = "/api/MyData/postmedication"
    const val Add_TREATMENT_API = "/api/MyData/posttreatment"
    const val Add_EXPENSE_API = "/api/MyData/postexpense"


    const val Add_School_API = "/api/MyData/postempschool"
    const val Add_emp_Work_API = "/api/MyData/postempwork"


    const val Post_Service_API = "/api/MyData/postservices"
    const val Post_Referral_API = "/api/MyData/postreferral"
    const val IS_LOGOUT = "is_logout"

    const val IMAGE_UPLOAD_FAILURE_MESSAGE = "Uploaded failure please try again!"
    const val IMAGE_UPLOADED_MESSAGE = "Successfully uploaded"
    //FAILURE MESSAGES
    const val SOMETHING_WENT_WRONG = "Something went wrong please try again later!"
    const val SESSION_EXPIRED = "Sorry, looks like you are logged in another device with the same user."


    var DEVICE_TOKEN_UPDATED = "device_token_updated"

    //menu constants
    const val MENU_VIEW_SCHEDULE = "view Schedule"
    const val MENU_VIEW_TARGETS = "view Targets"
    const val MENU_FULL_DOCTOR_DATABASE = "Full Doctor Database"
    const val MENU_UPGRADE_ACCOUNT = "Upgrade Account"
    const val MENU_ADD_TASK = "Add Task"
    const val MENU_REP_TEAM = "Rep Team"
    const val MENU_TRANSACTION_HISTORY = "Transaction history"
    const val MENU_SAMPLE_REQUEST = "Sample Request"
    const val PROGRAM_REQUEST = "Program Request"
    const val MENU_HELP = "Help"
    const val MENU_VIEW_ANALYTICS = "View Analytics"
    const val MENU_SETTINGS = "Settings"
    const val MENU_LOGOUT = "Logout"
    const val MENU_EDIT_PROFILE = "Edit Profile"
    const val IMAGE_PATH = "image_path"
    const val INVITATION_TYPE = "invitation_type"
    const val PDF_INVITE = 0
    const val CAMERA_INVITE = 1
    val PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=com.besttyme"

    const val NOTI_TYPE = "noti_type"
    const val IS_FORCE_UPDATE = "is_force_update"
    const val IMAGE_URL = "image_url"
    const val DIALOG_MODE = "dialog_mode"

    //doctor appointment type enum
    enum class TYPE(val type: Int) {
        SIMPLE(0), BREAKFAST(1), LUNCH(2), TASK(3), UNVERIFIEDOCTOR(4)
    }

    enum class WEEK(val type: Int) {
        MONDAY(0), TUESDAY(1), WEDNESDAY(2), THURSDAY(3), FRIDAY(4), SATURDAY(5), SUNDAY(6)
    }


    //Rep type enum
    enum class RepType(val type: Int) {
        MAIL(0), RESEND(1), DEFAULT(2)
    }

    //Rep status enum
    enum class RepStatus(val type: Int) {
        PENDING(0), ACCEPTED(1), REJECTED(2)
    }

    //Transaction type enum
    enum class TransactionType(val type: Int) {
        SUCCESS(0), FAILED(1), REFUNDED(2)
    }

    //margin
    const val LIST_LANDSCAPE_LEFT_RIGHT_MARGIN = 32
    const val CARD_VIEW_RECYCELRVEIW_LANDSCAPE_LEFT_RIGHT_MARGING = 80

    //Rep type enum
    enum class DocStatus(val type: Int) {
        PENDING(0), ACCEPTED(1), MAYBE(2), REJECTED(3)
    }

    const val SHOW_IMAGE = 1
    const val UPDATE_IMAGE = 2
    const val SELECTED_IMAGE = 0


    /****************FireBase Constant *******************************/

    const val GROUP_CHAT_NODE_MAIN = "ChatWindow"
    const val CHAT_CONSTANT_RILYO = "Rilyo"
    const val CHAT_TYPE_GROUP_CHAT = "groupChat"
    const val CHATS = "Chats"
    const val GROUP_CHAT = "GroupChat"

}