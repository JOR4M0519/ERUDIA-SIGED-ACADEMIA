import { Input } from "../components/custom/form/Input.jsx";
import glmImg from './resources/img.png';
import "./style.css";

const LoginPage = () => {
    return (
        <div className="box">
            {/* Image Section */}
            <div className="image">
                <img className="img" alt="GLM" src={glmImg} />
            </div>

            {/* Login Form Section */}
            <div className="login-frame">
                <div className="input-group">
                    <label htmlFor="usuario" className="text-wrapper">Usuario</label>
                    <Input id="usuario" className="usuario-input" type="text" placeholder="Ingresa tu usuario" />
                </div>
                <div className="input-group">
                    <label htmlFor="contrasena" className="text-wrapper">Contraseña</label>
                    <Input id="contrasena" className="password-input" type="password" placeholder="Ingresa tu contraseña" />
                </div>
            </div>
        </div>
    );
};

export { LoginPage };
