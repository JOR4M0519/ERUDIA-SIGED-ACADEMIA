import { NavLink } from "react-router-dom"
import { Home, User, Settings, Menu } from "lucide-react"

export default function Sidebar({ isOpen, setIsOpen }) {
  return (
    <aside
      className={`bg-white border-r w-[250px] p-4 transition-all duration-300 ${isOpen ? "translate-x-0" : "-translate-x-full"}`}
    >
      <div className="flex items-center justify-between mb-6">
        <img src="/logo.svg" alt="Logo" className="h-8" />
        {/* <button onClick={() => setIsOpen(!isOpen)} className="p-2 rounded-lg hover:bg-gray-100">
          <Menu className="h-5 w-5" />
        </button> */}
      </div>
      <nav className="space-y-2">
        <NavItem to="/dashboard" icon={<Home className="h-5 w-5" />} label="Dashboard" />
        <NavItem to="/profile" icon={<User className="h-5 w-5" />} label="Profile" />
        <NavItem to="/settings" icon={<Settings className="h-5 w-5" />} label="Settings" />
      </nav>
    </aside>
  )
}

function NavItem({ to, icon, label }) {
  return (
    <NavLink
      to={to}
      className={({ isActive }) => `
        flex items-center space-x-3 p-3 rounded-lg transition-colors
        ${isActive ? "bg-[#D4AF37] text-white" : "hover:bg-gray-100"}
      `}
    >
      {icon}
      <span className="font-medium">{label}</span>
    </NavLink>
  )
}
