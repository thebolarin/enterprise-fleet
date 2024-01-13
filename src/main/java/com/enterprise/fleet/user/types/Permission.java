package com.enterprise.fleet.user.types;

        import lombok.Getter;
        import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    SALES_REP_READ("sales:representative:read"),
    SALES_REP_UPDATE("sales:representative:update"),
    SALES_REP_CREATE("sales:representative:create"),
    SALES_REP_DELETE("sales:representative:delete");
    private final String permission;
}
