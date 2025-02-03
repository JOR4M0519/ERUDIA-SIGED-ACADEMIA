import Card from "./Card"

export default function ActionCards() {
  const actions = [
    { title: "Actualizar información personal", color: "bg-blue-600" },
    { title: "Cambiar contraseña", color: "bg-green-600" },
    { title: "Configurar notificaciones", color: "bg-purple-600" },
  ]

  return (
    <Card title="Acciones Rápidas">
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {actions.map((action, index) => (
          <button
            key={index}
            className={`${action.color} text-white p-4 rounded-lg hover:opacity-90 transition-opacity text-left`}
          >
            {action.title}
          </button>
        ))}
      </div>
    </Card>
  )
}

