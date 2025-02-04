import CryptoJS from "crypto-js";
import config from "./config";

/**
 * Obtiene la información del sessionStorage.
 */
export const getInfoBrowser = (name) => {
  return sessionStorage.getItem(name);
};

/**
 * Setea la información del sessionStorage.
 */
export const setInfoBrowser = (name, data) => {
  sessionStorage.setItem(name, data);
};



/**
 * Guarda el token de autenticación con ecriptación.
 */
export const setAuthToken = (token) => {
  return setInfoBrowser("token",encryptData(token));
};

/**
 * Obtiene el token de autenticación.
 */
export const getAuthToken = () => {
  try {
    return decryptData(getInfoBrowser("token"));  
  } catch (error) {
    return;
  }
  
};





/**
 * --------------------------------
 * Roles                          |
 * --------------------------------
 */

/**
 * Guarda los roles encriptados en sessionStorage.
 */
export const storeEncryptedRoles = (roles) => {
  const encryptedRoles = roles.map(role => role); //encryptData(role)
  setInfoBrowser("roles", JSON.stringify(encryptedRoles));
};

/**
 * Obtiene los roles desencriptados desde sessionStorage.
 */
export const getRoles = () => {
  try {
    const encryptedRoles = JSON.parse(getInfoBrowser("roles")) || [];
    
    return encryptedRoles.map(role => decryptData(role));
  } catch (error) {
    console.error("Error al desencriptar los roles:", error);
    return [];
  }
};

/**
 * Encripta la Data.
 */
export const encryptData = (data) => {
  return CryptoJS.AES.encrypt(data, config.encryptionKey).toString();
};

/**
 * Desencripta la Data.
 */
export const decryptData = (encryptedData) => {
  return CryptoJS.AES.decrypt(encryptedData, config.encryptionKey).toString(CryptoJS.enc.Utf8);
};
