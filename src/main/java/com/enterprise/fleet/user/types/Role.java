package com.enterprise.fleet.user.types;

import com.enterprise.fleet.user.types.Permission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Role {

  CUSTOMER(Collections.emptySet()),
  ADMIN(
          Set.of(
                  Permission.ADMIN_READ,
                  Permission.ADMIN_UPDATE,
                  Permission.ADMIN_DELETE,
                  Permission.ADMIN_CREATE,
                  Permission.SALES_REP_READ,
                  Permission.SALES_REP_UPDATE,
                  Permission.SALES_REP_DELETE,
                  Permission.SALES_REP_CREATE
          )
  ),
  SALES_REP(
          Set.of(
                  Permission.SALES_REP_READ,
                  Permission.SALES_REP_UPDATE,
                  Permission.SALES_REP_DELETE,
                  Permission.SALES_REP_CREATE
          )
  )

  ;

  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
