package com.app.services.users.imp;

import com.app.controller.auth.dto.AuthCreateUserRequest;
import com.app.controller.auth.dto.AuthLogin;
import com.app.controller.auth.dto.AuthResponse;
import com.app.persistence.entities.roles.RoleEntity;
import com.app.persistence.entities.users.UserEntity;
import com.app.persistence.repositories.roles.IRoleRepository;
import com.app.persistence.repositories.users.IUserRepository;
import com.app.services.users.ILoginUserService;
import com.app.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LoginUserService implements ILoginUserService {

    private final JwtUtil jwtUtil;
    private final UserDetailServiceImp userDetailServiceImp;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;

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

    @Transactional
    @Override
    public AuthResponse signUp(AuthCreateUserRequest authCreateUser) {
        var username = authCreateUser.username();
        var password = authCreateUser.password();
        var rolesRequest = authCreateUser.authCreateRoleRequest().roleListName();
        Set<RoleEntity> roleEntitySet = this.roleRepository.findRoleEntitiesByRoleNameIn(rolesRequest).stream()
                .collect(Collectors.toSet());

        if (roleEntitySet.isEmpty()) {
            throw new IllegalArgumentException("The specified roles does not exists");
        }

        if (this.userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already registered");
        }

        var userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roleEntitySet)
                .isEnabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();

        var userSaved = this.userRepository.save(userEntity);

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        userSaved.getRoles().forEach(role -> authorityList
                .add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleName().name()))));
        userSaved.getRoles().stream().flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved.getUsername(), userSaved.getPassword(), authorityList);
        var token = jwtUtil.createToken(authentication);
        return new AuthResponse(userSaved.getUsername(),
                "User created successfully", token, true);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.userDetailServiceImp.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(),
                userDetails.getAuthorities());
    }
}
