package com.fade.vote.validation;

import com.fade.vote.validation.validator.VoteTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = VoteTypeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface VoteTypeValidation {

    String message() default "투표 타입 파라미터 값을 확인해주세요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
