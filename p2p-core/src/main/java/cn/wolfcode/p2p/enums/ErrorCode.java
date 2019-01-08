package cn.wolfcode.p2p.enums;

public enum ErrorCode {
    LOGININFO_NICKNAME_NULL("100","昵称不能为空"),
    LOGININFO_NICKNAME_LENGTH("101","昵称长度应为{0} - {1}位"),
    LOGININFO_USERNAME_NULL("102","手机号不能为空"),
    LOGININFO_USERNAME_LENGTH("103","手机号长度应为{0}位"),
    LOGININFO_USERNAME_PATTERN("104","手机号格式不正确"),
    LOGININFO_USERNAME_EXIST("105","手机号已经被注册"),
    LOGININFO_PASSWORD_NOLL("106","密码不能为空"),
    LOGININFO_PASSWORD_LENGTH("107","密码长度应为{0} - {1}位"),
    LOGININFO_VERIFYCODE_NOLL("108","验证码不能为空"),
    LOGININFO_VERIFYCODE_LENGTH("109","验证码长度应为{0}位"),
    LOGININFO_ConfirmPwd_NULL("110","确认密码不能为空"),
    LOGININFO_ConfirmPwd_EQUALS("111","确认密码与密码不一致"),
    LOGININFO_VERIFYCODE_SEND("112","验证码发送过于频繁，请稍后再试");

    private String code;
    private String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getErrorMsg(){
        StringBuilder sb = new StringBuilder();
        sb.append(msg).append(" [").append(code).append("]");
        return sb.toString();
    }
}
