import { useSelector } from 'react-redux';
import { Navigate, Outlet } from 'react-router-dom';
import { PrivateRoutes} from '../models';
import { decodeRoles } from '../utilities';


export function RoleGuard({ rol }) {
  const userState = useSelector(store => store.user);
  const userRoles = decodeRoles(userState.roles) || [];

  return userRoles.includes(rol) ? ( // 🔥 Aquí se arregla la comparación
    <Outlet />
  ) : (
    <Navigate replace to={PrivateRoutes.PRIVATE} />
  );
  //  return userRole === rol ? (
  //   <Outlet />
  // ) : (
  //   <Navigate replace to={PrivateRoutes.PRIVATE} />
  // );
}
