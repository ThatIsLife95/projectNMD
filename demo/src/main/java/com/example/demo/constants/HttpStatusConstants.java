package com.example.demo.constants;

public class HttpStatusConstants {
    //COMMON ERROR
    public final static String UNAVAILABLE_CODE = "999";
    public final static String UNAVAILABLE_MESSAGE = "Xin lỗi, dịch vụ của chúng tôi tạm thời bị gián đoạn. Vui lòng thử lại sau";

    public static final String SUCCESS_CODE = "000";
    public static final String SUCCESS_MESSAGE = "Thành công";

    public static final String EMAIL_EXISTED_CODE = "001";
    public static final String EMAIL_EXISTED_MESSAGE = "Email đã được sử dụng. Xin vui lòng chọn email khác";

    public static final String INVALID_FIELD_CODE = "002";
    public static final String INVALID_FIELD_MESSAGE = "Some fields invalid";

    public static final String INVALID_LINK_CODE = "003";
    public static final String INVALID_LINK_CODE_MESSAGE = "Đường link không hợp lệ";

    public static final String EXPIRED_LINK_CODE = "004";
    public static final String EXPIRED_LINK_CODE_MESSAGE = "Đường link đã hết hạn";

    public static final String USERNAME_EXISTED_CODE = "005";
    public static final String USERNAME_EXISTED_MESSAGE = "Tên đăng nhập đã được sử dụng. Xin vui lòng chọn tên đăng nhập khác";

    public static final String ROLE_NOT_EXISTED_CODE = "006";
    public static final String ROLE_NOT_EXISTED_MESSAGE = "Role không tồn tại";

    public static final String PERMISSION_NOT_EXISTED_CODE = "007";
    public static final String PERMISSION_NOT_EXISTED_MESSAGE = "Permission không tồn tại";

    public static final String PERMISSION_EXISTED_CODE = "008";
    public static final String PERMISSION_EXISTED_MESSAGE = "Permission đã tồn tại";
    public static final String ROLE_EXISTED_CODE = "009";
    public static final String ROLE_EXISTED_MESSAGE = "Role đã tồn tại";
    public static final String VERIFY_NEW_DEVICE_CODE = "010";
    public static final String VERIFY_NEW_DEVICE_MESSAGE = "Bạn đang đăng nhập thiết bị mới, vui lòng kiểm tra email của bạn để xác minh!";
    public static final String LOCKED_ACCOUNT_CODE = "011";
    public static final String LOCKED_ACCOUNT_MESSAGE = "Tài khoản của bạn đã bị khóa vui lòng liên hệ Admin để mở khóa";
    public static final String INVALID_EMAIL_OR_PASSWORD_CODE = "012";
    public static final String INVALID_EMAIL_OR_PASSWORD_MESSAGE = "Tài khoản hoặc mật khẩu không đúng";
    public static final String EXPIRED_PASSWORD_CODE = "013";
    public static final String EXPIRED_PASSWORD_MESSAGE = "Mật khẩu của bạn đã hết hạn. Bạn vui lòng đổi mật khẩu!";

    public static final String NULL_POINTER_OR_BAD_REQUEST_CODE = "014";
    public static final String NULL_POINTER_OR_BAD_REQUEST_MESSAGE = "Bạn truyền sai tham số đầu vào hoặc có đối tượng con trỏ null";


    public static final String SQL_CONNECTION_ERROR_CODE = "015";
    public static final String SQL_CONNECTION_ERROR_MESSAGE = "Kết nối với database có vấn đề";
    public static final String CANNOT_SEND_EMAIL_CODE = "016";
    public static final String CANNOT_SEND_EMAIL_MESSAGE = "Dịch vụ email của chúng tôi tạm thời bị gián đoạn. Vui lòng thử lại sau";
    public static final String INVALID_JWT_CODE = "017";
    public static final String INVALID_JWT_MESSAGE = "Mã JWT không hợp lệ";
    public static final String DEVICE_NOT_EXISTED_CODE = "018";
    public static final String DEVICE_NOT_EXISTED_MESSAGE = "Device không tồn tại";

    public static final String USERNAME_NOT_EXISTED_CODE = "019";
    public static final String USERNAME_NOT_EXISTED_MESSAGE = "Username không tồn tại";

    public static final String EMAIL_NOT_EXISTED_CODE = "020";
    public static final String EMAIL_NOT_EXISTED_MESSAGE = "Email không tồn tại";
    public static final String INVALID_OLD_PASSWORD_CODE = "021";
    public static final String INVALID_OLD_PASSWORD_MESSAGE = "Mật khẩu cũ không đúng";
}
