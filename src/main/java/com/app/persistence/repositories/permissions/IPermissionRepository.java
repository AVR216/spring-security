package com.app.persistence.repositories.permissions;

import com.app.persistence.entities.permissions.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPermissionRepository extends JpaRepository<PermissionEntity, Long> {
}
