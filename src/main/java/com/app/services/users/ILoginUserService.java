package com.app.services.users;

import com.app.controller.auth.dto.AuthLogin;
import com.app.controller.auth.dto.AuthResponse;

public interface ILoginUserService {

    AuthResponse login(AuthLogin request);

}