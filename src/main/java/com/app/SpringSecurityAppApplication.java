package com.app;

import com.app.persistence.entities.permissions.PermissionEntity;
import com.app.persistence.entities.roles.RoleEntity;
import com.app.persistence.entities.roles.RoleEnum;
import com.app.persistence.entities.users.UserEntity;
import com.app.persistence.repositories.users.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@SpringBootApplication
public class SpringSecurityAppApplication implements CommandLineRunner {

	private static  final String PASSWORD = "$2a$10$Nx6Q6/qqw/P1t15pCBffWekdNZYndDQ1Zox.21f5hTZiA7.Belwa.";

	private final IUserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Permissions
		var createPermission = PermissionEntity.builder()
				.name("CREATE")
				.build();
		var readPermission = PermissionEntity.builder()
				.name("READ")
				.build();
		var updatePermission = PermissionEntity.builder()
				.name("UPDATE")
				.build();
		var deletePermission = PermissionEntity.builder()
				.name("DELETE")
				.build();
		var refactorPermission = PermissionEntity.builder()
				.name("REFACTOR")
				.build();

		// Roles
		var roleAdmin = RoleEntity.builder().roleName(RoleEnum.ADMIN).build();
		var roleDev = RoleEntity.builder().roleName(RoleEnum.DEV).build();
		var roleInvited = RoleEntity.builder().roleName(RoleEnum.INVITED).build();
		var roleUser = RoleEntity.builder().roleName(RoleEnum.USER).build();

		roleAdmin.setPermissions(Set.of(createPermission, readPermission, updatePermission, deletePermission));
		roleDev.setPermissions(Set.of(readPermission, createPermission, deletePermission, refactorPermission, updatePermission));
		roleInvited.setPermissions(Set.of(readPermission));
		roleUser.setPermissions(Set.of(createPermission, readPermission));

		// Users
		var adminJohn = UserEntity.builder()
				.username("John").password(PASSWORD).accountNonExpired(true).accountNonLocked(true)
				.isEnabled(true)
				.credentialNonExpired(true).roles(Set.of(roleAdmin)).build();

		var devLorena = UserEntity.builder()
				.username("Lorena").password(PASSWORD).accountNonExpired(true).accountNonLocked(true)
				.isEnabled(true)
				.credentialNonExpired(true).roles(Set.of(roleDev)).build();

		var invitedPedro = UserEntity.builder()
				.username("Pedro").password(PASSWORD).accountNonExpired(true).accountNonLocked(true)
				.isEnabled(true)
				.credentialNonExpired(true).roles(Set.of(roleInvited)).build();

		var userJuan = UserEntity.builder()
				.username("Juan").password(PASSWORD).accountNonExpired(true).accountNonLocked(true)
				.isEnabled(true)
				.credentialNonExpired(true).roles(Set.of(roleUser)).build();

		this.userRepository.saveAll(List.of(adminJohn, devLorena, invitedPedro, userJuan));
	}
}
