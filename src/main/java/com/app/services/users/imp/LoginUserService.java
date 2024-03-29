package com.app.services.users.imp;

import com.app.controller.auth.dto.AuthLogin;
import com.app.controller.auth.dto.AuthResponse;
import com.app.services.users.ILoginUserService;
import com.app.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LoginUserService implements ILoginUserService {

    private final JwtUtil jwtUtil;
    private final UserDetailServiceImp userDetailServiceImp;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public AuthResponse login(AuthLogin request) {
        String username = request.username();
        String password = request.password();
        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtil.createToken(authentication);
        return new AuthResponse(username, "User logged successfully", accessToken, true);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.userDetailServiceImp.loadUserByUsername(username);
        if(userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(),
                userDetails.getAuthorities());
    }
}
