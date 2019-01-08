package cn.wolfcode.p2p.base.anno;

import cn.wolfcode.p2p.base.validator.PhoneConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Constraint(
        validatedBy = {PhoneConstraintValidator.class}
)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {

    String message() default "{cn.wolfcode.constraints.phone.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
