import { Route, RouterProvider, createBrowserRouter, createRoutesFromElements } from "react-router-dom";
import { LoginPage } from './login/LoginPage.jsx';

const App = () => {
    const router = createBrowserRouter(createRoutesFromElements(
        <Route path="/" element={<LoginPage />} />
    ));

    return (
        <RouterProvider router={router} />
    );
}

export default App;
