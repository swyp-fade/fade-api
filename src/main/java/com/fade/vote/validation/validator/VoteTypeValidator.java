package com.fade.vote.validation.validator;

import com.fade.vote.constant.VoteType;
import com.fade.vote.validation.VoteTypeValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VoteTypeValidator implements ConstraintValidator<VoteTypeValidation, VoteType> {

    @Override
    public void initialize(VoteTypeValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(VoteType voteType, ConstraintValidatorContext context) {
        return voteType != null;
    }
}
