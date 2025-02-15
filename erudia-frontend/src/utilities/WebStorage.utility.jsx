export const persistStorage = (key, value) => {
  localStorage.setItem(key, JSON.stringify({ ...value }));
  };
  
export const clearStorage = (key) => {
  localStorage.removeItem(key);
};

export const getStorage = (key) => {
  localStorage.getItem(key);
};
  