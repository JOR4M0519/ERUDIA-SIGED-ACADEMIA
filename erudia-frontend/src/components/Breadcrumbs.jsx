import { Link, useLocation } from "react-router-dom";
import { ChevronRight } from "lucide-react";

export default function Breadcrumbs() {
  const location = useLocation();
  const pathSegments = location.pathname.split("/").filter(Boolean);

  return (
    <nav className="flex items-center space-x-2 text-gray-500 text-sm">
      <Link to="/" className="hover:text-gray-700">Inicio</Link>
      {pathSegments.map((segment, index) => {
        const path = `/${pathSegments.slice(0, index + 1).join("/")}`;
        const isLast = index === pathSegments.length - 1;

        return (
          <span key={index} className="flex items-center space-x-2">
            <ChevronRight className="h-4 w-4 text-gray-400" />
            {isLast ? (
              <span className="text-gray-900 font-medium">{decodeURIComponent(segment)}</span>
            ) : (
              <Link to={path} className="hover:text-gray-700">{decodeURIComponent(segment)}</Link>
            )}
          </span>
        );
      })}
    </nav>
  );
}
