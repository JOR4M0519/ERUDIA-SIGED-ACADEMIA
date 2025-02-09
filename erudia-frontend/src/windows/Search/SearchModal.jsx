import { useEffect, useState } from "react";
import { X } from "lucide-react";
import { searchService } from "./searchService";

export default function SearchModal() {
  const [isOpen, setIsOpen] = useState(false);

  useEffect(() => {
    // Suscribirse al estado del modal
    const subscription = searchService.getStatus().subscribe(setIsOpen);
    return () => subscription.unsubscribe();
  }, []);

  if (!isOpen) return null; // No renderizar si el modal está cerrado

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50 backdrop-blur-sm">
      <div className="bg-white rounded-lg shadow-lg w-[400px] p-5">
        {/* Header */}
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-lg font-semibold">Búsqueda</h2>
          <button onClick={searchService.close} className="p-2 rounded-lg hover:bg-gray-200">
            <X className="h-5 w-5" />
          </button>
        </div>

        {/* Input de Búsqueda */}
        <input
          type="text"
          placeholder="Buscar..."
          className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
        />

        {/* Resultados */}
        <div className="mt-4 space-y-2">
          <p className="text-gray-500">Resultados de búsqueda aparecerán aquí...</p>
        </div>

        {/* Footer */}
        <div className="mt-4 flex justify-end space-x-2">
          <button
            onClick={searchService.close}
            className="px-4 py-2 text-gray-700 bg-gray-200 rounded-lg hover:bg-gray-300"
          >
            Cerrar
          </button>
        </div>
      </div>
    </div>
  );
}
