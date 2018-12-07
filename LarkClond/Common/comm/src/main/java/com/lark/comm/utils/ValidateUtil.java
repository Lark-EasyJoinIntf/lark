package com.lark.comm.utils;

import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * 输入参数有效性验证
 * Controller 方法中 @Valid Bean bean, BindingResult result
 * 其中Bean中实例定义如
    @NotBlank(message = "缺少response_type参数")
    private String responseType;
 */
public class ValidateUtil {
    private static ValidateUtil validate;

    private ValidateUtil(){}

    public static ValidateUtil getValidate(){
        if (validate == null){
            validate = new ValidateUtil();
        }
        return validate;
    }

    public void validate(BindingResult result) {
        if (result.hasFieldErrors()) {
            List<FieldError> errorList = result.getFieldErrors();
            errorList.stream().forEach(item -> Assert.isTrue(false, item.getDefaultMessage()));
        }
    }
}
