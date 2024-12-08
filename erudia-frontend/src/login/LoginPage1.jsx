// import { useState } from "react";
// import { Form } from "../components/custom/form/Form.jsx";
// import { Input } from "../components/custom/form/Input.jsx";
// import logo from "../login/resources/img.png";
// import { Button } from "../components/custom/form/Button.jsx";

// const LoginPage = () => {
//     const handleSubmit = async (e) => {
//         e.preventDefault();
//         // Aquí iría la lógica para el manejo del submit
//     };

//     return (
//         <div className="flex w-full h-screen">
//             {/* Columna izquierda para el formulario */}
//             <div className="bg-white flex items-center justify-center">
//                 <Form onSubmit={handleSubmit}>
//                     <div className="flex flex-col items-center bg-amber-100 p-8 rounded-lg">
//                         <img src={logo} className="w-[125px] h-[250px]" alt="Logo"/>
//                         <div className="text-black font-bold font-['Montserrat'] mb-2">Usuario</div>
//                         <Input
//                             className="w-[501px] h-10 relative bg-white rounded-[10px] border-2 border-[#ffc100] mb-4"
//                             placeholder="Usuario"
//                         />
//                         <div className="text-black font-bold font-['Montserrat'] mb-2">Contraseña</div>
//                         <Input
//                             className="w-[501px] h-10 relative bg-white rounded-[10px] border-2  border-[#ffc100] mb-4"
//                             placeholder="Contraseña"
//                         />

//                         <div className="h-[65px]">
//                             <Button className="w-[268px] h-[51px] px-[90px] pt-3.5 pb-[13px] bg-[#646464] rounded-[15px] justify-center items-center inline-flex">
//                                 <div className="text-white text-xl font-bold font-['Montserrat']">
//                                     Contraseña
//                                 </div>
//                             </Button>
//                         </div>
//                     </div>
//                 </Form>
//             </div>
//             {/* Columna derecha para la imagen */}
//             <div className="bg-red-900 w-1080">
//                 aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
//             </div>
//         </div>
//     );
// };

// export { LoginPage };
