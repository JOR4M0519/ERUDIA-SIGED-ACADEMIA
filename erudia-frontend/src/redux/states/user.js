
import { createSlice } from '@reduxjs/toolkit';
import { clearStorage, getStorage, persistStorage } from '../../utilities';

export const EmptyUserState = {
    id: null,
    token: '',
    name: '',
    username: '',
    email: '',
};

export const UserKey = 'user';

export const userSlice = createSlice({
  name: UserKey,
  
  initialState: 
  
  localStorage.getItem(UserKey) ? 
  JSON.parse(localStorage.getItem(UserKey)) : EmptyUserState,
  
  reducers: {
    createUser: (state, action) => {
      persistStorage(UserKey, action.payload);
      return action.payload;
    },
    updateUser: (state, action) => {
      const result = { ...state, ...action.payload };
      persistStorage(UserKey, result);
      return result;
    },
    resetUser: () => {
      clearStorage(UserKey);
      return EmptyUserState;
    }
  }
});

export const { createUser, updateUser, resetUser } = userSlice.actions;

export default userSlice.reducer;




// export const UserEmpyState = {
//     name: '',
//     email: '',
// }

// export const userSlice = createSlice({
//     name: "user",
//     initialState: UserEmpyState,
//     reducers:{
//         createUser: (state, action)=>{
//             return action.payload;
//         },

//         modifyUser: (state, action) =>{
//             //Remplaza los valores que tenga la misma propiedad y mantiene los que no se hubiesen puesto 
//             return {...state, ...action.payload}
//         },
//         resetUser: () => {return UserEmpyState;}

        
//     }
// })


//export const {createUser, modifyUser, resetUser} = userSlice.action;
