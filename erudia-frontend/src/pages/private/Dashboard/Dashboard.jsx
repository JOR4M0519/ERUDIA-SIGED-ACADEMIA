
import ProfileSection from "../../../components/ProfileSection"
import PersonalInfoSection from "../../../components/PersonalInfoSection"
import ActionCards from "../../../components/ActionCards"

export function Dashboard() {
  
    const storedName = sessionStorage.getItem("name") || "Usuario";
    // Parsear el string a un array
    const storedRole = JSON.parse(sessionStorage.getItem("roles")) || []; 
    
    const userData = {
        name: storedName,
        period: "2023-1",
        studentId: "123456",
        role: storedRole
    }

    return (
        <div className="space-y-6">
        <header className="bg-white p-6 rounded-lg shadow-sm">
            <div className="flex justify-between items-center">
            <div>
                <h1 className="text-2xl font-semibold">Bienvenida {userData.name}</h1>
                <p className="text-gray-600">{userData.storedRole} Periodo {userData.period}</p>
            </div>
            <div className="text-sm text-gray-600">
                <p>Periodo {userData.period}</p>
                <p>ID: {userData.studentId}</p>
            </div>
            </div>
        </header>

        <div className="grid md:grid-cols-2 gap-6">
            <ProfileSection />
            <PersonalInfoSection />
        </div>

        <ActionCards />
        </div>
    )
}