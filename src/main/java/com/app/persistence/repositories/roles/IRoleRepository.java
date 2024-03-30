package com.app.persistence.repositories.roles;

import com.app.persistence.entities.roles.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {

    List<RoleEntity> findRoleEntitiesByRoleNameIn(Set<String> roleNames);

}
