import { useState } from "react";
import { useSelector } from "react-redux";
import { NavLink } from "react-router-dom";
import {
  Home,
  User,
  Settings,
  Search,
  BookUser,
  NotebookText,
  BookType,
  ShieldPlus,
  ChevronDown,
} from "lucide-react";
import Logout from "./Logout";
import { PrivateRoutes, SideBarRoles } from "../models";
import { decodeRoles,hasAccess } from "../utilities";
import { searchService } from "../windows/Search/searchService";


export default function Sidebar({ isOpen, setIsOpen }) {
  const userState = useSelector(store => store.user);
  const storedRole = decodeRoles(userState?.roles) ?? [];

  const navItems = [
    { navKey: "DASHBOARD", to: PrivateRoutes.DASHBOARD, icon: <Home className="h-5 w-5" />, label: "Inicio" },
    {
      navKey: "SEARCH",
      icon: <Search className="h-5 w-5" />,
      label: "Buscador",
      isModal: true, // Indica que es un modal
    },
    { navKey: "PROFILE", to: PrivateRoutes.PROFILE, icon: <User className="h-5 w-5" />, label: "Perfil" },
    { navKey: "SUBJECTS", to: PrivateRoutes.SUBJECTS, icon: <BookType className="h-5 w-5" />, label: "Materias" },
    { navKey: "ACTIVITIES", to: PrivateRoutes.ACTIVITIES, icon: <NotebookText className="h-5 w-5" />, label: "Actividades" },
    { navKey: "STUDENTTRACKING", to: PrivateRoutes.STUDENTTRACKING, icon: <BookUser className="h-5 w-5" />, label: "Observador" },
    { 
      navKey: "ADMIN",
      to: PrivateRoutes.ADMIN,
      label: "Admin",
      icon: <ShieldPlus className="h-5 w-5" />,
      subItems: [
        { to: "/admin/institucion", label: "Institución", navKey: "ADMIN" },
        { 
          label: "Usuarios", 
          navKey: "ADMIN",
          subItems: [
            { to: "/admin/usuarios/estudiantes", label: "Estudiantes", navKey: "ADMIN" },
            { to: "/admin/usuarios/empleados", label: "Empleados", navKey: "ADMIN" }
          ]
        }
      ]
    },
    { navKey: "SETTINGS", to: PrivateRoutes.SETTINGS, icon: <Settings className="h-5 w-5" />, label: "Configuraciones" },
  ];

  return (
    <aside className={`bg-white border-r w-[250px] p-4 transition-all duration-300 ${isOpen ? "translate-x-0" : "-translate-x-full"}`}>
      <div className="flex items-center justify-between mb-6">
        <img src="/logo.svg" alt="Logo" className="h-8" />
        <Logout />
      </div>
      <nav className="space-y-2">
        {navItems.map((item, index) =>
          hasAccess(storedRole, SideBarRoles[item.navKey] ?? []) ? (
            <NavItem key={index} {...item} userRoles={storedRole} />
          ) : null
        )}
      </nav>
    </aside>
  );
}

function NavItem({ to, icon, label, subItems, navKey, userRoles, isModal }) {
  const [isOpen, setIsOpen] = useState(false);

  if (!hasAccess(userRoles, SideBarRoles[navKey] ?? [])) return null;

  const handleClick = () => {
    if (isModal) {
      
      searchService.open(); // Abre el modal en SEARCH
      console.log("click")
    }
  };

  return (
    <div className="flex flex-col">
      <div className="flex justify-between items-center">
        {to ? (
          <NavLink
            to={to}
            end
            className={({ isActive }) => `
              flex items-center space-x-3 p-3 w-full rounded-lg transition-colors
              ${isActive ? "bg-[#D4AF37] text-white" : "hover:bg-gray-100"}
            `}
          >
            {icon}
            <span className="font-medium">{label}</span>
          </NavLink>
        ) : (
          <button
            onClick={handleClick}
            className="flex items-center space-x-3 p-3 w-full rounded-lg hover:bg-gray-100 transition-colors"
          >
            {icon}
            <span className="font-medium">{label}</span>
          </button>
        )}
        {subItems && (
          <button onClick={() => setIsOpen(!isOpen)} className="p-2">
            <ChevronDown className={`h-4 w-4 transition-transform ${isOpen ? "rotate-180" : ""}`} />
          </button>
        )}
      </div>

      {isOpen && subItems && (
        <div className="ml-6 mt-2 space-y-1">
          {subItems.map((subItem, index) => (
            <NavItem key={index} {...subItem} userRoles={userRoles} />
          ))}
        </div>
      )}
    </div>
  );
}
