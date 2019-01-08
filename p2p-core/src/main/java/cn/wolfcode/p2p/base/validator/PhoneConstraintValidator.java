package cn.wolfcode.p2p.base.validator;

import cn.wolfcode.p2p.base.anno.Phone;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

import static cn.wolfcode.p2p.util.Constants.REGEX_MOBILE;

public class PhoneConstraintValidator implements ConstraintValidator<Phone, String> {
    @Override
    public void initialize(Phone constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean flag = false;
        if (StringUtils.hasLength(value)){
            flag = Pattern.matches(REGEX_MOBILE, value);
        }

        return flag;
    }
}
