package cn.wolfcode.p2p.base.vo;

import cn.wolfcode.p2p.base.anno.Phone;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class LoginInfoVO {

    @NotNull(message = "{cn.wolfcode.constraints.nickName.NotNull.message}")
    @Range(min = 2,max = 6,message = "{cn.wolfcode.constraints.nickName.Range.message}")
    private String nickName;

    @NotNull(message = "{cn.wolfcode.constraints.username.NotNull.message}")
    @Phone
    private String username;

    @NotNull(message = "{cn.wolfcode.constraints.verifycode.NotNull.message}")
    private String verifycode;

    @NotNull(message = "{cn.wolfcode.constraints.password.NotNull.message}")
    private String password;

    @NotNull(message = "{cn.wolfcode.constraints.confirmPwd.NotNull.message}")
    private String confirmPwd;
}
