export default function Card({ title, children, className = "" }) {
    return (
      <div className={`bg-white p-6 rounded-lg shadow-sm ${className}`}>
        {title && <h2 className="text-lg font-semibold mb-4">{title}</h2>}
        {children}
      </div>
    )
  }
  
  