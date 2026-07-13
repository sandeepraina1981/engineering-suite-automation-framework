package resources;

import config.Config;

public class PathCreator {


    /***
     * @param ordID -> paramter organization id
     * @return  -> returns path paramter by appending org id to it -> "/organizations/{organizationId}"
     */
    public static String readOrganization(String basePath, String ordID) {
        APIResources resources = APIResources.valueOf(basePath);
        String path = resources.getResource();
        String orgIDPath = path + "/" + ordID;
        return orgIDPath;
    }

    /***
     * @param ordID -> paramter organization id
     * @return  -> returns path param for List of Organization users -> "/organizations/{organizationId}/users"
     */
    public static String listOrgUsers(String basePath, String ordID) {
        String listOrgusers = readOrganization(basePath, ordID) + "/users";
        return listOrgusers;
    }

    /***
     * @param ordID -> paramter organization id
     * @return  -> returns path param for List of Organization users -> "/organizations/{organizationId}/admins"
     */
    public static String listOrgadmin(String basePath, String ordID) {
        String listOrgusers = readOrganization(basePath, ordID) + "/admins";
        return listOrgusers;
    }

    /***
     * @param basePath
     * @param ordID -> paramter organization id
     * @param userID -> parameter user ID
     * @return -> returns path param for List of user permissions -> "/organizations/{organizationId}/users/{userId}/permissions"
     */
    public static String readOrgsUserPermissions(String basePath, String ordID, String userID) {
        return listOrgUsers(basePath, ordID) + "/" + userID + "/permissions";
    }

    /***
     * @param basePath
     * @param ordID -> paramter organization id
     * @param userID -> parameter user ID
     * @return -> returns path param for List of user permissions -> "/organizations/{organizationId}/users/{userId}/roles"
     */
    public static String readOrgsUserRoles(String basePath, String ordID, String userID) {
        return listOrgUsers(basePath, ordID) + "/" + userID + "/roles";
    }

    /***
     * @param basePath
     * @param ordID -> paramter organization id
     * @param userID -> parameter user ID
     * @param roleName -> role of user
     * @return -> returns path param for List of user permissions -> "/organizations/{organizationId}/users/{userId}/roles/{roleName}"
     */
    public static String grantRevokeRole(String basePath, String ordID, String userID, String roleName) {
        return readOrgsUserRoles(basePath, ordID, userID) + "/" + roleName;
    }

}
