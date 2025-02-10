import { X } from "lucide-react"

function PersonalInfoModal({ isOpen, onClose, studentInfo }) {
  if (!isOpen) return null

  return (
    <div className="fixed inset-0 bg-black/30 backdrop-blur-sm flex items-center justify-center z-50">
      <div className="bg-white rounded-xl shadow-lg max-w-2xl w-full mx-4">
        <div className="flex items-center justify-between p-6 border-b">
          <h3 className="text-xl font-semibold">Información Personal Detallada</h3>
          <button onClick={onClose} className="p-2 hover:bg-gray-100 rounded-full">
            <X size={20} />
          </button>
        </div>
        <div className="p-6">
          <div className="grid md:grid-cols-2 gap-6">
            <div>
              <p className="text-sm text-gray-500">Nombre completo</p>
              <p className="font-medium">{studentInfo.name}</p>
            </div>
            <div>
              <p className="text-sm text-gray-500">Email</p>
              <p className="font-medium">{studentInfo.personalInfo.email}</p>
            </div>
            <div>
              <p className="text-sm text-gray-500">Documento de identidad</p>
              <p className="font-medium">{studentInfo.personalInfo.documentId}</p>
            </div>
            <div>
              <p className="text-sm text-gray-500">Fecha de nacimiento</p>
              <p className="font-medium">{studentInfo.personalInfo.birthDate}</p>
            </div>
            <div>
              <p className="text-sm text-gray-500">Dirección</p>
              <p className="font-medium">{studentInfo.personalInfo.address}</p>
            </div>
            <div>
              <p className="text-sm text-gray-500">Teléfono</p>
              <p className="font-medium">{studentInfo.personalInfo.phone}</p>
            </div>
            <div>
              <p className="text-sm text-gray-500">Acudiente</p>
              <p className="font-medium">{studentInfo.personalInfo.guardian}</p>
            </div>
            <div>
              <p className="text-sm text-gray-500">Teléfono acudiente</p>
              <p className="font-medium">{studentInfo.personalInfo.guardianPhone}</p>
            </div>
          </div>
        </div>
        <div className="flex justify-end gap-4 p-6 border-t">
          <button onClick={onClose} className="px-4 py-2 border rounded-lg hover:bg-gray-50">
            Cerrar
          </button>
        </div>
      </div>
    </div>
  )
}

export default PersonalInfoModal;