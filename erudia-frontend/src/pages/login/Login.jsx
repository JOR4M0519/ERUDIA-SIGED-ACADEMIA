import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import  { useState } from "react"; // De pronto no sea necesaria

import googleOauthIcon from "./resources/google-oauth.png";
import image1 from "./resources/bg-Image.png";
import logo from "./resources/logo.png";
import "./style.css";

import { PrivateRoutes, PublicRoutes} from '../../models/index';
import { createUser, resetUser, UserKey } from '../../redux/states/user';
import { clearStorage } from '../../utilities';

import { encodeUserInfo, request } from "../../services/config/axios_helper"

const Login = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState(""); // Aplicar modal de error.

    useEffect(() => {
        clearStorage(UserKey);
        dispatch(resetUser());
        navigate(`/${PublicRoutes.LOGIN}`, { replace: true });
      }, []);

    const login = async (e) => {
        e.preventDefault();
        console.log("Iniciando sesión...");
    
        try {
            const response = await request("POST", "gtw", "/public/login", { username, password });
    
            if (response.status === 200) {
                const responseData = response.data; // Axios maneja la conversión automática de JSON
                
                if (responseData.token) {
                    //setAuthHeader(token); // Guarda el token, roles y nombre en sessionStorage

                    dispatch(createUser({...encodeUserInfo(responseData.user.id,responseData.token)}));
                    navigate(`${PrivateRoutes.DASHBOARD}`);
                }
            } else {
                setErrorMessage("Credenciales inválidas. Por favor verifica e intenta nuevamente.");
            }
        } catch (error) {
            console.error("Error durante el inicio de sesión:", error);
            setErrorMessage("Ocurrió un error inesperado. Intenta nuevamente.");
        }
    };

    return (
        <div className="frame flex w-full h-screen">
            {/* Left column for the login form */}
            <div className="flex flex-col bg-white justify-center items-center w-1/2 p-8">
                <form className="flex flex-col items-center bg-white p-8 rounded-lg" onSubmit={login}>
                    <img src={logo} alt="Logo" className="w-[200px] h-auto mb-6" />
                    
                    <div className="mb-4 w-full">
                        <label htmlFor="username" className="block text-black font-bold mb-2">Usuario</label>
                        <input
                            type="text"
                            id="username"
                            placeholder="Usuario"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            className="w-full h-10 px-3 border-2 border-[#ffc100] rounded-lg"
                        />
                    </div>
                    
                    <div className="mb-4 w-full">
                        <label htmlFor="password" className="block text-black font-bold mb-2">Contraseña</label>
                        <input
                            type="password"
                            id="password"
                            placeholder="Contraseña"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full h-10 px-3 border-2 border-[#ffc100] rounded-lg"
                        />
                    </div>

                    <div className="flex items-center justify-between w-full mb-4">
                        <label className="flex items-center">
                            <input type="checkbox" className="mr-2" />
                            Recordarme
                        </label>
                        <a href="#" className="text-sm text-blue-600 hover:underline">Olvide mi contraseña</a>
                    </div>

                    {errorMessage && (
                        <div className="text-red-500 text-sm mb-4">
                            {errorMessage}
                        </div>
                    )}

                    <button 
                        type="submit"
                        className="flex items-center mt-4 bg-gray-700 text-black font-bold rounded-lg hover:bg-gray-800"
                    >
                        Ingresar
                    </button>
                </form>

                <button className="flex items-center mt-4 bg-white border-2 border-gray-300 rounded-lg p-2">
                    <img 
                        src={googleOauthIcon} 
                        alt="Google Oauth" 
                        className="w-6 h-6 mr-2" 
                        style={{
                            width: "20px",
                            objectFit: "cover", // Ajusta cómo se recorta la imagen
                        }}
                    />
                    Entrar con Google
                </button>
            </div>

            {/* Right column for the image */}
            <div className="w-1/2 flex justify-center items-center bg-gray-100">
                <img
                    src={image1}
                    alt="Playground"
                    className="w-[95%] h-[95%] object-cover rounded-lg shadow-lg"
                />
            </div>
        </div>
    );
};

export { Login };
