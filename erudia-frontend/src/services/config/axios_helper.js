import axios from "axios";
import { jwtDecode } from "jwt-decode";
import {Roles} from '../../models/index';
import {encryptData, Config, getStorage} from "../../utilities/index";
import {UserKey} from "../../redux/states/user";


/**
 * Establece de acuerdo con el token de autenticación la decodificación
 * de los datos del usuarioy encripta los roles del usuario.
 *
 * @param {string} token - Token JWT obtenido después del login.
 */

export const encodeUserInfo = (token) => {
  const decodedToken = jwtDecode(token);

  // Obtener los roles del token -> elimina el "/" y filtra solo los válidos
  const userRoles = decodedToken.roles_group && Array.isArray(decodedToken.roles_group)
  ? decodedToken.roles_group.map(role => role.replace("/", "").toLowerCase())
  : [];

  // Filtrar los roles válidos
  const assignedRoles = userRoles.filter(role => Object.values(Roles).includes(role));

  // Encriptar los roles antes de almacenarlos
  const encryptedRoles = assignedRoles.map(role => encryptData(role));

  // Retornar el usuario con datos 
  return {
    id: null,
    token: token,
    name: decodedToken.name || "",
    username: decodedToken.username || "",
    email: decodedToken.email || "",
    roles: encryptedRoles,
  };
};

// Configuración global de axios
axios.defaults.baseURL = Config.apiBaseUrl;
axios.defaults.headers.post["Content-Type"] = "application/json";

/**
 * Realiza una petición HTTP al backend utilizando Axios.
 *
 * @param {"GET" | "POST" | "PUT" | "DELETE"} method - Método HTTP de la solicitud.
 * @param {"academy" | "gtw"} [serviceType="academy"] - Tipo de servicio al que se está llamando (por defecto "academy").
 * @param {string} url - Endpoint de la API, por ejemplo "/subjects".
 * @param {Object} [data={}] - Datos a enviar en el body de la solicitud.
 * @returns {Promise} Promesa con la respuesta de la API.
 */
export const request = (method, serviceType = "academy", url, data = {}) => {
  let withCredentials = false; // Indica si se deben enviar cookies o credenciales en la solicitud
  let headers = {};

  // Obtener el token de autenticación desde sessionStorage
  const token = getStorage(UserKey);

  // Si hay un token válido, lo añade a los headers
  if (token) {
    headers.Authorization = `Bearer ${decryptedToken}`;
  }

  // if (data && data.token != null) {
  //   headers.token = data.token;
  // }

  // Lista de endpoints que requieren credenciales
  const endpointsWithCredentials = ["/public/login", "/user/session"];

  if (endpointsWithCredentials.some(endpoint => url.includes(endpoint))) {
    withCredentials = true;
  }

  try {
    return axios({
      method: method,
      url: serviceType + url,
      headers: headers,
      data: data,
      withCredentials: withCredentials,
    });
  } catch (error) {
    console.error("Error en la solicitud:", error);
  }
};
