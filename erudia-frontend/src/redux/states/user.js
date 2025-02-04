
import { createSlice } from '@reduxjs/toolkit';
import { clearLocalStorage, persistLocalStorage } from '../../utilities';

export const EmptyUserState = {
  id: 0,
  name: '',
  email: ''
};

export const UserKey = 'user';

export const userSlice = createSlice({
  name: 'user',
  
  initialState: localStorage.getItem('user') ? 
  JSON.parse(localStorage.getItem('user')) :
  
  EmptyUserState,
  
  reducers: {
    createUser: (state, action) => {
      persistLocalStorage(UserKey, action.payload);
      return action.payload;
    },
    updateUser: (state, action) => {
      const result = { ...state, ...action.payload };
      persistLocalStorage(UserKey, result);
      return result;
    },
    resetUser: () => {
      clearLocalStorage(UserKey);
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
