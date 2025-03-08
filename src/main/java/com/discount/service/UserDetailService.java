package com.discount.service;

import com.discount.request.LoginUserDto;
import com.discount.response.LoginUserResponse;

public interface UserDetailService {

    LoginUserResponse authenticate(LoginUserDto userLoginDto);
}
