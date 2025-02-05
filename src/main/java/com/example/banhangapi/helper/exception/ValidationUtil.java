package com.example.banhangapi.helper.exception;

import com.example.banhangapi.api.request.RequestLogin;
import com.example.banhangapi.api.request.RequestRegister;
import com.example.banhangapi.api.request.RequestUpdate;
import jakarta.validation.Validation;
import com.example.banhangapi.api.entity.User;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValidationUtil<T> {

    private static Validator validator;

    private List<String> messages;

    static {
        // Tạo Validator từ Jakarta Validation
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Phương thức để lấy danh sách lỗi validation
    public static Set<ConstraintViolation<User>> validateUser(User user) {
        return validator.validate(user);  // Trả về danh sách các vi phạm
    }
    public static Set<ConstraintViolation<RequestRegister>> validateRegister(RequestRegister register) {
        return validator.validate(register);
    }
    public static Set<ConstraintViolation<RequestUpdate>> validateUpdate(RequestUpdate update) {
        return validator.validate(update);
    }
    public  static Set<ConstraintViolation<RequestLogin>> validateLogin (RequestLogin requestLogin) {
        return validator.validate(requestLogin);
    }

//    @Bean
    public Set<ConstraintViolation<T>> validateData(T data) {
        return validator.validate(data);
    }

    public ResponseValidate getMessage( T data) {
        if(!validator.validate(data).isEmpty()){
            ResponseValidate responseValidate = new ResponseValidate();
            StringBuilder errorMessages = new StringBuilder();
            List<String> errors = new ArrayList<>();
            for (ConstraintViolation<T> violation : validateData(data)) {
                errorMessages.append(violation.getMessage()).append(", ");
                errors.add(violation.getMessage());
            }

            return new ResponseValidate(false, errors);
        }else return new ResponseValidate(true, null);
    }
}
