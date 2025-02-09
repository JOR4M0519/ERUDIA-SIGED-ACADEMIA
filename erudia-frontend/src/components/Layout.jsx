import { useState } from "react"
import Sidebar from "./Sidebar"
import {SearchModal} from "../windows/Search"



export function Layout({ children }) {
  const [isSidebarOpen, setIsSidebarOpen] = useState(true)
  //const navigate = useNavigate()


  return (
  <>
    <div className="flex min-h-screen bg-gray-50">
      
      <Sidebar isOpen={isSidebarOpen} setIsOpen={setIsSidebarOpen} />
      <main className="flex-1 p-6">{children}</main>
      <SearchModal />
    </div>
  </>
    
  )
}

