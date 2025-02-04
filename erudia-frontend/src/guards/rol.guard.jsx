import { useSelector } from 'react-redux';
import { Navigate, Outlet } from 'react-router-dom';
import { PrivateRoutes, PublicRoutes, Roles } from '../models';


function RoleGuard({ rol }) {
  const userState = useSelector((store) => store.user);

   return userState.rol === rol ? (
    <Outlet />
  ) : (
    <Navigate replace to={PrivateRoutes.PRIVATE} />
  );
}

export default RoleGuard;