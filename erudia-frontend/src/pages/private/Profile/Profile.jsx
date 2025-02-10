"use client";

import { useState } from "react";
import { useSelector } from "react-redux";
import { Eye, Download, ChevronRight } from "lucide-react";
import { ProfileRoles } from "../../../models/ProfileRoles";
import { hasAccess } from "../../../utilities";
import { PersonalInfoModal } from "./index";
import { decodeRoles } from "../../../utilities";

function Profile() {
  const [showDetails, setShowDetails] = useState(false);
  const userState = useSelector((store) => store.user);
  const storedRole = decodeRoles(userState?.roles) ?? [];
  const currentUserId = userState?.id; // ID del usuario autenticado

  const studentInfo = {
    id: "123", // ID del usuario que se está viendo
    name: "Laura Mejía",
    group: "Segundo B DIRECCIÓN",
    period: "Periodo 4",
    personalInfo: {
      email: "laura.mejia@escuela.edu",
      documentId: "1234567890",
      birthDate: "2010-05-15",
      address: "Calle Principal #123",
      phone: "+1234567890",
      guardian: "María Mejía",
      guardianPhone: "+1234567891",
    },
  };

  // ✅ Aplicamos la función hasAccess con los permisos de ProfileRoles
  const canViewDirection = hasAccess(storedRole, ProfileRoles.VIEW_DIRECTION);
  const canViewPersonalInfo = hasAccess(storedRole, ProfileRoles.VIEW_PERSONAL_INFO) 
  || studentInfo.id === currentUserId;
  console.log("canViewPersonalInfo", studentInfo.id , currentUserId);

  return (
    <div className="min-h-screen bg-gray-50 flex">
      <main className="flex-1 overflow-auto">
        <div className="max-w-4xl mx-auto p-6">
          {/* Header */}
          <div className="flex flex-col md:flex-row md:items-center justify-between mb-6">
            <div>
              <h2 className="text-2xl font-bold">{studentInfo.name}</h2>
              <div className="flex items-center space-x-4 mt-2">
                {canViewDirection ? (
                  <button className="inline-flex items-center space-x-2 px-4 py-2 bg-[#D4AF37] text-white rounded-lg hover:bg-[#C19B2C] transition-colors">
                    <span>{studentInfo.group}</span>
                    <ChevronRight size={16} />
                  </button>
                ) : (
                  <span className="text-gray-600">{studentInfo.group}</span>
                )}
                <span className="text-gray-600">{studentInfo.period}</span>
              </div>
            </div>
          </div>

          {/* Content Sections */}
          <div className="space-y-6">
            {/* Profile Section */}
            <section className="bg-white rounded-xl shadow-sm">
              <div className="p-6">
                <div className="flex items-center justify-between mb-4">
                  <h3 className="text-lg font-semibold">Perfil (Info personal)</h3>
                  {canViewPersonalInfo && (
                    <button
                      onClick={() => setShowDetails(true)}
                      className="flex items-center space-x-2 text-[#D4AF37] hover:text-[#C19B2C]"
                    >
                      <Eye size={20} />
                      <span>Ver detalles</span>
                    </button>
                  )}
                </div>
                <div className="grid md:grid-cols-2 gap-4">
                  <div>
                    <p className="text-sm text-gray-500">Email</p>
                    <p>{studentInfo.personalInfo.email}</p>
                  </div>
                  <div>
                    <p className="text-sm text-gray-500">Documento</p>
                    <p>{studentInfo.personalInfo.documentId}</p>
                  </div>
                </div>
              </div>
            </section>

            {/* Grades Section */}
            <section className="bg-white rounded-xl shadow-sm">
              <div className="p-6">
                <h3 className="text-lg font-semibold mb-4">Notas en cada materia</h3>
                <p className="text-gray-500">No hay notas disponibles para este periodo.</p>
              </div>
            </section>

            {/* Observer Section */}
            <section className="bg-white rounded-xl shadow-sm">
              <div className="p-6">
                <h3 className="text-lg font-semibold mb-4">Observador (Editable)</h3>
                <textarea
                  className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-[#D4AF37]"
                  rows="4"
                  placeholder="Agregar observaciones..."
                />
              </div>
            </section>

            {/* Generate Report Button */}
            <div className="flex justify-end">
              <button className="flex items-center space-x-2 px-6 py-3 bg-[#D4AF37] text-white rounded-lg hover:bg-[#C19B2C] transition-colors">
                <Download size={20} />
                <span>Generar boletín</span>
              </button>
            </div>
          </div>
        </div>
      </main>

      {/* Personal Info Modal */}
      <PersonalInfoModal isOpen={showDetails} onClose={() => setShowDetails(false)} studentInfo={studentInfo} />
    </div>
  );
}

export default Profile;
