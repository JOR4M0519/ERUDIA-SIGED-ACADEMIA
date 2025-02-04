import { lazy, Suspense } from 'react';
import { Provider } from 'react-redux';
import { BrowserRouter, Navigate, Route } from 'react-router-dom';

import { AuthGuard, RoleGuard } from './guards';
import { PrivateRoutes, PublicRoutes, Roles } from './models';
import store from './redux/store';
import { RoutesWithNotFound } from './utilities';

import React from "react";

// import { Layout }  from "./components/Layout" 
import { Login } from './pages/login/Login';
import { Dashboard } from './pages/private/Dashboard/Dashboard';
import { Layout }  from "./components/Layout" 
import Private from './pages/private/Private';

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
                <Route path={`${PrivateRoutes.DASHBOARD}/*`} element={<Dashboard />} />
              </Route>
               
              <Route element={<RoleGuard rol={Roles.ADMIN} />}>
                <Route path={PrivateRoutes.DASHBOARD} element={<Layout><Dashboard /></Layout>} />
              </Route>
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