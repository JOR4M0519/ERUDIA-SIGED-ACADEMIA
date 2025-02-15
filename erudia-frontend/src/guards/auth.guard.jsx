import { useSelector } from 'react-redux';
import { Navigate, Outlet } from 'react-router-dom';
import { PrivateRoutes, PublicRoutes } from '../models';


const PrivateValidationFragment = <Outlet />;
const PublicValidationFragment = <Navigate replace to={PublicRoutes.LOGIN} />;

export const AuthGuard = ({ privateValidation }) => {
  const userState = useSelector((store) => store.user);
  
  return userState.token ? (
    privateValidation ? (
      PrivateValidationFragment
    ) : (
      PublicValidationFragment
    )
  ) : (
    <Navigate replace to={PublicRoutes.LOGIN} />
  );
};

export default AuthGuard;