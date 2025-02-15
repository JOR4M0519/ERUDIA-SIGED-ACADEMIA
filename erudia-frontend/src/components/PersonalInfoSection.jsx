import Card from "./Card"

export default function PersonalInfoSection() {
  return (
    <Card title="Información Personal">
      <div className="space-y-4">
        <InfoItem label="Correo" value="usuario@dominio.com" />
        <InfoItem label="Teléfono" value="+52 123 456 7890" />
        <InfoItem label="Dirección" value="Calle Example #123" />
      </div>
    </Card>
  )
}

function InfoItem({ label, value }) {
  return (
    <div>
      <p className="text-sm text-gray-600">{label}</p>
      <p className="font-medium">{value}</p>
    </div>
  )
}

