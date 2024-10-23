import { useState } from "react"
import { Button } from "../Button"
import { merge } from "../../../utils/merge"
import arrow from "../../../assets/icons/arrow.png"

const Select = ({ className = "", classButton = "", classItem = "", classList = "", name = "", description, options = [], onChange }) => {
    const [value, setValue] = useState(null)
    const [isActive, setIsActive] = useState(false)

    const handleToggleSelect = () => {
        setIsActive(!isActive)
    }

    const handleChangeSelect = (event) => {
        onChange(event, name)
        setIsActive(false)
        setValue(event.target.getAttribute("data-name"))
    }


    return (
        <div className={merge("relative z-50", className)}>
            <Button className={merge("w-full pl-3 flex items-center justify-between font-normal text-gray-300", classButton)} size="md" color="gray-200" onClick={handleToggleSelect}>
                <span>{value ? value : description}</span>
                <img className={`${isActive ? "rotate-180" : ""}`} src={arrow} alt="arrow icon" />
            </Button>
            {isActive && (
                <ul className={merge("w-full p-2 flex flex-col border rounded-md absolute top-12 z-50 bg-white", classList)}>
                    {options.map(({ name, value }, key) => (
                        <li className={merge("p-2 text-gray-200 rounded-md hover:cursor-pointer hover:text-black", classItem)} key={key} data-value={value} data-name={name} onClick={handleChangeSelect}>
                            {name}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    )
}

export { Select }