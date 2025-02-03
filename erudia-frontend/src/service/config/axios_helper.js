import axios from "axios";
import { storeEncryptedRoles, setInfoBrowser, setAuthToken, getAuthToken } from "./config_helper";
import config from "./config";
import { jwtDecode } from "jwt-decode";

/**
 * Establece el token de autenticación en el almacenamiento y encripta los roles del usuario.
 *
 * @param {string} token - Token JWT obtenido después del login.
 */
export const setAuthHeader = (token) => {
  const decodedToken = jwtDecode(token);
  
  // Encripta y almacena el token
  setAuthToken(token);
  
  // Encriptar y almacenar roles en sessionStorage
  storeEncryptedRoles(decodedToken.roles_group || []);
  
  // Guardar el nombre del usuario en sessionStorage
  setInfoBrowser("name", decodedToken.name || "");
};

// Configuración global de axios
axios.defaults.baseURL = config.apiBaseUrl;
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
  const token = getAuthToken();

  // Si hay un token válido, lo añade a los headers
  if (token && token !== "null") {
    headers = { Authorization: `Bearer ${token}` };
  }

  // if (data && data.token != null) {
  //   headers.token = data.token;
  // }

  // Si la solicitud es de login, habilita el envío de credenciales
  if (url.includes("login")) {
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
