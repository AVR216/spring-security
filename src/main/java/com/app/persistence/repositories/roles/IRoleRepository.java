package com.app.persistence.repositories.roles;

import com.app.persistence.entities.roles.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
}
