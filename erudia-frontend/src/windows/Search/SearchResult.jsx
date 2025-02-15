import { useNavigate } from "react-router-dom";
import { ArrowRight } from "lucide-react";

const SearchResults = ({ results }) => {
  const navigate = useNavigate(); // Hook para la navegación

  if (results.length === 0) {
    return <p className="text-gray-500 text-center">No se encontraron resultados.</p>;
  }

  return (
    <div className="space-y-4">
      {results.map((user) => (
        <div key={user.id} className="bg-white p-4 rounded-lg shadow flex items-center justify-between">
          <div>
            <h3 className="font-semibold">{user.name}</h3>
            <p className="text-sm text-gray-600">{}</p>
            <p className="text-sm text-gray-500">{user.role}</p>
          </div>
          <button
            onClick={() => navigate(`/profile/${user.id}`)}
            className="ml-4 flex items-center space-x-2 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-all"
          >
            <span>Ver Perfil</span>
            <ArrowRight className="h-5 w-5" />
          </button>
        </div>
      ))}
    </div>
  );
};

export default SearchResults;
