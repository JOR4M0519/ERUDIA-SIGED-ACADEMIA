import { Roles } from "./roles";

export const ProfileRoles = {
  VIEW_DIRECTION: [Roles.ADMIN], // Solo Admin puede ver la dirección del grado
  VIEW_PERSONAL_INFO: [Roles.ADMIN], // Solo Admin puede ver la info de cualquier usuario
};
