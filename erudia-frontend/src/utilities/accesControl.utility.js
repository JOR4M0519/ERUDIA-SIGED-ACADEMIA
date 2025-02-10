export const hasAccess = (userRoles, allowedRoles) => {
    return userRoles.some((role) => allowedRoles.includes(role));
  };
  