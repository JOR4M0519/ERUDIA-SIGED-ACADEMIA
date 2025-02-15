import { lazy, Suspense } from 'react';
import { Provider } from 'react-redux';
import { BrowserRouter, Navigate, Route } from 'react-router-dom';

import { AuthGuard, RoleGuard,MultiRoleGuard  } from './guards';
import { PrivateRoutes, PublicRoutes, Roles } from './models';
import store from './redux/store';
import { RoutesWithNotFound } from './utilities';
import React from "react";

import { Layout }  from "./components/Layout" 

import { Login } from './pages/login/Login';
import { Dashboard } from './pages/private/index';
import { Admin } from './pages/private/Admin';
import { Subject } from './pages/private/Subject';
import { Activities } from './pages/private/Activities';
import { StudentTracking } from './pages/private/StudentTracking';
import { Settings } from './pages/private/Setting';
import { Profile } from './pages/private/Profile';

// const Login = lazy(() => import('./pages/login/Login'));
// const Private = lazy(() => import('./pages/Private/Private'));


function App() {
  return (
    <div className="App">
      {/* Agregar spin de carga */}
      <Suspense fallback={<div>Loading...</div>}>
        <Provider store={store}>
          <BrowserRouter>
            {/* Componente redirige a un sitio cuando no existe la ruta */}
            <RoutesWithNotFound>
              {/* Redirige a una ruta cuando ingresa a la raiz y esta autenticado */}
              <Route path="/" element={<Navigate to={PrivateRoutes.DASHBOARD} />} />
              
              <Route path={PublicRoutes.LOGIN} element={<Login />} />

              <Route element={<AuthGuard privateValidation={true} />}>
                <Route path={`${PrivateRoutes.DASHBOARD}/*`} element={<Layout><Dashboard /></Layout>} />
              </Route>
               
              {/* <Route element={<RoleGuard rol={Roles.ADMIN} />}>
                <Route path={PrivateRoutes.ADMIN} element={<Layout><Admin/></Layout>} />
              </Route> */}
              
              {/* Rutas protegidas dinámicamente por roles */}
              <Route element={<MultiRoleGuard navKey="ADMIN" />}>
                <Route path={PrivateRoutes.ADMIN} element={<Layout><Admin /></Layout>} />
              </Route>

              <Route element={<MultiRoleGuard navKey="PROFILE" />}>
                <Route path={PrivateRoutes.PROFILE} element={<Layout><Profile /></Layout>} />
              </Route>

              <Route element={<MultiRoleGuard navKey="SUBJECTS" />}>
                <Route path={PrivateRoutes.SUBJECTS} element={<Layout><Subject/></Layout>} />
              </Route>

              <Route element={<MultiRoleGuard navKey="ACTIVITIES" />}>
                <Route path={PrivateRoutes.ACTIVITIES} element={<Layout><Activities /></Layout>} />
              </Route>

              <Route element={<MultiRoleGuard navKey="STUDENTTRACKING" />}>
                <Route path={PrivateRoutes.STUDENTTRACKING} element={<Layout><StudentTracking /></Layout>} />
              </Route>

              <Route element={<MultiRoleGuard navKey="SETTINGS" />}>
                <Route path={PrivateRoutes.SETTINGS} element={<Layout><Settings /></Layout>} />
              </Route>

              <Route path={`${PrivateRoutes.PROFILE}/*`} element={<Layout><Settings /></Layout>} />
              


            </RoutesWithNotFound>
          </BrowserRouter>
        </Provider>
      </Suspense>
    </div>
  );
}



// const App = () => {
//   return (
//     <BrowserRouter>
//       <Routes>
//         <Route path="/"           element={<LoginPage />} />
//         <Route path="/login"      element={<LoginPage />} />
//         <Route path="/dashboard"  element={  
//           <Layout>
//             <Dashboard />
//           </Layout>
//           } />

//           {/* Cuano no exista la ruta */}
//         <Route path="*"           element={<LoginPage />} /> 
//       </Routes>
//     </BrowserRouter>
//   );
// };

export default App;