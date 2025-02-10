import { useState, useEffect } from "react";
import { X, Search, ChevronDown } from "lucide-react";
import { getstoreFilterOptions, searchService, storeFilterOptions } from "./searchService";
import { request } from "../../services/config/axios_helper";
import SearchResults from "./SearchResult";

export default function SearchModal() {
  const [isOpen, setIsOpen] = useState(false);
  const [isFilterOpen, setIsFilterOpen] = useState(false);
  const [filters, setFilters] = useState({ role: "", subject: "", level: "" });
  const [searchQuery, setSearchQuery] = useState("");
  const [searchResults, setSearchResults] = useState([]);

  // Estado para opciones de filtro
  const [roles, setRoles] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [levels, setLevels] = useState([]);

  useEffect(() => {
    const subscription = searchService.getStatus().subscribe((status) => {
      setIsOpen(status);
      if (!status) {
        setSearchResults([]); 
        setSearchQuery("");
      }
    });

    return () => subscription.unsubscribe();
  }, []);

  useEffect(() => {
    const fetchData = async () => {
      const storedRoles = getstoreFilterOptions("rolesFilter");
      const storedSubjects = getstoreFilterOptions("subjectFilter");
      const storedLevels = getstoreFilterOptions("levelsFilter");

      if (storedRoles) setRoles(storedRoles);
      else fetchDataFilter("rolesFilter", "/private/groups","gtw");

      if (storedSubjects) setSubjects(storedSubjects);
      else fetchDataFilter("subjectFilter", "/subjects","academy");

      if (storedLevels) setLevels(storedLevels);
      else fetchDataFilter("levelsFilter", "/educational-levels","academy");
    };

    fetchData();
  }, []);

  const fetchDataFilter = async (key, path,service) => {
    try {
      const response = await request("GET", service, path, {});
      if (response.status !== 200) throw new Error("Error cargando la información");
      const data = response.data;

      switch (key) {
        case "rolesFilter":
          setRoles(data);
          break;
        case "subjectFilter":
          setSubjects(data);
          break;
        case "levelsFilter":
          setLevels(data);
          break;
      }
      storeFilterOptions(key, data);
    } catch (error) {
      console.error(`Error obteniendo ${key}:`, error);
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    console.log("Búsqueda con filtros:", filters);
    try {
    const response = await request("GET", "gtw", `/private/groups/users/${filters.role}`, {});
    setSearchResults(response.data);

    
      // 🔥 Simulación de resultados
      const simulatedResults = [
        { id: "1", name: "Juan Pérez", email: "juan@example.com", role: "Estudiante" },
        { id: "2", name: "María García", email: "maria@example.com", role: "Profesor" },
        { id: "3", name: "Carlos López", email: "carlos@example.com", role: "Administrativo" },
      ];
      //setSearchResults(simulatedResults);
    } catch (error) {
      console.error("Error en la búsqueda:", error);
      setSearchResults([]);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      {/* Backdrop */}
      <div className="absolute inset-0 bg-black/30 backdrop-blur-sm" onClick={searchService.close} />

      {/* Modal */}
      <div className="relative w-full md:max-w-2xl bg-white shadow-2xl md:rounded-xl flex flex-col max-h-screen">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b">
          <h2 className="text-2xl font-semibold">Buscador</h2>
          <button onClick={searchService.close} className="hover:bg-gray-100 p-2 rounded-full transition-colors">
            <X className="h-6 w-6" />
          </button>
        </div>

        <form onSubmit={handleSearch} className="flex flex-col flex-1 overflow-y-auto">
          {/* Search Input */}
          <div className="p-6 border-b">
            <div className="relative">
              <Search className="absolute left-4 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-400" />
              <input
                type="text"
                placeholder="Buscar..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-12 pr-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
          </div>

          {/* Filters */}
          <div className="p-6 border-b">
            <div className="flex items-center justify-between mb-6">
              <button
                type="button"
                onClick={() => setIsFilterOpen(!isFilterOpen)}
                className="flex items-center space-x-2 px-4 py-2 text-sm bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors"
              >
                <span>Filtros</span>
                <ChevronDown className={`h-4 w-4 transition-transform ${isFilterOpen ? "rotate-180" : ""}`} />
              </button>
            </div>

            {/* Filter Dropdowns */}
            <div className={`grid grid-cols-1 md:grid-cols-3 gap-4 ${isFilterOpen ? "" : "hidden"}`}>
              <select
                value={filters.role}
                onChange={(e) => setFilters((prev) => ({ ...prev, role: e.target.value }))}
                className="w-full p-3 bg-gray-50 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Seleccione Rol</option>
                {roles.map((role) => (
                  <option key={role.id} value={role.id}>
                    {role.name[0].toUpperCase()+role.name.substring(1)}
                  </option>
                ))}
              </select>

              <select
                value={filters.subject}
                onChange={(e) => setFilters((prev) => ({ ...prev, subject: e.target.value }))}
                className="w-full p-3 bg-gray-50 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Seleccione Materia</option>
                {subjects.map((subject) => (
                  <option key={subject.id} value={subject.id}>
                    {subject.subjectName}
                  </option>
                ))}
              </select>

              <select
                value={filters.level}
                onChange={(e) => setFilters((prev) => ({ ...prev, level: e.target.value }))}
                className="w-full p-3 bg-gray-50 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Seleccione Nivel</option>
                {levels.map((level) => (
                  <option key={level.id} value={level.id}>
                    {level.levelName}
                  </option>
                ))}
              </select>
            </div>
          </div>

          {/* Results Area */}
          <div className="flex-1 overflow-y-auto p-6">
            <SearchResults results={searchResults} />
          </div>

          {/* Search Button */}
          <div className="p-6 border-t mt-auto">
            <button type="submit" className="w-full py-3 px-4 bg-blue-600 text-white font-medium rounded-xl hover:bg-blue-700">
              Buscar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
