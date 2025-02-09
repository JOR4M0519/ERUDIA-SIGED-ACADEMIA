import { Roles } from "./roles";


export const SideBarRoles = {
    DASHBOARD:      [Roles.ADMIN,Roles.USER],
    SEARCH:         [Roles.ADMIN,Roles.USER],
    PROFILE:        [Roles.ADMIN,Roles.USER],
    SUBJECTS:       [Roles.ADMIN,Roles.USER],
    ACTIVITIES:     [Roles.ADMIN,Roles.TEACHER],
    STUDENTTRACKING:[Roles.ADMIN,Roles.TEACHER],
    ADMIN:          [Roles.ADMIN,],
    SETTINGS:       [Roles.ADMIN,]
  };
