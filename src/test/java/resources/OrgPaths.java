package resources;

public class OrgPaths {


    public static String readOrganization(APIResources resource, String organizationId) {
        String path = resource.getResource(); // e.g., "/organizations"
        return PathBuilder.joinEncoded(path, organizationId);
    }


    public static String listOrgUsers(APIResources resource, String organizationId) {
        return PathBuilder.joinEncoded(resource.getResource(), organizationId, "users");
    }


    public static String listOrgAdmins(APIResources resource, String organizationId) {
        return PathBuilder.joinEncoded(resource.getResource(), organizationId, "admins");
    }

    public static String readOrgsUserPermissions(APIResources resource, String organizationId, String userId) {
        return PathBuilder.joinEncoded(resource.getResource(), organizationId, "users", userId, "permissions");
    }

    public static String readOrgsUserRoles(APIResources resource, String organizationId, String userId) {
        return PathBuilder.joinEncoded(resource.getResource(), organizationId, "users", userId, "roles");
    }

    public static String grantRevokeRole(APIResources resource, String organizationId, String userId, String roleName) {
        return PathBuilder.joinEncoded(resource.getResource(), organizationId, "users", userId, "roles", roleName);
    }

}
