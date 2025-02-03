import Card from "./Card"

export default function ProfileSection() {

  const storedName = sessionStorage.getItem("name") || "Usuario";

  return (
    <Card title="Mi perfil">
      <div className="flex items-start space-x-4">
        <img
          src="https://v0.dev/placeholder.svg?height=100&width=100"
          alt="Profile"
          className="w-24 h-24 rounded-full"
        />
        <div className="space-y-2">
          <p className="text-sm text-gray-600">Nombre completo</p>
          <p className="font-medium">{storedName}</p>
          <button className="text-sm text-blue-600 hover:underline">Actualizar foto</button>
        </div>
      </div>
    </Card>
  )
}

